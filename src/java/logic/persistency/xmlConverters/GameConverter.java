/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logic.persistency.xmlConverters;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.HashSet;
import common.model.game.GameBoardPosition;
import logic.model.Game;
import common.model.game.ReadOnlySquare;
import logic.persistency.exceptions.InvalidTictactoeXmlException;
import logic.persistency.exceptions.XmlUniqueViolationException;

/**
 *
 * @author Eldar
 */
public class GameConverter<TLogicModel extends ReadOnlySquare, TXmlModel> {
    private XmlSquareConverter<TLogicModel, TXmlModel> squareConverter;
    
    public GameConverter(XmlSquareConverter<TLogicModel, TXmlModel> squareConverter) {               
        this.squareConverter = Preconditions.checkNotNull(squareConverter);    
    }    
    
    public void fromXml(Collection<TXmlModel> xmlModels, Game<TLogicModel> logicGameModelToFill, DuplicatePositionErrorFactory errorGenerator) throws XmlUniqueViolationException, InvalidTictactoeXmlException {
        Preconditions.checkNotNull(xmlModels);
        Preconditions.checkNotNull(logicGameModelToFill);
        Preconditions.checkNotNull(errorGenerator);
            
        HashSet<GameBoardPosition> boardPositions = Sets.newHashSetWithExpectedSize(xmlModels.size());

        for (TXmlModel currXmlModel : xmlModels) {
            TLogicModel currLogicModel = this.squareConverter.fromXml(currXmlModel);
            GameBoardPosition currBoardPosition = currLogicModel.getPosition();

            if (boardPositions.contains(currBoardPosition)) {
                throw errorGenerator.createError(currBoardPosition);
            } else {
                boardPositions.add(currBoardPosition);
            }

            logicGameModelToFill.setSquareByItsPosition(currLogicModel);
        }
    }
    
    public void toXml(Game<TLogicModel> logicBoardModel, Collection<TXmlModel> xmlModelsToFill) {
        Preconditions.checkNotNull(logicBoardModel);
        Preconditions.checkNotNull(xmlModelsToFill);
        
        for (TLogicModel currLogicModel : logicBoardModel) {
            Optional<TXmlModel> currXmlModel = this.squareConverter.toXml(currLogicModel);

            if (currXmlModel.isPresent()) {
                xmlModelsToFill.add(currXmlModel.get());
            }
        }
    }
}
