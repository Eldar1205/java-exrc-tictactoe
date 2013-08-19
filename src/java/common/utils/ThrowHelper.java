/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package common.utils;

import com.google.common.base.Preconditions;
import common.model.boardPosition.BoardPosition;

/**
 *
 * @author Eldar
 */
public class ThrowHelper {
    public static int throwOnArgumentOutOfRange(String argumentName, int paramValue, int lowerInclusiveBound, int upperExclusiveBound) {
        if (paramValue < lowerInclusiveBound || paramValue >= upperExclusiveBound) {
            String message = String.format("Argument '%s' is %d and must be between %d and %d (inclusive)", 
                argumentName, paramValue, lowerInclusiveBound, upperExclusiveBound - 1);
        
            throw new IllegalArgumentException(message);
        }
        
        return paramValue;
    }
       
    public static void throwOnIllegalArgument(boolean throwCondition, String argumentName, String errorMessageTemplate, Object... args) {
        ThrowHelper.throwOnIllegalArgument(throwCondition, argumentName, String.format(errorMessageTemplate, args));
    }
    
    public static void throwOnIllegalArgument(boolean throwCondition, String argumentName, String errorMessage) {        
        Preconditions.checkArgument(!throwCondition, "%s%sArgument: %s", errorMessage, System.lineSeparator(), argumentName);
    }
    
    public static void throwOnIllegalState(boolean throwCondition, String errorMessage) {
        Preconditions.checkState(!throwCondition, errorMessage);
    }
    
    public static <T>  T throwOnNull(T reference, String argumentName) {
        return Preconditions.checkNotNull(reference, "Null is not allowed for %s", argumentName);
    }
    
    public static <E extends Enum<E>> UnsupportedOperationException createUnsupportedEnumValue(E enumValue){
        String message = String.format("%s.%s is not supported", enumValue.getDeclaringClass().getName(), enumValue.name());
        
        return new UnsupportedOperationException(message);
    }
    
    public static String positionTextForError(BoardPosition position) {        
        return positionTextForError(position.getRow(), position.getCol());
    }
    
    public static String positionTextForError(int row, int column) {        
        return String.format("[%d,%d]", row, column);
    }
}
