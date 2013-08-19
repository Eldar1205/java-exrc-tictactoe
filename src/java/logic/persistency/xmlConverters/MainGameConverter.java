/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logic.persistency.xmlConverters;

import logic.persistency.exceptions.XmlUniqueViolationException;
import com.google.common.base.Preconditions;
import java.util.List;
import common.model.game.GameBoardPosition;
import logic.engine.GameStatusManager;
import logic.model.SubGame;
import logic.persistency.exceptions.InvalidTictactoeXmlException;
import logic.persistency.exceptions.MainGameNotPlayedException;
import logic.persistency.exceptions.XmlConcludedGameException;
import logic.persistency.exceptions.XmlFullCurrentBoardException;
import logic.persistency.exceptions.XmlMissingHumanPlayerNameException;
import logic.persistency.xmlModels.ObjectFactory;

/**
 *
 * @author Eldar
 */
public class MainGameConverter {
    private BoardPositionConverter positionConverter;
    private PlayerIdentityConverter currentTurnConverter;
    private PlayerConverter playerConverter;
    private GameConverter<logic.model.SubGame, logic.persistency.xmlModels.Board> boardConverter;
    private ObjectFactory xmlModelsFactory;
    
    public MainGameConverter(ObjectFactory xmlModelsFactory) {
        this.positionConverter = new BoardPositionConverter();
        this.currentTurnConverter = new PlayerIdentityConverter();
        this.playerConverter = new PlayerConverter(xmlModelsFactory);
        this.boardConverter = new GameConverter<logic.model.SubGame, logic.persistency.xmlModels.Board>(new SubGameConverter(xmlModelsFactory));
        this.xmlModelsFactory = xmlModelsFactory;    
    }    
    
    public logic.model.MainGame fromXml(logic.persistency.xmlModels.Tictactoe xmlModel) 
            throws XmlMissingHumanPlayerNameException, XmlUniqueViolationException, XmlFullCurrentBoardException, 
                   XmlConcludedGameException, InvalidTictactoeXmlException {        
        Preconditions.checkNotNull(xmlModel);
        
        logic.model.MainGame logicModel = logicPlayersFromXml(xmlModel.getPlayers());
        logicSubGamesFromXml(logicModel, xmlModel.getBoards());
        logicTurnStateFromXml(logicModel, xmlModel.getGame());
        
        return logicModel;
    }
    
    public logic.persistency.xmlModels.Tictactoe toXml(logic.model.MainGame logicModel) throws MainGameNotPlayedException {
        Preconditions.checkNotNull(logicModel);
        
        logic.persistency.xmlModels.Tictactoe xmlModel = this.xmlModelsFactory.createTictactoe();
        xmlModel.setPlayers(logicPlayersToXml(logicModel));
        xmlModel.setBoards(logicSubGamesToXml(logicModel));
        xmlModel.setGame(logicTurnStateToXml(logicModel));
        
        return xmlModel;
    }
    
    private logic.model.MainGame logicPlayersFromXml(logic.persistency.xmlModels.Tictactoe.Players xmlPlayersModel) 
            throws XmlMissingHumanPlayerNameException, XmlUniqueViolationException {
        List<logic.persistency.xmlModels.Player> xmlPlayersModelsList = xmlPlayersModel.getPlayer();
        
        // XSD ensures there will be two players        
        common.model.player.Player firstPlayer = this.playerConverter.fromXml(xmlPlayersModelsList.get(0));
        common.model.player.Player secondPlayer = this.playerConverter.fromXml(xmlPlayersModelsList.get(1));
        
        if (firstPlayer.getIdentity() == secondPlayer.getIdentity()) {
            throw new XmlUniqueViolationException("Player identity", firstPlayer.getIdentity().name());
        }
        
        return new logic.model.MainGame(firstPlayer, secondPlayer);
    }
    
    private logic.persistency.xmlModels.Tictactoe.Players logicPlayersToXml(logic.model.MainGame logicModel) {
        logic.persistency.xmlModels.Tictactoe.Players xmlPlayersModel = this.xmlModelsFactory.createTictactoePlayers();
        
        for (common.model.player.Player currLogicPlayerModel : logicModel.getPlayers()) {
            xmlPlayersModel.getPlayer().add(this.playerConverter.toXml(currLogicPlayerModel));
        }
        
        return xmlPlayersModel;
    }

    private void logicSubGamesFromXml(logic.model.MainGame logicModel, logic.persistency.xmlModels.Tictactoe.Boards xmlBoardsModel) throws XmlUniqueViolationException, XmlConcludedGameException, InvalidTictactoeXmlException {        
        this.boardConverter.fromXml(xmlBoardsModel.getBoard(), logicModel, new DuplicatePositionErrorFactory() {
            @Override
            public XmlUniqueViolationException createError(GameBoardPosition duplicatePosition) {
                return new XmlUniqueViolationException("Board position", positionConverter.xmlPositionTextForError(duplicatePosition));
            }
        });
        
        GameStatusManager<SubGame> manager = new GameStatusManager<SubGame>(logicModel);
        
        if (manager.refreshStatusIfNotConcluded()) {
            throw new XmlConcludedGameException();
        }
    }
    
    private logic.persistency.xmlModels.Tictactoe.Boards logicSubGamesToXml(logic.model.MainGame logicModel) throws MainGameNotPlayedException {
        logic.persistency.xmlModels.Tictactoe.Boards xmlBoardsModel = this.xmlModelsFactory.createTictactoeBoards();
        this.boardConverter.toXml(logicModel, xmlBoardsModel.getBoard());
        
        if (xmlBoardsModel.getBoard().isEmpty()) {
            throw new MainGameNotPlayedException();
        }
        
        return xmlBoardsModel;
    }
    
    private void logicTurnStateFromXml(logic.model.MainGame logicModel, logic.persistency.xmlModels.Tictactoe.Game xmlGameModel) throws XmlFullCurrentBoardException {
        logicModel.setTurnPlayer(this.currentTurnConverter.fromXml(xmlGameModel.getCurrentTurn()));
        logic.persistency.xmlModels.Tictactoe.Game.CurrentBoard xmlCurrentBoardModel = xmlGameModel.getCurrentBoard();
        
        if (xmlCurrentBoardModel == null) {
            logicModel.setPlayableTurnSubGames();
        } else {
            GameBoardPosition specificSubGamePosition = this.positionConverter.fromXml(xmlCurrentBoardModel.getRow(), xmlCurrentBoardModel.getCol());
            
            if (logicModel.getSquareByPosition(specificSubGamePosition).hasFreeSquares()) {
                logicModel.setTurnSubGame(specificSubGamePosition);
            } else {
                throw new XmlFullCurrentBoardException(xmlCurrentBoardModel.getRow(), xmlCurrentBoardModel.getCol());
            }
        }
    }    

    private logic.persistency.xmlModels.Tictactoe.Game logicTurnStateToXml(logic.model.MainGame logicModel) {
        logic.persistency.xmlModels.Tictactoe.Game xmlGameModel = this.xmlModelsFactory.createTictactoeGame();
        xmlGameModel.setCurrentTurn(this.currentTurnConverter.toXml(logicModel.getTurnPlayer().getIdentity()));
        logic.model.SubGame[] logicTurnSubGames = logicModel.getTurnSubGames();
        
        if (logicTurnSubGames.length > 1) {
            xmlGameModel.setCurrentBoard(null);
        } else {
            logic.persistency.xmlModels.Tictactoe.Game.CurrentBoard xmlCurrentBoardModel = this.xmlModelsFactory.createTictactoeGameCurrentBoard();
            GameBoardPosition logicTurnSubGamePosition = logicTurnSubGames[0].getPosition();
            xmlCurrentBoardModel.setRow(this.positionConverter.toXmlRow(logicTurnSubGamePosition));
            xmlCurrentBoardModel.setCol(this.positionConverter.toXmlColumn(logicTurnSubGamePosition));
            xmlGameModel.setCurrentBoard(xmlCurrentBoardModel);
        }
        
        return xmlGameModel;
    }
}
