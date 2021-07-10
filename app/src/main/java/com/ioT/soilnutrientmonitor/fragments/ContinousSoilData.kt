package com.ioT.soilnutrientmonitor.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.ioT.soilnutrientmonitor.R
import com.mikhaellopez.circularprogressbar.CircularProgressBar
import org.w3c.dom.Text

class ContinousSoilData : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v:View = inflater.inflate(R.layout.fragment_continous_soil_data, container, false)

        val dataText: TextView = v.findViewById(R.id.ViewSoilData)
        val fetchData : Button = v.findViewById(R.id.FetchData)

        val queue = Volley.newRequestQueue(requireContext())
        val url = "http://blynk-cloud.com/eXpqtX-9hyztgT-hSy2Modfq9agCaod9/get/V8"

        var resp: List<String> = mutableListOf()
        fetchData.setOnClickListener{
            // Request a string response from the provided URL.
            val stringRequest = StringRequest(
                Request.Method.GET, url,
                { response ->
                    resp = response.replace("[", "")
                        .replace("]", "")
                        .replace("\"", "")
                        .split(",")
                    var r: String = "Test for Light Availability:\nRED: " + resp[0] + "\n"
                    var g: String = "GREEN: " + resp[1] + "\n"
                    var b: String = "BLUE:" +resp[2] + "\n\n"
                    var ph: String = "PH:" + resp[3] + "\n"
                    var temp = "TEMPERATURE:" + resp[4] + "\n"
                    var humid: String = "HUMIDITY:" + resp[5] + "\n"
                    var moisture: String = "MOISTURE:" + resp[6] + "\n"
                    var salinity: String = "SALINITY:" + resp[7]
                    dataText.text = r+g+b+ph+temp+humid+moisture+salinity

                    val temperatureProgressBar = v.findViewById<CircularProgressBar>(R.id.temperatureProgressBar)
                    temperatureProgressBar.apply {
                        // set progress with animation
                        setProgressWithAnimation(resp[4].toFloat(), 600)

                        // Set Progress Max
                        progressMax = 100f

                        // Set ProgressBar Color
                        progressBarColor = Color.WHITE

                        // Set background ProgressBar Color
                        backgroundProgressBarColor = Color.parseColor("#33691e") //dark green

                        // Set Width
                        progressBarWidth = 7f // in DP
                        backgroundProgressBarWidth = 12f // in DP

                        // Other
                        roundBorder = true
                        startAngle = 0f
                        progressDirection = CircularProgressBar.ProgressDirection.TO_RIGHT
                    }

                    temperatureProgressBar.onProgressChangeListener = { progress ->
                        temperatureProgressBar.visibility = View.VISIBLE
                        var temp = v.findViewById<TextView>(R.id.tempText)
                        temp.visibility = View.VISIBLE
                        temp.text = "Temperature in C: ${resp[4]}/100"

                    }

                    val humidityProgressBar = v.findViewById<CircularProgressBar>(R.id.humidityProgressBar)
                    humidityProgressBar.apply {
                        // set progress with animation
                        setProgressWithAnimation(resp[5].toFloat(), 600)

                        // Set Progress Max
                        progressMax = 100f

                        // Set ProgressBar Color
                        progressBarColor = Color.WHITE

                        // Set background ProgressBar Color
                        backgroundProgressBarColor = Color.parseColor("#33691e") //dark green

                        // Set Width
                        progressBarWidth = 7f // in DP
                        backgroundProgressBarWidth = 12f // in DP

                        // Other
                        roundBorder = true
                        startAngle = 0f
                        progressDirection = CircularProgressBar.ProgressDirection.TO_RIGHT
                    }

                    humidityProgressBar.onProgressChangeListener = { progress ->
                        humidityProgressBar.visibility = View.VISIBLE
                        var humidity = v.findViewById<TextView>(R.id.humidityText)
                        humidity.visibility = View.VISIBLE
                        humidity.text = "HUMIDITY %: ${resp[5]}/100"
                    }

                    val phProgressBar = v.findViewById<CircularProgressBar>(R.id.phProgressBar)
                    phProgressBar.apply {
                        // set progress with animation
                        setProgressWithAnimation(resp[3].toFloat(), 600)

                        // Set Progress Max
                        progressMax = 50f

                        // Set ProgressBar Color
                        progressBarColor = Color.WHITE

                        // Set background ProgressBar Color
                        backgroundProgressBarColor = Color.parseColor("#33691e") //dark green

                        // Set Width
                        progressBarWidth = 7f // in DP
                        backgroundProgressBarWidth = 12f // in DP

                        // Other
                        roundBorder = true
                        startAngle = 0f
                        progressDirection = CircularProgressBar.ProgressDirection.TO_RIGHT
                    }

                    phProgressBar.onProgressChangeListener = { progress ->
                        phProgressBar.visibility = View.VISIBLE
                        var ph = v.findViewById<TextView>(R.id.phText)
                        ph.visibility = View.VISIBLE
                        ph.text = "PH: ${resp[3]}/14"
                    }
            },
            { dataText.text = "response error" })

        queue.add(stringRequest)
        }
        return v
    }
}