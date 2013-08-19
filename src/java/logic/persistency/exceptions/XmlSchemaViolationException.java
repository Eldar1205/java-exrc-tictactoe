/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logic.persistency.exceptions;

/**
 *
 * @author Eldar
 */
public class XmlSchemaViolationException extends InvalidTictactoeXmlException {
    public XmlSchemaViolationException() {
        super("Xml file violates the schema");
    }    
}
