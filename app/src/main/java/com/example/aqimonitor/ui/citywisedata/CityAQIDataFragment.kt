package com.example.aqimonitor.ui.citywisedata

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aqimonitor.R
import com.example.aqimonitor.config.DEBUG_ON
import com.example.aqimonitor.database.model.CityAQIData
import com.example.aqimonitor.helpers.AQIGradeFormatter
import com.example.aqimonitor.helpers.StringFormatter
import com.example.aqimonitor.ui.citywisedata.adapter.CityAQIDataListAdapter
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.android.material.snackbar.Snackbar
import java.util.*
import kotlin.collections.ArrayList

class CityAQIDataFragment : Fragment() {

    private lateinit var cityAQIDataViewModel: CityAQIDataViewModel
    private lateinit var recyclerViewAQIData: RecyclerView
    private val aqiDataList = ArrayList<CityAQIData>()

    private lateinit var connectionLostSnackBar: Snackbar

    private lateinit var lineChartAQIDataForACity: LineChart
    private val entryList = ArrayList<Entry>()
    private lateinit var lineDataSet: LineDataSet

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        cityAQIDataViewModel =
            ViewModelProvider(requireActivity()).get(CityAQIDataViewModel::class.java)
    }

    override fun onResume() {
        super.onResume()
        cityAQIDataViewModel.refreshCityAQIData()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_city_aqi_data, container, false)

        connectionLostSnackBar = Snackbar.make(
            root,
            "Unable to connect to server at the moment, try again after a while!",
            Snackbar.LENGTH_LONG
        ).setAction("Retry") { cityAQIDataViewModel.refreshCityAQIData() }

        val cityListAdapter = CityAQIDataListAdapter(aqiDataList)
        recyclerViewAQIData = root.findViewById(R.id.recyclerViewAQIData)
        recyclerViewAQIData.isNestedScrollingEnabled = false
        recyclerViewAQIData.layoutManager =
            LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        recyclerViewAQIData.adapter = cityListAdapter

        lineChartAQIDataForACity = root.findViewById(R.id.lineChartAQIDataForACity)
        initChart()

        val textViewCityName: TextView = root.findViewById(R.id.textViewCity)
        val textViewCurrentAQI: TextView = root.findViewById(R.id.textViewCurrentAQI)
        cityAQIDataViewModel.cityAQIData.observe(viewLifecycleOwner, Observer {

            val mostRecentAQIValueFormattedString =
                StringFormatter.getDoubleFormatterWithTwoDecimals(it.currentAQI.toDouble())

            textViewCityName.text = it.cityName
            textViewCurrentAQI.text = mostRecentAQIValueFormattedString

            textViewCurrentAQI.setTextColor(
                resources.getColor(
                    AQIGradeFormatter.getAQIColorHighlights(
                        it.currentAQI
                    )
                )
            )

        })

        cityAQIDataViewModel.getCityAQIDataFromDB().observe(viewLifecycleOwner, Observer {
            aqiDataList.clear()
            aqiDataList.addAll(it)
            recyclerViewAQIData.adapter?.notifyDataSetChanged()

            val mostRecentAQIValue = aqiDataList[0].currentAQI
            val mostRecentAQIValueFormattedString =
                StringFormatter.getDoubleFormatterWithTwoDecimals(mostRecentAQIValue.toDouble())

            textViewCurrentAQI.text = mostRecentAQIValueFormattedString

            textViewCurrentAQI.setTextColor(
                resources.getColor(
                    AQIGradeFormatter.getAQIColorHighlights(
                        mostRecentAQIValue
                    )
                )
            )

            setChartData()
        })

        cityAQIDataViewModel.isConnectionLost.observe(viewLifecycleOwner, Observer {
            connectionLostSnackBar.show()
        })

        return root
    }

    override fun onPause() {
        super.onPause()
        cityAQIDataViewModel.stopCityAQIDataUpdates()
    }

    private fun initChart() {
        // chart styling
        lineChartAQIDataForACity.description.isEnabled = false
        lineChartAQIDataForACity.setPinchZoom(false)
        lineChartAQIDataForACity.setDrawGridBackground(false)
        lineChartAQIDataForACity.legend.isEnabled = false

        // enable scaling and dragging
        lineChartAQIDataForACity.isDragEnabled = true
        lineChartAQIDataForACity.setScaleEnabled(true)

        lineChartAQIDataForACity.isDoubleTapToZoomEnabled = false
        lineChartAQIDataForACity.setTouchEnabled(true)

        // disable dual axis (only use LEFT axis)
        lineChartAQIDataForACity.axisRight.isEnabled = true
        lineChartAQIDataForACity.axisLeft.isEnabled = false
        lineChartAQIDataForACity.xAxis.isEnabled = false

        lineChartAQIDataForACity.setViewPortOffsets(64f, 0f, 110f, 0f)

        // x axis
        val xAxis: XAxis = lineChartAQIDataForACity.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        // xAxis.enableGridDashedLine(10f, 10f, 0f)
        // xAxis.granularity = 1f
        xAxis.labelCount = 7

        // y axis
        val leftAxis: YAxis = lineChartAQIDataForACity.axisLeft
        // leftAxis.granularity = 1f // only intervals of 1 day
        leftAxis.labelCount = 7

        // horizontal grid lines
        // leftAxis.enableGridDashedLine(10f, 10f, 0f)

        // axis range
        leftAxis.axisMinimum = -50f
        leftAxis.axisMaximum = 550f

        // draw points over time
        lineChartAQIDataForACity.animateX(1500)

        // Utils.init(activity)
        // set blank data set entryList
        lineDataSet = LineDataSet(entryList, "")

        lineDataSet.mode = LineDataSet.Mode.CUBIC_BEZIER
        // lineDataSet.cubicIntensity = 1f
        lineDataSet.setDrawFilled(true)
        lineDataSet.setDrawCircles(true)
        lineDataSet.lineWidth = 2f

        lineDataSet.highLightColor = ContextCompat.getColor(requireActivity(), R.color.purple_500)
        /*lineDataSet.fillDrawable = ContextCompat.getDrawable(requireActivity(), AQIGradeFormatter.getAQIColorHighlights(
            maxYAxisValues.toString()
        ))*/

        // lineDataSet.setDrawHorizontalHighlightIndicator(false)

        val data: LineData = LineData(lineDataSet)
        // data.setDrawValues(false)
        lineChartAQIDataForACity.data = data

    }

    private fun setChartData() {
        // val entryList = ArrayList<Entry>()
        entryList.clear()
        for ((i, cityAQIData) in aqiDataList.reversed().withIndex()) {
            entryList.add(Entry(i.toFloat(), cityAQIData.currentAQI.toFloat()))
        }

        // test
        /*for (i in 0..45) {
            val value = (Math.random() * 180) - 30
            entryList.add(Entry(i.toFloat(), value.toFloat()))
        }*/
        // test

        // dynamic axis range
        // y axis
        val leftAxis: YAxis = lineChartAQIDataForACity.axisLeft
        val listOfCurrentAQIValues = arrayListOf<Float>()
        aqiDataList.map {
            listOfCurrentAQIValues.add(it.currentAQI.toFloat())
        }
        val minYAxisValues = Collections.min(listOfCurrentAQIValues)
        val maxYAxisValues = Collections.max(listOfCurrentAQIValues)
        leftAxis.axisMinimum = minYAxisValues - 4
        leftAxis.axisMaximum = maxYAxisValues + 4

        // Utils.init(activity)
        if (lineChartAQIDataForACity.data != null && lineChartAQIDataForACity.data.dataSetCount > 0) {
            // re init data set
            lineDataSet = lineChartAQIDataForACity.data.getDataSetByIndex(0) as LineDataSet

            // set color based on last AQI value
            val colorRes: Int = AQIGradeFormatter.getAQIColorHighlights(maxYAxisValues.toString())
            val colorResList = ArrayList<Int>()
            colorResList.add(ContextCompat.getColor(requireActivity(), colorRes))
            lineDataSet.circleColors = colorResList
            lineDataSet.color = ContextCompat.getColor(requireActivity(), colorRes)
            lineDataSet.fillColor = ContextCompat.getColor(requireActivity(), colorRes)

            // set new data set
            lineDataSet.values = entryList
            lineDataSet.notifyDataSetChanged()
            lineChartAQIDataForACity.data.notifyDataChanged()
            lineChartAQIDataForACity.notifyDataSetChanged()
        } else {
            if (DEBUG_ON) Log.e(CityAQIDataFragment::class.java.simpleName, "error!!! :/")
        }
    }

}