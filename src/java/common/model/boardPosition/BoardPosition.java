/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package common.model.boardPosition;

import common.utils.ThrowHelper;

/**
 *
 * @author Yasmin
 */
public abstract class BoardPosition {
    private int row;
    private int col;

    protected BoardPosition(int row, int col, int dimension) {
        this.row = ThrowHelper.throwOnArgumentOutOfRange("row", row, 0, dimension);
        this.col = ThrowHelper.throwOnArgumentOutOfRange("col", col, 0, dimension);
    }

    public int getRow() {
        return this.row;
    }

    public int getCol() {
        return this.col;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 71 * hash + this.row;
        hash = 71 * hash + this.col;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final BoardPosition other = (BoardPosition) obj;
        if (this.row != other.row) {
            return false;
        }
        if (this.col != other.col) {
            return false;
        }
        return true;
    }
}
