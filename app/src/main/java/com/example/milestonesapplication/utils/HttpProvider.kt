package com.example.milestonesapplication.utils

import android.os.AsyncTask
import com.example.milestonesapplication.interfaces.DataLoadInterface
import com.example.milestonesapplication.models.Milestone
import com.example.milestonesapplication.models.Region
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.IOException
import java.util.*

class HttpProvider(
        private var dataLoadInterface: DataLoadInterface) : AsyncTask<Void, Void, Void>() {
    private var milestones = ArrayList<Milestone>()
    private var regions = ArrayList<Region>()
    private var regionCode: String? = null

    fun setRegion(regionCode: String) {
        this.regionCode = regionCode
    }

    override fun doInBackground(vararg p0: Void?): Void? {
        var doc: Document? = null

        try {
            val url = when (regionCode) {
                "all" -> "https://гибдд.рф/r/49/milestones"
                else -> "https://гибдд.рф/r/$regionCode/milestones"
            }
            doc = Jsoup.connect(url).timeout(10 * 2000).get()
        } catch (e: IOException) {
            dataLoadInterface.onErrorDataLoad()
            e.printStackTrace()
        }

        if (doc != null) {
            regions = RegionsFromHtmlParser().parse(doc.html())
            if (regionCode != "all")
                milestones = MilestonesFromHtmlParser().parse(doc.html())
        } else {
            dataLoadInterface.onErrorDataLoad()
        }

        return null
    }

    override fun onPostExecute(result: Void?) {
        super.onPostExecute(result)
        dataLoadInterface.onDataLoaded(regions, milestones)
    }

}