/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logic.engine.sequences;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import common.utils.ThrowHelper;
import common.model.game.GameBoardPosition;
import java.util.ArrayList;
import java.util.List;
import logic.model.Game;
import common.model.game.ReadOnlySquare;

/**
 *
 * @author Eldar
 */
public class PromisingSequencesGenerator {
    public static <TSquare extends ReadOnlySquare> ImmutableList<SquaresSequence<TSquare>> generate(Game<TSquare> game) {
        ThrowHelper.throwOnNull(game, "game");
        
        ArrayList<SquaresSequence<TSquare>> promisingSequences = Lists.newArrayListWithCapacity(GameBoardPosition.DIMENSION + 2);

        for (int i = 0; i < GameBoardPosition.DIMENSION; ++i) {
            addIfPromising(promisingSequences, new RowSequence<TSquare>(game, i));
            addIfPromising(promisingSequences, new ColumnSequence<TSquare>(game, i));
        }
        
        addIfPromising(promisingSequences, new MainDiagonalSequence<TSquare>(game));
        addIfPromising(promisingSequences, new SecondaryDiagonalSequence<TSquare>(game));
        
        return ImmutableList.copyOf(promisingSequences);
    }
    
    private static <TSquare extends ReadOnlySquare> void addIfPromising(List<SquaresSequence<TSquare>> promisingSequences, SquaresSequence<TSquare> seq) {
        if (seq.isPromising()) {
            promisingSequences.add(seq);
        }
    }
}
