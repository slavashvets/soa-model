package be.tln.icc

import be.tln.icc.util.ChangeType
import com.predic8.soamodel.Difference
import spock.lang.Shared

class ComplexTypeCompareTest extends BaseCompareSpec {

    @Shared String complexTypeChangesV1 = "${baseResourceDir}/ComplexTypeChangesV1.xsd"
    @Shared String complexTypeChangesV2 = "${baseResourceDir}/ComplexTypeChangesV2.xsd"

    @Shared List<Difference> differences

    void setupSpec () {
        differences = compareSchema(complexTypeChangesV1, complexTypeChangesV2)
    }

    def "verify that adding a sequence is detected as a non compatible change" () {
        when: 'a sequence is added'  // Difference provided in test files

        then: 'an add sequence change is detected'
        def change = findChange(differences, ChangeType.SEQ_ADDED)
        change

        then: 'this change is considered non compatible'
        change.breaks()
    }

}
