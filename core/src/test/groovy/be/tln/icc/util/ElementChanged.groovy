package be.tln.icc.util

import com.predic8.soamodel.Difference

class ElementChanged {

    static def changeType = ChangeType.ELEMENT_CHANGED

    static boolean isChangeType(Difference difference) {
        difference?.description ==~ /Element has changed from .* to .*/
    }

    static boolean isChange(Difference difference, Map args) {
        isChangeType(difference) &&
            (!args['from'] || difference.description ==~ /.*${args['from']}.*to.*/) &&
            (!args['to'] || difference.description ==~ /.*to.*${args['to']}.*/)
    }

}
