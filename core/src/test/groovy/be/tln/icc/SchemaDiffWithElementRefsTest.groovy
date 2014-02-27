package be.tln.icc

import be.tln.icc.util.EnhancedDifference
import com.predic8.schema.diff.SchemaDiffGenerator
import com.predic8.soamodel.Difference

class SchemaDiffWithElementRefsTest extends BaseCompareTest {


    def refConstructV1 = "$baseResourceDir/RefConstructV1.xsd"
    def refConstructV2 = "$baseResourceDir/RefConstructV2.xsd"

    def differences

    void setUp() {
        differences = compareSchema(refConstructV1, refConstructV2)
    }

    /**
     * Tests that when a ref changes to another element that is of the same complex type
     * as the original element the difference is still detected.
     */
    void "test diff for ref to different element of same type"() {
        use(EnhancedDifference) {
            Difference topLevelDiff = new Difference().findUniqueByDescription(differences, /.*PostpaidComplexType.*/)

            assert topLevelDiff
            assert topLevelDiff.findUniqueByDescription(/.*AlternativeEnterpriseNumber.*added.*/)
            assert topLevelDiff.findUniqueByDescription(/.*EnterpriseNumber.*removed.*/)
            true
        }
    }

    void "test diff for ref to different elements with different type" () {
        use(EnhancedDifference) {
            Difference topLevelDiff = new Difference().findUniqueByDescription(differences, /.*PrepaidComplexType.*/)

            assert topLevelDiff
            assert topLevelDiff.findUniqueByDescription(/.*EnterpriseIdentification.*added.*/)
            assert topLevelDiff.findUniqueByDescription(/.*EnterpriseNumber.*removed.*/)
            true
        }
    }

    void "test diff for ref changes inside choice" () {
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

    void "test diff for changes in subtree of types" () {
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
