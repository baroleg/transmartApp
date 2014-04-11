import groovy.sql.Sql

import java.sql.Clob

public class VcfExportService {
    boolean transactional = true

    def dataSource

    def getDataSetIdByPath(path) {
        def sql = new Sql(dataSource)
        sql.firstRow("""
          select
            vss.dataset_id as sourcesystem_cd
          from
            concept_dimension cd,
            DEAPP.de_subject_sample_mapping sm,
            i2b2metadata.i2b2 i2,
            de_variant_subject_summary vss
          where
            i2.c_fullname = ?
            and i2.C_VISUALATTRIBUTES = ?
            and i2.c_basecode = sm.concept_code
            and sm.platform = ?
            and (vss.assay_id = sm.assay_id)
        """, path, 'LAH', 'VCF')
    }

    def getDataSetIds() {
        def sql = new Sql(dataSource)
        sql.rows("select distinct dataset_id from deapp.de_variant_dataset")*.dataset_id
    }

    def storeDataSetToFile(String dataSetId, Collection<String> sampleIdsToExport, File outputFile) {
        def sql = new Sql(dataSource)
        outputFile.delete()
        outputFile.withWriter { out ->
            def conditions = ['dataset_id = ?']
            def parameters = [dataSetId]
            def samples = sql.rows("""
                  select *
                  from deapp.de_variant_subject_idx
                  where ${conditions.join(' and ')}
                """, parameters)
            if (sampleIdsToExport) {
                sampleIdsToExport = (sampleIdsToExport as Set)
                samples = samples.findAll { it.subject_id in sampleIdsToExport }
            }
            out.println("##fileformat=VCFv4.1")
            sql.eachRow("select * from deapp.de_variant_population_info where dataset_id = ?", [dataSetId]) { row ->
                def info = [ID: row.info_name,
                            Number: row.number,
                            Type: row.type,
                            Description: readText(row.description)
                ].findAll { !it.value.is(null) }.collect { "${it.key}=${it.value}" }.join(',')
                out.println("##INFO=<${info}>")
            }
            out.println("#CHROM\tPOS\tID\tREF\tALT\tQUAL\tFILTER\tINFO\tFORMAT\t${samples*.subject_id.join('\t')}")
            if (sampleIdsToExport) {
                sql.eachRow("select * from deapp.de_variant_subject_detail where dataset_id = ?", [dataSetId]) { row ->
                    def variantValue = readText(row.variant_value).split('\t')
                    def indexes = samples.collect { it.position - 1 }
                    out.println(([row.chr, row.pos, row.rs_id, row.ref, row.alt, row.qual, row.filter, row.info, row.format] +
                            variantValue[indexes]).join('\t'))
                }
            } else {
                sql.eachRow("select * from deapp.de_variant_subject_detail where dataset_id = ?", [dataSetId]) { row ->
                    out.println([row.chr, row.pos, row.rs_id, row.ref, row.alt, row.qual, row.filter, row.info, row.format, readText(row.variant_value)].join('\t'))
                }
            }
        }
    }

    private static String readText(Object valueOrClob) {
        valueOrClob instanceof Clob ? (valueOrClob as Clob).characterStream.text : valueOrClob
    }
}