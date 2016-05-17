package eu.epitech.sami.easybill;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class QuestionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        Bundle extras = getIntent().getExtras();

        if (extras != null)
        {
            extras.getString("CATEGORY");

            ArrayList<String>       list;
            ArrayAdapter<String>    adapter;

        }
    }
}