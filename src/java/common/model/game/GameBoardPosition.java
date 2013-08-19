/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package common.model.game;

import common.model.boardPosition.BoardPosition;
import common.model.boardPosition.BoardPositionFactory;
import common.model.boardPosition.BoardPositionIterable;

/**
 *
 * @author Eldar
 */
public class GameBoardPosition extends BoardPosition {
    public static final int DIMENSION = 3;
    public static final int SQUARES_COUNT = DIMENSION * DIMENSION;
    
    public GameBoardPosition(int row, int col) {        
        super(row, col, DIMENSION);
    }
    
    public static BoardPositionIterable<GameBoardPosition> iterate() {
        return new BoardPositionIterable<GameBoardPosition>(new BoardPositionFactory<GameBoardPosition>() {
            @Override
            public GameBoardPosition create(int row, int col) {
                return new GameBoardPosition(row, col);
            }

            @Override
            public int getDimension() {
                return GameBoardPosition.DIMENSION;
            }
        });
    }
}
