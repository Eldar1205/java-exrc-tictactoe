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
public class MainDiagonalSequence<TSquare extends ReadOnlySquare> extends SquaresSequence<TSquare> {
    public MainDiagonalSequence(Game<TSquare> game) {
        super(game);
    }
    
    @Override
    protected GameBoardPosition generatePosition(int squareIndex) {
        return new GameBoardPosition(squareIndex, squareIndex);
    }
}
