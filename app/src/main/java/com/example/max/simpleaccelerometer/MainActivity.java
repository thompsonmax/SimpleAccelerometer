package com.example.max.simpleaccelerometer;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private float lastX, lastY, lastZ;

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;

    private float deltaX = 0;
    private float deltaY = 0;
    private float deltaZ = 0;

    private TextView linearX, linearY, linearZ, linearMag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeViews();

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        if (mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            // Success: we have an accelerometer
            mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    public void initializeViews() {
        linearX = (TextView) findViewById(R.id.LinX);
        linearY = (TextView) findViewById(R.id.LinY);
        linearZ = (TextView) findViewById(R.id.LinZ);
        linearMag = (TextView) findViewById(R.id.LinMag);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // clean current values
        displayCleanValues();
        // display the current x,y,z accelerometer values
        displayCurrentValues();

        // get the change of the x,y,z values of the accelerometer
        deltaX = Math.abs(lastX - event.values[0]);
        deltaY = Math.abs(lastY - event.values[1]);
        deltaZ = Math.abs(lastZ - event.values[2]);

        // if the change is below 2, it is just plain noise

        if (deltaX < 2)
            deltaX = 0;
        if (deltaY < 2)
            deltaY = 0;

    }

    public void displayCurrentValues() {
        linearX.setText(Float.toString(deltaX));
        linearY.setText(Float.toString(deltaY));
        linearZ.setText(Float.toString(deltaZ));
        linearMag.setText(Float.toString(magnitude3D(deltaX, deltaY, deltaZ)));
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void displayCleanValues() {
        linearX.setText("0.0");
        linearY.setText("0.0");
        linearZ.setText("0.0");
        linearMag.setText("0.0");
    }


    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    private float magnitude3D(float x, float y, float z) {
        return (float) Math.sqrt(x * x + y * y + z * z);
    }
}
