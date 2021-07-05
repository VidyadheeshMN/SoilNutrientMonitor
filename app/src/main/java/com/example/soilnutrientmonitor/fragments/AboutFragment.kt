package com.example.soilnutrientmonitor.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.soilnutrientmonitor.R

class AboutFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v: View = inflater.inflate(R.layout.fragment_about, container, false)
        var ParagraphText = v.findViewById<TextView>(R.id.paragraph)
        ParagraphText.text = "The goal of this App is to help farmers maximise their profits\n" +
                "and help them predict the crops which can be\n" +
                "grown based on the nutrient values present in their land\n" +
                "The nutrient values that are considered for the prediction are\n" +
                "Nitrogen, Phosphorous, Potassium, PH, Electrical Conductivity, \n" +
                "Sulphur, Copper, Iron, Manganese, Zinc and Boron"
        return v
    }
}