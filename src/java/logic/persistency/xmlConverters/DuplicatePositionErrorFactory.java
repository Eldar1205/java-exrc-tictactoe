/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logic.persistency.xmlConverters;

import logic.persistency.exceptions.XmlUniqueViolationException;
import common.model.game.GameBoardPosition;

/**
 *
 * @author Eldar
 */
public interface DuplicatePositionErrorFactory {
    XmlUniqueViolationException createError(GameBoardPosition duplicatePosition);
}
