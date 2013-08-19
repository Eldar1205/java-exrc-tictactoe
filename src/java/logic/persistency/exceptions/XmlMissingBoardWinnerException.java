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
public class XmlMissingBoardWinnerException extends InvalidTictactoeXmlException {
    private int boardRow;
    private int boardColumn;
    private GameValue possibleWinner;
    
    public XmlMissingBoardWinnerException(int boardRow, int boardColumn, GameValue possibleWinner) {
        super("Xml board has no winner yet there is a player with winning sequence");
        this.boardRow = boardRow;
        this.boardColumn = boardColumn;
        this.possibleWinner = possibleWinner;
    }        
    
    @Override
    public String getMessage() {
        String boardPositionClause = 
                System.lineSeparator() + "Xml board position: " + ThrowHelper.positionTextForError(this.boardRow, this.boardColumn);
        String possibleWinnerClause = System.lineSeparator() + "Possible winner: " + this.possibleWinner;
        
        return super.getMessage() + boardPositionClause + possibleWinnerClause;
    }
}
