package be.tln.icc.util

import com.predic8.soamodel.Difference

class SequenceAddedChange {

    def changeType = ChangeType.SEQ_ADDED

    static boolean isChangeType (Difference difference) {
        difference?.description ==~ /sequence added\./
    }

    static boolean isChange(Difference difference, Map args = [:]) {
        isChangeType(difference)
    }

}
