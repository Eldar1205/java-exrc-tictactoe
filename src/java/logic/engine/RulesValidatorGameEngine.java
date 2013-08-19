/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logic.engine;

import common.utils.ThrowHelper;
import java.util.Arrays;
import common.model.game.GameBoardPosition;
import common.model.game.GameMove;
import logic.model.BasicSquare;
import logic.model.MainGame;
import logic.model.SubGame;

/**
 *
 * @author Eldar
 */
public class RulesValidatorGameEngine implements GameEngine {
    private GameEngine innerEngine;

    public RulesValidatorGameEngine(GameEngine innerEngine) {
        this.innerEngine = ThrowHelper.throwOnNull(innerEngine, "innerEngine");
    }
    
    @Override
    public MainGame getMainGame() {
        return this.innerEngine.getMainGame();
    }
    
    @Override
    public void play(GameMove move) {
        ThrowHelper.throwOnNull(move, "move");
        
        this.validateMove(move);
        this.innerEngine.play(move);
    }
    
    private void validateMove(GameMove move) { 
        ThrowHelper.throwOnIllegalState(getMainGame().isConcluded(), "Concluded game has been played");
        
        SubGame subGame = valiadatePlayableSubGame(move.getSubGamePosition());
        validateFreeBasicSquare(subGame, move.getBasicSquarePosition());
    }

    private SubGame valiadatePlayableSubGame(GameBoardPosition subGamePosition) {
        SubGame subGame = getMainGame().getSquareByPosition(subGamePosition);
        
        ThrowHelper.throwOnIllegalArgument(!Arrays.asList(getMainGame().getTurnSubGames()).contains(subGame), "subGamePosition", 
                "%s is not a position of a playable sub game", ThrowHelper.positionTextForError(subGamePosition));
        
        return subGame;
    }
    
    private void validateFreeBasicSquare(SubGame subGame, GameBoardPosition basicSquarePosition) {
        GameBoardPosition subGamePosition = subGame.getPosition();
        BasicSquare basicSquare = subGame.getSquareByPosition(basicSquarePosition);
        
        ThrowHelper.throwOnIllegalArgument(basicSquare.hasOwner(), "basicSquarePosition", 
                "Basic square positioned at %s in sub game positioned at %s is already owned by %s", 
                ThrowHelper.positionTextForError(basicSquarePosition), 
                ThrowHelper.positionTextForError(subGamePosition),
                basicSquare.getOwner());
    }
}
