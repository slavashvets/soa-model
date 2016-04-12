package be.tln.icc

import com.predic8.soamodel.Difference

import static be.tln.icc.util.ChangeType.ELEMENT_CHANGED
import static be.tln.icc.util.ChangeType.IMPORT_REMOVED
import static be.tln.icc.util.ChangeType.NAMESPACE

@SuppressWarnings("GroovyAssignabilityCheck")
class XsdInternalChangesTest extends BaseCompareTest {

    String xsdInternalChanges_001 = "${baseResourceDir}/XsdInternalChanges_v001.wsdl"
    String xsdInternalChanges_002 = "${baseResourceDir}/XsdInternalChanges_v002.wsdl"
    String xsdInternalChangesV1 = "${baseResourceDir}/XsdInternalChangesV1.xsd"
    String xsdInternalChangesV2 = "${baseResourceDir}/XsdInternalChangesV2.xsd"

    List<Difference> schemaCompareDifferences
    List<Difference> wsdlCompareDifferences

    void setUp () {
        schemaCompareDifferences = compareSchema(xsdInternalChangesV1, xsdInternalChangesV2)
        wsdlCompareDifferences = compareWsdl(xsdInternalChanges_001, xsdInternalChanges_002)
    }

    void "test that a namespace change of the XSD is detected as a non compatible change" () {
        when: 'when the namespace of an XSD is changed'

        then: 'the namespace change is detected'
        def change = findChange(schemaCompareDifferences, NAMESPACE, [from: 'http://xsdInternalChanges/v001', to: 'http://xsdInternalChangesXX/v001'])
        assert change
    }

    void "test that the removal of a schema import is detected as a compatible change" () {
        when: 'a schema import is removed'

        then: 'the removal is detected as a change'
        def change = findChange(schemaCompareDifferences, IMPORT_REMOVED, [namespace: 'http://myservice.be/BusinessException/v001'])
        assert change
    }

    void "test wsdl: verify that a namespace change of the XSD is detected as a non compatible change" () {
        when: 'when the namespace of an XSD is changed'

        then: 'the namespace change is detected'
        def changes = findChanges(wsdlCompareDifferences, ELEMENT_CHANGED, [from: 'http://xsdInternalChanges/v001', to: 'http://xsdInternalChangesXX/v001'])
        assert changes

        def change = changes.first()

        then: 'the change is considered breaking'
        assert change.breaks()
    }

}
