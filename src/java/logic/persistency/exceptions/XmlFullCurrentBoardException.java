/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logic.persistency.exceptions;

import common.utils.ThrowHelper;

/**
 *
 * @author Eldar
 */
public class XmlFullCurrentBoardException extends InvalidTictactoeXmlException {
    private int boardRow;
    private int boardColumn;
    
    public XmlFullCurrentBoardException(int boardRow, int boardColumn) {
        super("Xml current board has no free cells");
        this.boardRow = boardRow;
        this.boardColumn = boardColumn;
    }    
    
    @Override
    public String getMessage() {
        String boardPositionClause = 
                System.lineSeparator() + "Xml current board position: " + ThrowHelper.positionTextForError(this.boardRow, this.boardColumn);
        
        return super.getMessage() + boardPositionClause;
    }
}
