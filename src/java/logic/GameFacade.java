/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import com.google.common.base.Optional;
import common.utils.ThrowHelper;
import logic.engine.BasicGameEngine;
import common.model.game.ReadOnlySubGame;
import logic.engine.GameEngine;
import logic.engine.RulesValidatorGameEngine;
import common.model.player.PlayerAI;
import logic.engine.playerAI.PlayerAIFactory;
import logic.model.MainGame;
import common.model.player.Player;
import common.model.player.PlayerIdentity;
import common.model.game.GameBoardPosition;
import common.model.game.GameDifficulty;
import common.model.game.GameMove;
import java.io.File;
import logic.persistency.GamePersistencyTracker;
import logic.persistency.exceptions.GamesXmlRepositoryException;

/**
 *
 * @author Eldar
 */
public class GameFacade {   
    private GameEngine engine;
    private GamePersistencyTracker persistencyTracker;
    private boolean hasUnsavedMoves;
    private boolean isSaveAllowed;
    
    private GameFacade(GamePersistencyTracker persistencyTracker) {
        this.persistencyTracker = persistencyTracker;
        this.engine = new RulesValidatorGameEngine(new BasicGameEngine(persistencyTracker.getGame()));
        this.hasUnsavedMoves = false;
        this.isSaveAllowed = persistencyTracker.isLoadedGame();
    }
    
    public static GameFacade newGame(Player firstPlayer, Player secondPlayer) {    
        MainGame game = new MainGame(firstPlayer, secondPlayer);
        game.setPlayableTurnSubGames();
        
        return new GameFacade(GamePersistencyTracker.trackNewGame(game));
    }
        
    public static GameFacade loadGame(String fileName) throws GamesXmlRepositoryException {
        return loadGame(new File(ThrowHelper.throwOnNull(fileName, "fileName")));
    }
    
    public static GameFacade loadGame(File file) throws GamesXmlRepositoryException {
        GamePersistencyTracker persistencyTracker = GamePersistencyTracker.loadAndTrackGame(file);
        
        return new GameFacade(persistencyTracker);
    }
    
    public boolean isSaveAllowed() {
        return this.isSaveAllowed;
    }
    
    public boolean hasUnsavedMoves() {
        return this.hasUnsavedMoves;
    }
    
    public boolean hasDefaultSaveFile() {
        return this.persistencyTracker.hasDefaultSaveFile();
    }
    
    public Optional<File> getDefaultSaveFile() {
        return this.persistencyTracker.getDefaultSaveFile();
    }
    
    public void save(String fileName) throws GamesXmlRepositoryException {
        save(new File(ThrowHelper.throwOnNull(fileName, "fileName")));
    }
    
    public void save(File file) throws GamesXmlRepositoryException {
        this.persistencyTracker.saveGame(file);
        this.hasUnsavedMoves = false;
    }
    
    public ReadOnlySubGame getSubGameByPosition(GameBoardPosition position) {        
        return getGame().getSquareByPosition(position);
    }
    
    public ReadOnlySubGame[] getTurnSubGames() {
        return getGame().getTurnSubGames();
    }
    
    public Player getPlayerByIdentity(PlayerIdentity identity) {
        return getGame().getPlayerByIdentity(identity);
    }
    
    public Player[] getPlayers() {
        return getGame().getPlayers();
    }
    
    public Player getTurnPlayer() {
        return getGame().getTurnPlayer();
    }
    
    public boolean isConcluded() {
        return getGame().isConcluded();
    }
    
    public boolean isTie() {
        return getGame().isTie();
    }
    
    public boolean hasWinner() {
        return getGame().hasOwner();
    }
    
    public Optional<PlayerIdentity> getWinner() {
        return getGame().getOwner();
    }
    
    public PlayerAI createComputerAI(GameDifficulty difficulty) {
        ThrowHelper.throwOnNull(difficulty, "difficulty");
        
        return PlayerAIFactory.create(getGame(), difficulty.movesAhead());
    }
    
    public void play(GameMove move) {
        this.engine.play(move);
        this.isSaveAllowed = true; // After at least one move was made, saving is allowed
        this.hasUnsavedMoves = true;
    }

    private MainGame getGame() {
        return this.persistencyTracker.getGame();
    }
}
