/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package common.model.game;

import common.utils.ThrowHelper;

/**
 *
 * @author Eldar
 */
public class GameMove {
    private GameBoardPosition subGamePosition;
    private GameBoardPosition basicSquarePosition;
    
    public GameMove(GameBoardPosition subGamePosition, GameBoardPosition basicSquarePosition) {        
        this.subGamePosition = ThrowHelper.throwOnNull(subGamePosition, "subGamePosition");
        this.basicSquarePosition = ThrowHelper.throwOnNull(basicSquarePosition, "basicSquarePosition");
    }    

    public GameBoardPosition getSubGamePosition() {
        return this.subGamePosition;
    }

    public GameBoardPosition getBasicSquarePosition() {
        return this.basicSquarePosition;
    }
}
