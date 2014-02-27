package be.tln.icc

class SchemaCompareBugsTest extends BaseCompareTest {

    void "test schema comparison where simple type changes to choice" () {
        def differences = compareSchema(commonXsdV1, commonXsdV2)

        assert !differences.find { it.breaks() } //TODO switched after update cbe559dd5ec98bce9d9fc9e760e3ea5a7e46541e
    }

    void "test schema comparison where choice changes to simple type" () {
        def differences = compareSchema(commonXsdV2, commonXsdV1)

        assert !differences.find { it.breaks() }  //TODO #193
    }

}
