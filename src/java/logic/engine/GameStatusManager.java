/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logic.engine;

import logic.engine.sequences.SquaresSequence;
import logic.engine.sequences.PromisingSequencesGenerator;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import common.utils.ThrowHelper;
import common.model.game.ReadOnlySquare;
import common.model.player.PlayerIdentity;
import logic.model.Game;

/**
 *
 * @author Eldar
 */
public class GameStatusManager<TSquare extends ReadOnlySquare> {
    private Game<TSquare> managedGame;
    
    public GameStatusManager(Game<TSquare> managedGame) {
        this.managedGame = ThrowHelper.throwOnNull(managedGame, "managedGame");
    }
    
    public boolean refreshStatusIfNotConcluded() {
        boolean statusChanged = false;
        
        if (!this.managedGame.isConcluded()) {
            ImmutableList<SquaresSequence<TSquare>> promisingSequences = PromisingSequencesGenerator.generate(this.managedGame);
        
            if (promisingSequences.isEmpty()) {
                this.managedGame.setTie();
                statusChanged = true;
            } else {
                for (SquaresSequence<TSquare> sequence : promisingSequences) {
                    Optional<PlayerIdentity> optionalWinner = sequence.getWinner();

                    if (optionalWinner.isPresent()) {
                        this.managedGame.setOwner(optionalWinner.get());
                        statusChanged = true;
                        break;
                    }
                }
            }
        }
        
        return statusChanged;
    }
    
    public boolean hasWinningSequence(PlayerIdentity playerIdentity) {
        ThrowHelper.throwOnNull(playerIdentity, "playerIdentity");
        
        for (SquaresSequence sequence : PromisingSequencesGenerator.generate(this.managedGame)) {            
            if (sequence.isWinner(playerIdentity)) {
                return true;
            }
        }
        
        return false;
    }
}
