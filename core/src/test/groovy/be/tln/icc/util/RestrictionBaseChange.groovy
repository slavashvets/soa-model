package be.tln.icc.util

import com.predic8.soamodel.Difference

class RestrictionBaseChange {

    static def changeType = ChangeType.RESTRICTION_BASE_CHANGE

    static boolean isChangeType (Difference difference) {
        difference?.type == 'restriction' && difference?.description ==~ /Restriction base has changed .*/
    }

    static boolean isChange (Difference difference, Map args = [:]) {
        isChangeType(difference) &&
                (!args['from'] || difference?.description ==~ /.*${args['from']} .*/) &&
                (!args['to'] || difference?.description ==~ /.*${args['to']}.*/)
    }

}
