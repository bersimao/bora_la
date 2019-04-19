package fatec.com.br.appprofangela;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class TrajetosDoDia extends AppCompatActivity {

    ArrayList<String> arrayListTrajetosDoDia = new ArrayList<>();

    ArrayAdapter arrayAdapter;

    ListView listViewTrajetosDoDia;

    Intent intent = getIntent();


    public void queryRecorrencias (){

        ParseObject objetoGrupo = ParseObject.createWithoutData("GrupoCarona", BaseActivity.grupoSelecionadoId);


        ParseQuery<ParseObject> query = ParseQuery.getQuery("Recorrencia");

        query.whereEqualTo("pointerGrupoCarona", objetoGrupo);

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {

                if(e == null){

                    Log.i("TRAJ.DO.DIA 1","Objetos encontrados!" );

                    if (objects.size() > 0){

                        Log.i("TRAJ.DO.DIA 2","Nº de objs. " + String.valueOf(objects.size()));

                        for (ParseObject o : objects){

                            String frequenciaSemEspacos = o.getString("frequencia").replaceAll("\\s", "");

                            String intervalorSemEspacos = o.getString("intervalo").replaceAll("\\s", "");

                            List<String> listDiasDaSemana = o.getList("diasDaSemana");

                            String diasDaSemanaSemEspacos = listDiasDaSemana.toString().substring(1, listDiasDaSemana.toString().length() -1);

                            diasDaSemanaSemEspacos = diasDaSemanaSemEspacos.replaceAll("\\s", "");

                            Log.i("TRAJ.DO.DIA 2","ObjetoId: " + o.getObjectId());

                            Log.i("TRAJ.DO.DIA 3","Frequencia: " + frequenciaSemEspacos);
                            Log.i("TRAJ.DO.DIA 3","Intervalo: " + frequenciaSemEspacos);
                            Log.i("TRAJ.DO.DIA 3","Dias da Semana: " + diasDaSemanaSemEspacos);
                            Log.i("TRAJ.DO.DIA 3","DiaSelecionado: " + BaseActivity.dataSelecionadaCalendario);

                            try {

                                boolean check = RecurringDates.rrule(o.getString("frequencia"),
                                        o.getString("intervalo"),
                                        diasDaSemanaSemEspacos,
                                        BaseActivity.dataSelecionadaCalendario);

                                if(check){

                                    Toast.makeText(TrajetosDoDia.this, "Objeto " + o.getObjectId().toString() + " pertence a Recorrência! ", Toast.LENGTH_SHORT).show();

                                } else {

                                    Toast.makeText(TrajetosDoDia.this, "Objeto " + o.getObjectId().toString() + " não pertence a Recorrência! ", Toast.LENGTH_SHORT).show();
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trajetos_do_dia);

        listViewTrajetosDoDia = findViewById(R.id.listView_trajetos_do_dia);

        queryRecorrencias();

        for(int i = 0 ; i < 5 ; i++){

            arrayListTrajetosDoDia.add("Teste");
        }

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice, arrayListTrajetosDoDia);

        listViewTrajetosDoDia.setAdapter(arrayAdapter);

    }
}
