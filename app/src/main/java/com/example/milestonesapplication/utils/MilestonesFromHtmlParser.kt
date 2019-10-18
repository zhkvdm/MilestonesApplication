package com.example.milestonesapplication.utils

import android.text.TextUtils
import com.example.milestonesapplication.models.Milestone
import com.google.android.gms.maps.model.LatLng
import java.util.*

class MilestonesFromHtmlParser {
    private var milestones = ArrayList<Milestone>()

    fun parse(http: String): ArrayList<Milestone> {
        val stringBuilder = StringBuilder(http)
        var firstIndex = 0
        var lastIndex = 0
        while (firstIndex >= 0) {
            firstIndex = stringBuilder.indexOf("data.points.push", lastIndex)
            lastIndex = stringBuilder.indexOf(");", firstIndex)
            if (firstIndex < 0 || lastIndex < 0) {
                return milestones
            }
            milestones.add(parseStruct(stringBuilder.substring(firstIndex, lastIndex)))
        }
        return milestones
    }

    private fun parseStruct(structString: String): Milestone {
        val stringBuilder = StringBuilder(structString)
        val milestone = Milestone()

        var firstIndex: Int = stringBuilder.indexOf("balloonContentBody:")
        var lastIndex = stringBuilder.indexOf("\",", firstIndex)
        milestone.name = parseTitle(stringBuilder.substring(firstIndex + 21, lastIndex))

        firstIndex = stringBuilder.indexOf("hintContent:", lastIndex)
        lastIndex = stringBuilder.indexOf("\",", firstIndex)
        milestone.description = stringBuilder.substring(firstIndex + 14, lastIndex).replace("&quot;", "\"")

        firstIndex = stringBuilder.indexOf("coordinates:", lastIndex)
        lastIndex = stringBuilder.indexOf("]", firstIndex)
        milestone.location = parseLocation(stringBuilder.substring(firstIndex + 14, lastIndex))

        return milestone
    }

    private fun parseTitle(http: String): String {
        val stringBuilder = StringBuilder(http)
        val res = StringBuilder()
        var firstIndex = 0
        var lastIndex = 0
        while (firstIndex != -1) {
            firstIndex = stringBuilder.indexOf("<li>", lastIndex)
            lastIndex = stringBuilder.indexOf("</li>", firstIndex)
            if (firstIndex != -1 && lastIndex != -1) {
                if (!TextUtils.isEmpty(res))
                    res.append( ";\n")
                res.append(stringBuilder.substring(firstIndex + 4, lastIndex))
            }
        }
        return res.toString()
    }

    private fun parseLocation(stringLocation: String): LatLng {
        val stringBuilder = StringBuilder(stringLocation)
        val lastIndex = stringBuilder.indexOf(",")
        val firstIndex = stringBuilder.indexOf(",", lastIndex)
        val latitude: Double
        val longitude: Double

        latitude = java.lang.Double.parseDouble(stringBuilder.substring(0, lastIndex))
        longitude = java.lang.Double.parseDouble(stringBuilder.substring(firstIndex + 2, stringBuilder.length))

        return LatLng(latitude, longitude)
    }
}