/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logic.model;

import common.model.game.GameBoardPosition;
import com.google.common.base.Optional;
import com.google.common.collect.AbstractIterator;
import com.google.common.collect.UnmodifiableIterator;
import java.util.Iterator;
import common.model.player.PlayerIdentity;
import common.model.game.ReadOnlySquare;
import common.utils.ThrowHelper;

/**
 *
 * @author Eldar
 */
public abstract class Game<TSquare extends ReadOnlySquare> implements Iterable<TSquare> {
    private Optional<PlayerIdentity> owner;
    private GameStatus status;
    
    protected Game() {        
        internalSetNotConcluded();
    }
    
    public boolean isConcluded() {
        return this.status != GameStatus.NOT_CONCLUDED;
    }
    
    public boolean isTie() {
        return this.status == GameStatus.TIE;
    }
    
    public boolean isOwner(PlayerIdentity player) {
        return this.owner.equals(Optional.of(player));
    }
    
    public boolean hasOwner() {
        return this.status == GameStatus.WON;
    }
    
    public Optional<PlayerIdentity> getOwner() {
        return this.owner;
    }
    
    public GameStatus getStatus() {
        return this.status;
    }
    
    public void setNotConcluded() {
        internalSetNotConcluded();
    }
    
    public void setTie() {
        this.owner = Optional.absent();
        this.status = GameStatus.TIE;
    }
    
    public void setOwner(PlayerIdentity owner) {
        this.owner = Optional.of(ThrowHelper.throwOnNull(owner, "owner"));
        this.status = GameStatus.WON;
    }
   
    public abstract TSquare getSquareByPosition(GameBoardPosition position);
    public abstract void setSquareByItsPosition(TSquare square);
    
    @Override
    public UnmodifiableIterator<TSquare> iterator() {
        return new SquaresIterator();
    }
    
    // This method exists so we could call it from the constructor and not calling the corresponding overridable method
    private void internalSetNotConcluded() {
        this.owner = Optional.absent();
        this.status = GameStatus.NOT_CONCLUDED;
    }
    
    private class SquaresIterator extends AbstractIterator<TSquare> {
        private Iterator<GameBoardPosition> positionsIterator;
        
        public SquaresIterator() {
            this.positionsIterator = GameBoardPosition.iterate().iterator();
        }

        @Override
        protected TSquare computeNext() {
            if (this.positionsIterator.hasNext()) {
                return getSquareByPosition(this.positionsIterator.next());
            }
            
            return endOfData();
        }
    }
}
