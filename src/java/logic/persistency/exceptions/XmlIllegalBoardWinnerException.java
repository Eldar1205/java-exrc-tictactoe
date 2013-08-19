/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logic.persistency.exceptions;

import logic.persistency.xmlModels.GameValue;
import common.utils.ThrowHelper;

/**
 *
 * @author Eldar
 */
public class XmlIllegalBoardWinnerException extends InvalidTictactoeXmlException {
    private int boardRow;
    private int boardColumn;
    private GameValue illegalWinner;
    
    public XmlIllegalBoardWinnerException(int boardRow, int boardColumn, GameValue illegalWinner) {
        super("Xml board winner has no winning sequence in the board");
        this.boardRow = boardRow;
        this.boardColumn = boardColumn;
        this.illegalWinner = illegalWinner;    
    }        
    
    @Override
    public String getMessage() {
        String boardPositionClause = 
                System.lineSeparator() + "Xml board position: " + ThrowHelper.positionTextForError(this.boardRow, this.boardColumn);
        String illegalWinnerClause = System.lineSeparator() + "Illegal winner: " + this.illegalWinner;
        
        return super.getMessage() + boardPositionClause + illegalWinnerClause;
    }
}
