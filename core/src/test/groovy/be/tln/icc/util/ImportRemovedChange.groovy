package be.tln.icc.util

import com.predic8.soamodel.Difference

class ImportRemovedChange {

    static def changeType = ChangeType.IMPORT_REMOVED

    static boolean isChangeType (Difference difference) {
        difference?.type == 'import' && difference?.description ==~ /Imported .* removed./
    }

    static boolean isChange (Difference difference, Map args = [:]) {
        isChangeType(difference) &&
                (!args['namespace'] || difference?.description ==~ /.*namespace=${args['namespace']}.*/)
    }

}
