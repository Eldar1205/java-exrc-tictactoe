/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package common.model.game;

import com.google.common.base.Optional;
import common.model.player.PlayerIdentity;

/**
 *
 * @author Eldar
 */
public interface ReadOnlySquare {
    GameBoardPosition getPosition();
    boolean isOwnable();
    boolean hasOwner();
    Optional<PlayerIdentity> getOwner();
}
