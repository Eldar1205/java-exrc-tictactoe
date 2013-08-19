/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logic.model;

import com.google.common.collect.Lists;
import common.model.game.GameBoardPosition;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.EnumMap;
import common.utils.ThrowHelper;
import common.model.player.Player;
import common.model.player.PlayerIdentity;

/**
 *
 * @author Eldar
 */
public class MainGame extends Game<SubGame> {
    private EnumMap<PlayerIdentity, Player> players;
    private Player turnPlayer;
    private SubGame[][] subGamesBoard;
    private SubGame[] turnSubGames;
    
    public MainGame(Player firstPlayer, Player secondPlayer) { 
        ThrowHelper.throwOnNull(firstPlayer, "firstPlayer");
        ThrowHelper.throwOnNull(secondPlayer, "secondPlayer");
        
        initPlayers(firstPlayer, secondPlayer); 
        initSubGames();
    }
    
    public SubGame[] getTurnSubGames() {
        return this.turnSubGames.clone();
    }
    
    public void setTurnSubGame(GameBoardPosition turnSubGamePosition) {
        ThrowHelper.throwOnNull(turnSubGamePosition, "turnSubGamePosition");
        
        SubGame turnSubGame = this.getSquareByPosition(turnSubGamePosition);        
        this.turnSubGames = new SubGame[] { turnSubGame };
    }

    public void setTurnSubGames(SubGame[] subGames) {
        ThrowHelper.throwOnNull(subGames, "subGames");
        
        this.turnSubGames = subGames.clone();
    }
    
    public void setPlayableTurnSubGames() {
        ArrayList<SubGame> playables = Lists.newArrayListWithCapacity(GameBoardPosition.SQUARES_COUNT);

        for (SubGame currSubGame : this) {
            if (currSubGame.hasFreeSquares()) {
                playables.add(currSubGame);
            }
        }

        this.turnSubGames = playables.toArray(new SubGame[playables.size()]);
    }
    
    public Player getPlayerByIdentity(PlayerIdentity identity) {
        ThrowHelper.throwOnNull(identity, "identity");
        
        return this.players.get(identity);
    }
        
    public Player[] getPlayers() {
        return this.players.values().toArray(new Player[this.players.size()]);
    }
    
    public Player getTurnPlayer() {
        return this.turnPlayer;
    }
    
    public void setTurnPlayer(PlayerIdentity playerIdentity) {
        ThrowHelper.throwOnNull(playerIdentity, "playerIdentity");
        
        Player thePlayer = this.players.get(playerIdentity);
        
        if (thePlayer == null) {
            throw ThrowHelper.createUnsupportedEnumValue(playerIdentity);
        }
        
        this.turnPlayer = thePlayer;
    }    

    @Override
    public SubGame getSquareByPosition(GameBoardPosition position) {
        ThrowHelper.throwOnNull(position, "position");
        
        return this.subGamesBoard[position.getRow()][position.getCol()];
    }

    @Override
    public void setSquareByItsPosition(SubGame subGame) {
        ThrowHelper.throwOnNull(subGame, "square");
        
        this.subGamesBoard[subGame.getPosition().getRow()][subGame.getPosition().getCol()] = subGame;
    }

    private void initPlayers(Player firstPlayer, Player secondPlayer) {
        ThrowHelper.throwOnIllegalArgument(firstPlayer.getIdentity() == secondPlayer.getIdentity(), 
                "firstPlayer & secondPlayer", "Both players have same identity %s", firstPlayer.getIdentity().name());
        
        this.players = Maps.newEnumMap(PlayerIdentity.class);
        this.players.put(firstPlayer.getIdentity(), firstPlayer);
        this.players.put(secondPlayer.getIdentity(), secondPlayer);
        this.turnPlayer = this.players.get(PlayerIdentity.X);
    }

    private void initSubGames() {
        this.subGamesBoard = new SubGame[GameBoardPosition.DIMENSION][GameBoardPosition.DIMENSION];
        this.turnSubGames = new SubGame[GameBoardPosition.SQUARES_COUNT]; // Every sub game is playable at first
        
        for (GameBoardPosition currPos : GameBoardPosition.iterate()) {
            this.subGamesBoard[currPos.getRow()][currPos.getCol()] = new SubGame(currPos);
            this.turnSubGames[currPos.getRow() * GameBoardPosition.DIMENSION + currPos.getCol()] = 
                    this.subGamesBoard[currPos.getRow()][currPos.getCol()];
        }
    }
}
