/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logic.persistency;

import com.google.common.base.Charsets;
import com.google.common.io.CharSource;
import com.google.common.io.Closer;
import com.google.common.io.Files;
import logic.persistency.exceptions.GamesXmlRepositoryException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import logic.model.MainGame;
import logic.persistency.xmlConverters.MainGameConverter;
import logic.persistency.xmlModels.ObjectFactory;
import logic.persistency.xmlModels.Tictactoe;
import common.utils.ThrowHelper;
import java.net.URL;
import javax.xml.bind.UnmarshalException;
import javax.xml.bind.util.ValidationEventCollector;
import logic.persistency.exceptions.InvalidTictactoeXmlException;
import logic.persistency.exceptions.MainGameNotPlayedException;
import logic.persistency.exceptions.XmlSchemaViolationException;

/**
 *
 * @author Eldar
 */
public class GamesXmlRepository {
    private static final String SCHEMA_NAME = "tictactoe.xsd";
    private static final String SCHEMA_FOLDER = "/XSD/";
    
    private MainGameConverter gameConverter;
    
    public GamesXmlRepository() {
        this.gameConverter = new MainGameConverter(new ObjectFactory());
    }
    
    // This class is used to make sure only one JAXBContext and Schema is created (for performance reasons)
    // and to make sure it's created lazily and thread-safely when needed.
    private static class JAXBSingletons {
        private static final JAXBContext CONTEXT_INSTANCE;
        private static final Schema SCHEMA_INSTANCE;
        
        static {
            try {
                CONTEXT_INSTANCE = JAXBContext.newInstance(Tictactoe.class);
                SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
                URL schemaURL = GamesXmlRepository.class.getResource(SCHEMA_FOLDER + SCHEMA_NAME);
                SCHEMA_INSTANCE = sf.newSchema(schemaURL);
            } catch (Throwable cause) {
                throw new ExceptionInInitializerError(cause);
            }
        }
    }
    
    public MainGame loadGame(File file) throws XmlSchemaViolationException, InvalidTictactoeXmlException, GamesXmlRepositoryException {
        ThrowHelper.throwOnNull(file, "file");
        
        return guardedLoadGame(file);
    }
    
    public void saveGame(File file, MainGame game) throws MainGameNotPlayedException, GamesXmlRepositoryException {
        ThrowHelper.throwOnNull(file, "file");
        ThrowHelper.throwOnNull(game, "game");
        
        guardedSaveGame(file, game);
    }
    
    private MainGame guardedLoadGame(File file) throws XmlSchemaViolationException, InvalidTictactoeXmlException, GamesXmlRepositoryException {
        ValidationEventCollector schemaViolationEvents = new ValidationEventCollector();
        
        try {
            Closer closer = Closer.create();

            try {
                // The reason a FileInputStream is used to prevent JAXB from suppressing IO exception and therefore keep file locked in an exception occured
                FileInputStream xmlFileStream = closer.register(new FileInputStream(file));
                Unmarshaller unmarshaller = JAXBSingletons.CONTEXT_INSTANCE.createUnmarshaller();
                unmarshaller.setSchema(JAXBSingletons.SCHEMA_INSTANCE);
                unmarshaller.setEventHandler(schemaViolationEvents);
                Tictactoe xmlModel = (Tictactoe)unmarshaller.unmarshal(xmlFileStream);
                return this.gameConverter.fromXml(xmlModel);
            } catch (Throwable e) { // must catch Throwable
                throw closer.rethrow(e, UnmarshalException.class, InvalidTictactoeXmlException.class);
            } finally {
              closer.close();
            }
        } catch (UnmarshalException cause) {
            if (schemaViolationEvents.hasEvents()) {
                throw new XmlSchemaViolationException();
            } else {
                throw wrapLoadThrowable(file.getPath(), cause);
            }
        } catch (InvalidTictactoeXmlException cause) {
            throw cause;
        } catch (Throwable cause) {
            throw wrapLoadThrowable(file.getPath(), cause);
        }
    }
    
    private void guardedSaveGame(File file, MainGame game) throws MainGameNotPlayedException, GamesXmlRepositoryException {
        try {
            Closer closer = Closer.create();

            try {
                // The reason a FileOutputStream is used to prevent JAXB from suppressing IO exception and therefore keep file locked in an exception occured
                FileOutputStream xmlFileStream = closer.register(new FileOutputStream(file));
                Tictactoe gameXml = this.gameConverter.toXml(game); // Do this first so a marshaller won't be created if the game isn't valid for save
                Marshaller marshaller = JAXBSingletons.CONTEXT_INSTANCE.createMarshaller();
                marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
                marshaller.setSchema(JAXBSingletons.SCHEMA_INSTANCE);
                marshaller.setProperty(Marshaller.JAXB_NO_NAMESPACE_SCHEMA_LOCATION, SCHEMA_NAME);
                marshaller.marshal(gameXml, xmlFileStream);
            } catch (Throwable e) { // must catch Throwable
                throw closer.rethrow(e, MainGameNotPlayedException.class);
            } finally {
              closer.close();
            }
        } catch (MainGameNotPlayedException cause) {
            throw cause;
        } catch (Throwable cause) {
            throw wrapSaveThrowable(file.getPath(), cause);
        }
    }
    
    private GamesXmlRepositoryException wrapLoadThrowable(String fileName, Throwable cause) {
        String errMsg = String.format("Error loading game file '%s'", fileName);
        return new GamesXmlRepositoryException(errMsg, cause);
    }
    
    private GamesXmlRepositoryException wrapSaveThrowable(String fileName, Throwable cause) {
        String errMsg = String.format("Error saving game to file '%s'", fileName);
        return new GamesXmlRepositoryException(errMsg, cause);
    }
}
