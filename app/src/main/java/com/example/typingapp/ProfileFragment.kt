package com.example.typingapp

import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.androidplot.xy.*
import java.text.FieldPosition
import java.text.Format
import java.text.ParsePosition
import kotlin.math.roundToInt
import kotlin.random.Random

class ProfileFragment : Fragment(R.layout.fragment_profile) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val series1Number = Array(10) { Random.nextInt(50, 120)}.toList()
        val domainLabels = Array(series1Number.size) {i -> i + 1}.toList()

        val series1 : XYSeries = SimpleXYSeries(
            series1Number, SimpleXYSeries.ArrayFormat.Y_VALS_ONLY
            ,"WPM");

        val wpmSeries = LineAndPointFormatter(Color.BLUE, Color.rgb(145, 83, 20), null, null)

        wpmSeries.interpolationParams = CatmullRomInterpolator.Params(10,
            CatmullRomInterpolator.Type.Centripetal)

        val plot = view.findViewById<XYPlot>(R.id.plot)
        plot.addSeries(series1,wpmSeries)
        plot.legend.isVisible = false

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
//        PanZoom.attach(plot)
    }
}