package fatec.com.br.appprofangela;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import scala.util.parsing.combinator.testing.Str;

//import scala.util.parsing.combinator.testing.Str;

public class DiaSemanaSelecionado extends AppCompatActivity {

    String diaSemanaSelecionado = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dia_semana_selecionado);

        LinearLayout linearLayout = findViewById(R.id.linearLayout_dia_semana_selecionado);

        Intent intent = getIntent();

        diaSemanaSelecionado = intent.getStringExtra("diaSemana");

        Log.i("D.SEM.SELEC.INTENT", diaSemanaSelecionado);

        ParseObject objetoGrupo = ParseObject.createWithoutData("GrupoCarona", BaseActivity.grupoSelecionadoId);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Recorrencia");

        query.whereEqualTo("pointerGrupoCarona", objetoGrupo);

        query.whereEqualTo("diasDaSemana", diaSemanaSelecionado);

        query.include("arrayTrajetos");

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e == null){

                    if(objects.size() > 0){

                        Log.i("D.SEM.ARRAY", "Objetos Encontrados!" + objects.size());

                        int count = 0;

                        TextView[] myTextView = new TextView[10]; //Arrumar o tamanho deste Array !!!

                        for (ParseObject o : objects) {

                            Log.i("D.SEM.ARRAY.ID", "objetoId: " + o.getObjectId());

                            List<ParseObject> trajetos = o.getList("arrayTrajetos");

                            if(trajetos != null){
                                if(trajetos.size() > 0){

                                    for(int i = 0; i < trajetos.size(); i++){

                                        Log.i("D.SEM.ARRAY.TRAJ,ID", "objetoId Trajeto: " + trajetos.get(i).getObjectId());

                                        Double km = trajetos.get(i).getDouble("distancia")/1000;

                                        DecimalFormat df = new DecimalFormat("##.##");

                                        String kmString = df.format(km);

                                        String origem = trajetos.get(i).getString("origemEnd");

                                        String destino = trajetos.get(i).getString("destinoEnd");

                                        List<String> participantesArray = trajetos.get(i).getList("participantes");

                                        String motorista = trajetos.get(i).getString("motorista");

                                        String frequencia = o.getString("frequencia");

                                        List<String> diasDaSemana = o.getList("diasDaSemana");

                                        String textoFormatado = "Motorista: " + motorista +
                                                "\nOrigem: " + origem +
                                                "\nDestino: " + destino +
                                                "\nDistância: " + kmString + " km" +
                                                "\nParticipantes: " + participantesArray.toString().substring(1, participantesArray.toString().length() -1) +
                                                "\nFrequência: " + frequencia +
                                                "\nDias da Semana: " + diasDaSemana .toString().substring(1, diasDaSemana.toString().length() -1) +
                                                ".";

                                        Log.i("D.SEM.ARRAY.TEXTO: ", textoFormatado);

                                        TextView rowTextView = new TextView(DiaSemanaSelecionado.this);

                                        rowTextView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                        rowTextView.setId(count);
                                        rowTextView.setTextSize(17);
                                        rowTextView.setGravity(Gravity.START);
                                        rowTextView.setLines(10);
                                        rowTextView.setSingleLine(false);
                                        rowTextView.setText(textoFormatado);
                                        rowTextView.setClickable(true);

                                        View viewDivider = new View(DiaSemanaSelecionado.this);

                                        viewDivider.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1));

                                        viewDivider.setBackgroundColor(Color.parseColor("#737373"));

                                        linearLayout.addView(rowTextView);

                                        linearLayout.addView(viewDivider);

                                        myTextView[count] = rowTextView;

                                        myTextView[count].setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                                Toast.makeText(DiaSemanaSelecionado.this, "TextView Selecionado # " + v.getId(), Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                        count++;
                                    }
                                }
                            }
                        }
                    }

                }
            }
        });





/* //FORMA DE SE OBTER OS TRAJETOS DAS RECORRENCIAS PELO RELACIONAMENTO DE POINTERS, AO INVÉS DE ARRAYS.

        ParseObject objetoGrupo2 = ParseObject.createWithoutData("GrupoCarona", BaseActivity.grupoSelecionadoId);

        ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Recorrencia");

        query2.whereEqualTo("pointerGrupoCarona", objetoGrupo2);

        query2.whereEqualTo("diasDaSemana", diaSemanaSelecionado);

        ParseQuery<ParseObject> secondQuery = ParseQuery.getQuery("Trajetos");

        secondQuery.whereMatchesKeyInQuery("pointerRecorrencia", "objectId", query2);

        secondQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {

                if (e == null){
                    if (objects.size() > 0){

                        Log.i("D.SEM.MATCHES", "Objetos Encontrados!" + objects.size());

                        int count = 0;

                        TextView[] myTextView = new TextView[10]; //Arrumar o tamanho deste Array !!!

                        for (ParseObject o : objects){

                            ParseObject recurr = o.getParseObject("pointerRecorrencia");

                            Double km = o.getDouble("distancia")/1000;

                            DecimalFormat df = new DecimalFormat("##.##");

                            String kmString = df.format(km);

                            String origem = o.getString("origemEnd");

                            String destino = o.getString("destinoEnd");

                            List<ParseObject> participantesArray = o.getList("participantes");

                            String motorista = o.getString("motorista");

                            try {
                                recurr.fetchIfNeeded();

                                BaseActivity.freqParse = recurr.getString("frequencia"); //Se der erro, verificar se existe a variável freqParse na classe BaseActivity

                                BaseActivity.listDiasDaSemanaParse = recurr.getList("diasDaSemana"); //Se der erro, verificar se existe a variável listDiasDaSemanaParse na classe BaseActivity
                            } catch (ParseException e1) {
                                e1.printStackTrace();
                            }

                            String frequencia = BaseActivity.freqParse;

                            List<String> diasDaSemana = BaseActivity.listDiasDaSemanaParse;

                            String textoFormatado = "Motorista: " + motorista +
                                    "\nOrigem: " + origem +
                                    "\nDestino: " + destino +
                                    "\nDistância: " + kmString + " km" +
                                    "\nParticipantes: " + participantesArray.toString().substring(1, participantesArray.toString().length() -1) +
                                    "\nFrequência: " + frequencia +
                                    "\nDias da Semana: " + diasDaSemana .toString().substring(1, diasDaSemana.toString().length() -1) +
                                    ".";

                            //textViewTrajetosDia.setText(textoFormatado);

                            //SET O TEXTVIEW
                            TextView rowTextView = new TextView(DiaSemanaSelecionado.this);

                            rowTextView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                            rowTextView.setId(count);
                            rowTextView.setTextSize(17);
                            rowTextView.setGravity(Gravity.START);
                            rowTextView.setLines(10);
                            rowTextView.setSingleLine(false);
                            rowTextView.setText(textoFormatado);
                            rowTextView.setClickable(true);

                            View viewDivider = new View(DiaSemanaSelecionado.this);

                            viewDivider.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1));

                            viewDivider.setBackgroundColor(Color.parseColor("#737373"));

                            linearLayout.addView(rowTextView);

                            linearLayout.addView(viewDivider);

                            myTextView[count] = rowTextView;

                            myTextView[count].setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    Toast.makeText(DiaSemanaSelecionado.this, "TextView Selecionado # " + v.getId(), Toast.LENGTH_SHORT).show();
                                }
                            });

                            count++;
                        }
                    } else {
                        Log.i("D.SEM.SELEC", "Não há objeto");
                    }
                }
            }
        });
*/

        //INÍCIO DA QUERY COM OBJETOS SALVOS NUM ARRAY JSON
//=================================================================================//
/*
        ParseObject objetoGrupo3 = ParseObject.createWithoutData("GrupoCarona", BaseActivity.grupoSelecionadoId);

        ParseQuery<ParseObject> query3 = ParseQuery.getQuery("Trajetos");

        query3.whereEqualTo("pointerGrupoCarona", objetoGrupo3);

        query3.whereEqualTo("diasDaSemana", diaSemanaSelecionado);

        query3.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {

                if (e == null){

                    if (objects.size() > 0){

                        Log.i("D.SEM.SELEC", "Objetos Encontrados");

                        int count = 0;

                        TextView[] myTextView = new TextView[10]; //Arrumar o tamanho deste Array !!!

                        for (ParseObject o : objects){

                            JSONArray jsonArray = o.getJSONArray("trajetosJSON");

                            if(jsonArray.length() > 0){

                                Log.i("D.SEM.SELEC.JSON2", String.valueOf(jsonArray.length()));

                                for (int i = 0; i < jsonArray.length(); i++){

                                    try {
                                        JSONObject trajetoJSON = jsonArray.getJSONObject(i);

                                        Double km = trajetoJSON.getDouble("km")/1000;
                                        DecimalFormat df = new DecimalFormat("##.##");
                                        String kmString = df.format(km);  //Integer km = m != null ? m.intValue() : null; //Outra forma adequada de transformar Long (m) em Integer

                                        String origem = trajetoJSON.getString("origem");

                                        String destino = trajetoJSON.getString("destino");

                                        JSONArray participantesJSON = trajetoJSON.getJSONArray("participantes");
                                        ArrayList<String> participantesArray = new ArrayList<>();

                                        for(int n = 0; n < participantesJSON.length(); n++){

                                            participantesArray.add(participantesJSON.getString(n));
                                        }

                                        String motorista = o.getString("motorista");

                                        String frequencia = o.getString("frequencia");

                                        List<String> diasDaSemana = o.getList("diasDaSemana");

                                        String textoFormatado = "Motorista: " + motorista +
                                                                "\nOrigem: " + origem +
                                                                "\nDestino: " + destino +
                                                                "\nDistância: " + kmString + " km" +
                                                                "\nParticipantes: " + participantesArray.toString().substring(1, participantesArray.toString().length() -1) +
                                                                "\nFrequência: " + frequencia +
                                                                "\nDias da Semana: " + diasDaSemana .toString().substring(1, diasDaSemana.toString().length() -1) +
                                                                ".";

                                        //textViewTrajetosDia.setText(textoFormatado);

                                        //SET O TEXTVIEW
                                        TextView rowTextView = new TextView(DiaSemanaSelecionado.this);

                                        rowTextView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                        rowTextView.setId(i);
                                        rowTextView.setTextSize(17);
                                        rowTextView.setGravity(Gravity.START);
                                        rowTextView.setLines(10);
                                        rowTextView.setSingleLine(false);
                                        rowTextView.setText(textoFormatado);
                                        rowTextView.setClickable(true);

                                        View viewDivider = new View(DiaSemanaSelecionado.this);

                                        viewDivider.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1));

                                        viewDivider.setBackgroundColor(Color.parseColor("#737373"));

                                        linearLayout.addView(rowTextView);

                                        linearLayout.addView(viewDivider);

                                        myTextView[count] = rowTextView;

                                        Log.i("D.SEM.SELEC.JSON.PART", participantesJSON.toString());

                                    } catch (JSONException e1) {
                                        e1.printStackTrace();
                                    }

                                    myTextView[count].setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            Toast.makeText(DiaSemanaSelecionado.this, "TextView Selecionado # " + v.getId(), Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                    count++;
                                }

                                //linearLayoutMaster.addView(linearLayout);

                                Log.i("D.SEM.SELEC.JSON.COUNT", String.valueOf(count));
                            }
                        }
                        //scrollView.addView(linearLayout);

                        Log.i("D.SEM.SELEC.JSON.COUNT2", String.valueOf(count));
                    }

                } else {

                    Log.i("D.SEM.SELEC", "Não há objeto");
                }
            }
        });
*/
        //FIM DA QUERY COM OBJETOS SALVOS NUM ARRAY JSON
//=================================================================================//
    }
}
