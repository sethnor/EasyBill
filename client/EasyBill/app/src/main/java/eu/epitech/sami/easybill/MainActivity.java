package eu.epitech.sami.easybill;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Client c = Client.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        c.connect();

        final Button button = (Button) findViewById(R.id.buttonPhoto);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), PhotoActivity.class);

                startActivity(i);
            }
        });

        final Button gaz = (Button) findViewById(R.id.buttonGaz);
        gaz.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), QuestionsActivity.class);

                i.putExtra("CATEGORY", "gaz");
                startActivity(i);
            }
        });

        final Button elec = (Button) findViewById(R.id.buttonElec);
        elec.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), QuestionsActivity.class);

                i.putExtra("CATEGORY", "elec");
                startActivity(i);
            }
        });
    }
}
