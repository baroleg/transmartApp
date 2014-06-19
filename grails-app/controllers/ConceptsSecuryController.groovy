import grails.converters.JSON

/**
 * Created by transmart on 5/21/14.
 */
class ConceptsSecuryController {
    def conceptsResourceService
    def i2b2HelperService

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
        def accession = children*.sourcesystemCd

        if(!i2b2HelperService.isAdmin(user)) {
            def userid = user?.id
            groovy.sql.Sql sql = new groovy.sql.Sql(dataSource)
            def accessionSQLSet = sql.rows("""
                select
                  distinct be.accession
                from
                  searchapp.search_auth_sec_object_access sasoa
                    left join searchapp.search_auth_group_member sagm
                    on sasoa.auth_principal_id = sagm.auth_group_id,
                  searchapp.search_sec_access_level ssal,
                  searchapp.search_secure_object sso,
                  biomart.bio_experiment be
                  where
                    ssal.access_level_value > 0
                    and sasoa.secure_access_level_id = ssal.search_sec_access_level_id
                    and coalesce(sagm.auth_user_id, sasoa.auth_principal_id) = ?
                    and sasoa.secure_object_id = sso.search_secure_object_id
                    and sso.bio_data_id = be.bio_experiment_id
            """, userid)
            accession = accessionSQLSet.accession
        }

        children = children.findAll {
            accession.contains(it.sourcesystemCd)
        }

        render children as JSON
    }

}
