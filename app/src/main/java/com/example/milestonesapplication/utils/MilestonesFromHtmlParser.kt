package com.example.milestonesapplication.utils

import android.text.TextUtils
import com.example.milestonesapplication.model.Milestone
import com.google.android.gms.maps.model.LatLng
import java.util.*

class MilestonesFromHtmlParser {
    private var milestones = ArrayList<Milestone>()

    fun parse(http: String): ArrayList<Milestone> {
        var firstIndex = 0
        var lastIndex = 0
        while (firstIndex >= 0) {
            firstIndex = TextUtils.indexOf(http, "data.points.push", lastIndex)
            lastIndex = TextUtils.indexOf(http, ");", firstIndex)
            if (firstIndex < 0 || lastIndex < 0) {
                return milestones
            }
            milestones.add(parseStruct(TextUtils.substring(http, firstIndex, lastIndex)))
        }
        return milestones
    }

    private fun parseStruct(structString: String): Milestone {
        val milestone = Milestone()

        var firstIndex: Int = TextUtils.indexOf(structString, "balloonContentBody:")
        var lastIndex = TextUtils.indexOf(structString, "\",", firstIndex)
        milestone.name = parseTitle(TextUtils.substring(structString, firstIndex + 21, lastIndex))

        firstIndex = TextUtils.indexOf(structString, "hintContent:", lastIndex)
        lastIndex = TextUtils.indexOf(structString, "\",", firstIndex)
        milestone.description = TextUtils.substring(structString, firstIndex + 14, lastIndex).replace("&quot;", "\"")

        firstIndex = TextUtils.indexOf(structString, "coordinates:", lastIndex)
        lastIndex = TextUtils.indexOf(structString, "]", firstIndex)
        milestone.location = parseLocation(TextUtils.substring(structString, firstIndex + 14, lastIndex))

        return milestone
    }

    private fun parseTitle(http: String): String {
        var res = ""
        var firstIndex = 0
        var lastIndex = 0
        while (firstIndex != -1) {
            firstIndex = TextUtils.indexOf(http, "<li>", lastIndex)
            lastIndex = TextUtils.indexOf(http, "</li>", firstIndex)
            if (firstIndex != -1 && lastIndex != -1) {
                if (TextUtils.isEmpty(res))
                    res = TextUtils.substring(http, firstIndex + 4, lastIndex)
                else
                    res = res + ";\n" + TextUtils.substring(http, firstIndex + 4, lastIndex)
            }
        }
        return res
    }

    private fun parseLocation(string: String): LatLng {
        var res: String
        val firstIndex: Int
        val lastIndex: Int = TextUtils.indexOf(string, ",")
        val latitude: Double
        val longitude: Double

        res = TextUtils.substring(string, 0, lastIndex)
        latitude = java.lang.Double.parseDouble(res)

        firstIndex = TextUtils.indexOf(string, ",", lastIndex)
        res = TextUtils.substring(string, firstIndex + 2, string.length)
        longitude = java.lang.Double.parseDouble(res)

        return LatLng(latitude, longitude)
    }
}