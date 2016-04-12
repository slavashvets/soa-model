package be.tln.icc

class CompareTest extends BaseCompareTest {

    void "test that comparing v1 with v1 shows no differences" () {
        when: 'comparing the same WSDL'
        def differences = compareWsdl(myServiceV1, myServiceV1)

        then: 'no differences should be found'
        assert differences.isEmpty()
    }

    void "test compare Schema with simple changed to choice" () {
        def differences = compareSchema(inputCatalogXsdV1, inputCatalogXsdV2)
        assert differences
    }

}
