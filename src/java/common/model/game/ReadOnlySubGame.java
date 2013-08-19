/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package common.model.game;

/**
 *
 * @author Eldar
 */
public interface ReadOnlySubGame extends ReadOnlySquare {
    boolean isTie();
    boolean hasFreeSquares();
    ReadOnlyBasicSquare getSquareByPosition(GameBoardPosition position);
    ReadOnlyBasicSquare[] getFreeSquares();
}
