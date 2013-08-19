/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logic.persistency.exceptions;

import com.google.common.base.Strings;

/**
 *
 * @author Eldar
 */
public class XmlUniqueViolationException extends InvalidTictactoeXmlException {
    private String duplicatedFieldName;
    private String duplicatedValueString;
    
    public XmlUniqueViolationException(String duplicatedFieldName, String duplicatedValueString) {
        this(duplicatedFieldName, duplicatedValueString, "Unique constraint violated");
    }
    
    public XmlUniqueViolationException(String duplicatedFieldName, String duplicatedValueString, String message) {
        super(message);
        this.duplicatedFieldName = duplicatedFieldName;
        this.duplicatedValueString = duplicatedValueString;
    } 
    
    @Override
    public String getMessage() {
        String duplicatedFieldNameClause = Strings.isNullOrEmpty(this.duplicatedFieldName) 
                ? ""
                : System.lineSeparator() + "Duplicated field name: " + this.duplicatedFieldName;
        String duplicatedValueClause = Strings.isNullOrEmpty(this.duplicatedValueString)
                ? ""
                : System.lineSeparator() + "Duplicated value: " + this.duplicatedValueString;
        
        return super.getMessage() + duplicatedFieldNameClause + duplicatedValueClause;
    }
}
