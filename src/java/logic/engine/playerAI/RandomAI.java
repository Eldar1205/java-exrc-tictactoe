/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logic.engine.playerAI;

import common.model.player.PlayerAI;
import com.google.common.collect.ImmutableList;
import common.utils.ThrowHelper;
import java.util.Random;
import common.model.game.GameMove;
import logic.model.MainGame;

/**
 *
 * @author Eldar
 */
public class RandomAI implements PlayerAI {
    private MainGame game;
    private Random random;

    public RandomAI(MainGame game) {
        this.game = ThrowHelper.throwOnNull(game, "game");    
        this.random = new Random();
    }
    
    @Override
    public GameMove chooseMove() {
        ImmutableList<GameMove> possibleMoves = PossibleMovesGenerator.generate(this.game);
        
        return possibleMoves.get(random.nextInt(possibleMoves.size()));
    }
}
