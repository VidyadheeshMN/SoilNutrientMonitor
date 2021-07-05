package com.example.soilnutrientmonitor.fragments

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.soilnutrientmonitor.R

class ContinousSoilData : Fragment() {

    //private lateinit var myContext: Context

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
        //r, g, b, ph, temp, humidity, moisture, salinity
        fetchData.setOnClickListener{
            // Request a string response from the provided URL.
            val stringRequest = StringRequest(
                Request.Method.GET, url,
                Response.Listener<String> { response ->
                    var a: List<String> = response.replace("[", "").replace("]", "").replace("\"", "").split(",")
                    var r: String = "RED:" + a[0] + "\n"
                    var g: String = "GREEN:" + a[1] + "\n"
                    var b: String = "BLUE:" +a[2] + "\n"
                    var ph: String = "PH:" + a[3] + "\n"
                    var temp: String = "TEMPERATURE:" + a[4] + "\n"
                    var humid: String = "HUMIDITY:" + a[5] + "\n"
                    var moisture: String = "MOISTURE:" + a[6] + "\n"
                    var salinity: String = "SALINITY:" + a[7]
                    dataText.text = r+g+b+ph+temp+humid+moisture+salinity
                },
                Response.ErrorListener { dataText.text = "response error" })

            queue.add(stringRequest)
        }
        return v
    }
}