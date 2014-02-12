package be.tln.icc.util

import com.predic8.soamodel.Difference

class TypeRemovedChange {

    static def changeType = ChangeType.TYPE_REMOVED

    static boolean isChangeType (Difference difference) {
        difference?.description ==~ /.*Type .* removed./
    }

    static boolean isChange (Difference difference, Map args = [:]) {
        isChangeType(difference) &&
                (!args['type'] || difference?.description ==~ /${args['type']}.*/) &&
                (!args['name'] || difference?.description ==~ /.*${args['name']}.*/)
    }

}
