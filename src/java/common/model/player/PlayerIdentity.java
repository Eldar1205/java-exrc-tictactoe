/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package common.model.player;

/**
 *
 * @author Eldar
 */
public enum PlayerIdentity {
    X { 
        @Override public PlayerIdentity opposite() { return PlayerIdentity.O; }
    },
    O { 
        @Override public PlayerIdentity opposite() { return PlayerIdentity.X; }
    };
    
    public abstract PlayerIdentity opposite();
}
