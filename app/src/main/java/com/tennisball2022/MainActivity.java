package com.tennisball2022;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * @author Joxx0181
 * @version 2022.0506
 */

@SuppressWarnings("ALL")
public class MainActivity extends AppCompatActivity implements SensorEventListener {

    /**
     * Initializing
     * @param sensorManager for system sensor
     * @param sensorOfAccelerometer for motion sensor
     * @param backgroundFrame for the backgroundimage
     * @param tennisBall for the ball image
     * @param widthOfFrame for frame width
     * @param heightOfFrame for frame height
     * @param radiusOfFrame for frame radius
     * @param widthOfScreen for screen width
     * @param heightOfScreen for screen height
     */
    SensorManager sensorManager;
    Sensor sensorOfAccelerometer;
    ImageView backgroundFrame;
    ImageView tennisBall;
    DisplayMetrics metrics;

    private int widthOfFrame;
    private int heightOfFrame;
    private int radiusOfFrame;
    private int widthOfScreen;
    private int heightOfScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Getting view that is identified by the android:id
        backgroundFrame = findViewById(R.id.bgFrame);
        tennisBall = findViewById(R.id.tennisBall);

        // Setting width, height and radius
        widthOfFrame = backgroundFrame.getWidth();
        heightOfFrame = backgroundFrame.getHeight();
        radiusOfFrame = 15;

        // Getting system sensor
        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        assert sensorManager != null;

        // Selecting accelerometer sensor for detect the motion
        sensorOfAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        // Getting height and width of screen
        metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        heightOfScreen = metrics.heightPixels;
        widthOfScreen = metrics.widthPixels;

        // Perform register event on sensorManager - using .SENSOR_DELAY_FASTEST for setting ball speed
        sensorManager.registerListener(this, sensorOfAccelerometer, SensorManager.SENSOR_DELAY_FASTEST);
    }

    // Method called when there is a new sensor event, which is when the ball move
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        // Current ball coordinates
        float X = tennisBall.getX();
        float Y = tennisBall.getY();

        // Coordinates for where ball moves to
        float nextX = X + sensorEvent.values[1];
        float nextY = Y + sensorEvent.values[0];

        // Moving ball to the left side
        if((nextX - radiusOfFrame) >= (widthOfFrame / 2)){
            tennisBall.setX(nextX);
        }
        else
        {
            // Calling method for displaying message to the user
            onBallHitFrame();
            tennisBall.setX(nextX + 35);
        }

        // Moving ball to the top
        if((nextY - radiusOfFrame) >= (heightOfFrame / 2) + 10){
            tennisBall.setY(nextY);
        }
        else
        {
            // Calling method for displaying message to the user
            onBallHitFrame();
            tennisBall.setY(nextY + 35);
        }

        // Moving ball to the right side
        if ((nextX + radiusOfFrame) > widthOfScreen - (widthOfFrame / 2) - 160) {

            // Calling method for displaying message to the user
            onBallHitFrame();
            tennisBall.setX(nextX - 35);
        }

        // Moving ball to the bottom
        if ((nextY + radiusOfFrame) > heightOfScreen - (heightOfFrame / 2) - 320) {

            // Calling method for displaying message to the user
            onBallHitFrame();
            tennisBall.setY(nextY - 35);
        }

    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    // Method called for resuming TennisBall2022 app
    @Override
    protected void onResume() {
        super.onResume();

        // Perform register event on sensorManager - using .SENSOR_DELAY_FASTEST for setting ball speed
        sensorManager.registerListener(this, sensorOfAccelerometer, SensorManager.SENSOR_DELAY_FASTEST);
    }

    // Method CALLED when app not in use
    @Override
    protected void onPause() {
        super.onPause();

        // Remove register event on sensorManager
        sensorManager.unregisterListener(this);
    }

     // Method called for displaying a message to user
    public void onBallHitFrame(){

        // Using Toast to view a little message for the user when ball hits edge of frame
        Toast.makeText(MainActivity.this, "Ball off court !", Toast.LENGTH_SHORT).show();
    }
}