/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logic.persistency.xmlConverters;

import com.google.common.base.Preconditions;
import common.utils.ThrowHelper;

/**
 *
 * @author Eldar
 */
public class PlayerIdentityConverter {
    public common.model.player.PlayerIdentity fromXml(logic.persistency.xmlModels.GameValue xmlModel) {
        Preconditions.checkNotNull(xmlModel);
        
        switch (xmlModel) {
            case X:
                return common.model.player.PlayerIdentity.X;
            case O:
                return common.model.player.PlayerIdentity.O;
            default:
                throw ThrowHelper.createUnsupportedEnumValue(xmlModel);
        }
    }
    
    public logic.persistency.xmlModels.GameValue toXml(common.model.player.PlayerIdentity logicModel) {
        Preconditions.checkNotNull(logicModel);
        
        switch (logicModel) {
            case X:
                return logic.persistency.xmlModels.GameValue.X;
            case O:
                return logic.persistency.xmlModels.GameValue.O;
            default:
                throw ThrowHelper.createUnsupportedEnumValue(logicModel);
        }
    }
}
