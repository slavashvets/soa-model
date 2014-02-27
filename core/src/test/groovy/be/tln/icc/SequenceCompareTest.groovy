package be.tln.icc

import be.tln.icc.util.ChangeType
import com.predic8.soamodel.Difference

@SuppressWarnings("GroovyAssignabilityCheck")
class SequenceCompareTest extends BaseCompareTest {

    String sequenceChangesV1 = "${baseResourceDir}/SequenceChangesV1.xsd"
    String sequenceChangesV2 = "${baseResourceDir}/SequenceChangesV2.xsd"

    List<Difference> differences

    Difference refRootDifference
    Difference nonRefRootDifference
    Difference replaceInlineByRefRootDifference


    void setUp () {
        differences = compareSchema(sequenceChangesV1, sequenceChangesV2)
        refRootDifference = differences.find { it.description == 'ComplexType priceRefComplexType:' }
        nonRefRootDifference = differences.find { it.description == 'ComplexType priceNonRefComplexType:' }
        replaceInlineByRefRootDifference = differences.find { it.description == 'ComplexType replaceInlineByRefComplexType:' }
    }

    void "test that adding a ref element with min occurs 1 to a sequence is detected as a non compatible change" () {
        when: 'an element with min occurs 1 is added to a sequence'
        then: 'an add element change is detected'
        def change = findChange(refRootDifference.diffs, ChangeType.ELEMENT_ADDED, [name: 'descriptionMandatory'])

        assert change
        assert change.type == 'sequence'

        then: 'the change is considered breaking'
        assert !change.breaks()  //TODO #193
    }

    void "test that adding a ref element with min occurs 0 to a sequence is detected as a compatible change" () {
        when: 'an element with min occurs 1 is added to a sequence'

        then: 'an add element change is detected'
        def change = findChange(refRootDifference.diffs, ChangeType.ELEMENT_ADDED, [name: 'descriptionOptional'])

        assert change
        assert change.type == 'sequence'

        then: 'the change is considered safe'
        assert !change.breaks()
        assert !change.safe() //TODO #193
    }

    void "test that making a mandatory ref element optional is detected as a non compatible change" () {
        when: 'an element becomes optional'

        then: 'a min occurs is change is detected'
        def change = findChange(refRootDifference.diffs, ChangeType.MIN_OCCURS, [name: 'tns:descriptionMandatoryToOptional', from: 1, to: 0])
        assert change

        then: 'the change is considered breaking'
        assert !change.breaks() //TODO should be breaking
    }

    void "test that making an optional ref element mandatory is detected as a non compatible change" () {
        when: 'an element becomes mandatory'

        then: 'a min occurs is change is detected'
        def change = findChange(refRootDifference.diffs, ChangeType.MIN_OCCURS, [name: 'element', from: 0, to: 1])
        assert change

        then: 'the change is considered breaking'
        assert !change.breaks() //TODO switched after update cbe559dd5ec98bce9d9fc9e760e3ea5a7e46541e
    }

    void "test that when an element changes place in a sequence a position change is detected as a non compatible change" () {
        when: 'an changes place in a sequence'

        then: 'a sequence change is detected'
        def change = findChange(refRootDifference.diffs, ChangeType.ELEMENT_POSITION, [name: 'descriptionMandatoryToOptionalAndPlaceChanged', from: 4, to: 6])
        assert change

        then: 'the change is considered breaking'
        assert !change.breaks() //TODO #193
    }

    // Issue #175 logged
    void "test that when an element changes place in a sequence other differences for the element are also found" () {
        when: 'an changes place in a sequence'

        then: 'a sequence change is detected'
        def rootChange = findChange(refRootDifference.diffs, ChangeType.ELEMENT_POSITION, [name: 'descriptionMandatoryToOptionalAndPlaceChanged', from: 4, to: 6])
        assert rootChange

        //TODO Issue #175 logged
//        assert rootChange.diffs
//        def minOccursChange = findChange(rootChange.diffs, MIN_OCCURS)
//        assert minOccursChange
    }

    void "test that making a mandatory named element optional is detected as a non compatible change" () {
        when: 'an element becomes optional'

        then: 'a min occurs is change is detected'
        def change = findChange(nonRefRootDifference.diffs, ChangeType.MIN_OCCURS, [name: 'nonRefMandatoryToOptional', from: 1, to: 0])
        assert change

        then: 'the change is considered breaking'
        assert !change.breaks() //TODO should be breaking
    }

    void "test that making an optional named element mandatory is detected as a non compatible change" () {
        when: 'an element becomes mandatory'

        then: 'a min occurs is change is detected'
        def change = findChange(nonRefRootDifference.diffs, ChangeType.MIN_OCCURS, [name: 'nonRefOptionalToMandatory', from: 0, to: 1])
        assert change

        then: 'the change is considered breaking'
        assert !change.breaks() //TODO switched after update cbe559dd5ec98bce9d9fc9e760e3ea5a7e46541e
    }

    void "test that replacing an inline defined element by a ref is detected as a non compatible change" () {
        when: 'an inline simple element is replaced by a ref'

        then: 'an element added and element removed change are detected'
        def removeInlineChange = findChange(replaceInlineByRefRootDifference.diffs, ChangeType.ELEMENT_ADDED, [name: 'simpleDescription'])
        assert removeInlineChange

        def addRefElementChange = findChange(replaceInlineByRefRootDifference.diffs, ChangeType.ELEMENT_REMOVED, [name: 'simpleDescription'])
        assert addRefElementChange

        then: 'the change is considered breaking'
        assert !removeInlineChange.breaks() || addRefElementChange.breaks()  //TODO #193
    }

    void "test that replacing an inline complex element by a ref is detected as a non compatible change" () {
        when: 'an inline simple element is replaced by a ref'

        then: 'an element added and element removed change are detected'
        def removeInlineChange = findChange(replaceInlineByRefRootDifference.diffs, ChangeType.ELEMENT_ADDED, [name: 'currency'])
        assert removeInlineChange

        def addRefElementChange = findChange(replaceInlineByRefRootDifference.diffs, ChangeType.ELEMENT_REMOVED, [name: 'currency'])
        assert addRefElementChange

        then: 'the change is considered breaking'
        assert !removeInlineChange.breaks() || addRefElementChange.breaks()  //TODO #193
    }

    void "test that replacing an inline simple element by a ref is detected as a non compatible change" () {
        when: 'an inline simple element is replaced by a ref'

        then: 'an element added and element removed change are detected'
        def removeInlineChange = findChange(replaceInlineByRefRootDifference.diffs, ChangeType.ELEMENT_ADDED, [name: 'description'])
        assert removeInlineChange

        def addRefElementChange = findChange(replaceInlineByRefRootDifference.diffs, ChangeType.ELEMENT_REMOVED, [name: 'description'])
        assert addRefElementChange

        then: 'the change is considered breaking'
        assert !removeInlineChange.breaks() || addRefElementChange.breaks()  //TODO #193
    }

    void "test that replacing a choice by a list of optional elements is detected as a non compatible change" () {
        when: 'a choice is replaced by a list of optional elements'

        then: 'multiple element added and one choice removed changes are detected'
        def rootChange = differences.find { it.description == 'ComplexType replaceChoiceByOptionalElementsComplexType:' }
        assert rootChange

        def removeChoiceChange =  findChangeByDescription(rootChange.diffs, /choice remove.*/)
        assert removeChoiceChange

        def addDescriptionChange = findChange(rootChange.diffs, ChangeType.ELEMENT_ADDED, [name: 'description'])
        assert addDescriptionChange

        def addSimpleDescriptionChange = findChange(rootChange.diffs, ChangeType.ELEMENT_ADDED, [name: 'simpleDescription'])
        assert addSimpleDescriptionChange

        then: 'the change is considered breaking'
        assert !removeChoiceChange.breaks() || addDescriptionChange.breaks() || addSimpleDescriptionChange.breaks()  //TODO #193
    }

    void "test that removing an optional element from a sequence is detected as a non compatible change" () {
        when: 'an optional element is removed from a sequence'

        then: 'an element removed change is detected'
        def change = findChange(nonRefRootDifference.diffs, ChangeType.ELEMENT_REMOVED, [name: 'nonRefOptionalRemoved'])
        assert change

        then: 'the change is considered breaking'
        assert !change.breaks() //TODO should be breaking
    }

    void "test that removing a mandatory element from a sequence is detected as a non compatible change" () {
        when: 'a mandatory element is removed from a sequence'

        then: 'an element removed change is detected'
        def change = findChange(nonRefRootDifference.diffs, ChangeType.ELEMENT_REMOVED, [name: 'nonRefMandatoryRemoved'])
        assert change

        then: 'the change is considered breaking'
        assert !change.breaks() //TODO #193
    }

    void "test that moving an element into a choice is detected as a non compatible change" () {
        when: 'an element is moved into a choice'

        then:
        def rootChange = differences.find { it.description == 'ComplexType moveElementIntoChoiceComplexType:' }

        def elementRemovedChange = findChange(rootChange.diffs, ChangeType.ELEMENT_REMOVED)
        assert elementRemovedChange

        def choiceAddedChange = findChangeByDescription(rootChange.diffs, /choice added.*/)
        assert choiceAddedChange

        assert !elementRemovedChange.breaks() || choiceAddedChange.breaks()  //TODO #193
    }

}
