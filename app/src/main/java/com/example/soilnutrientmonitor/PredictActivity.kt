package com.example.soilnutrientmonitor

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName

class PredictActivity : AppCompatActivity() {

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var firebaseAuth: FirebaseAuth

    var nValue:Float = (-1).toFloat(); var pValue:Float = (-1).toFloat(); var kValue:Float = (-1).toFloat()
    var phValue:Float = (-1).toFloat(); var ecValue:Float = (-1).toFloat(); var sValue:Float = (-1).toFloat()
    var cuValue:Float = (-1).toFloat(); var feValue:Float = (-1).toFloat(); var mnValue:Float = (-1).toFloat()
    var znValue:Float = (-1).toFloat(); var bValue:Float = (-1).toFloat(); lateinit var showResponse: TextView;
    lateinit var showPredictedCrop: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()

        setContentView(R.layout.activity_predict)
        val pred: Button = findViewById(R.id.btn_predCrop)
        pred.setBackgroundColor(Color.BLUE)
        showResponse = findViewById<TextView>(R.id.dispResponse)
        showPredictedCrop = findViewById<TextView>(R.id.PredictedCropName)
        if (!Python.isStarted()) {
            Python.start(AndroidPlatform(this))
        }
    }

    fun checkUser(){
        val firebaseUser = firebaseAuth.currentUser
        if(firebaseUser == null){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        else{
            val email = firebaseUser.email
            Toast.makeText(this, email.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    fun predict(v: View){

        var edTxt_nValue = findViewById(R.id.edTxt_n) as EditText
        if (edTxt_nValue.text.toString() != "") nValue = edTxt_nValue.text.toString().toFloat()

        var edTxt_pValue = findViewById(R.id.edTxt_p) as EditText
        if (edTxt_pValue.text.toString() != "") pValue = edTxt_pValue.text.toString().toFloat()

        var edTxt_kValue = findViewById(R.id.edTxt_k) as EditText
        if (edTxt_kValue.text.toString() != "") kValue = edTxt_kValue.text.toString().toFloat()

        var edTxt_phValue = findViewById(R.id.edTxt_ph) as EditText
        if (edTxt_phValue.text.toString() != "") phValue = edTxt_phValue.text.toString().toFloat()

        var edTxt_ecValue = findViewById(R.id.edTxt_ec) as EditText
        if (edTxt_ecValue.text.toString() != "") ecValue = edTxt_ecValue.text.toString().toFloat()

        var edTxt_sValue = findViewById(R.id.edTxt_s) as EditText
        if (edTxt_sValue.text.toString() != "") sValue = edTxt_sValue.text.toString().toFloat()

        var edTxt_cuValue = findViewById(R.id.edTxt_cu) as EditText
        if (edTxt_cuValue.text.toString() != "") cuValue = edTxt_cuValue.text.toString().toFloat()

        var edTxt_feValue = findViewById(R.id.edTxt_fe) as EditText
        if (edTxt_feValue.text.toString() != "") feValue = edTxt_feValue.text.toString().toFloat()

        var edTxt_mnValue = findViewById(R.id.edTxt_mn) as EditText
        if (edTxt_mnValue.text.toString() != "") mnValue = edTxt_mnValue.text.toString().toFloat()

        var edTxt_znValue = findViewById(R.id.edTxt_zn) as EditText
        if (edTxt_znValue.text.toString() != "") znValue = edTxt_znValue.text.toString().toFloat()

        var edTxt_bValue = findViewById<EditText>(R.id.edTxt_b)
        if (edTxt_bValue.text.toString() != "") bValue = edTxt_bValue.text.toString().toFloat()

        var defaultValue:Float = (-1).toFloat()
        if(nValue == defaultValue || pValue == defaultValue || kValue == defaultValue || phValue == defaultValue
            || ecValue == defaultValue || sValue == defaultValue || cuValue == defaultValue || feValue == defaultValue
            || mnValue == defaultValue || znValue == defaultValue || bValue == defaultValue)
            Toast.makeText(applicationContext,"Nutrient Values cannot be null", Toast.LENGTH_LONG).show()

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
        val mToast = Toast.makeText(applicationContext,a, Toast.LENGTH_SHORT)
        mToast.show()
        return a
    }
}

data class NutrientInfo(@SerializedName("nitrogen") var nitrogen: Float, @SerializedName("phosphorous") var phosphorous: Float,
                        @SerializedName("potassium") var potassium: Float, @SerializedName("ph") var ph: Float,
                        @SerializedName("electrical conductivity") var electricalConductivity: Float,
                        @SerializedName("sulphur") var sulphur: Float, @SerializedName("copper") var copper: Float,
                        @SerializedName("iron") var iron: Float, @SerializedName("manganese") val manganese: Float,
                        @SerializedName("zinc") var zinc: Float, @SerializedName("boron") var boron: Float)