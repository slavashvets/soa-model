package be.tln.icc.util

import com.predic8.soamodel.Difference

class ElementAddedChange {

    static def changeType = ChangeType.ELEMENT_ADDED

    static boolean isChangeType (Difference difference) {
        difference?.description ==~ /Element .* added.*/
    }

    static boolean isChange (Difference difference, Map args = [:]) {
        isChangeType(difference) &&
                (!args['name'] || difference?.description ==~ /.*${args['name']} .*/)
    }

}
