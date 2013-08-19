/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package common.model.player;

import com.google.common.base.Optional;
import common.utils.ThrowHelper;
import common.model.game.GameDifficulty;

/**
 *
 * @author Eldar
 */
public class ComputerPlayer extends Player {
    private GameDifficulty diffculty;
    
    public ComputerPlayer(PlayerIdentity identity, GameDifficulty diffculty, Optional<String> name) {        
        super(identity, name);
        this.diffculty = ThrowHelper.throwOnNull(diffculty, "diffculty");
    }

    public GameDifficulty getDiffculty() {
        return this.diffculty;
    }

    @Override
    protected void guardedAccept(PlayersVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    protected <T> T guardedAccept(ValuedPlayersVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
