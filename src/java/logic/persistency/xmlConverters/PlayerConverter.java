/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logic.persistency.xmlConverters;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import logic.persistency.exceptions.XmlMissingHumanPlayerNameException;
import logic.persistency.xmlModels.ObjectFactory;
import common.utils.ThrowHelper;
import common.model.player.ComputerPlayer;
import common.model.game.GameDifficulty;
import common.model.player.HumanPlayer;
import common.model.player.ValuedPlayersVisitor;
import logic.persistency.xmlModels.PlayerType;

/**
 *
 * @author Eldar
 */
public class PlayerConverter {
    private PlayerIdentityConverter identityConverter;
    private ObjectFactory xmlModelsFactory;
    
    public PlayerConverter(ObjectFactory xmlModelsFactory) {        
        this.xmlModelsFactory = Preconditions.checkNotNull(xmlModelsFactory);
        this.identityConverter = new PlayerIdentityConverter();
    }
    
    public common.model.player.Player fromXml(logic.persistency.xmlModels.Player xmlModel) throws XmlMissingHumanPlayerNameException {
        Preconditions.checkNotNull(xmlModel);
        
        common.model.player.PlayerIdentity playerIdentity = this.identityConverter.fromXml(xmlModel.getValue());
                
        switch (xmlModel.getType()) {
            case COMPUTER:
                return new ComputerPlayer(playerIdentity, GameDifficulty.NORMAL, Optional.fromNullable(Strings.emptyToNull(xmlModel.getName())));
            case HUMAN:
                if (Strings.isNullOrEmpty(xmlModel.getName())) {
                    throw new XmlMissingHumanPlayerNameException(xmlModel.getValue());
                }
                
                return new HumanPlayer(playerIdentity, xmlModel.getName());
            default:
                throw ThrowHelper.createUnsupportedEnumValue(xmlModel.getType());
        }
    }
    
    public logic.persistency.xmlModels.Player toXml(common.model.player.Player logicModel) {
        Preconditions.checkNotNull(logicModel);
        
        logic.persistency.xmlModels.Player xmlModel = this.xmlModelsFactory.createPlayer();
        xmlModel.setValue(this.identityConverter.toXml(logicModel.getIdentity()));
        xmlModel.setType(logicModel.accept(new PlayerXmlTypeVisitor()));
        xmlModel.setName(logicModel.getName().orNull());
        
        return xmlModel;
    }
    
    private class PlayerXmlTypeVisitor implements ValuedPlayersVisitor<PlayerType> {
        @Override
        public PlayerType visit(ComputerPlayer computerPlayer) {
            return PlayerType.COMPUTER;
        }

        @Override
        public PlayerType visit(HumanPlayer humanPlayer) {
            return PlayerType.HUMAN;
        }
    }
}
