/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logic.engine.playerAI;

import common.model.player.PlayerAI;
import common.utils.ThrowHelper;
import logic.model.MainGame;

/**
 *
 * @author Eldar
 */
public class PlayerAIFactory {
    public static PlayerAI create(MainGame game, int movesAhead) {
        ThrowHelper.throwOnIllegalArgument(movesAhead < 0, "movesAhead", "Value cannot be negetive");
        
        if (movesAhead == 0) {
            return new RandomAI(game);
        } else {
            return new MinMaxAI(game, movesAhead);
        }
    }
}
