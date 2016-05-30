package eu.epitech.sami.easybill;

import android.content.Context;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

public class CameraView extends SurfaceView implements SurfaceHolder.Callback {

    private Camera              camera;
    private SurfaceHolder       holder;

    public                      CameraView(Context c, Camera cam) {
        super(c);

        camera = cam;
        camera.setDisplayOrientation(90);

        holder = getHolder();
        holder.addCallback(this);
        holder.setType(SurfaceHolder.SURFACE_TYPE_NORMAL);
    }

    @Override
    public void                 surfaceCreated(SurfaceHolder surfaceHolder) {
        try {
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void                 surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
        if(holder.getSurface() == null)
            return;

        try{
            camera.stopPreview();
        } catch (Exception e){
            e.printStackTrace();
        }

        try{
            camera.setPreviewDisplay(holder);
            camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void                 surfaceDestroyed(SurfaceHolder surfaceHolder) {
        camera.stopPreview();
        camera.release();
    }
}