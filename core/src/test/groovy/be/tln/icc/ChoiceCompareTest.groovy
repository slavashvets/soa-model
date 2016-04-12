package be.tln.icc

import be.tln.icc.util.ChangeType
import com.predic8.soamodel.Difference

@SuppressWarnings("GroovyAssignabilityCheck")
class ChoiceCompareTest extends BaseCompareTest {

    String choiceChangesV1 = "${baseResourceDir}/ChoiceChangesV1.xsd"
    String choiceChangesV2 = "${baseResourceDir}/ChoiceChangesV2.xsd"

    List<Difference> differences

    void setUp () {
        differences = compareSchema(choiceChangesV1, choiceChangesV2)
    }

    //TODO according xsls should be non compatible
    void testThatAddingAMandatoryElementInAChoiceIsDetectedAsACompatibleChange () {
        when: 'a mandatory element is added to a choice'
        then: 'an add element change is detected'
        def change = findChange(differences, ChangeType.ELEMENT_ADDED, [name: 'description'])
        assert change
        assert change.type == 'element'

        then: 'this change is considered compatible'
        assert !change.breaks()
        assert !change.safe() //TODO #193
    }

}
