/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logic.persistency;

import com.google.common.base.Optional;
import common.model.player.Player;
import common.utils.ThrowHelper;
import java.io.File;
import logic.model.MainGame;
import logic.persistency.exceptions.GamesXmlRepositoryException;
import logic.persistency.exceptions.InvalidTictactoeXmlException;
import logic.persistency.exceptions.MainGameNotPlayedException;
import logic.persistency.exceptions.XmlSchemaViolationException;

/**
 *
 * @author Eldar
 */
public class GamePersistencyTracker {
    private final MainGame game;
    private Optional<File> defaultSaveFile;
    private GamesXmlRepository gamesRepository;
    private Optional<File> loadedGameFile;
    
    private GamePersistencyTracker(MainGame game, GamesXmlRepository gamesRepository, Optional<File> defaultSaveFile) {
        this.game = game;
        this.gamesRepository = gamesRepository;
        this.defaultSaveFile = defaultSaveFile;
        this.loadedGameFile = defaultSaveFile; // See static creation methods to see why loaded game file is set to initial default save file
    }
    
    public static GamePersistencyTracker trackNewGame(MainGame game) {
        return new GamePersistencyTracker(ThrowHelper.throwOnNull(game, "game"), new GamesXmlRepository(), Optional.<File>absent());
    }
    
    public static GamePersistencyTracker loadAndTrackGame(File file) throws XmlSchemaViolationException, InvalidTictactoeXmlException, GamesXmlRepositoryException {
        ThrowHelper.throwOnNull(file, "file");
        GamesXmlRepository gamesRepository = new GamesXmlRepository();
        MainGame game = gamesRepository.loadGame(file);
        
        return new GamePersistencyTracker(game, gamesRepository, Optional.of(file));
    }
    
    public MainGame getGame() {
        return this.game;
    }
    
    public boolean isLoadedGame() {
        return this.loadedGameFile.isPresent();
    }
    
    public Optional<File> getLoadedGameFile() {
        return this.loadedGameFile;
    }
    
    public boolean hasDefaultSaveFile() {
        return this.defaultSaveFile.isPresent();
    }
    
    public Optional<File> getDefaultSaveFile() {
        return this.defaultSaveFile;
    }
    
    public void saveGame(File file) throws MainGameNotPlayedException, GamesXmlRepositoryException {
        this.gamesRepository.saveGame(ThrowHelper.throwOnNull(file, "file"), this.game);
        this.defaultSaveFile = Optional.of(file); // Save succeeded, update default save file
    }
}
