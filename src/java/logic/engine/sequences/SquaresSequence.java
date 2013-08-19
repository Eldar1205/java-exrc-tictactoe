/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logic.engine.sequences;

import com.google.common.base.Optional;
import com.google.common.collect.EnumMultiset;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multiset;
import com.google.common.collect.UnmodifiableIterator;
import java.util.Set;
import common.model.game.GameBoardPosition;
import common.model.player.PlayerIdentity;
import logic.model.Game;
import common.model.game.ReadOnlySquare;
import common.utils.ThrowHelper;

/**
 *
 * @author Eldar
 */
public abstract class SquaresSequence<TSquare extends ReadOnlySquare> implements Iterable<TSquare> {
    private Game<TSquare> game;
    private ImmutableList<TSquare> squares;
    private Optional<PlayerIdentity> promisedPlayer;
    private Optional<Integer> promiseRank;
    
    protected SquaresSequence(Game<TSquare> game) {        
        this.game = ThrowHelper.throwOnNull(game, "game");
    }
    
    public boolean isPromising() {
        validateStatsGathered();
        
        return this.promiseRank.isPresent();
    }
    
    public boolean hasPromisedPlayer() {
        validateStatsGathered();
        
        return this.promisedPlayer.isPresent();
    }
    
    public boolean isPromisingForPlayer(PlayerIdentity player) {
        ThrowHelper.throwOnNull(player, "player");
        validateStatsGathered();
        
        if (!this.isPromising()) {
            return false;
        }
        
        return this.promiseRank.get() == 0 || this.promisedPlayer.equals(Optional.of(player));
    }
    
    public Optional<Integer> getPromiseRank() {
        return this.promiseRank;
    }
    
    public boolean hasWinner() {
        validateStatsGathered();
        
        return this.promiseRank.equals(Optional.of(GameBoardPosition.DIMENSION));
    }
    
    public boolean isWinner(PlayerIdentity player) {
        ThrowHelper.throwOnNull(player, "player");
        validateStatsGathered();
        
        return getWinner().equals(Optional.of(player));
    }
    
    public Optional<PlayerIdentity> getWinner() {
        validateStatsGathered();
        
        if (hasWinner()) {
            return this.promisedPlayer;
        } else {
            return Optional.absent();
        }
    }
    
    @Override
    public UnmodifiableIterator<TSquare> iterator() {
        return this.squares.iterator();
    }
    
    protected abstract GameBoardPosition generatePosition(int squareIndex);

    private void validateStatsGathered() {
        if (this.squares == null) {
            setEmpty(); // Setting to empty so optional fields won't be null
            initSquares();
            gatherStats();
        }
    }
    
    private void gatherStats() {
        Multiset<PlayerIdentity> squaresOwnership = scanSquaresOwnership();
        
        if (this.isPromising()) { // Sequence can be set to not-promising while scanning due to not-ownable squares
            updateStatsByOwnership(squaresOwnership);
        }
    }
    
    private Multiset<PlayerIdentity> scanSquaresOwnership() {
        Multiset<PlayerIdentity> playersSquaresOwnage = EnumMultiset.create(PlayerIdentity.class);
        
        for (ReadOnlySquare currSquare : this.squares) {
            if (!currSquare.isOwnable()) {
                setNotPromising();
                break;
            } else if (currSquare.hasOwner()) {
                playersSquaresOwnage.add(currSquare.getOwner().get());
            }
        }
        
        return playersSquaresOwnage;
    }
    
    private void updateStatsByOwnership(Multiset<PlayerIdentity> squaresOwnership) {
        Set<PlayerIdentity> playersOwningSquares = squaresOwnership.elementSet();
    
        if (playersOwningSquares.isEmpty()) {
            setEmpty(); 
        } else if (playersOwningSquares.size() == 1) {
            this.promisedPlayer = Optional.of(playersOwningSquares.iterator().next());
            this.promiseRank = Optional.of(squaresOwnership.size());
        } else if (playersOwningSquares.size() > 1) {
            setNotPromising();
        }
    }
        
    private void initSquares() {
        ImmutableList.Builder<TSquare> builder = ImmutableList.builder();

        for (int i = 0; i < GameBoardPosition.DIMENSION; ++i) {
            builder.add(this.game.getSquareByPosition(generatePosition(i)));
        }
        
        this.squares = builder.build();
    }
    
    private void setNotPromising() {
        this.promiseRank = Optional.absent();
        this.promisedPlayer = Optional.absent();
    }
    
    private void setEmpty() {
        this.promiseRank = Optional.of(0);
        this.promisedPlayer = Optional.absent();
    }
}
