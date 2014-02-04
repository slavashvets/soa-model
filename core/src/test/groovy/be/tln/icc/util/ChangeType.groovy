package be.tln.icc.util

// Kind of typing the diffs, might be better to do this with meta programming
// directly onto the Difference class
public enum ChangeType {

    NAMESPACE([NamespaceChange]),
    ADD_OPERATION([OperationAddedChange]),
    REMOVE_OPERATION([OperationRemovedChange]),
    FAULT_ADDED([FaultAddedChange]),
    ENUM_VALUE_ADDED([EnumerationValueAddedChange]),
    ENUM_VALUE_REMOVED([EnumerationValueRemovedChange]),
    SEQ_ADDED([SequenceAddedChange]),
    ELEMENT_ADDED([ElementAddedChange]),
    ELEMENT_REMOVED([ElementRemovedChange]),
    MIN_OCCURS([MinOccursChange]),
    IMPORT_REMOVED([ImportRemovedChange]),
    ELEMENT_POSITION([ElementPositionChange]),
    RESTRICTION_BASE_CHANGE([RestrictionBaseChange]),
    TYPE_CHANGE([TypeChange]),
    TYPE_ADDED([TypeAddedChange]),
    TYPE_REMOVED([TypeRemovedChange]);

    final List changeClasses

    private ChangeType(List changeClasses = []) {
        this.changeClasses = changeClasses.asImmutable()
    }

}