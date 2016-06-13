package eu.epitech.sami.easybill;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import org.json.JSONArray;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class PreviewActivity extends AppCompatActivity {

    private Client      c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);

        ImageView img = (ImageView) this.findViewById(R.id.imageView);
        Bundle      b = getIntent().getExtras();
        String      path = b.getString("path");

        img.setImageURI(Uri.parse(path));
        img.setRotation(90);
        img.setScaleY(2);
        img.setScaleX(4);

        c = Client.getInstance(this);

        c.sendPic(path);

        Log.d("debug", "img sent");

        parseZones(c.getString());

//        Log.d("debug", c.readString());
    }

    private void parseZones(String str) {

        createButton(0, 0, 10, 10);
    }

    private void createButton(int x1, int y1, int x2, int y2) {
        final Button      b = new Button(this);

        b.setPadding(x1, y1, x2, y2);
        b.setAlpha(0);
    }
}