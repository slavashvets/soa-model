package be.tln.icc

import be.tln.icc.BaseCompareSpec

class SchemaCompareBugsTest extends BaseCompareSpec {

    def "verify schema comparison where simple type changes to choice" () {
        when:
        def differences = compareSchema(commonXsdV1, commonXsdV2)

        then:
        !differences.find { it.breaks() } //TODO switched after update cbe559dd5ec98bce9d9fc9e760e3ea5a7e46541e
    }

    def "verify schema comparison where choice changes to simple type" () {
        when:
        def differences = compareSchema(commonXsdV2, commonXsdV1)

        then:
        !differences.find { it.breaks() }  //TODO #193
    }

}
