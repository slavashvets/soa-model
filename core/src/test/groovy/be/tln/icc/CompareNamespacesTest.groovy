package be.tln.icc

import be.tln.icc.util.ChangeType
import be.tln.icc.util.DifferencesUtil

class CompareNamespacesTest extends BaseCompareSpec {

    def "compare WSDLs with a different namespace" () {
        when: 'comparing two WSDLs with a different namespace'
        List differences = compareWsdl(myServiceV1, myServiceV3)

        then: 'the differences should contain a namespace change'
        differences
        DifferencesUtil.containsChange(differences, ChangeType.NAMESPACE, [from: 'http://my.be/MyService/v001', to: 'http://my.be/MyService/v002'])
    }

}
