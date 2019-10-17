package com.example.milestonesapplication.utils

import android.app.Activity
import android.os.AsyncTask
import com.example.milestonesapplication.model.Milestone
import com.example.milestonesapplication.model.Region
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.IOException
import java.util.*

class HttpProvider(code: String) : AsyncTask<Void, Void, Void>() {
    private var milestones = ArrayList<Milestone>()
    private var regions = ArrayList<Region>()
    private var regionCode = code

    private var httpProviderInterface: HttpProviderInterface? = null

    fun setDelegate(activity: Activity) {
        if (activity is HttpProviderInterface)
            httpProviderInterface = activity
    }

    interface HttpProviderInterface {
        fun onTaskPostExecute(result: Int)

        fun onTaskPostExecute(regions: ArrayList<Region>, milestones: ArrayList<Milestone>)
    }

    override fun doInBackground(vararg p0: Void?): Void? {
        var doc: Document? = null


        try {
            doc = if (regionCode == "all")
                Jsoup.connect("https://гибдд.рф/r/49/milestones").timeout(10 * 2000).get()
            else
                Jsoup.connect("https://гибдд.рф/r/$regionCode/milestones").timeout(10 * 2000).get()
        } catch (e: IOException) {
            httpProviderInterface?.onTaskPostExecute(0)
            e.printStackTrace()
        }

        if (doc != null) {
            regions = RegionsFromHtmlParser().parse(doc.html())
            if(regionCode != "all")
                milestones = MilestonesFromHtmlParser().parse(doc.html())
        } else {
            httpProviderInterface?.onTaskPostExecute(0)
        }

        return null
    }

    override fun onPostExecute(result: Void?) {
        super.onPostExecute(result)
        httpProviderInterface?.onTaskPostExecute(regions, milestones)
    }

}