package eu.epitech.sami.easybill;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class QuestionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        Bundle extras = getIntent().getExtras();

        if (extras != null)
        {
            extras.getString("CATEGORY");

            String[] values = new String[] { "Qu'est ce que le solde antérieur ?", "Qu'est ce que le tarif bleu ?", "Qu'est ce que le solde en faveur ?",
                    "Qu'est ce que le net à payer ?", "Qu'est ce que l'historique de consommation ?", "Qu'est ce que la facture de régularisation ?", "Quels sont les tarifs et diverses contribution ?",
                    "Quelles sont mes heures creuses ?", "Combien me coûte l'abonnement par mois ?", "Qu'est-ce que l'auto-relève ?", "Quel est le prix de l'abonnement ?"};
            ListView listV = (ListView)findViewById(R.id.listView);

            final ArrayList<String> list = new ArrayList<String>();
            for (int i = 0; i < values.length; ++i) {
                list.add(values[i]);
            }

            // à tester !!! //
            list.add(readFromFile());

            ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
            listV.setAdapter(adapter);
        }
    }

    private String readFromFile() {

        String ret = "";

        try {
            InputStream inputStream = openFileInput("questions.txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }
}