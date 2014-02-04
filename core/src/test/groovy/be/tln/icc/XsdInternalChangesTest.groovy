package be.tln.icc

import com.predic8.soamodel.Difference
import spock.lang.Shared

import static be.tln.icc.util.ChangeType.IMPORT_REMOVED
import static be.tln.icc.util.ChangeType.NAMESPACE

@SuppressWarnings("GroovyAssignabilityCheck")
class XsdInternalChangesTest extends BaseCompareSpec {

    @Shared String xsdInternalChangesV1 = "${baseResourceDir}/XsdInternalChangesV1.xsd"
    @Shared String xsdInternalChangesV2 = "${baseResourceDir}/XsdInternalChangesV2.xsd"

    @Shared List<Difference> differences

    void setupSpec () {
        differences = compareSchema(xsdInternalChangesV1, xsdInternalChangesV2)
    }

    def "verify that a namespace change of the XSD is detected as a non compatible change" () {
        when: 'when the namespace of an XSD is changed'

        then: 'the namespace change is detected'
        def change = findChange(differences, be.tln.icc.util.ChangeType.NAMESPACE, [from: 'http://xsdInternalChanges/v001', to: 'http://xsdInternalChangesXX/v001'])
        change

        then: 'the change is considered breaking'
        !change.breaks() //TODO #193
    }

    def "verify that the removal of a schema import is detected as a compatible change" () {
        when: 'a schema import is removed'

        then: 'the removal is detected as a change'
        def change = findChange(differences, be.tln.icc.util.ChangeType.IMPORT_REMOVED, [namespace: 'http://myservice.be/BusinessException/v001'])
        change

        then: 'the change is considered safe'
        !change.breaks()
        !change.safe()  //TODO #193
    }

}
