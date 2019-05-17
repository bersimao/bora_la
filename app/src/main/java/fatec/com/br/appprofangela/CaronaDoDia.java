package fatec.com.br.appprofangela;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import scala.util.parsing.json.JSON;

// TELA DO DIA SELECIONADO NO CALENDÁRIO DO GRUPO DE CARONA
public class CaronaDoDia extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ArrayList<String> caroneirosList = new ArrayList<>();

    ArrayAdapter arrayAdapter;

    ListView listView;

    TextView diaCarona;

    String calendarioDia, dia, mes, ano, DataId, grupoAtivo;

    Button buttonTrajetos;

    private JSONArray jsonArray = new JSONArray();

    //ProgressBar progressBar = new ProgressBar(this);

    //============== MÉTODOS DA GAVETA LATERAL ============= INÍCIO
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_carona_do_dia);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

        verificarDataAnterior();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void sairSistema() {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_meusdados) {
            // Handle the camera action
            Toast.makeText(CaronaDoDia.this, "MEUS DADOS!", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_caroneiros) {
            Toast.makeText(CaronaDoDia.this, "CARONEIROS!", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_buscarcaroneiros) {
            Toast.makeText(CaronaDoDia.this, "ENCONTRAR CARONEIROS!", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_gruposcarona) {
            Intent intentGrupoCarona = new Intent(CaronaDoDia.this, GrupoCarona.class);
            startActivity(intentGrupoCarona);

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {
            ParseUser.logOut();
            sairSistema();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout_carona_do_dia);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
//============== MÉTODOS DA GAVETA LATERAL ============= FIM

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer_carona_do_dia);
        Toolbar toolbar = findViewById(R.id.toolbar_carona_do_dia);
        setSupportActionBar(toolbar);
        setTitle("Bora-lá");

        //progressBar.setContentDescription("Carregando");

        //progressBar.setIndeterminate(false);

        //progressBar.setClickable(false);

        //progressBar.setVisibility(View.VISIBLE);

        final Intent intent = getIntent();

//============== GAVETA LATERAL =============
        DrawerLayout drawer = findViewById(R.id.drawer_layout_carona_do_dia);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view_carona_do_dia);
        navigationView.setNavigationItemSelectedListener(this);
//============== GAVETA LATERAL =============

        diaCarona = findViewById(R.id.textViewDiaCarona);

        dia = intent.getStringExtra("passdia");

        mes = intent.getStringExtra("passmes");

        ano = intent.getStringExtra("passano");

        DataId = intent.getStringExtra("DataId");

        grupoAtivo = intent.getStringExtra("grupoAtivo");

        listView = findViewById(R.id.list_view_with_checkbox);

        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);

        calendarioDia = ano + mes + dia;

        buttonTrajetos = findViewById(R.id.buttonTrajetosDoDia);

        diaCarona.setText(dia + "/ " + mes + "/ " + ano);

        queryRecorrencias();

        //FORMA DE POPULAR OS CARONEIROS DO DIA SELECIONADO, COM TODOS OS CARONEIROS DO GRUPO DE CARONA.
        //caroneirosList = intent.getStringArrayListExtra("caroneirosList");
        //arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice, caroneirosList);
        //listView.setAdapter(arrayAdapter);

        //ParseObject dataId = ParseObject.createWithoutData("Calendario", DataId);

        buttonTrajetos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentTrajetoDoDia = new Intent(CaronaDoDia.this, TrajetosDoDia.class);

                startActivity(intentTrajetoDoDia);
            }
        });
    }

    public void queryRecorrencias() {

        ParseObject objetoGrupo2 = ParseObject.createWithoutData("GrupoCarona", BaseActivity.grupoSelecionadoId);

        ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Recorrencia");

        query2.whereEqualTo("pointerGrupoCarona", objetoGrupo2);

        query2.include("arrayTrajetos");

        query2.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {

                if (e == null) {

                    if (objects.size() > 0) {

                        for (ParseObject o : objects) {

                            String frequenciaSemEspacos = o.getString("frequencia").replaceAll("\\s", "");

                            String intervaloSemEspacos = o.getString("intervalo").replaceAll("\\s", "");

                            List<String> listDiasDaSemana = o.getList("diasDaSemana");

                            String diasDaSemanaSemEspacos = listDiasDaSemana.toString().substring(1, listDiasDaSemana.toString().length() - 1);

                            diasDaSemanaSemEspacos = diasDaSemanaSemEspacos.replaceAll("\\s", "");

                            Log.i("TRAJ.DO.DIA.ARR", "ObjetoId: " + o.getObjectId());

                            Log.i("TRAJ.DO.DIA.ARR", "Frequencia: " + frequenciaSemEspacos);
                            Log.i("TRAJ.DO.DIA.ARR", "Intervalo: " + intervaloSemEspacos);
                            Log.i("TRAJ.DO.DIA.ARR", "Dias da Semana: " + diasDaSemanaSemEspacos);
                            Log.i("TRAJ.DO.DIA.ARR", "DiaSelecionado: " + BaseActivity.dataSelecionadaCalendario);

                            try {

                                boolean check = RecurringDates.rrule(o.getString("frequencia"),
                                        o.getString("intervalo"),
                                        listDiasDaSemana, o.getString("dataInicial"),
                                        BaseActivity.dataSelecionadaCalendario);

                                if (check) {

                                    //Toast.makeText(CaronaDoDia.this, "Objeto " + o.getObjectId() + " pertence a Recorrência! ", Toast.LENGTH_SHORT).show();

                                    List<ParseObject> trajetos = o.getList("arrayTrajetos");

                                    if (trajetos != null) {

                                        if (trajetos.size() > 0) {

                                            Log.i("TRAJ.DO.DIA.ARR", "trajetos.size(): " + trajetos.size());

                                            ArrayList<String> participantesTrajetos = new ArrayList<>();

                                            for (int i = 0; i < trajetos.size(); i++) {

                                                List<String> participantesArray = trajetos.get(i).getList("participantes");

                                                Log.i("TRAJ.DO.DIA.ARR", "participantesArray: " + participantesArray.toString());

                                                for (int n = 0; n < participantesArray.size(); n++) {
                                                    JSONObject json = new JSONObject();
                                                    participantesTrajetos.add(participantesArray.get(n));
                                                    json.put("nome", participantesArray.get(n));
                                                    json.put("distancia", trajetos.get(i).getNumber("distancia"));
                                                    json.put("objectId", trajetos.get(i).getObjectId());
                                                    json.put("Id", BaseActivity.dataSelecionadaCalendario );
                                                    jsonArray.put(json);
                                                    //Log.i("TRAJETO.JSON.NOME ",participantesArray.get(n) + " - " + String.valueOf(trajetos.get(i).getNumber("distancia")) );
                                                }
                                            }
                                            //Log.i("TRAJETO.JSON ",jsonArray.toString());

                                            Set<String> participantesUnicosTrajetos = new HashSet<>(participantesTrajetos);

                                            ArrayList<String> arrayListTemp = new ArrayList<>(participantesUnicosTrajetos);

                                            setArrayListTrajetosDoDia(arrayListTemp);

                                            setarStatusCaroneiro();

                                            //progressBar.setVisibility(View.INVISIBLE);

                                            findViewById(R.id.loadingPanel).setVisibility(View.GONE);

                                            Log.i("TRAJ.DO.DIA.ARR", "arrayListTemp: " + arrayListTemp.toString());

                                        }
                                    }

                                } else {

                                    Toast.makeText(CaronaDoDia.this, "Objeto " + o.getObjectId() + " não pertence a Recorrência! ", Toast.LENGTH_SHORT).show();
                                }

                            } catch (java.text.ParseException e1) {

                                e1.printStackTrace();
                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }
                        }
                    }
                }
            }
        });
    }

    public ArrayList<String> getCaroneirosList() {
        return caroneirosList;
    }

    public void setArrayListTrajetosDoDia(ArrayList<String> participantes) {

        for (int i = 0; i < participantes.size(); i++) {

            caroneirosList.add(participantes.get(i));
        }

        Log.i("TESTE.CARONA.setArr", caroneirosList.toString());

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice, caroneirosList);

        listView.setAdapter(arrayAdapter);
    }

    public void setarStatusCaroneiro() {

        ParseObject grupoDoDia = ParseObject.createWithoutData("GrupoCarona", grupoAtivo);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Calendario");

        query.whereFullText("data", calendarioDia);

        query.whereEqualTo("pointerGrupoCarona", grupoDoDia);

        query.include("naoFoi");

        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(final ParseObject objectData, ParseException e) {

                if (e == null) {

                    Log.i("TESTE.CARONA", "e == null");

                    if (objectData != null) {

                        if (objectData.getList("naoFoi") != null) {

                            for (String username : caroneirosList) {

                                Log.i("TESTE.CARONA", username);
                                Log.i("TESTE.CARONA", "naoFoi != null");
                                if (objectData.getList("naoFoi").contains(username)) {
                                    Log.i("TESTE.CARONA", "Contém!");
                                    listView.setItemChecked(caroneirosList.indexOf(username), true);
                                }
                            }

                            Log.i("TESTE.CARONA", String.valueOf(objectData.getList("naoFoi").size()));
                            Log.i("TESTE.CARONA.List", String.valueOf(caroneirosList.size()));

                            Log.i("TESTE.CARONA", "object != null");

                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                    CheckedTextView checkedTextView = (CheckedTextView) view;

                                    if (checkedTextView.isChecked()) {
                                        //Log.i("BoraLaTeste", "Linha selecionada");

                                        objectData.fetchIfNeededInBackground();
                                        objectData.addAllUnique("naoFoi", Arrays.asList(caroneirosList.get(position)));

                                        for(int i = 0; i < jsonArray.length(); i++){
                                            try {

                                                Log.i("naoFoi", jsonArray.getJSONObject(i).getString("nome"));

                                                Log.i("naoFoi", jsonArray.getJSONObject(i).getString("nome") + (caroneirosList.get(position)));

                                                if(jsonArray.getJSONObject(i).getString("nome").equals((caroneirosList.get(position)))){
                                                    objectData.addAllUnique("naoFoiDistancia", Arrays.asList(jsonArray.getJSONObject(i).toString()));

                                                }

                                            } catch (JSONException e1) {
                                                e1.printStackTrace();
                                            }
                                        }
                                        //objectData.put("naoFoiDistancia", jsonArray);
                                        Log.i("TRAJETO.JSON ", jsonArray.toString());
                                        objectData.saveInBackground();

                                    } else {
                                        //Log.i("BoraLaTeste", "Linha desmarcada");
                                        objectData.fetchIfNeededInBackground();
                                        objectData.removeAll("naoFoi", Arrays.asList(caroneirosList.get(position)));

                                        for(int i = 0; i < jsonArray.length(); i++){
                                            try {

                                                Log.i("naoFoi", jsonArray.getJSONObject(i).getString("nome"));

                                                Log.i("naoFoi", jsonArray.getJSONObject(i).getString("nome") + (caroneirosList.get(position)));

                                                if(jsonArray.getJSONObject(i).getString("nome").equals((caroneirosList.get(position)))){
                                                    objectData.removeAll("naoFoiDistancia", Arrays.asList(jsonArray.getJSONObject(i).toString()));

                                                }
                                            } catch (JSONException e1) {
                                                e1.printStackTrace();
                                            }
                                        }
                                        objectData.saveInBackground();
                                    }
                                }
                            });

                        } else {

                            Log.i("TESTE.CARONA.List", "objectData.getList('naoFoi') == null)");
                        }
                    } else {

                        Log.i("TESTE.CARONA", "object == null");
                    }
                } else {

                    Log.i("TESTE.CARONA", "e != null" + e.getMessage());

                    ParseObject novaData = new ParseObject("Calendario");
                    novaData.put("data", ano + mes + dia);
                    novaData.put("anoMes", ano + mes);
                    novaData.put("pointerGrupoCarona", grupoDoDia);

                    novaData.saveInBackground();

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            CheckedTextView checkedTextView = (CheckedTextView) view;

                            novaData.fetchInBackground(new GetCallback<ParseObject>() {
                                @Override
                                public void done(ParseObject objectNovaData, ParseException e) {

                                    if (checkedTextView.isChecked()) {

                                        objectNovaData.fetchIfNeededInBackground();
                                        //Log.i("BoraLaTeste", "Linha selecionada");
                                        objectNovaData.addAllUnique("naoFoi", Arrays.asList(caroneirosList.get(position)));
                                        //objectNovaData.put("naoFoiDistancia", jsonArray);

                                        for(int i = 0; i < jsonArray.length(); i++){
                                            try {

                                                Log.i("naoFoi", jsonArray.getJSONObject(i).getString("nome"));

                                                Log.i("naoFoi", jsonArray.getJSONObject(i).getString("nome") + (caroneirosList.get(position)));

                                                if(jsonArray.getJSONObject(i).getString("nome").equals((caroneirosList.get(position)))){
                                                    objectNovaData.addAllUnique("naoFoiDistancia", Arrays.asList(jsonArray.getJSONObject(i).toString()));
                                                }
                                            } catch (JSONException e1) {
                                                e1.printStackTrace();
                                            }
                                        }

                                        Log.i("CARONA.DO.DIA.JSON ", jsonArray.toString());
                                        objectNovaData.saveInBackground();

                                    } else {
                                        //Log.i("BoraLaTeste", "Linha desmarcada");
                                        objectNovaData.fetchIfNeededInBackground();
                                        objectNovaData.removeAll("naoFoi", Arrays.asList(caroneirosList.get(position)));

                                        for(int i = 0; i < jsonArray.length(); i++){
                                            try {

                                                Log.i("naoFoi", jsonArray.getJSONObject(i).getString("nome") + (caroneirosList.get(position)));

                                                if(jsonArray.getJSONObject(i).getString("nome").equals((caroneirosList.get(position)))){
                                                    objectNovaData.removeAll("naoFoiDistancia", Arrays.asList(jsonArray.getJSONObject(i).toString()));
                                                }
                                            } catch (JSONException e1) {
                                                e1.printStackTrace();
                                            }
                                        }
                                        objectNovaData.saveInBackground();
                                    }
                                }
                            });
                        }
                    });
                    Log.i("BoraLa-CaronaDoDia", " e != null " + e.getMessage());
                }
            }
        });
    }

    public void verificarDataAnterior() {

        Log.i("DIA.ATUAL.VER1.1", calendarioDia);

        ParseObject grupoDoDia = ParseObject.createWithoutData("GrupoCarona", grupoAtivo);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Calendario");

        query.whereFullText("data", calendarioDia);

        query.whereEqualTo("pointerGrupoCarona", grupoDoDia);

        query.include("naoFoi");

        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {

                if (e == null) {

                    Log.i("DIA.ATUAL.VER1", "e == null");

                    if (object.getList("naoFoi") != null) {

                        Log.i("DIA.ATUAL.VER1", "naoFoi != null");

                        if (object.getList("naoFoi").isEmpty()) {

                            Log.i("DIA.ATUAL.VER1", "naoFoi isEmpty");

                            object.deleteInBackground();
                        }

                    } else {

                        Log.i("DIA.ATUAL.VER1", "naoFoi == null");

                        object.deleteInBackground();
                    }
                }
            }
        });
    }
}
