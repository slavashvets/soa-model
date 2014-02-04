package be.tln.icc.util

import com.predic8.soamodel.Difference

class ElementRemovedChange {

    static def changeType = ChangeType.ELEMENT_REMOVED

    static boolean isChangeType (Difference difference) {
        difference?.description ==~ /Element .* removed.*/
    }

    static boolean isChange (Difference difference, Map args = [:]) {
        isChangeType(difference) &&
                (!args['name'] || difference?.description ==~ /.*${args['name']} .*/)
    }

}
