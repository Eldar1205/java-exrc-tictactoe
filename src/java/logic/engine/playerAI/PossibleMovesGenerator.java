/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logic.engine.playerAI;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import common.model.game.GameMove;
import logic.model.BasicSquare;
import logic.model.MainGame;
import logic.model.SubGame;

/**
 *
 * @author Eldar
 */
public class PossibleMovesGenerator {
    public static ImmutableList<GameMove> generate(MainGame game) {
        ArrayList<GameMove> possibleMoves = Lists.newArrayList();
        
        for (SubGame possibleSubGame : game.getTurnSubGames()) {
            for (BasicSquare possibleBasicSquare : possibleSubGame.getFreeSquares()) {
                possibleMoves.add(new GameMove(possibleSubGame.getPosition(), possibleBasicSquare.getPosition()));
            }
        }
        
        return ImmutableList.copyOf(possibleMoves);
    }    
}
