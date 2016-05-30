package eu.epitech.sami.easybill;

import android.app.Activity;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;

public class PhotoActivity extends Activity {

    private Camera              camera;
    private CameraView          cameraView;
    private Button imageButton;

    protected void              onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        imageButton = (Button) findViewById(R.id.button);

        imageButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Log.d("status", "capture image");

            }

        });

        try{
            camera = Camera.open(0);
        } catch (Exception e){
            Log.d("ERROR", "Failed to get camera: " + e.getMessage());
        }

        if(camera != null) {
            Log.d("status", "camera not null");
            cameraView = new CameraView(this, camera);
            FrameLayout camera_view = (FrameLayout)findViewById(R.id.cameraPreview);
            camera_view.addView(cameraView);
        }
        else
            Log.d("status", "camera null");
    }
}