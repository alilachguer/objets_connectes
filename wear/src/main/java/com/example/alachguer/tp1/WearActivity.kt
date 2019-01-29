package com.example.alachguer.tp1


import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.support.wearable.activity.WearableActivity
import android.util.Log
import android.widget.Button
import android.widget.TextView

class WearActivity : WearableActivity(), SensorEventListener {


    private var mSensorManager: SensorManager? = null
    private var mSensor: Sensor? = null
    // Steps counted since the last reboot
    private var mSteps = 0

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val resetButton = findViewById<Button>(R.id.button)

        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        mSensor = mSensorManager!!.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)

        resetButton.setOnClickListener {
            mSteps=0
            refreshStepCount()
        }



    }

    override fun onResume() {
        super.onResume()
        mSensorManager!!.registerListener(this, mSensor,
                SensorManager.SENSOR_DELAY_NORMAL)
        refreshStepCount()
    }

    override fun onPause() {
        super.onPause()
        mSensorManager!!.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent) {
        Log.i(TAG, "onSensorChanged - " + event.values[0])
        if (event.sensor.type == Sensor.TYPE_STEP_DETECTOR) {
            Log.i(TAG, "Total step count: $mSteps")
            //mSteps = event.values[0].toInt()
            mSteps = mSteps+1
            refreshStepCount()
        }
    }

    private fun refreshStepCount() {
        val desc = findViewById<TextView>(R.id.daily_step_count_desc)
        desc.text = mSteps.toString() + " pas"
    }


    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        Log.i(TAG, "onAccuracyChanged - $sensor")
    }

    companion object {
        private val TAG = WearActivity::class.java.name
    }




}
