/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logic.model;

import common.model.game.GameBoardPosition;
import com.google.common.base.Optional;
import common.model.game.ReadOnlyBasicSquare;
import common.utils.ThrowHelper;
import common.model.player.PlayerIdentity;

/**
 *
 * @author Eldar
 */
public class BasicSquare implements ReadOnlyBasicSquare {
    private GameBoardPosition position;
    private Optional<PlayerIdentity> owner;

    public BasicSquare(GameBoardPosition position) {        
        this.position = ThrowHelper.throwOnNull(position, "position");        
        this.owner = Optional.absent();
    }
    
    @Override
    public GameBoardPosition getPosition() {
        return position;
    }

    @Override
    public boolean isOwnable() {
        return true; 
    }

    @Override
    public boolean hasOwner() {
        return this.owner.isPresent();
    }
    
    @Override
    public Optional<PlayerIdentity> getOwner() {
        return this.owner;
    }

    public void setOwner(Optional<PlayerIdentity> owner) {
        this.owner = owner; // Absent is allowed because AI algorithms may wish to undo play operations while performing look-ahead techniques
    }
}
