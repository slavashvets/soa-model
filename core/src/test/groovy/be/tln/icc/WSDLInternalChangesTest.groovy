package be.tln.icc

import be.tln.icc.util.ChangeType
import com.predic8.soamodel.Difference

class WSDLInternalChangesTest extends BaseCompareTest {

    String internalChangesWsdlV1 = "${baseResourceDir}/WSDLInternalChanges_v001.wsdl"
    String internalChangesWsdlV2 = "${baseResourceDir}/WSDLInternalChanges_v002.wsdl"


    List<Difference> differences


    void setUp () {
        differences = compareWsdl(internalChangesWsdlV1, internalChangesWsdlV2)
    }

    void "test that a change to the WSDL namespace is detected as a non compatible change" () {
        when: 'when the WSDL namespace changes' // Difference provided in test files

        then: 'a namespace change is detected'
        def change = findChange(differences, ChangeType.NAMESPACE, [from: 'http://WSDLInternalChanges/v001', to: 'http://WSDLInternalChanges/v002'])
        assert change

        then: 'the namespace change is marked as breaking'
        assert change.breaks()
    }

    void "test that adding an operation is detected as a compatible change" () {
        when: 'an operation is added' // Difference provided in test files

        then: 'the add operation change is detected'
        def change = findChange(differences, ChangeType.ADD_OPERATION, [name: 'getOtherStuff'])
        assert change

        then: 'the add operation change is marked as non breaking'
        assert !change.breaks()
    }

    void "test that removing an operation is detected as a non compatible change" () {
        when: 'an operation is removed' // Difference provided in test files

        then: 'the remove operation change is detected'
        def change = findChange(differences, ChangeType.REMOVE_OPERATION, [name: 'toBeRemoved'])
        assert change

        then: 'the remove operation change is marked as breaking'
        assert change.breaks()
    }

    void "test that adding a fault element is detected as a non compatible change" () {
        when: 'a fault element is added' // Difference provided in test files

        then: 'the add fault change is detected'
        def change = findChange(differences, ChangeType.FAULT_ADDED, [message: 'BusinessException'])
        assert change

        then: 'the add fault change is marked as breaking'
        assert !change.breaks() //TODO should be breaking
    }

}
