package be.tln.icc

import com.predic8.soamodel.Difference
import spock.lang.Shared

import static be.tln.icc.util.ChangeType.ELEMENT_CHANGED
import static be.tln.icc.util.ChangeType.IMPORT_REMOVED
import static be.tln.icc.util.ChangeType.NAMESPACE

@SuppressWarnings("GroovyAssignabilityCheck")
class XsdInternalChangesTest extends BaseCompareSpec {

    @Shared String xsdInternalChanges_001 = "${baseResourceDir}/XsdInternalChanges_v001.wsdl"
    @Shared String xsdInternalChanges_002 = "${baseResourceDir}/XsdInternalChanges_v002.wsdl"
    @Shared String xsdInternalChangesV1 = "${baseResourceDir}/XsdInternalChangesV1.xsd"
    @Shared String xsdInternalChangesV2 = "${baseResourceDir}/XsdInternalChangesV2.xsd"

    @Shared List<Difference> schemaCompareDifferences
    @Shared List<Difference> wsdlCompareDifferences

    void setupSpec () {
        schemaCompareDifferences = compareSchema(xsdInternalChangesV1, xsdInternalChangesV2)
        wsdlCompareDifferences = compareWsdl(xsdInternalChanges_001, xsdInternalChanges_002)
    }

    def "verify that a namespace change of the XSD is detected as a non compatible change" () {
        when: 'when the namespace of an XSD is changed'

        then: 'the namespace change is detected'
        def change = findChange(schemaCompareDifferences, NAMESPACE, [from: 'http://xsdInternalChanges/v001', to: 'http://xsdInternalChangesXX/v001'])
        change
    }

    def "verify that the removal of a schema import is detected as a compatible change" () {
        when: 'a schema import is removed'

        then: 'the removal is detected as a change'
        def change = findChange(schemaCompareDifferences, IMPORT_REMOVED, [namespace: 'http://myservice.be/BusinessException/v001'])
        change
    }

    def "wsdl: verify that a namespace change of the XSD is detected as a non compatible change" () {
        when: 'when the namespace of an XSD is changed'

        then: 'the namespace change is detected'
        def changes = findChanges(wsdlCompareDifferences, ELEMENT_CHANGED, [from: 'http://xsdInternalChanges/v001', to: 'http://xsdInternalChangesXX/v001'])
        changes
        def change = changes.first()

        then: 'the change is considered breaking'
        change.breaks()
    }

}
