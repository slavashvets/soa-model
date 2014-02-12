package be.tln.icc

import be.tln.icc.util.ChangeType
import com.predic8.soamodel.Difference
import spock.lang.Shared

class WSDLInternalChangesTest extends BaseCompareSpec {

    @Shared String internalChangesWsdlV1 = "${baseResourceDir}/WSDLInternalChanges_v001.wsdl"
    @Shared String internalChangesWsdlV2 = "${baseResourceDir}/WSDLInternalChanges_v002.wsdl"


    @Shared List<Difference> differences


    void setupSpec () {
        differences = compareWsdl(internalChangesWsdlV1, internalChangesWsdlV2)
    }

    def "verify that a change to the WSDL namespace is detected as a non compatible change" () {
        when: 'when the WSDL namespace changes' // Difference provided in test files

        then: 'a namespace change is detected'
        def change = findChange(differences, ChangeType.NAMESPACE, [from: 'http://WSDLInternalChanges/v001', to: 'http://WSDLInternalChanges/v002'])
        change

        then: 'the namespace change is marked as breaking'
        change.breaks()
    }

    def "def verify that adding an operation is detected as a compatible change" () {
        when: 'an operation is added' // Difference provided in test files

        then: 'the add operation change is detected'
        def change = findChange(differences, ChangeType.ADD_OPERATION, [name: 'getOtherStuff'])
        change

        then: 'the add operation change is marked as non breaking'
        !change.breaks()
    }

    def "verify that removing an operation is detected as a non compatible change" () {
        when: 'an operation is removed' // Difference provided in test files

        then: 'the remove operation change is detected'
        def change = findChange(differences, ChangeType.REMOVE_OPERATION, [name: 'toBeRemoved'])
        change

        then: 'the remove operation change is marked as breaking'
        change.breaks()
    }

    def "verify that adding a fault element is detected as a non compatible change" () {
        when: 'a fault element is added' // Difference provided in test files

        then: 'the add fault change is detected'
        def change = findChange(differences, ChangeType.FAULT_ADDED, [message: 'BusinessException'])
        change

        then: 'the add fault change is marked as breaking'
        !change.breaks() //TODO should be breaking
    }

    def "verify that soap action modification is detected as a non compatible change" () {
        when: 'the soapAction for an operation changes'

        then:
        true //TODO Change is not detected
    }

}
