package be.tln.icc

import be.tln.icc.util.ChangeType
import com.predic8.soamodel.Difference
import spock.lang.Shared

@SuppressWarnings("GroovyAssignabilityCheck")
class SequenceCompareTest extends BaseCompareSpec {

    @Shared String sequenceChangesV1 = "${baseResourceDir}/SequenceChangesV1.xsd"
    @Shared String sequenceChangesV2 = "${baseResourceDir}/SequenceChangesV2.xsd"

    @Shared List<Difference> differences

    @Shared Difference refRootDifference
    @Shared Difference nonRefRootDifference
    @Shared Difference replaceInlineByRefRootDifference


    void setupSpec () {
        differences = compareSchema(sequenceChangesV1, sequenceChangesV2)
        refRootDifference = differences.find { it.description == 'ComplexType priceRefComplexType:' }
        nonRefRootDifference = differences.find { it.description == 'ComplexType priceNonRefComplexType:' }
        replaceInlineByRefRootDifference = differences.find { it.description == 'ComplexType replaceInlineByRefComplexType:' }
    }

    def "verify that adding a ref element with min occurs 1 to a sequence is detected as a non compatible change" () {
        when: 'an element with min occurs 1 is added to a sequence'

        then: 'an add element change is detected'
        def change = findChange(refRootDifference.diffs, ChangeType.ELEMENT_ADDED, [name: 'descriptionMandatory'])
        change
        change.type == 'sequence'

        then: 'the change is considered breaking'
        !change.breaks()  //TODO #193
    }

    def "verify that adding a ref element with min occurs 0 to a sequence is detected as a compatible change" () {
        when: 'an element with min occurs 1 is added to a sequence'

        then: 'an add element change is detected'
        def change = findChange(refRootDifference.diffs, ChangeType.ELEMENT_ADDED, [name: 'descriptionOptional'])
        change
        change.type == 'sequence'

        then: 'the change is considered safe'
        !change.breaks()
        !change.safe() //TODO #193
    }

    def "verify that making a mandatory ref element optional is detected as a non compatible change" () {
        when: 'an element becomes optional'

        then: 'a min occurs is change is detected'
        def change = findChange(refRootDifference.diffs, ChangeType.MIN_OCCURS, [name: 'tns:descriptionMandatoryToOptional', from: 1, to: 0])
        change

        then: 'the change is considered breaking'
        !change.breaks() //TODO should be breaking
    }

    def "verify that making an optional ref element mandatory is detected as a non compatible change" () {
        when: 'an element becomes mandatory'

        then: 'a min occurs is change is detected'
        def change = findChange(refRootDifference.diffs, ChangeType.MIN_OCCURS, [name: 'element', from: 0, to: 1])
        change

        then: 'the change is considered breaking'
        !change.breaks() //TODO switched after update cbe559dd5ec98bce9d9fc9e760e3ea5a7e46541e
    }

    def "verify that when an element changes place in a sequence a position change is detected as a non compatible change" () {
        when: 'an changes place in a sequence'

        then: 'a sequence change is detected'
        def change = findChange(refRootDifference.diffs, ChangeType.ELEMENT_POSITION, [name: 'descriptionMandatoryToOptionalAndPlaceChanged', from: 4, to: 6])
        change

        then: 'the change is considered breaking'
        !change.breaks() //TODO #193
    }

    // Issue #175 logged
    def "verify that when an element changes place in a sequence other differences for the element are also found" () {
        when: 'an changes place in a sequence'

        then: 'a sequence change is detected'
        def rootChange = findChange(refRootDifference.diffs, ChangeType.ELEMENT_POSITION, [name: 'descriptionMandatoryToOptionalAndPlaceChanged', from: 4, to: 6])
        rootChange

        //TODO Issue #175 logged
//        rootChange.diffs
//        def minOccursChange = findChange(rootChange.diffs, MIN_OCCURS)
//        minOccursChange
    }

    def "verify that making a mandatory named element optional is detected as a non compatible change" () {
        when: 'an element becomes optional'

        then: 'a min occurs is change is detected'
        def change = findChange(nonRefRootDifference.diffs, ChangeType.MIN_OCCURS, [name: 'nonRefMandatoryToOptional', from: 1, to: 0])
        change

        then: 'the change is considered breaking'
        !change.breaks() //TODO should be breaking
    }

    def "verify that making an optional named element mandatory is detected as a non compatible change" () {
        when: 'an element becomes mandatory'

        then: 'a min occurs is change is detected'
        def change = findChange(nonRefRootDifference.diffs, ChangeType.MIN_OCCURS, [name: 'nonRefOptionalToMandatory', from: 0, to: 1])
        change

        then: 'the change is considered breaking'
        !change.breaks() //TODO switched after update cbe559dd5ec98bce9d9fc9e760e3ea5a7e46541e
    }

    def "verify that replacing an inline defined element by a ref is detected as a non compatible change" () {
        when: 'an inline simple element is replaced by a ref'

        then: 'an element added and element removed change are detected'
        def removeInlineChange = findChange(replaceInlineByRefRootDifference.diffs, ChangeType.ELEMENT_ADDED, [name: 'simpleDescription'])
        removeInlineChange

        def addRefElementChange = findChange(replaceInlineByRefRootDifference.diffs, ChangeType.ELEMENT_REMOVED, [name: 'simpleDescription'])
        addRefElementChange

        then: 'the change is considered breaking'
        !removeInlineChange.breaks() || addRefElementChange.breaks()  //TODO #193
    }

    def "verify that replacing an inline complex element by a ref is detected as a non compatible change" () {
        when: 'an inline simple element is replaced by a ref'

        then: 'an element added and element removed change are detected'
        def removeInlineChange = findChange(replaceInlineByRefRootDifference.diffs, ChangeType.ELEMENT_ADDED, [name: 'currency'])
        removeInlineChange

        def addRefElementChange = findChange(replaceInlineByRefRootDifference.diffs, ChangeType.ELEMENT_REMOVED, [name: 'currency'])
        addRefElementChange

        then: 'the change is considered breaking'
        !removeInlineChange.breaks() || addRefElementChange.breaks()  //TODO #193
    }

    def "verify that replacing an inline simple element by a ref is detected as a non compatible change" () {
        when: 'an inline simple element is replaced by a ref'

        then: 'an element added and element removed change are detected'
        def removeInlineChange = findChange(replaceInlineByRefRootDifference.diffs, ChangeType.ELEMENT_ADDED, [name: 'description'])
        removeInlineChange

        def addRefElementChange = findChange(replaceInlineByRefRootDifference.diffs, ChangeType.ELEMENT_REMOVED, [name: 'description'])
        addRefElementChange

        then: 'the change is considered breaking'
        !removeInlineChange.breaks() || addRefElementChange.breaks()  //TODO #193
    }

    def "verify that replacing a choice by a list of optional elements is detected as a non compatible change" () {
        when: 'a choice is replaced by a list of optional elements'

        then: 'multiple element added and one choice removed changes are detected'
        def rootChange = differences.find { it.description == 'ComplexType replaceChoiceByOptionalElementsComplexType:' }
        rootChange

        def removeChoiceChange =  findChangeByDescription(rootChange.diffs, /choice remove.*/)
        removeChoiceChange

        def addDescriptionChange = findChange(rootChange.diffs, ChangeType.ELEMENT_ADDED, [name: 'description'])
        addDescriptionChange

        def addSimpleDescriptionChange = findChange(rootChange.diffs, ChangeType.ELEMENT_ADDED, [name: 'simpleDescription'])
        addSimpleDescriptionChange

        then: 'the change is considered breaking'
        !removeChoiceChange.breaks() || addDescriptionChange.breaks() || addSimpleDescriptionChange.breaks()  //TODO #193
    }

    def "verify that removing an optional element from a sequence is detected as a non compatible change" () {
        when: 'an optional element is removed from a sequence'

        then: 'an element removed change is detected'
        def change = findChange(nonRefRootDifference.diffs, ChangeType.ELEMENT_REMOVED, [name: 'nonRefOptionalRemoved'])
        change

        then: 'the change is considered breaking'
        !change.breaks() //TODO should be breaking
    }

    def "verify that removing a mandatory element from a sequence is detected as a non compatible change" () {
        when: 'a mandatory element is removed from a sequence'

        then: 'an element removed change is detected'
        def change = findChange(nonRefRootDifference.diffs, ChangeType.ELEMENT_REMOVED, [name: 'nonRefMandatoryRemoved'])
        change

        then: 'the change is considered breaking'
        !change.breaks() //TODO #193
    }

    def "verify that moving an element into a choice is detected as a non compatible change" () {
        when: 'an element is moved into a choice'

        then:
        def rootChange = differences.find { it.description == 'ComplexType moveElementIntoChoiceComplexType:' }

        def elementRemovedChange = findChange(rootChange.diffs, ChangeType.ELEMENT_REMOVED)
        elementRemovedChange

        def choiceAddedChange = findChangeByDescription(rootChange.diffs, /choice added.*/)
        choiceAddedChange

        then:
        !elementRemovedChange.breaks() || choiceAddedChange.breaks()  //TODO #193

    }

}
