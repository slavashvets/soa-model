package be.tln.icc.util

import com.predic8.soamodel.Difference

class MinOccursChange {

    static def changeType = ChangeType.MIN_OCCURS

    static boolean isChangeType (Difference difference) {
        difference?.description ==~ /MinOccurs changed from .*/
    }

    static boolean isChange (Difference difference, Map args = [:]) {
        isChangeType(difference) &&
//                (!args['name'] || difference?.description ==~ /.*${args['name']} .*/) &&
                (!args['from'] || difference?.description ==~ /.* from ${args['from']} .*/) &&
                (!args['to'] || difference?.description ==~ /.*to ${args['to']}.*/)
    }

}
