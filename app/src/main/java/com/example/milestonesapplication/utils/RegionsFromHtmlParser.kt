package com.example.milestonesapplication.utils

import com.example.milestonesapplication.models.Region
import java.util.*

class RegionsFromHtmlParser {
    private var regions = ArrayList<Region>()

    fun parse(http: String): ArrayList<Region> {
        val stringBuilder = StringBuilder(http)
        var firstIndex = 0
        var lastIndex = 0

        while (firstIndex >= 0) {
            firstIndex = stringBuilder.indexOf("<option", lastIndex)
            lastIndex = stringBuilder.indexOf("</option>", firstIndex)
            if (firstIndex < 0 || lastIndex < 0) {
                return regions
            }
            regions.add(
                    parseStruct(stringBuilder.substring(firstIndex, lastIndex)))
        }
        return regions
    }

    private fun parseStruct(structString: String): Region {
        val stringBuilder = StringBuilder(structString)
        val region = Region()

        var firstIndex = stringBuilder.indexOf("value=\"")
        var lastIndex = stringBuilder.indexOf("\">", firstIndex)
        region.code = stringBuilder.substring(firstIndex + 7, lastIndex)

        firstIndex = lastIndex
        lastIndex = structString.length
        region.region = stringBuilder.substring(firstIndex + 2, lastIndex)
        if(region.region == "Российская Федерация")
            region.region = "Выберите регион"

        return region
    }
}