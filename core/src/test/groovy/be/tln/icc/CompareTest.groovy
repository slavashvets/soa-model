package be.tln.icc

class CompareTest extends BaseCompareSpec {

    def "verify that comparing v1 with v1 shows no differences" () {
        when: 'comparing the same WSDL'
        def differences = compareWsdl(myServiceV1, myServiceV1)

        then: 'no differences should be found'
        differences.isEmpty()
    }

    def "compare Schema with simple changed to choice" () {
        when:
        def differences = compareSchema(inputCatalogXsdV1, inputCatalogXsdV2)

        then:
        differences
    }

}
