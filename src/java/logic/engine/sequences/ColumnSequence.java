/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logic.engine.sequences;

import common.model.game.GameBoardPosition;
import logic.model.Game;
import common.model.game.ReadOnlySquare;

/**
 *
 * @author Eldar
 */
public class ColumnSequence<TSquare extends ReadOnlySquare> extends SquaresSequence<TSquare> {
    private int col;
        
    public ColumnSequence(Game<TSquare> game, int col) {
        super(game);
        this.col = col;
    }

    @Override
    protected GameBoardPosition generatePosition(int squareIndex) {
        return new GameBoardPosition(squareIndex, this.col);
    }
}
