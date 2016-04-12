package be.tln.icc

import be.tln.icc.util.ChangeType
import com.predic8.schema.Schema
import com.predic8.schema.SchemaParser
import com.predic8.schema.diff.SchemaDiffGenerator
import com.predic8.soamodel.Difference
import com.predic8.wsdl.Definitions
import com.predic8.wsdl.WSDLParser
import com.predic8.wsdl.diff.WsdlDiffGenerator
import be.tln.icc.util.DifferencesUtil as DU

abstract class BaseCompareTest extends GroovyTestCase {

    final static String baseResourceDir = './src/test/resources/tln'

    final String myServiceV1 = "${baseResourceDir}/testmodel_v1/MyService_v001.wsdl"
    final String myOtherServiceV1 = "${baseResourceDir}/testmodel_v1/MyOtherService_v001.wsdl"
    final String inputCatalogXsdV1 = "${baseResourceDir}/testmodel_v1/xsd/InputCatalog_v001.xsd"
    final String commonXsdV1 = "${baseResourceDir}/testmodel_v1/xsd/Common_v001.xsd"

    final String myServiceV2 = "${baseResourceDir}/testmodel_v1/MyService_v001.wsdl"
    final String myOtherServiceV2 = "${baseResourceDir}/testmodel_v1/MyOtherService_v001.wsdl"
    final String inputCatalogXsdV2 = "${baseResourceDir}/testmodel_v2/xsd/InputCatalog_v001.xsd"
    final String commonXsdV2 = "${baseResourceDir}/testmodel_v2/xsd/Common_v001.xsd"

    final String myServiceV3 = "${baseResourceDir}/testmodel_v3/MyService_v002.wsdl"
    final String myOtherServiceV3 = "${baseResourceDir}/testmodel_v3/MyOtherService_v001.wsdl"
    final String inputCatalogXsdV3 = "${baseResourceDir}/testmodel_v3/xsd/InputCatalog_v001.xsd"
    final String commonXsdV3 = "${baseResourceDir}/testmodel_v3/xsd/Common_v001.xsd"

    List<Difference> compareSchema (String schemaA, String schemaB) {
        new SchemaDiffGenerator(parseSchema(schemaA), parseSchema(schemaB)).compare()
    }

    List<Difference> compareWsdl(String wsdlA, String wsdlB) {
        new WsdlDiffGenerator(parseWsdl(wsdlA), parseWsdl(wsdlB)).compare()
    }

    Definitions parseWsdl(String path) {
        new WSDLParser().parse(path)
    }

    Schema parseSchema(String path) {
        new SchemaParser().parse(path)
    }

    Difference findChange (List<Difference> changes, ChangeType type, Map args = [:]) {
        DU.findChange(changes, type, args)
    }

    List<Difference> findChanges (List<Difference> changes, ChangeType type, Map args = [:]) {
        DU.findChanges(changes, type, args)
    }

    Difference findChangeByDescription (List<Difference> changes, regex) {
        def differences = findChangesByDescription(changes, regex)
        if (differences.size() > 1) {
            throw new AssertionError("Only one Difference expected but found ${differences.size()} differences")
        }
        differences ? differences.first() : null
    }

    List<Difference> findChangesByDescription (List<Difference> changes, regex) {
        (changes.findAll { it?.description ==~ regex } +
            changes.collect { findChangesByDescription(it?.diffs ?: [], regex).flatten() }).flatten()
    }

}
