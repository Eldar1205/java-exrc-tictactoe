/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logic.persistency.exceptions;

import logic.persistency.xmlModels.GameValue;

/**
 *
 * @author Eldar
 */
public class XmlMissingHumanPlayerNameException extends InvalidTictactoeXmlException {
    private GameValue humanPlayerIdentity;
    
    public XmlMissingHumanPlayerNameException(GameValue humanPlayerIdentity) {
        super("Xml human player doesn't have name");
        this.humanPlayerIdentity = humanPlayerIdentity;    
    }
    
    @Override
    public String getMessage() {
        String humanPlayerIdentityClause = System.lineSeparator() + "Player: " + this.humanPlayerIdentity;
        
        return super.getMessage() + humanPlayerIdentityClause;
    }
}
