/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package common.model.player;

import common.model.game.GameMove;

/**
 *
 * @author Eldar
 */
public interface PlayerAI {
    GameMove chooseMove();
}
