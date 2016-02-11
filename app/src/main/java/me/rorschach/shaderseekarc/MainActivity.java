package me.rorschach.shaderseekarc;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "ShaderSeekArc";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//
//        ShaderSeekArc seekArc = (ShaderSeekArc) findViewById(R.id.seek_arc);
//
//        int[] colors = new int[]{0xFF2C3EFF, 0xFF53FF65, 0xFF000000};
//        seekArc.setColors(colors);
//
//        float[] positions = new float[]{0, 1/2f, 1};
//        seekArc.setPositions(positions);
//
//        seekArc.setStartColor(0xFFFF2D0C);
//        seekArc.setEndColor(0xFF1636FF);
//
//        seekArc.setStartValue(20);
//        seekArc.setEndValue(30);
//        seekArc.setProgress(25);
//
//        seekArc.setStartAngle(-180);
//        seekArc.setEndAngle(180);
//
//        seekArc.setOnSeekArcChangeListener(new ShaderSeekArc.OnSeekArcChangeListener() {
//            @Override
//            public void onProgressChanged(ShaderSeekArc seekArc, float progress) {
//                Log.d(TAG, "progress " + progress);
//            }
//
//            @Override
//            public void onStartTrackingTouch(ShaderSeekArc seekArc) {
//                Log.d(TAG, "onStartTrackingTouch");
//            }
//
//            @Override
//            public void onStopTrackingTouch(ShaderSeekArc seekArc) {
//                Log.d(TAG, "onStopTrackingTouch");
//            }
//        });
    }
}
