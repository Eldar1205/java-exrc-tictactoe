/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logic.model;

import com.google.common.collect.Lists;
import common.model.game.GameBoardPosition;
import java.util.ArrayList;
import common.model.game.ReadOnlySubGame;
import common.utils.ThrowHelper;

/**
 *
 * @author Eldar
 */
public class SubGame extends Game<BasicSquare> implements ReadOnlySubGame {
    private GameBoardPosition position;
    private BasicSquare[][] squaresBoard;
    
    public SubGame(GameBoardPosition position) {        
        this.position = ThrowHelper.throwOnNull(position, "position");  
        
        this.squaresBoard = new BasicSquare[GameBoardPosition.DIMENSION][GameBoardPosition.DIMENSION];
        
        for (GameBoardPosition currPos : GameBoardPosition.iterate()) {
            this.squaresBoard[currPos.getRow()][currPos.getCol()] = new BasicSquare(currPos);
        }
    }
    
    @Override
    public GameBoardPosition getPosition() {
        return this.position;
    }
    
    @Override
    public boolean isOwnable() {
        return !isTie();
    }
    
    public boolean hasOwnedSquares() {
        for (BasicSquare currSquare : this) {
            if (currSquare.hasOwner()) {
                return true;
            }
        }
        
        return false;
    }
    
    @Override
    public boolean hasFreeSquares() {
        for (BasicSquare currSquare : this) {
            if (!currSquare.hasOwner()) {
                return true;
            }
        }
        
        return false;
    }
    
    @Override
    public BasicSquare[] getFreeSquares() {
        ArrayList<BasicSquare> freeSquares = Lists.newArrayListWithCapacity(GameBoardPosition.SQUARES_COUNT);
        
        for (BasicSquare currSquare : this) {
            if (!currSquare.hasOwner()) {
                    freeSquares.add(currSquare);
                }
        }
        
        return freeSquares.toArray(new BasicSquare[freeSquares.size()]);
    }

    @Override
    public BasicSquare getSquareByPosition(GameBoardPosition position) {
        ThrowHelper.throwOnNull(position, "position");
        
        return this.squaresBoard[position.getRow()][position.getCol()];
    }

    @Override
    public void setSquareByItsPosition(BasicSquare square) {
        ThrowHelper.throwOnNull(square, "square");
        
        this.squaresBoard[square.getPosition().getRow()][square.getPosition().getCol()] = square;
    }
}
