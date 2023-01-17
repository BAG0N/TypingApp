package com.example.typingapp

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.androidplot.util.PixelUtils
import com.androidplot.xy.*
import java.text.FieldPosition
import java.text.Format
import java.text.ParsePosition
import kotlin.math.roundToInt

class ProfileFragment : Fragment(R.layout.fragment_profile) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val prefs = requireActivity().getSharedPreferences("Settings", 0)
        val scores = prefs.getString("Scores", "50,75,50")!!
        println("scores $scores")
        val series1Number = scores.split(",").map { wpm -> wpm.toInt() }
        val domainLabels = Array(series1Number.size) {i -> i + 1}.toList()
        println("getAverageWPM: ${getAverageWPM()}")

        val series1 : XYSeries = SimpleXYSeries(
            series1Number, SimpleXYSeries.ArrayFormat.Y_VALS_ONLY
            ,"WPM");

        val wpmFormatter = LineAndPointFormatter(Color.BLUE, Color.rgb(145, 83, 20), null, null)

        wpmFormatter.interpolationParams = CatmullRomInterpolator.Params(10,
            CatmullRomInterpolator.Type.Centripetal)

        val plot = view.findViewById<XYPlot>(R.id.plot)
        plot.addSeries(series1,wpmFormatter)
        plot.legend.isVisible = false

        wpmFormatter.vertexPaint.strokeWidth = PixelUtils.dpToPix(10f)

        plot.graph.getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).format = object : Format() {
            override fun format(
                obj: Any?,
                toAppendTo: StringBuffer,
                pos: FieldPosition
            ): StringBuffer {
                val i = (obj as Number).toFloat().roundToInt()
                return toAppendTo.append("")
            }

            override fun parseObject(source: String?, pos: ParsePosition): Any? {
                return null
            }

        }
        PanZoom.attach(plot)

    }

    private fun getAverageWPM(): Int {
        val prefs = requireActivity().getSharedPreferences("Settings", 0)
        val scores = prefs.getString("Scores", "50,75,50")!!.split(',')
        return scores.sumOf { i -> i.toInt() } / scores.size
    }
}