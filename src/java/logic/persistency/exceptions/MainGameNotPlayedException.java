/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logic.persistency.exceptions;

/**
 *
 * @author Eldar
 */
public class MainGameNotPlayedException extends GamesXmlRepositoryException {
    public MainGameNotPlayedException() {
        super("Cannot save a game that hasn't been played");
    }    
}
