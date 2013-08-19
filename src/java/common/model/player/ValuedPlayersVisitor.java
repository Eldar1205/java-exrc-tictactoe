/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package common.model.player;

/**
 *
 * @author Eldar
 */
public interface ValuedPlayersVisitor<T> {
    T visit(ComputerPlayer computerPlayer);
    T visit(HumanPlayer humanPlayer);
}
