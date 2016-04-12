package org.membrane_soa.soa_model

class ResourceLists {

    static IMAGE_DIR = '/web/images'
    static IMAGE_LIST = ['add.png', 'fault.png', 'lightning.png', 'remove.png', 'request.png', 'response.png', 'tick.png', 'treeview-default.gif', 'treeview-default-line.gif']

    static WEB_RESOURCES_DIR = '/web'
    static WEB_RESOURCES_LIST = ['a.css', 'jquery.js', 'jquery.treeview.js', 'jquery.treeview.css', 'run_prettify.js']

    static copyWebResources(String baseDir) {
        new File("$baseDir${WEB_RESOURCES_DIR}").mkdirs()
        WEB_RESOURCES_LIST.each { fileName ->
            new File("$baseDir${WEB_RESOURCES_DIR}/$fileName").withOutputStream { out ->
                out << this.class.getResourceAsStream("${WEB_RESOURCES_DIR}/$fileName")
            }
        }
    }

    static copyImageResources(String baseDir) {
        new File("$baseDir${IMAGE_DIR}").mkdirs()
        IMAGE_LIST.each { fileName ->
            new File("$baseDir${IMAGE_DIR}/$fileName").withOutputStream { out ->
                out << this.class.getResourceAsStream("${IMAGE_DIR}/$fileName")
            }
        }
    }

}
