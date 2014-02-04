package be.tln.icc.util

import com.predic8.soamodel.Difference

class OperationRemovedChange {

    static def changeType = ChangeType.REMOVE_OPERATION

    static boolean isChangeType (Difference difference) {
        difference?.description ==~ /^Operation.*removed\.$/
    }

    static boolean isChange(Difference difference, Map args = [:]) {
        isChangeType(difference) &&
                (!args['name'] || difference?.description ==~ /.*${args['name']}.*/)
    }

}
