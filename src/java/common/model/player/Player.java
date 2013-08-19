/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package common.model.player;

import com.google.common.base.Optional;
import common.utils.ThrowHelper;

/**
 *
 * @author Eldar
 */
public abstract class Player {
    private PlayerIdentity identity;
    private Optional<String> name;
    
    protected Player(PlayerIdentity identity, Optional<String> name) {        
        this.identity = identity;
        this.name = name;
    }
   
    public PlayerIdentity getIdentity() {
        return this.identity;
    }
    
    public boolean hasName() {
        return this.name.isPresent();
    }
    
    public Optional<String> getName() {
        return this.name;
    }
    
    public void accept(PlayersVisitor visitor) {
        ThrowHelper.throwOnNull(visitor, "visitor");
        
        guardedAccept(visitor);
    }
    
    public <T> T accept(ValuedPlayersVisitor<T> visitor) {
        ThrowHelper.throwOnNull(visitor, "visitor");
        
        return guardedAccept(visitor);
    }
    
    protected abstract void guardedAccept(PlayersVisitor visitor);
    protected abstract <T> T guardedAccept(ValuedPlayersVisitor<T> visitor);
}
