package be.tln.icc.util

import com.predic8.soamodel.Difference

class NamespaceChange {

    static def changeType = ChangeType.NAMESPACE

    static boolean isChangeType (Difference difference) {
        (!difference?.type || difference?.type == 'targetNamespace') &&
                difference?.description ==~ /(?i)TargetNamespace.*/
    }

    static boolean isChange(Difference difference, Map args = [:]) {
        isChangeType(difference) &&
            (!args['from']  || difference?.description ==~ /.*from ${args['from']}.*/) &&
            (!args['to']    || difference?.description ==~ /.*to ${args['to']}.*/)
    }

}
