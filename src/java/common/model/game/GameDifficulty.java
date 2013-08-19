/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package common.model.game;

/**
 *
 * @author Eldar
 */
public enum GameDifficulty {
    EASY {
        @Override public int movesAhead() { return 0; }
    },
    NORMAL {
        @Override public int movesAhead() { return 1; }
    },
    HARD {
        @Override public int movesAhead() { return 3; }
    },
    MASTER {
        @Override public int movesAhead() { return 5; }
    };
    
    public abstract int movesAhead();
}
