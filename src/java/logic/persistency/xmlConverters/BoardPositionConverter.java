/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logic.persistency.xmlConverters;

import com.google.common.base.Preconditions;
import common.model.game.GameBoardPosition;
import common.utils.ThrowHelper;

/**
 *
 * @author Eldar
 */
public class BoardPositionConverter {
    public GameBoardPosition fromXml(int xmlModelRow, int xmlModelColumn) {
        return new GameBoardPosition(xmlModelRow - 1, xmlModelColumn - 1);
    }
    
    public int toXmlRow(GameBoardPosition position) {
        Preconditions.checkNotNull(position);
        
        return position.getRow() + 1;
    }
    
    public int toXmlColumn(GameBoardPosition position) {
        Preconditions.checkNotNull(position);
        
        return position.getCol() + 1;
    }
    
    public String xmlPositionTextForError(GameBoardPosition position) {                
        return ThrowHelper.positionTextForError(toXmlRow(position), toXmlColumn(position)); 
    }
}
