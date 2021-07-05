package com.example.soilnutrientmonitor.fragments

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import com.example.soilnutrientmonitor.R
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName


class PredictFragment : Fragment() {

    var nValue:Float = (-1).toFloat(); var pValue:Float = (-1).toFloat(); var kValue:Float = (-1).toFloat()
    var phValue:Float = (-1).toFloat(); var ecValue:Float = (-1).toFloat(); var sValue:Float = (-1).toFloat()
    var cuValue:Float = (-1).toFloat(); var feValue:Float = (-1).toFloat(); var mnValue:Float = (-1).toFloat()
    var znValue:Float = (-1).toFloat(); var bValue:Float = (-1).toFloat(); lateinit var showResponse: TextView;
    lateinit var showPredictedCrop: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v:View = inflater.inflate(R.layout.fragment_predict, container, false)
        val pred: Button = v.findViewById(R.id.btn_predCrop)
        pred.setBackgroundColor(Color.BLUE)
        pred.setOnClickListener{predict(v)}
        showResponse = v.findViewById<TextView>(R.id.dispResponse)
        showPredictedCrop = v.findViewById<TextView>(R.id.PredictedCropName)
        return v
    }

    fun predict(v: View){

        var edTxt_nValue = v.findViewById(R.id.edTxt_n) as EditText
        if (edTxt_nValue.text.toString() != "") nValue = edTxt_nValue.text.toString().toFloat()

        var edTxt_pValue = v.findViewById(R.id.edTxt_p) as EditText
        if (edTxt_pValue.text.toString() != "") pValue = edTxt_pValue.text.toString().toFloat()

        var edTxt_kValue = v.findViewById(R.id.edTxt_k) as EditText
        if (edTxt_kValue.text.toString() != "") kValue = edTxt_kValue.text.toString().toFloat()

        var edTxt_phValue = v.findViewById(R.id.edTxt_ph) as EditText
        if (edTxt_phValue.text.toString() != "") phValue = edTxt_phValue.text.toString().toFloat()

        var edTxt_ecValue = v.findViewById(R.id.edTxt_ec) as EditText
        if (edTxt_ecValue.text.toString() != "") ecValue = edTxt_ecValue.text.toString().toFloat()

        var edTxt_sValue = v.findViewById(R.id.edTxt_s) as EditText
        if (edTxt_sValue.text.toString() != "") sValue = edTxt_sValue.text.toString().toFloat()

        var edTxt_cuValue = v.findViewById(R.id.edTxt_cu) as EditText
        if (edTxt_cuValue.text.toString() != "") cuValue = edTxt_cuValue.text.toString().toFloat()

        var edTxt_feValue = v.findViewById(R.id.edTxt_fe) as EditText
        if (edTxt_feValue.text.toString() != "") feValue = edTxt_feValue.text.toString().toFloat()

        var edTxt_mnValue = v.findViewById(R.id.edTxt_mn) as EditText
        if (edTxt_mnValue.text.toString() != "") mnValue = edTxt_mnValue.text.toString().toFloat()

        var edTxt_znValue = v.findViewById(R.id.edTxt_zn) as EditText
        if (edTxt_znValue.text.toString() != "") znValue = edTxt_znValue.text.toString().toFloat()

        var edTxt_bValue = v.findViewById<EditText>(R.id.edTxt_b)
        if (edTxt_bValue.text.toString() != "") bValue = edTxt_bValue.text.toString().toFloat()

        var defaultValue:Float = (-1).toFloat()
        if(nValue == defaultValue || pValue == defaultValue || kValue == defaultValue || phValue == defaultValue
            || ecValue == defaultValue || sValue == defaultValue || cuValue == defaultValue || feValue == defaultValue
            || mnValue == defaultValue || znValue == defaultValue || bValue == defaultValue)
            Toast.makeText(v.context,"Nutrient Values cannot be null", Toast.LENGTH_LONG).show()

        else{
            val python = Python.getInstance()
            val pythonFile = python.getModule("PredictCrop")
            val predictedCrop = pythonFile.callAttr("predictCrop", nValue, pValue, kValue, phValue,
                ecValue, sValue, cuValue, feValue, mnValue, znValue, bValue)
            Log.d("outputPY: ", predictedCrop.toString())
            showPredictedCrop.text = "Predicted Crop is $predictedCrop"
            ConvertDataToJSON(nValue, pValue, kValue, phValue,
                ecValue, sValue, cuValue, feValue, mnValue, znValue, bValue)
        }
    }

    fun ConvertDataToJSON( nitrogen: Float, phosphorous: Float, potassium: Float,
                           ph: Float, electricalConductivity: Float, sulphur: Float,
                           copper: Float, iron: Float, manganese: Float, zinc: Float, boron: Float): String {
        val nutrientInfo = NutrientInfo(nValue, pValue, kValue, phValue, ecValue, sValue, cuValue, feValue,
            mnValue, znValue, bValue)

        var a:String = GsonBuilder().setPrettyPrinting().create().toJson(nutrientInfo)
        return a
    }
}

data class NutrientInfo(@SerializedName("nitrogen") var nitrogen: Float, @SerializedName("phosphorous") var phosphorous: Float,
                        @SerializedName("potassium") var potassium: Float, @SerializedName("ph") var ph: Float,
                        @SerializedName("electrical conductivity") var electricalConductivity: Float,
                        @SerializedName("sulphur") var sulphur: Float, @SerializedName("copper") var copper: Float,
                        @SerializedName("iron") var iron: Float, @SerializedName("manganese") val manganese: Float,
                        @SerializedName("zinc") var zinc: Float, @SerializedName("boron") var boron: Float)