/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logic.persistency.exceptions;

/**
 *
 * @author Eldar
 */
public class GamesXmlRepositoryException extends Exception {
    public GamesXmlRepositoryException(String message, Throwable cause) {
        super(message, cause);
    }
    
    protected GamesXmlRepositoryException(String message) {
        super(message);
    }
}
