/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logic.persistency.xmlConverters;

import com.google.common.base.Optional;
import common.model.game.GameBoardPosition;
import logic.persistency.xmlModels.ObjectFactory;
import common.utils.ThrowHelper;

/**
 *
 * @author Eldar
 */
public class BasicSquareConverter implements XmlSquareConverter<logic.model.BasicSquare, logic.persistency.xmlModels.Cell> {
    private BoardPositionConverter positionConverter;
    private PlayerIdentityConverter playerIdentityConverter;
    private ObjectFactory xmlModelsFactory;
    
    public BasicSquareConverter(ObjectFactory xmlModelsFactory) {
        this.xmlModelsFactory = ThrowHelper.throwOnNull(xmlModelsFactory, "xmlModelsFactory");
        this.positionConverter = new BoardPositionConverter();
        this.playerIdentityConverter = new PlayerIdentityConverter();
    }
    
    @Override
    public logic.model.BasicSquare fromXml(logic.persistency.xmlModels.Cell xmlModel) {
        ThrowHelper.throwOnNull(xmlModel, "xmlModel");
        
        GameBoardPosition squarePosition = this.positionConverter.fromXml(xmlModel.getRow(), xmlModel.getCol());
        logic.model.BasicSquare logicModel = new logic.model.BasicSquare(squarePosition);
        logicModel.setOwner(Optional.of(this.playerIdentityConverter.fromXml(xmlModel.getValue())));
        
        return logicModel;
    }
    
    @Override
    public Optional<logic.persistency.xmlModels.Cell> toXml(logic.model.BasicSquare logicModel) {
        ThrowHelper.throwOnNull(logicModel, "logicModel");
        
        if (!logicModel.hasOwner()) { 
            return Optional.absent();
        }
        
        logic.persistency.xmlModels.Cell xmlModel = this.xmlModelsFactory.createCell();
        xmlModel.setRow(this.positionConverter.toXmlRow(logicModel.getPosition()));
        xmlModel.setCol(this.positionConverter.toXmlColumn(logicModel.getPosition()));
        xmlModel.setValue(this.playerIdentityConverter.toXml(logicModel.getOwner().get())); // The condition above verifies there is an owner

        return Optional.of(xmlModel);
    }
}
