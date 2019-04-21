package fatec.com.br.appprofangela;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TrajetosDoDia extends AppCompatActivity {

    ArrayList<String> arrayListTrajetosDoDia = new ArrayList<>();

    ArrayList<String> arrayListTemp = new ArrayList<>();

    ArrayAdapter arrayAdapter;

    ListView listViewTrajetosDoDia;

    Intent intent = getIntent();


    public void queryRecorrencias (){

        ParseObject objetoGrupo2 = ParseObject.createWithoutData("GrupoCarona", BaseActivity.grupoSelecionadoId);

        ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Recorrencia");

        query2.whereEqualTo("pointerGrupoCarona", objetoGrupo2);

        query2.include("arrayTrajetos");


        query2.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {

                if (e == null){

                    if (objects.size() > 0){

                        for (ParseObject o : objects){

                            String frequenciaSemEspacos = o.getString("frequencia").replaceAll("\\s", "");

                            String intervaloSemEspacos = o.getString("intervalo").replaceAll("\\s", "");

                            List<String> listDiasDaSemana = o.getList("diasDaSemana");

                            String diasDaSemanaSemEspacos = listDiasDaSemana.toString().substring(1, listDiasDaSemana.toString().length() -1);

                            diasDaSemanaSemEspacos = diasDaSemanaSemEspacos.replaceAll("\\s", "");

                            Log.i("TRAJ.DO.DIA.ARR","ObjetoId: " + o.getObjectId());

                            Log.i("TRAJ.DO.DIA.ARR","Frequencia: " + frequenciaSemEspacos);
                            Log.i("TRAJ.DO.DIA.ARR","Intervalo: " + intervaloSemEspacos);
                            Log.i("TRAJ.DO.DIA.ARR","Dias da Semana: " + diasDaSemanaSemEspacos);
                            Log.i("TRAJ.DO.DIA.ARR","DiaSelecionado: " + BaseActivity.dataSelecionadaCalendario);

                            try {

                                boolean check = RecurringDates.rrule(o.getString("frequencia"),
                                        o.getString("intervalo"),
                                        listDiasDaSemana,o.getString("dataInicial"),
                                        BaseActivity.dataSelecionadaCalendario);

                                if(check){

                                    Toast.makeText(TrajetosDoDia.this, "Objeto " + o.getObjectId()+ " pertence a Recorrência! ", Toast.LENGTH_SHORT).show();

                                    List<ParseObject> trajetos = o.getList("arrayTrajetos");

                                    if(trajetos != null){

                                        if(trajetos.size() > 0){

                                            Log.i("TRAJ.DO.DIA.ARR","trajetos.size(): " + trajetos.size());

                                            for(int i = 0; i < trajetos.size(); i++){

                                                String origemNome = trajetos.get(i).getString("origemNome");

                                                String destinoNome = trajetos.get(i).getString("destinoNome");

                                                int maxLengthOrigem = (origemNome.length() < 20)?origemNome.length():20;

                                                int maxLengthDestino = (destinoNome.length() < 20)?destinoNome.length():20;

                                                origemNome = origemNome.substring(0, maxLengthOrigem);

                                                destinoNome = destinoNome.substring(0, maxLengthDestino);

                                                arrayListTemp.add(origemNome+" até "+destinoNome);
                                            }

                                            setArrayListTrajetosDoDia(arrayListTemp);

                                            Log.i("TRAJ.DO.DIA.ARR","arrayListTemp: " + arrayListTemp.toString());

                                        }

                                    }

                                } else {

                                    Toast.makeText(TrajetosDoDia.this, "Objeto " + o.getObjectId()+ " não pertence a Recorrência! ", Toast.LENGTH_SHORT).show();
                                }

                            } catch (java.text.ParseException e1) {

                                e1.printStackTrace();
                            }
                        }
                    }
                }
            }
        });
    }

    public void setArrayListTrajetosDoDia(ArrayList<String> participantes){

        for(int i = 0; i < participantes.size(); i++){

            arrayListTrajetosDoDia.add(participantes.get(i));
        }
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayListTrajetosDoDia);

        listViewTrajetosDoDia.setAdapter(arrayAdapter);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trajetos_do_dia);

        listViewTrajetosDoDia = findViewById(R.id.listView_trajetos_do_dia);

        queryRecorrencias();

        listViewTrajetosDoDia.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(TrajetosDoDia.this, "Linha # " + String.valueOf(position), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
