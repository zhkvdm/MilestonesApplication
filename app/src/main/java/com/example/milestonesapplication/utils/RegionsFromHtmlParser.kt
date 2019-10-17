package com.example.milestonesapplication.utils

import android.text.TextUtils
import com.example.milestonesapplication.model.Region
import java.util.*

class RegionsFromHtmlParser {
    private var regions = ArrayList<Region>()

    fun parse(http: String): ArrayList<Region> {
        var res: String
        var firstIndex = 0
        var lastIndex = 0

        while (firstIndex >= 0) {
            firstIndex = TextUtils.indexOf(http, "<option", lastIndex)
            lastIndex = TextUtils.indexOf(http, "</option>", firstIndex)
            if (firstIndex < 0 || lastIndex < 0) {
                return regions
            }
            res = TextUtils.substring(http, firstIndex, lastIndex)
            regions.add(parseStruct(res))
        }
        return regions
    }

    private fun parseStruct(structString: String): Region {
        val region = Region()

        var firstIndex = TextUtils.indexOf(structString, "value=\"")
        var lastIndex = TextUtils.indexOf(structString, "\">", firstIndex)
        region.code = TextUtils.substring(structString, firstIndex + 7, lastIndex)

        firstIndex = lastIndex
        lastIndex = structString.length
        region.region = TextUtils.substring(structString, firstIndex + 2, lastIndex)
        if(region.region == "Российская Федерация")
            region.region = "Выберите регион"

        return region
    }
}