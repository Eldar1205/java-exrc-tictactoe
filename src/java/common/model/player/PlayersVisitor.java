/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package common.model.player;

/**
 *
 * @author Eldar
 */
public interface PlayersVisitor {
    void visit(ComputerPlayer computerPlayer);
    void visit(HumanPlayer humanPlayer);
}
