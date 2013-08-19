/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logic.persistency.xmlConverters;

import com.google.common.base.Optional;
import logic.persistency.exceptions.InvalidTictactoeXmlException;
import logic.persistency.exceptions.XmlUniqueViolationException;

/**
 *
 * @author Eldar
 */
public interface XmlSquareConverter<TLogicModel, TXmlModel> {
    TLogicModel fromXml(TXmlModel xmlModel) throws XmlUniqueViolationException, InvalidTictactoeXmlException;
    Optional<TXmlModel> toXml(TLogicModel logicModel);
}