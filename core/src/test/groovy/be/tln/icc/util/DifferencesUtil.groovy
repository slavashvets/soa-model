package be.tln.icc.util

import com.predic8.soamodel.Difference

class DifferencesUtil {

    static List<Difference> findChanges (List changes, ChangeType changeType, args = [:]) {
        changes.collect { findChanges(it, changeType, args) }.flatten()
    }

    /**
     * Throws AssertionError when more than one Difference is found
     */
    static Difference findChange (List changes, ChangeType changeType, args = [:]) {
        def differences = changes.collect { findChanges(it, changeType, args) }.flatten()
        if (differences.size() > 1) {
            throw new AssertionError("Only one Difference expected but found ${differences.size()} differences")
        }
        differences ? differences.first() : null
    }

    static boolean containsChange (List changes, ChangeType changeType, args = [:]) {
        changes.collect { containsChange(it, changeType, args) }.find { it }
    }

    static List<Difference> findChanges (Difference change, ChangeType changeType, args = [:]) {
        (isChange(change, changeType, args) ? [change] : []) + change.diffs.collect { findChanges(it, changeType, args) }.flatten()
    }

    static boolean containsChange (Difference change, ChangeType changeType, args = [:]) {
        if (isChange(change, changeType, args)) {
            true
        } else {
            change.diffs.collect { isChange(it, changeType, args) }.find { it }
        }
    }

    //TODO have changes register themselves, instead of listing here
    static boolean isChange (Difference change, ChangeType changeType, args = [:]) {
        changeType.changeClasses.collect { it.isChange(change, args) }.find { it }
    }

}
