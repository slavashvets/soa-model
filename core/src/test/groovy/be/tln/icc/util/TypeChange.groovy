package be.tln.icc.util

import com.predic8.soamodel.Difference

class TypeChange {

    static def changeType = ChangeType.TYPE_CHANGE

    static boolean isChangeType (Difference difference) {
        difference?.description ==~ /The type of .* changed from .* to .*/
    }

    static boolean isChange (Difference difference, Map args = [:]) {
        isChangeType(difference) &&
                (!args['from'] || difference?.description ==~ /.*${args['from']} .*/) &&
                (!args['to'] || difference?.description ==~ /.*${args['to']}.*/)
    }

}
