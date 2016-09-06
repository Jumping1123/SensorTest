package com.example.jumping.sensortest;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private SensorManager sensorManager;
    private ImageView compassimg;
    private TextView lightlevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        compassimg = (ImageView) findViewById(R.id.compass_img);
        lightlevel = (TextView) findViewById(R.id.light_level);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor lightsensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        Sensor magneticsensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        Sensor accelerometersensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(listener, lightsensor, SensorManager.SENSOR_DELAY_UI);
        sensorManager.registerListener(listener, magneticsensor, SensorManager.SENSOR_DELAY_UI);
        sensorManager.registerListener(listener, accelerometersensor, SensorManager.SENSOR_DELAY_UI);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (sensorManager != null) {
            sensorManager.unregisterListener(listener);
        }
    }

    private SensorEventListener listener = new SensorEventListener() {
        float[] accelerometervalues = new float[3];
        float[] magneticvalues = new float[3];
        private float lastrotatedegree;

        @Override
        public void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                accelerometervalues = event.values.clone();
                float xvalue = Math.abs(event.values[0]);
                float yvalue = Math.abs(event.values[1]);
                float zvalue = Math.abs(event.values[2]);
                if (xvalue > 20 || yvalue > 20 || zvalue > 20) {
                    Toast.makeText(Myapplication.getContext(),"摇一摇",Toast.LENGTH_SHORT).show();
                }
            } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                magneticvalues = event.values.clone();
            } else if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
                float value = event.values[0];
                lightlevel.setText("Current light level is " + value + " lx");
            }
            float[] R = new float[9];
            float[] values = new float[3];
            SensorManager.getRotationMatrix(R, null, accelerometervalues, magneticvalues);
            SensorManager.getOrientation(R, values);
            float rotatedegree = -(float) Math.toDegrees(values[0]);
            if (Math.abs(rotatedegree - lastrotatedegree) > 1) {
                RotateAnimation animation = new RotateAnimation(lastrotatedegree, rotatedegree, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                animation.setFillAfter(true);
                compassimg.startAnimation(animation);
                lastrotatedegree = rotatedegree;
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };
}
