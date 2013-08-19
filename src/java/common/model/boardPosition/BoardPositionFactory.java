/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package common.model.boardPosition;

/**
 *
 * @author Yasmin
 */
public interface BoardPositionFactory<TPos extends BoardPosition> {
    TPos create(int row, int col);
    int getDimension();
}
