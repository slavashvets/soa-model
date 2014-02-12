package be.tln.icc

import be.tln.icc.util.EnhancedDifference
import com.predic8.schema.diff.SchemaDiffGenerator
import com.predic8.soamodel.Difference
import spock.lang.Shared

class SchemaDiffWithElementRefsTest extends BaseCompareSpec {


    @Shared def refConstructV1 = "$baseResourceDir/RefConstructV1.xsd"
    @Shared def refConstructV2 = "$baseResourceDir/RefConstructV2.xsd"

    @Shared def differences

    void setupSpec() {
        differences = compareSchema(refConstructV1, refConstructV2)
    }

    /**
     * Tests that when a ref changes to another element that is of the same complex type
     * as the original element the difference is still detected.
     */
    def "verify diff for ref to different element of same type"() {
        when: ''

        then: ''
        use(EnhancedDifference) {
            Difference topLevelDiff = new Difference().findUniqueByDescription(differences, /.*PostpaidComplexType.*/)

            assert topLevelDiff
            assert topLevelDiff.findUniqueByDescription(/.*AlternativeEnterpriseNumber.*added.*/)
            assert topLevelDiff.findUniqueByDescription(/.*EnterpriseNumber.*removed.*/)
            true
        }
    }

    def "verify diff for ref to different elements with different type" () {
        when: ''

        then: ''
        use(EnhancedDifference) {
            Difference topLevelDiff = new Difference().findUniqueByDescription(differences, /.*PrepaidComplexType.*/)

            assert topLevelDiff
            assert topLevelDiff.findUniqueByDescription(/.*EnterpriseIdentification.*added.*/)
            assert topLevelDiff.findUniqueByDescription(/.*EnterpriseNumber.*removed.*/)
            true
        }
    }

    def "verify diff for ref changes inside choice" () {
        when: ''

        then: ''
        use(EnhancedDifference) {
            Difference topLevelDiff = new Difference().findUniqueByDescription(differences, /.*AnotherComplexType.*/)

            assert topLevelDiff
            assert topLevelDiff.findUniqueByDescription(/.*AlternativeEnterpriseNumber.*added.*/)
            assert topLevelDiff.findUniqueByDescription(/.*AlternativeEnterpriseIdentification.*added.*/)
            assert topLevelDiff.findUniqueByDescription(/.*EnterpriseNumber.*removed.*/)
            assert topLevelDiff.findUniqueByDescription(/.*EnterpriseIdentification.*removed.*/)
            true
        }
    }

    def "verify diff for changes in subtree of types" () {
        when: ''

        then: ''
        use(EnhancedDifference) {
            Difference topLevelDiff = new Difference().findUniqueByDescription(differences, /.*DeepComplex.*/)

            assert topLevelDiff
            assert topLevelDiff.findUniqueByDescription(/.*tns:EnterpriseNumber.*removed.*/)
            assert topLevelDiff.findUniqueByDescription(/.*AlternativeEnterpriseNumber.*removed.*/)
            assert topLevelDiff.findUniqueByDescription(/.*tns:EnterpriseIdentification.*added.*/)
            assert topLevelDiff.findUniqueByDescription(/.*AlternativeEnterpriseIdentification.*added.*/)
            true
        }
    }

    private def compare(a, b) {
        new SchemaDiffGenerator(a: a, b: b).compare()
    }

}
