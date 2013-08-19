/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logic.persistency.xmlConverters;

import logic.persistency.exceptions.XmlIllegalBoardWinnerException;
import logic.persistency.exceptions.XmlUniqueViolationException;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import common.utils.ThrowHelper;
import common.model.game.GameBoardPosition;
import logic.engine.GameStatusManager;
import logic.persistency.exceptions.InvalidTictactoeXmlException;
import logic.persistency.exceptions.XmlMissingBoardWinnerException;
import logic.persistency.xmlModels.ObjectFactory;

/**
 *
 * @author Eldar
 */
public class SubGameConverter implements XmlSquareConverter<logic.model.SubGame, logic.persistency.xmlModels.Board> {
    private BoardPositionConverter positionConverter;
    private PlayerIdentityConverter playerIdentityConverter;
    private GameConverter<logic.model.BasicSquare, logic.persistency.xmlModels.Cell> boardConverter;
    private ObjectFactory xmlModelsFactory;
    
    public SubGameConverter(ObjectFactory xmlModelsFactory) {
        this.xmlModelsFactory = Preconditions.checkNotNull(xmlModelsFactory);
        this.positionConverter = new BoardPositionConverter();
        this.playerIdentityConverter = new PlayerIdentityConverter();
        this.boardConverter = new GameConverter<logic.model.BasicSquare, logic.persistency.xmlModels.Cell>(new BasicSquareConverter(xmlModelsFactory));
    }
    
    @Override
    public logic.model.SubGame fromXml(logic.persistency.xmlModels.Board xmlModel) throws XmlUniqueViolationException, XmlIllegalBoardWinnerException, XmlMissingBoardWinnerException, InvalidTictactoeXmlException {       
        ThrowHelper.throwOnNull(xmlModel, "xmlModel");
        
        final GameBoardPosition subGamePosition = this.positionConverter.fromXml(xmlModel.getRow(), xmlModel.getCol());
        logic.model.SubGame logicModel = new logic.model.SubGame(subGamePosition);
        
        this.boardConverter.fromXml(xmlModel.getCell(), logicModel, new DuplicatePositionErrorFactory() {
            @Override
            public XmlUniqueViolationException createError(GameBoardPosition duplicatePosition) {
                return new XmlUniqueViolationException(
                        "Cell position", positionConverter.xmlPositionTextForError(duplicatePosition), 
                        "Unique constraint violated in board positioned at " + positionConverter.xmlPositionTextForError(subGamePosition));
            }
        });
        
        if (xmlModel.getWinner() != null) {
            checkedSetSubGameOwner(xmlModel, logicModel);
        } else {
            checkedRefreshNotOwnedSubGameStatus(xmlModel, logicModel);
        }
        
        return logicModel;
    }
    
    @Override
    public Optional<logic.persistency.xmlModels.Board> toXml(logic.model.SubGame logicModel) {
        ThrowHelper.throwOnNull(logicModel, "logicModel");
        
        if (!logicModel.hasOwnedSquares()) {
            return Optional.absent();
        }
        
        logic.persistency.xmlModels.Board xmlModel = this.xmlModelsFactory.createBoard();
        xmlModel.setRow(this.positionConverter.toXmlRow(logicModel.getPosition()));
        xmlModel.setCol(this.positionConverter.toXmlColumn(logicModel.getPosition()));
        
        if (logicModel.hasOwner()) {
            xmlModel.setWinner(this.playerIdentityConverter.toXml(logicModel.getOwner().get()));
        }
        
        this.boardConverter.toXml(logicModel, xmlModel.getCell());
        
        return Optional.of(xmlModel);
    }

    private void checkedSetSubGameOwner(logic.persistency.xmlModels.Board xmlModel, logic.model.SubGame logicModel) throws XmlIllegalBoardWinnerException {
        common.model.player.PlayerIdentity logicModelWinner = this.playerIdentityConverter.fromXml(xmlModel.getWinner());
        
        if (new GameStatusManager<logic.model.BasicSquare>(logicModel).hasWinningSequence(logicModelWinner)) {
            logicModel.setOwner(logicModelWinner);
        } else {
            throw new XmlIllegalBoardWinnerException(xmlModel.getRow(), xmlModel.getCol(), xmlModel.getWinner());
        }
    }

    private void checkedRefreshNotOwnedSubGameStatus(logic.persistency.xmlModels.Board xmlModel, logic.model.SubGame logicModel) 
            throws XmlMissingBoardWinnerException {
        new GameStatusManager<logic.model.BasicSquare>(logicModel).refreshStatusIfNotConcluded();
        
        if (logicModel.hasOwner()) {
            throw new XmlMissingBoardWinnerException(xmlModel.getRow(), xmlModel.getCol(), 
                                                     this.playerIdentityConverter.toXml(logicModel.getOwner().get()));
        }
    }
}
