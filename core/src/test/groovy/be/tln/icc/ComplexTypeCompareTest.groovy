package be.tln.icc

import be.tln.icc.util.ChangeType
import com.predic8.soamodel.Difference

class ComplexTypeCompareTest extends BaseCompareTest {

    String complexTypeChangesV1 = "${baseResourceDir}/ComplexTypeChangesV1.xsd"
    String complexTypeChangesV2 = "${baseResourceDir}/ComplexTypeChangesV2.xsd"

    List<Difference> differences

    void setUp () {
        differences = compareSchema(complexTypeChangesV1, complexTypeChangesV2)
    }

    void "test that adding a sequence is detected as a non compatible change" () {
        when: 'a sequence is added'  // Difference provided in test files
        then: 'an add sequence change is detected'

        def change = findChange(differences, ChangeType.SEQ_ADDED)
        assert change
        assert change.breaks()
    }

}
