/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logic.engine;

import common.model.game.GameMove;
import logic.model.MainGame;

/**
 *
 * @author Eldar
 */
public interface GameEngine {
    MainGame getMainGame();
    void play(GameMove move);
}
