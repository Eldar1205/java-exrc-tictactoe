/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logic.persistency.exceptions;

/**
 *
 * @author Eldar
 */
public class XmlConcludedGameException extends InvalidTictactoeXmlException {
    public XmlConcludedGameException() {
        super("Xml boards represent a concluded game (not a valid saved game file)");
    }    
}
