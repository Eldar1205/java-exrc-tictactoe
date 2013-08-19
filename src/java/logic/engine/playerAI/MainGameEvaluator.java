/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logic.engine.playerAI;

import com.google.common.math.IntMath;
import logic.engine.sequences.SquaresSequence;
import common.model.player.PlayerIdentity;
import logic.model.MainGame;
import logic.model.SubGame;
import logic.engine.sequences.PromisingSequencesGenerator;
import logic.model.BasicSquare;
import common.model.game.ReadOnlySquare;

/**
 *
 * @author Eldar
 */
public class MainGameEvaluator {
    private static final int SUB_GAME_PROMISING_SEQUENCE_EXPONENT_ADD = 0;
    private static final int MAIN_GAME_PROMISING_SEQUENCE_EXPONDED_ADD = 2;
    private static final int PROMISING_SEQUENCE_SCORE_BASE = 10;
    private static final int WIN_SCORE = 1000000;
    
    private MainGame game;
    
    private MainGameEvaluator(MainGame game) {
        this.game = game;
    }
    
    public static int evaluate(MainGame game, PlayerIdentity player) {        
        MainGameEvaluator instance = new MainGameEvaluator(game);
        
        return instance.evaluateBenefitialFeatures(player) - instance.evaluateBenefitialFeatures(player.opposite());
    }    
    
    private int evaluateBenefitialFeatures(PlayerIdentity player) {
        if (this.game.isConcluded()) {
            return this.game.isOwner(player) ? WIN_SCORE : 0;
        }
        
        int benefit = 0;
        
        for (SquaresSequence<SubGame> mainGamePromisingSeq : PromisingSequencesGenerator.generate(this.game)) {
            if (mainGamePromisingSeq.isPromisingForPlayer(player)) {
                benefit += mainGamePromisingSequenceBenefit(mainGamePromisingSeq);
                
                for (SubGame subGame : mainGamePromisingSeq) {
                    if (!subGame.isConcluded()) {
                        for (SquaresSequence<BasicSquare> subGamePromisingSeq : PromisingSequencesGenerator.generate(subGame)) {
                            if (subGamePromisingSeq.isPromisingForPlayer(player)) {
                                benefit += subGamePromisingSequenceBenefit(subGamePromisingSeq);
                            }
                        }
                    }
                }
            }
        }
        
        return benefit;
    }
    
    private int mainGamePromisingSequenceBenefit(SquaresSequence<SubGame> mainGamePromisingSeq) {
        return promisingSequenceBenefit(mainGamePromisingSeq, MAIN_GAME_PROMISING_SEQUENCE_EXPONDED_ADD);
    }
    
    private int subGamePromisingSequenceBenefit(SquaresSequence<BasicSquare> subGamePromisingSeq) {
        return promisingSequenceBenefit(subGamePromisingSeq, SUB_GAME_PROMISING_SEQUENCE_EXPONENT_ADD);
    }
    
    private int promisingSequenceBenefit(SquaresSequence<? extends ReadOnlySquare> promisingSeq, int exponentAdd) {
        int scoreExponent = promisingSeq.getPromiseRank().get() + exponentAdd;
        
        return IntMath.pow(PROMISING_SEQUENCE_SCORE_BASE, scoreExponent);
    }
}
