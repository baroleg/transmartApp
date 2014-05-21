package com.recomdata.dataexport.util

import org.apache.commons.lang.StringUtils;

class ExportUtil {

    public static String getShortConceptPath(String conceptPath, removalArr) {
        def arr = StringUtils.split(conceptPath, "\\")
        //Remove upto Study-name and any tailing string values specified in the removalArr
        def valList = arr.toList()[1..-1]
        while (valList.size() > 0 && removalArr.any { it.equalsIgnoreCase(valList[-1]) }) {
            valList.remove(valList.size() - 1);
        }

        def shortenedConceptPath = StringUtils.join(valList, '\\')
        shortenedConceptPath = StringUtils.leftPad(shortenedConceptPath, shortenedConceptPath.length() + 1, '\\')

        return shortenedConceptPath
    }

    public static String getSampleValue(String value, String sampleType, String timepoint, String tissueType) {
        def retVal = null;
        if (StringUtils.equalsIgnoreCase(value, "E") || StringUtils.equalsIgnoreCase(value, "normal")) {
            def retVals = []
            if (null != sampleType && StringUtils.isNotEmpty(sampleType)) retVals.add(sampleType)
            if (null != timepoint && StringUtils.isNotEmpty(timepoint)) retVals.add(timepoint)
            if (null != tissueType && StringUtils.isNotEmpty(tissueType)) retVals.add(tissueType)
            retVal = StringUtils.join(retVals, "/")
        } else {
            retVal = value
        }
        return retVal
    }
}
