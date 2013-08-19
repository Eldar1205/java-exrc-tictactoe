/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package common.model.boardPosition;

import com.google.common.collect.AbstractSequentialIterator;
import com.google.common.collect.UnmodifiableIterator;
import common.utils.ThrowHelper;

/**
 *
 * @author Yasmin
 */
public class BoardPositionIterable<TPos extends BoardPosition> implements Iterable<TPos> {
    private BoardPositionFactory<TPos> factory;
    
    public BoardPositionIterable(BoardPositionFactory<TPos> factory){
        this.factory = ThrowHelper.throwOnNull(factory, "factory");
    }
            
    @Override
    public UnmodifiableIterator<TPos> iterator() {
        return new BoardPositionIterator();
    }
   
    private class BoardPositionIterator extends AbstractSequentialIterator<TPos>{
        public BoardPositionIterator() {
            super(factory.create(0, 0));
        }
        
        @Override
        protected TPos computeNext(TPos previous) {
            int nextRow = previous.getRow();
            int nextCol = previous.getCol() + 1;
            
            if (nextCol == factory.getDimension()) {
                nextCol = 0;
                nextRow++;
            }
            
            return (nextRow == factory.getDimension()) ? null : factory.create(nextRow, nextCol);
        }
    }
}
