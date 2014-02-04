package be.tln.icc.util

import com.predic8.soamodel.Difference

class FaultAddedChange {

    static def changeType = ChangeType.FAULT_ADDED

    static boolean isChangeType (Difference difference) {
        difference?.type == 'fault' &&
            difference?.description ==~ /Fault with message .* added.*/
    }

    static boolean isChange (Difference difference, Map args = [:]) {
        isChangeType(difference) &&
            (!args['message'] || difference?.description ==~ /.*${args['message']} .*/)
    }

}
