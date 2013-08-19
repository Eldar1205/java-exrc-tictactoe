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
public class RowSequence<TSquare extends ReadOnlySquare> extends SquaresSequence<TSquare> {
    private int row;

    public RowSequence(Game<TSquare> game, int row) {
        super(game);
        this.row = row;
    }

    @Override
    protected GameBoardPosition generatePosition(int squareIndex) {
        return new GameBoardPosition(this.row, squareIndex);
    }    
}
