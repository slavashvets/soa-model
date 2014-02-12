package be.tln.icc.util

import com.predic8.soamodel.Difference

class ElementPositionChange {

    // Example description:
    // Position of element {http://sequenceChanges/v001}descriptionMandatoryToOptionalAndPlaceChanged changed from 4 to 6.

    static def changeType = ChangeType.ELEMENT_POSITION

    static boolean isChangeType (Difference difference) {
        difference?.type == 'sequence' && difference?.description ==~ /Position of element .* changed.*/
    }

    static boolean isChange (Difference difference, Map args = [:]) {
        isChangeType(difference) &&
                (!args['name'] || difference?.description ==~ /.*${args['name']} .*/) &&
                (!args['from'] || difference?.description ==~ /.*changed from ${args['from']} .*/) &&
                (!args['to'] || difference?.description ==~ /.*to ${args['to']}.*/)
    }

}
