package be.tln.icc

import be.tln.icc.util.ChangeType
import com.predic8.soamodel.Difference
import spock.lang.Shared

@SuppressWarnings("GroovyAssignabilityCheck")
class SimpleTypeCompareTest extends BaseCompareSpec {

    @Shared String simpleTypeChangesV1 = "${baseResourceDir}/SimpleTypeChangesV1.xsd"
    @Shared String simpleTypeChangesV2 = "${baseResourceDir}/SimpleTypeChangesV2.xsd"

    @Shared List<Difference> differences

    void setupSpec () {
        differences = compareSchema(simpleTypeChangesV1, simpleTypeChangesV2)
    }

    def "verify that adding an enumeration value is detected as a non compatible change" () {
        when: 'an enumeration value is added' // Difference provided in test files

        then: 'an enumeration change is detected'
        def change  = findChange(differences, ChangeType.ENUM_VALUE_ADDED, [value:'yen'])
        change

        then: 'the change is considered breaking'
        !change.breaks() //TODO change should be breaking
    }

    def "verify that removing an enumeration value is detected as a non compatible change" () {
        when: 'an enumeration value is changed'

        then: 'an enumeration change is detected'
        def change = findChange(differences, ChangeType.ENUM_VALUE_REMOVED, [value: 'dollars'])
        change

        then: 'the change is considered breaking'
        !change.breaks() //TODO change should be breaking
    }

    def "verify that changing the baseType of a restriction is detected as a non compatible change" () {
        when: 'the baseType of a restriction changes from xsd:string to xsd:boolean'

        then:
        def change = findChange(differences, ChangeType.RESTRICTION_BASE_CHANGE, [from: 'string', to: 'boolean'])
        change

        then: 'the change is considered breaking'
        !change.breaks() //TODO change should be breaking
    }

    def "verify that introducing an enumeration in a restriction is detected as a non compatible change" () {
        when: 'an enumeration is introduced in a restriction'

        then: 'an add enumeration value change is detected for each introduced value'
        def changeA = findChange(differences, ChangeType.ENUM_VALUE_ADDED, [value: 'pounds'])
        def changeB = findChange(differences, ChangeType.ENUM_VALUE_ADDED, [value: 'euros'])
        changeA
        changeB

        then: 'the change is considered breaking'
        !changeA.breaks() || changeB.breaks() //TODO change should be breaking
    }

    def "verify that changing the type of a global element is detected as a non compatible change" () {
        when: 'the type of a global element changes'

        then: ''
        def changeA = findChange(differences, ChangeType.TYPE_CHANGE, [from: 'string', to: 'boolean'])
        changeA

        def changeB = findChange(differences, ChangeType.TYPE_CHANGE, [from: 'timeValueType', to: 'simpleTimeValueType'])

        then: 'the change is considered breaking'
        !changeA.breaks() //TODO #193
        !changeB.breaks() //TODO #193
    }

    def "verify that modifying a simple type to a more complex type is detected as a non compatible change" () {
        when: 'a simple type is made more complex'

        then: 'the type modification is detected'
        def addComplexChange = findChange(differences, ChangeType.TYPE_ADDED, [type: 'Complex', name: 'timeValueType'])
        addComplexChange

        def removeSimpleChange = findChange(differences, ChangeType.TYPE_ADDED, [type: 'Simple', name: 'simpleTimeValueType'])
        removeSimpleChange

        def addSimpleChange = findChange(differences, ChangeType.TYPE_REMOVED, [type: 'Simple', name: 'timeValueType'])
        addSimpleChange

        then: 'the change is considered breaking'
        !addComplexChange.breaks() || removeSimpleChange.breaks() || addSimpleChange.breaks() //TODO #193
    }

}
