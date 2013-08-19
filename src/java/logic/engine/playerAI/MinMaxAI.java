/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logic.engine.playerAI;

import common.model.player.PlayerAI;
import com.google.common.base.Optional;
import common.utils.ThrowHelper;
import common.model.game.GameMove;
import common.model.player.PlayerIdentity;
import logic.model.BasicSquare;
import logic.model.MainGame;
import logic.model.SubGame;
import logic.engine.BasicGameEngine;

/**
 *
 * @author Eldar
 */
public class MinMaxAI implements PlayerAI {
    private MainGame game;
    private PlayerIdentity maximizingPlayer;
    private int movesAhead;
    private GameMove bestMove;
    
    public MinMaxAI(MainGame game, int movesAhead) {
        ThrowHelper.throwOnIllegalArgument(movesAhead <= 0, "movesAhead", "Value isn't higher than zero");
        this.game = ThrowHelper.throwOnNull(game, "game");
        this.movesAhead = movesAhead;
    }
    
    @Override
    public GameMove chooseMove() {
        this.bestMove = null;
        this.maximizingPlayer = this.game.getTurnPlayer().getIdentity();
        new MinMaxStage().recEvaluate(true, this.movesAhead, Integer.MIN_VALUE, Integer.MAX_VALUE);
        
        ThrowHelper.throwOnIllegalState(this.bestMove == null, "chooseMove called on concluded game");
        return this.bestMove;
    }
    
    private class MinMaxStage {
        private PlayerIdentity capturedTurnPlayer;
        private SubGame[] capturedTurnSubGames;
        private SubGame capturedChosenSubGame;
        private BasicSquare capturedChosenBasicSquare;
        private boolean wasMainGameConcludedByMove;
        private boolean wasChosenSubGameConcludedByMove;
        
        public int recEvaluate(boolean isRoot, int depth, int alpha, int beta) {
            if (depth == 0 || game.isConcluded()) {
                return MainGameEvaluator.evaluate(game, maximizingPlayer);
            }
            
            boolean isMaximizingPlayer = game.getTurnPlayer().getIdentity() == maximizingPlayer;
            
            for (GameMove possibleMove : PossibleMovesGenerator.generate(game)) {
                capture(possibleMove);
                play(possibleMove);

                // Only a max node may be a root
                if (!isMaximizingPlayer) { 
                    beta = Math.min(beta, new MinMaxStage().recEvaluate(false, depth - 1, alpha, beta));
                } else if (!isRoot) {
                    alpha = Math.max(alpha, new MinMaxStage().recEvaluate(false, depth - 1, alpha, beta));
                } else { // Root max node
                    int origAlpha = alpha;
                    alpha = Math.max(alpha, new MinMaxStage().recEvaluate(false, depth - 1, alpha, beta));
                    
                    if (alpha > origAlpha) {
                        bestMove = possibleMove;
                    }
                }

                restore();

                if (alpha >= beta) {
                    break;
                }
            }

            return isMaximizingPlayer ? alpha : beta;
        }

        private void capture(GameMove move) {
            this.capturedTurnSubGames = game.getTurnSubGames();
            this.capturedTurnPlayer = game.getTurnPlayer().getIdentity();
            this.capturedChosenSubGame = game.getSquareByPosition(move.getSubGamePosition());
            this.capturedChosenBasicSquare = this.capturedChosenSubGame.getSquareByPosition(move.getBasicSquarePosition());
        }

        private void play(GameMove move) {
            boolean isSubGameNotConcludedBeforeMove = !this.capturedChosenSubGame.isConcluded();
            new BasicGameEngine(game).play(move);        
            this.wasChosenSubGameConcludedByMove = isSubGameNotConcludedBeforeMove && this.capturedChosenSubGame.isConcluded();
            this.wasMainGameConcludedByMove = game.isConcluded();
        }

        private void restore() {
            this.capturedChosenBasicSquare.setOwner(Optional.<PlayerIdentity>absent());
            game.setTurnSubGames(this.capturedTurnSubGames);        
            game.setTurnPlayer(this.capturedTurnPlayer);

            if (this.wasChosenSubGameConcludedByMove) {
                this.capturedChosenSubGame.setNotConcluded();
            }

            if (this.wasMainGameConcludedByMove) {
                game.setNotConcluded();
            }
        }
    }
}
