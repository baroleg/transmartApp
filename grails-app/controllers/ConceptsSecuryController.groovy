import grails.converters.JSON

/**
 * Created by transmart on 5/21/14.
 */
class ConceptsSecuryController {
    def conceptsResourceService

    def getCategories() {
        render conceptsResourceService.allCategories as JSON
    }

    def springSecurityService
    def dataSource

    def getChildren() {
        def parentConceptKey = params.get('concept_key')
        def parent = conceptsResourceService.getByKey(parentConceptKey)
        def children = parent.children
        def user = springSecurityService.getPrincipal()
        def userid = user?.id

        groovy.sql.Sql sql = new groovy.sql.Sql(dataSource)
        def accessionSQLSet = sql.rows("select \n" +
                "be.accession " +
                "from \n" +
                "searchapp.search_auth_sec_object_access sasoa,\n" +
                "searchapp.search_sec_access_level ssal,\n" +
                "searchapp.search_secure_object sso,\n" +
                "biomart.bio_experiment be\n" +
                "where\n" +
                "ssal.access_level_value > 0\n" +
                "and sasoa.secure_access_level_id = ssal.search_sec_access_level_id\n" +
                "and (sasoa.auth_principal_id = ? or\n" +
                "sasoa.auth_principal_id in ( " +
                "select sagm.auth_group_id from searchapp.search_auth_group_member sagm where sagm.auth_user_id = ?))" +
                "and sasoa.secure_object_id = sso.search_secure_object_id\n" +
                "and sso.bio_data_id = be.bio_experiment_id", userid, userid)

        def accession = accessionSQLSet.accession

        children = children.findAll {
            accession.contains(it.sourcesystemCd)
        }

        render children as JSON
    }

}
