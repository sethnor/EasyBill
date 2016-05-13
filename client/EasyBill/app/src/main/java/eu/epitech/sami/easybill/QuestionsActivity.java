package eu.epitech.sami.easybill;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class QuestionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        Bundle extras = getIntent().getExtras();

        if (extras != null)
        {
            Client c = Client.getInstance();

            extras.getString("CATEGORY");

            c.write("UPDATE_0");

            Log.d("status", "update sent");

            Log.d("answer", Client.readString());
        }


    }
}
