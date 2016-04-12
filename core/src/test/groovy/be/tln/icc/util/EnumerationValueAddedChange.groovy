package be.tln.icc.util

import com.predic8.soamodel.Difference

class EnumerationValueAddedChange {

    static def changeType = ChangeType.ENUM_VALUE_ADDED

    static boolean isChangeType (Difference difference) {
        difference?.type == 'facet' &&
                difference?.description ==~ /Enumerartion with value: .* added.*/
    }

    static boolean isChange (Difference difference, Map args = [:]) {
        isChangeType(difference) &&
                (!args['value'] || difference?.description ==~ /.*${args['value']} .*/)
    }

}
