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
public class HumanPlayer extends Player {
    public HumanPlayer(PlayerIdentity identity, String name) {        
        super(identity, Optional.of(ThrowHelper.throwOnNull(name, "name")));
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
