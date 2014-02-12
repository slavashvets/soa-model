package be.tln.icc

import be.tln.icc.util.ChangeType
import com.predic8.soamodel.Difference
import spock.lang.Shared

@SuppressWarnings("GroovyAssignabilityCheck")
class ChoiceCompareTest extends BaseCompareSpec {

    @Shared String choiceChangesV1 = "${baseResourceDir}/ChoiceChangesV1.xsd"
    @Shared String choiceChangesV2 = "${baseResourceDir}/ChoiceChangesV2.xsd"

    @Shared List<Difference> differences

    void setupSpec () {
        differences = compareSchema(choiceChangesV1, choiceChangesV2)
    }

    //TODO according xsls should be non compatible
    def "verify that adding a mandatory element in a choice is detected as a compatible change" () {
        when: 'a mandatory element is added to a choice'

        then: 'an add element change is detected'
        def change = findChange(differences, ChangeType.ELEMENT_ADDED, [name: 'description'])
        change
        change.type == 'element' //TODO should this not be choice?

        then: 'this change is considered compatible'
        !change.breaks()
        !change.safe() //TODO #193
    }

}
