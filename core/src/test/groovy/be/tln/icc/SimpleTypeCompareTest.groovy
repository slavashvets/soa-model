package be.tln.icc

import be.tln.icc.util.ChangeType
import com.predic8.soamodel.Difference

@SuppressWarnings("GroovyAssignabilityCheck")
class SimpleTypeCompareTest extends BaseCompareTest {

    String simpleTypeChangesV1 = "${baseResourceDir}/SimpleTypeChangesV1.xsd"
    String simpleTypeChangesV2 = "${baseResourceDir}/SimpleTypeChangesV2.xsd"

    List<Difference> differences

    void setUp () {
        differences = compareSchema(simpleTypeChangesV1, simpleTypeChangesV2)
    }

    void "test that adding an enumeration value is detected as a non compatible change" () {
        when: 'an enumeration value is added' // Difference provided in test files

        then: 'an enumeration change is detected'
        def change  = findChange(differences, ChangeType.ENUM_VALUE_ADDED, [value:'yen'])
        assert change

        then: 'the change is considered breaking'
        assert !change.breaks() //TODO change should be breaking
    }

    void "test that removing an enumeration value is detected as a non compatible change" () {
        when: 'an enumeration value is changed'

        then: 'an enumeration change is detected'
        def change = findChange(differences, ChangeType.ENUM_VALUE_REMOVED, [value: 'dollars'])
        assert change

        then: 'the change is considered breaking'
        assert !change.breaks() //TODO change should be breaking
    }

    void "test that changing the baseType of a restriction is detected as a breaking change" () {
        when: 'the baseType of a restriction changes from xsd:string to xsd:boolean'

        def change = findChange(differences, ChangeType.RESTRICTION_BASE_CHANGE, [from: 'string', to: 'boolean'])
        assert change

        then: 'the change is considered breaking'
        assert !change.breaks() //TODO change should be breaking
    }

    void "test that introducing an enumeration in a restriction is detected as a non compatible change" () {
        when: 'an enumeration is introduced in a restriction'

        then: 'an add enumeration value change is detected for each introduced value'
        def changeA = findChange(differences, ChangeType.ENUM_VALUE_ADDED, [value: 'pounds'])
        def changeB = findChange(differences, ChangeType.ENUM_VALUE_ADDED, [value: 'euros'])
        assert changeA
        assert changeB

        then: 'the change is considered breaking'
        assert !changeA.breaks() || changeB.breaks() //TODO change should be breaking
    }

    void "test that changing the type of a global element is detected as a non compatible change" () {
        when: 'the type of a global element changes'

        then: ''
        def changeA = findChange(differences, ChangeType.TYPE_CHANGE, [from: 'string', to: 'boolean'])
        assert changeA

        def changeB = findChange(differences, ChangeType.TYPE_CHANGE, [from: 'timeValueType', to: 'simpleTimeValueType'])
        assert changeB

        then: 'the change is considered breaking'
        assert !changeA.breaks() //TODO #193
        assert !changeB.breaks() //TODO #193
    }

    void "test that modifying a simple type to a more complex type is detected as a non compatible change" () {
        when: 'a simple type is made more complex'

        then: 'the type modification is detected'
        def addComplexChange = findChange(differences, ChangeType.TYPE_ADDED, [type: 'Complex', name: 'timeValueType'])
        assert addComplexChange

        def removeSimpleChange = findChange(differences, ChangeType.TYPE_ADDED, [type: 'Simple', name: 'simpleTimeValueType'])
        assert removeSimpleChange

        def addSimpleChange = findChange(differences, ChangeType.TYPE_REMOVED, [type: 'Simple', name: 'timeValueType'])
        assert addSimpleChange

        then: 'the change is considered breaking'
        assert !addComplexChange.breaks() || removeSimpleChange.breaks() || addSimpleChange.breaks() //TODO #193
    }

}
