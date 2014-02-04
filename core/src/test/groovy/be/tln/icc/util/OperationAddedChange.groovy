package be.tln.icc.util

import com.predic8.soamodel.Difference

class OperationAddedChange {

    static def changeType = ChangeType.ADD_OPERATION

    static boolean isChangeType (Difference difference) {
        difference?.description ==~ /^Operation.*added\.$/
    }

    static boolean isChange(Difference difference, Map args = [:]) {
        isChangeType(difference) &&
            (!args['name'] || difference?.description ==~ /.*${args['name']}.*/)
    }

}
