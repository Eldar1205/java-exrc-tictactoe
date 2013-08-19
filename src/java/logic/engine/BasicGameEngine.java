/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logic.engine;

import com.google.common.base.Optional;
import common.utils.ThrowHelper;
import common.model.game.GameBoardPosition;
import common.model.game.GameMove;
import logic.model.BasicSquare;
import logic.model.MainGame;
import logic.model.SubGame;

/**
 *
 * @author Eldar
 */
public class BasicGameEngine implements GameEngine {
    private MainGame game;
    private GameStatusManager<SubGame> mainGameStatusManager;
    
    public BasicGameEngine(MainGame game) {        
        this.game = ThrowHelper.throwOnNull(game, "game");    
        this.mainGameStatusManager = new GameStatusManager<SubGame>(this.game);
    }
    
    @Override 
    public MainGame getMainGame() {
        return this.game;
    }
    
    @Override
    public void play(GameMove move)
    {
        ThrowHelper.throwOnNull(move, "move");
        
        playMove(move);
        setNextTurnSubGame(move.getBasicSquarePosition());
        setNextTurnPlayer();
    }
    
    private void playMove(GameMove move) {
        SubGame subGame = this.game.getSquareByPosition(move.getSubGamePosition());
        BasicSquare basicSquare = subGame.getSquareByPosition(move.getBasicSquarePosition());
        basicSquare.setOwner(Optional.of(this.game.getTurnPlayer().getIdentity()));
        
        if (new GameStatusManager<BasicSquare>(subGame).refreshStatusIfNotConcluded()) {
            this.mainGameStatusManager.refreshStatusIfNotConcluded();
        }
    }
    
    private void setNextTurnSubGame(GameBoardPosition basicSquarePosition) {
        SubGame nextSubGame = this.game.getSquareByPosition(basicSquarePosition);
        
        if (nextSubGame.hasFreeSquares()) {
            this.game.setTurnSubGame(basicSquarePosition);
        } else {
            this.game.setPlayableTurnSubGames();
        }
    }

    private void setNextTurnPlayer() {
        this.game.setTurnPlayer(this.game.getTurnPlayer().getIdentity().opposite());
    }
}
