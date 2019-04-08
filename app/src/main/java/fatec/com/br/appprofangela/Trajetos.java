package fatec.com.br.appprofangela;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.TravelMode;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import scala.util.regexp.Base;

public class Trajetos extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private static final int overview = 0;

    String recorrencia = "", motorista = "";
    EditText editTextLocalInicial, editTextNomeTrajeto;
    Button buttonSalvarTrajetos, buttonCancelarTrajetos, buttonAdicionarDestino, buttonOutraRecorrencia;
    ImageButton imgBtnPesqLocalInicial;
    ListView participantesListView;
    List<Map<String, String>> participantesData = new ArrayList<Map<String, String>>();
    ArrayAdapter arrayAdapterSpinner;
    SimpleAdapter simpleAdapter;
    RadioGroup radioGroupRecorrencia;
    RadioButton radioButton;
    Spinner dropdownMotoristas;


/*  CAIXA DE DIALOGO -> INICIO
    private void showMyDialog(Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.caixa_dialogo_add_trajeto);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);

        String enderecoDest1 = " ", enderecoDest2 = " ", localInicial;
        TextView destino1, destino2;
        EditText dialogDestino11, dialogDestino21;
        Button buttonPesqDest1, buttonPesqDest2;
        int destCount = 0;
        ArrayList<String> participantesArray = new ArrayList<>();
        StringBuilder sb = new StringBuilder();


        participantesArray = BaseActivity.caroneirosDoGrupo;

        TextView dialogTextView = dialog.findViewById(R.id.dialog_txtTitle);
        ListView dialogListView = dialog.findViewById(R.id.dialog_listView_participantes_trajeto);
        Button dialog_btnBtmLeft = dialog.findViewById(R.id.dialog_btnLeft);
        Button dialog_btnBtmRight = dialog.findViewById(R.id.dialog_btmRight);
        //dialogDestino11 = findViewById(R.id.dialog_edit_text_destino11); //TEXT VIEW DO DIALOG
        //dialogDestino21 = findViewById(R.id.dialog_edit_text_destino21); //TEXT VIEW DO DIALOG
        dialogDestino11 = this.findViewById(R.id.dialog_edit_text_destino11); //TEXT VIEW DO DIALOG
        dialogListView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice, participantesArray);

        dialogListView.setAdapter(arrayAdapter);

        BaseActivity.participantes.clear();

        Log.i("TRAJETOS - 2", BaseActivity.participantes.toString());

        dialogListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                CheckedTextView checkedTextView = (CheckedTextView) view;

                if (checkedTextView.isChecked()){

                    BaseActivity.participantes.add(participantesArray.get(position));
                } else {

                    BaseActivity.participantes.remove(participantesArray.get(position));
                }
            }
        });

        dialog_btnBtmLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //BOTÃO CANCELAR
                dialog.dismiss();
            }
        });

        dialog_btnBtmRight.setOnClickListener(new View.OnClickListener() { //BOTÃO SALVAR
            @Override
            public void onClick(View v) {
                // do whatever you want here

                Log.i("TRAJETOS - 3", BaseActivity.participantes.toString());

                Map<String, String> dataInfo = new HashMap<String, String>();

                sb.setLength(0);

                for (int i = 0; i < BaseActivity.participantes.size(); i++){

                    Log.i("TRAJETOS", BaseActivity.participantes.get(i));

                    sb.append(BaseActivity.participantes.get(i));

                    if(i != (BaseActivity.participantes.size() - 1)){

                        sb.append(" - ");
                    }
                }
                Log.i("TRAJETOS-SB", sb.toString());

                dataInfo.put("trajetos", "De - Até");

                dataInfo.put("participantes", sb.toString());

                participantesData.add(dataInfo);

                simpleAdapter.notifyDataSetChanged();

                BaseActivity.opcaoShowDialog = 0;

                dialog.dismiss();
            }
        });
*/
    /**
     * if you want the dialog to be specific size, do the following
     * this will cover 85% of the screen (85% width and 85% height)
     */
/*
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int dialogWidth = (int)(displayMetrics.widthPixels * 0.85);
        int dialogHeight = (int)(displayMetrics.heightPixels * 0.75);
        dialog.getWindow().setLayout(dialogWidth, dialogHeight);
        dialog.show();
    }
CAIXA DE DIALOGO -> FIM */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer_trajetos);
        Toolbar toolbar = findViewById(R.id.toolbar_trajetos);
        setSupportActionBar(toolbar);

        //TESTE SPINNER DOS MOTORISTAS
        String[] spinnerMotoristas = new String[]{"Bernardo", "Jaime"};

        //final Context context = Trajetos.this;  //Context para a CAIXA DE DIÁLOGO

        participantesListView = findViewById(R.id.list_view_trajetos_participantes);
        radioGroupRecorrencia = findViewById(R.id.radio_group_recorrencia);
        editTextLocalInicial = findViewById(R.id.edit_text_endereco_local_inicial);
        editTextNomeTrajeto = findViewById(R.id.edit_text_nome_trajeto);
        buttonSalvarTrajetos = findViewById(R.id.button_salvar_trajetos);
        buttonCancelarTrajetos = findViewById(R.id.button_cancelar_trajetos);
        imgBtnPesqLocalInicial = findViewById(R.id.imageButton_pesq_local_inicial);
        buttonAdicionarDestino = findViewById(R.id.button_adicionar_destinos_participantes);
        buttonOutraRecorrencia = findViewById(R.id.button_outra_recorrencia);
        simpleAdapter = new SimpleAdapter(Trajetos.this, participantesData, android.R.layout.simple_list_item_2, new String[] {"participantes", "trajetos"}, new int[] {android.R.id.text1, android.R.id.text2});
        participantesListView.setAdapter(simpleAdapter);
        Utility.setListViewHeightBasedOnChildren(participantesListView);
        editTextLocalInicial.setText(BaseActivity.localInicial);
        dropdownMotoristas = findViewById(R.id.spinner_motoristas_trajeto);
        arrayAdapterSpinner = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, spinnerMotoristas);
        dropdownMotoristas.setAdapter(arrayAdapterSpinner);

        editTextNomeTrajeto.setText(BaseActivity.nomeTrajeto);

        if (BaseActivity.opcaoShowDialog == 1) {

            Intent intent = new Intent(Trajetos.this, CaixaDialogo.class);

            startActivity(intent);
            //showMyDialog(context); MOSTRA A CAIXA DE DIÁLOGO
        }

        if (BaseActivity.participantesAA.isEmpty() && BaseActivity.enderecoDestino.isEmpty()){

            Log.i("TRAJETOS", "localInicial = vazio");

            Map<String, String> dataInfo = new HashMap<String, String>();

            for (int i = 0; i < 2; i++){

                dataInfo.put("participantes", "Participantes ");

                dataInfo.put("trajetos", "Destino ");

                participantesData.add(dataInfo);
            }
            simpleAdapter.notifyDataSetChanged();

        } else {

            for(int i = 0; i < BaseActivity.participantesAA.size(); i++){

                Map<String, String> dataInfo = new HashMap<String, String>();

                String s = BaseActivity.participantesAA.get(i).toString();

                dataInfo.put("participantes", s.substring(1, s.length() -1)); //Remove as chaves "[]" do array convertido em string.

                dataInfo.put("trajetos", BaseActivity.enderecoDestino.get(i));

                participantesData.add(dataInfo);
            }
            simpleAdapter.notifyDataSetChanged();
        }

        imgBtnPesqLocalInicial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BaseActivity.localInicial = "";

                BaseActivity.nomeTrajeto = editTextNomeTrajeto.getText().toString();

                int destCount = 1;

                Intent maps = new Intent(Trajetos.this, Maps.class);

                maps.putExtra("destCount", destCount);

                startActivity(maps);
            }
        });

        buttonAdicionarDestino.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BaseActivity.enderecoDestinoTemp = "";

                BaseActivity.participantesTemp.clear();

                BaseActivity.nomeTrajeto = editTextNomeTrajeto.getText().toString();

                Intent intent = new Intent(Trajetos.this, CaixaDialogo.class);

                startActivity(intent);
            }
        });

        buttonOutraRecorrencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int selectedId = radioGroupRecorrencia.getCheckedRadioButtonId();

                radioButton = findViewById(selectedId);

                recorrencia = radioButton.getText().toString();

                Toast.makeText(Trajetos.this, "Recorr.: " + setRecorrencia(recorrencia), Toast.LENGTH_SHORT).show();

            }
        });

        buttonSalvarTrajetos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int selectedId = radioGroupRecorrencia.getCheckedRadioButtonId();
                radioButton = findViewById(selectedId);
                //JSONObject partJsonObject = new JSONObject();
                JSONArray jsonArray = new JSONArray();

                if (radioButton == null || BaseActivity.localInicial.equals("") || BaseActivity.enderecoDestino.isEmpty()){

                    Toast.makeText(Trajetos.this, "Você não inseriu todas as informações necessárias", Toast.LENGTH_SHORT).show();

                } else {

                    String origin = BaseActivity.localInicial;
                    String destiny = BaseActivity.enderecoDestino.get(0);
                    recorrencia = radioButton.getText().toString();
                    motorista = dropdownMotoristas.getSelectedItem().toString();

                    DirectionsResult results = getDirectionsDetails(origin,destiny, TravelMode.DRIVING);

                    //getEndLocationTitle(results);

                    if (results != null){

                        JSONObject mainJsonObject = new JSONObject();
                        JSONObject partJsonObject = new JSONObject();

                        for(int i = 0; i < BaseActivity.participantesAA.size(); i++){

                            try {
                                partJsonObject.put("nome", BaseActivity.participantesAA.get(i));
                                partJsonObject.put("recorrencia", recorrencia);

                            } catch (JSONException e){

                                e.printStackTrace();
                            }
                        }

                        try {
                            mainJsonObject.put("ID", 1);
                            mainJsonObject.put("motorista", motorista);
                            mainJsonObject.put("origem", origin);
                            mainJsonObject.put("destino", destiny);
                            mainJsonObject.put("km", getEndLocationTitle(results));
                            mainJsonObject.put("participantes", partJsonObject);
                            jsonArray.put(mainJsonObject);

                        } catch (JSONException e){

                            e.printStackTrace();
                        }
                        //Log.i("Trajeto_SAVE3.2", mainJsonObject.toString());

                        Log.i("Trajeto_SAVE3.1", jsonArray.toString());

                        Log.i("Trajeto_SAVE_R", getEndLocationTitle(results));

                        if(BaseActivity.enderecoDestino.size() > 1){

                            for(int i = 0; i < (BaseActivity.enderecoDestino.size()-1); i++){

                                String origem = BaseActivity.enderecoDestino.get(i);
                                String destino = BaseActivity.enderecoDestino.get(i+1);
                                DirectionsResult results2 = getDirectionsDetails(origem,destino, TravelMode.DRIVING);
                                JSONObject mainJsonObject2 = new JSONObject();
                                JSONObject partJsonObject2 = new JSONObject();

                                for(int y = 0; y < BaseActivity.participantesAA.size(); y++){

                                    try {
                                        partJsonObject2.put("nome", BaseActivity.participantesAA.get(i));
                                        partJsonObject2.put("recorrencia", recorrencia);


                                    } catch (JSONException e){

                                        e.printStackTrace();
                                    }
                                }

                                try {
                                    mainJsonObject2.put("ID", i+2);
                                    mainJsonObject2.put("motorista", motorista);
                                    mainJsonObject2.put("origem", origem);
                                    mainJsonObject2.put("destino", destino);
                                    mainJsonObject2.put("km", getEndLocationTitle(results));
                                    mainJsonObject2.put("participantes", partJsonObject2);
                                } catch (JSONException e){

                                    e.printStackTrace();
                                }

                                jsonArray.put(mainJsonObject2);

                                //getEndLocationTitle(results2);

                                Log.i("Trajeto_SAVE2", getEndLocationTitle(results2));
                            }
                        }

                        Log.i("Trajeto_ArrArr", BaseActivity.participantesAA.toString());

                        Log.i("Trajeto_SAVE3", jsonArray.toString());

                        setRecorrencia(recorrencia);

                        Intent intent = new Intent(Trajetos.this, DiasSemana.class);

                        startActivity(intent);

                        //Log.i("Trajetos-LocIni", BaseActivity.localInicial);
                        //Log.i("Trajetos-Particip", BaseActivity.participantes.toString());
                        //Log.i("Trajetos-Destinos", BaseActivity.enderecoDestino.toString());
                        //Log.i("Trajetos-RadioBtn", setRecorrencia(recorrencia));
                    } else {

                        Toast.makeText(Trajetos.this, "Ocorreu um erro, verifique os endereços inseridos e tente novamente.", Toast.LENGTH_SHORT).show();

                    }
                }
            }
        });

        buttonCancelarTrajetos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Trajetos.this, DiasSemana.class);

                startActivity(intent);

                BaseActivity.participantesTemp.clear();
                BaseActivity.localInicial = "";
            }
        });

        //============== DRAWER ============= INÍCIO
/*
        //==============BOTÃO FLUTUANTE PARA ADICIONAR GRUPOS DE CARONA =============
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Trajetos.this, CaixaDialogo.class);

                startActivity(intent);

               //showMyDialog(context);
            }
        });
*/
        DrawerLayout drawer = findViewById(R.id.drawer_layout_trajetos);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_trajetos);
        navigationView.setNavigationItemSelectedListener(this);
        //============== DRAWER ============= FIM
    }

    public String setRecorrencia (String opcaoSelecionada){

        String s = "";

        switch (opcaoSelecionada){
            case "Todo dia":
                s = "FREQ=DAILY";
                break;
            case "Toda semana":
                s = "FREQ=WEEKLY";
                break;
            case "Todo mês":
                s = "FREQ=MONTHLY";
                break;
            case "Todo ano":
                s = "FREQ=YEARLY";
                break;
            default:
                s = "";
        }

        return s;
    }


    //============== MÉTODOS PARA A DRAWER ============== INÍCIO
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_trajetos);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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
            Toast.makeText(Trajetos.this, "MEUS DADOS!", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_caroneiros) {
            Toast.makeText(Trajetos.this, "CARONEIROS!", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_buscarcaroneiros) {
            Toast.makeText(Trajetos.this, "ENCONTRAR CARONEIROS!", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_gruposcarona) {
            Intent intentGrupoCarona = new Intent(Trajetos.this, GrupoCarona.class);
            startActivity(intentGrupoCarona);

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {
            ParseUser.logOut();
            sairSistema();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout_trajetos);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    //============== MÉTODOS PARA A DRAWER ============== FIM

    private String getKey(){

        String mapsKey = getText(R.string.google_maps_api_key).toString();
        return  mapsKey;
    }

    private DirectionsResult getDirectionsDetails(String orign, String destination, TravelMode mode){
        /*AndroidThreeTen.init(context); //Instancia do ThreeTen Android Backport, que permite utilizar os métodos do LocalDate em API's
                                        //mais antigas que a 26.

        //Instant now = Instant.now(); //Otra forma de pegar a data atual, porém mostra o dia e hora UTC.
        LocalDate date = LocalDate.parse("9999-12-31");
        Instant instant = date.atStartOfDay(ZoneId.of("America/Sao_Paulo")).toInstant();
        Log.i("DirectionsAPI5",instant.toString());*/
        try{
            return DirectionsApi.newRequest(getGeoContext())
                    .mode(mode)
                    .origin(orign)
                    .destination(destination)
                    .departureTimeNow()
                    .await();

        } catch (ApiException e){
            e.printStackTrace();
            Log.i("DirectionsAPI1",e.toString());
            return null;
        } catch (InterruptedException e){
            Log.i("DirectionsAPI2",e.toString());
            e.printStackTrace();
            Log.i("DirectionsAPI3",e.toString());
            return null;

        } catch (IOException e){
            Log.i("DirectionsAPI4",e.toString());
            e.printStackTrace();
            return null;
        }
    }

    private String getEndLocationTitle(DirectionsResult results){
        return  /*"Time :"+ results.routes[overview].legs[overview].duration.humanReadable + " Distance :" +*/ results.routes[overview].legs[overview].distance.humanReadable;
    }

    private GeoApiContext getGeoContext() {
        GeoApiContext geoApiContext = new GeoApiContext.Builder()
                .queryRateLimit(3)
                .apiKey(getKey())
                .connectTimeout(3, TimeUnit.SECONDS)
                .readTimeout(3, TimeUnit.SECONDS)
                .writeTimeout(3, TimeUnit.SECONDS)
                .build();
        return geoApiContext;
    }


}