package fatec.com.br.appprofangela;


import android.app.DatePickerDialog;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
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
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import scala.util.regexp.Base;

//import scala.util.regexp.Base;

public class Trajetos extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int overview = 0;
    final Calendar myCalendar = Calendar.getInstance();

    String motorista = "", resumoRecorrendia = "", freq = "", interval = "";
    EditText editTextLocalInicial, editTextCalendario;
    Button buttonSalvarTrajetos, buttonCancelarTrajetos, buttonAdicionarDestino, buttonOutraRecorrencia;
    TextView textViewResumoRecorrencia;
    //ImageButton imgBtnPesqLocalInicial;
    ListView participantesListView;
    List<Map<String, String>> participantesData = new ArrayList<Map<String, String>>();
    ArrayList<String> byday = new ArrayList<>();
    ArrayList<String> queryArrayNome = new ArrayList<>();
    ArrayAdapter arrayAdapterSpinner;
    SimpleAdapter simpleAdapter;
    RadioGroup radioGroupRecorrencia;
    RadioButton radioButton;
    Spinner dropdownMotoristas;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer_trajetos);
        Toolbar toolbar = findViewById(R.id.toolbar_trajetos);
        setSupportActionBar(toolbar);

        //TESTE SPINNER DOS MOTORISTAS
        String[] spinnerMotoristas = BaseActivity.spinnerMotoristas;

        //final Context context = Trajetos.this;  //Context para a CAIXA DE DIÁLOGO

        participantesListView = findViewById(R.id.list_view_trajetos_participantes);
        radioGroupRecorrencia = findViewById(R.id.radio_group_recorrencia);
        editTextLocalInicial = findViewById(R.id.edit_text_endereco_local_inicial);
        editTextCalendario = findViewById(R.id.editTextCalendario);
        textViewResumoRecorrencia = findViewById(R.id.textView_resumo_recorrencia);
        buttonSalvarTrajetos = findViewById(R.id.button_salvar_trajetos);
        buttonCancelarTrajetos = findViewById(R.id.button_cancelar_trajetos);
        //imgBtnPesqLocalInicial = findViewById(R.id.imageButton_pesq_local_inicial);
        buttonAdicionarDestino = findViewById(R.id.button_adicionar_destinos_participantes);
        buttonOutraRecorrencia = findViewById(R.id.button_outra_recorrencia);
        simpleAdapter = new SimpleAdapter(Trajetos.this, participantesData, android.R.layout.simple_list_item_2, new String[]{"participantes", "trajetos"}, new int[]{android.R.id.text1, android.R.id.text2});
        participantesListView.setAdapter(simpleAdapter);
        BaseActivity.setListViewHeightBasedOnChildren(participantesListView);
        editTextLocalInicial.setText(BaseActivity.localInicial);
        dropdownMotoristas = findViewById(R.id.spinner_motoristas_trajeto);
        arrayAdapterSpinner = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, spinnerMotoristas);
        dropdownMotoristas.setAdapter(arrayAdapterSpinner);
        //progressBar = findViewById(R.id.loadingPanel);

        //findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);

        if (BaseActivity.opcaoShowDialog == 1) {

            Intent intent = new Intent(Trajetos.this, CaixaDialogo.class);

            startActivity(intent);
            //showMyDialog(context); MOSTRA A CAIXA DE DIÁLOGO
        }

        if (BaseActivity.participantesAA.isEmpty() && BaseActivity.enderecoDestino.isEmpty()) {

            Log.i("TRAJETOS", "localInicial = vazio");

            Map<String, String> dataInfo = new HashMap<String, String>();

            for (int i = 0; i < 2; i++) {

                dataInfo.put("participantes", "Participantes ");

                dataInfo.put("trajetos", "Destino ");

                participantesData.add(dataInfo);
            }
            simpleAdapter.notifyDataSetChanged();

        } else {

            for (int i = 0; i < BaseActivity.participantesAA.size(); i++) {  //ALTERAR AQUI!!!!!

                Map<String, String> dataInfo = new HashMap<String, String>();

                //queryObjectName(BaseActivity.participantesAA.get(i)); //queryObjectName(BaseActivity.participantesAA.get(i).toString())
                //Log.i("TRAJETO2", BaseActivity.participantesAATemp.toString());
                String s =  BaseActivity.participantesAA.get(i).toString(); // era String s = BaseActivity.participantesAA.get(i);
                //Log.i("TRAJETO3", BaseActivity.participantesAATemp.toString());
                //Log.i("TRAJETO", "I -> " + s);

                dataInfo.put("participantes", s.substring(1, s.length() - 1)); //Remove as chaves "[]" do array convertido em string.

                dataInfo.put("trajetos", BaseActivity.enderecoDestino.get(i));

                participantesData.add(dataInfo);

                BaseActivity.participantesAATemp.clear();
            }
            simpleAdapter.notifyDataSetChanged();
        }

        editTextLocalInicial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BaseActivity.localInicial = "";

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

                BaseActivity.nomeDestinoTemp = "";

                BaseActivity.participantesTemp.clear();

                Intent intent = new Intent(Trajetos.this, CaixaDialogo.class);

                startActivity(intent);
            }
        });

        if (BaseActivity.situacaoRadioGroupRecorrencia == 0) {

            for (int i = 0; i < radioGroupRecorrencia.getChildCount(); i++) {
                radioGroupRecorrencia.getChildAt(i).setEnabled(false);
            }

            Intent intent = getIntent();

            resumoRecorrendia = intent.getStringExtra("resumoRecorrencia");

            textViewResumoRecorrencia.setText(resumoRecorrendia);
        }

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };

        editTextCalendario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new DatePickerDialog(Trajetos.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        buttonOutraRecorrencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Trajetos.this, Recorrencia.class);

                startActivity(intent);
            }
        });

        buttonSalvarTrajetos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //ArrayList<ParseObject> arrayListTrajetos = new ArrayList<>();

                int selectedId = radioGroupRecorrencia.getCheckedRadioButtonId();

                radioButton = findViewById(selectedId);

                //JSONArray jsonMainArray = new JSONArray();

                if ((radioButton == null && BaseActivity.situacaoRadioGroupRecorrencia == 1) || BaseActivity.localInicial.equals("") || BaseActivity.enderecoDestino.isEmpty()) {

                    Toast.makeText(Trajetos.this, "Você não inseriu todas as informações necessárias", Toast.LENGTH_SHORT).show();

                } else {

                    if (radioButton == null && BaseActivity.situacaoRadioGroupRecorrencia == 0) {

                        Intent intent = getIntent();

                        freq = intent.getStringExtra("freqRecorrencia");

                        interval = intent.getStringExtra("intervalRecorrencia");

                        byday = (ArrayList<String>) getIntent().getSerializableExtra("bydayRecorrencia");

                    } else {

                        freq = radioButton.getText().toString();

                        setRecorrencia(freq);

                        interval = "1";

                        byday.add("MO");
                        byday.add("TU");
                        byday.add("WE");
                        byday.add("TH");
                        byday.add("FR");
                        byday.add("SA");
                        byday.add("SU");
                    }

                    String origin = BaseActivity.localInicial;

                    String destiny = BaseActivity.enderecoDestino.get(0);

                    motorista = dropdownMotoristas.getSelectedItem().toString();

                    DirectionsResult results = getDirectionsDetails(origin, destiny, TravelMode.DRIVING);

                    Log.i("TRAJ.PARSE_INTERVAL", interval);

                    if (results != null) {
                        progressBar.setVisibility(View.VISIBLE);

                        /*
                        progress = new ProgressBar(Trajetos.this);
                        progress.setClickable(false);
                        progress.setVisibility(View.VISIBLE);
                        progress.isShown();*/

                        salvarTrajetos(results, origin, destiny);

                        Intent intent = new Intent(Trajetos.this, DiasSemana.class);

                        //progress.setVisibility(View.GONE);

                        //findViewById(R.id.loadingPanel).setVisibility(View.GONE);

                        progressBar.setVisibility(View.GONE);

                        startActivity(intent);

                        finish();

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

                finish();


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

    private void salvarTrajetos(DirectionsResult results, String origin, String destiny) {

        ParseObject objetoGrupo = ParseObject.createWithoutData("GrupoCarona", BaseActivity.grupoSelecionadoId);

        ParseObject recorrencia = new ParseObject("Recorrencia");

        recorrencia.put("frequencia", freq);
        recorrencia.put("pointerGrupoCarona", objetoGrupo);
        recorrencia.put("frequencia", freq);
        recorrencia.put("intervalo", interval);
        recorrencia.put("diasDaSemana", byday);
        recorrencia.put("dataInicial", dataInicialSelecionada());
        recorrencia.put("dataFinal", "false");

        recorrencia.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.i("TRAJ.PARSE.RECORR: ", "Trajeto Salvo OK");
                } else {
                    Log.i("TRAJ.PARSE.RECORR: ", e.getMessage());
                }
            }
        });

        recorrencia.fetchInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    Log.i("TRAJETOS_RECORR.PARSE", "Obj. Recorrencia Encontrado!");

                    ParseObject trajeto = new ParseObject("Trajetos");


                    trajeto.put("motorista", motorista);
                    trajeto.put("origemEnd", origin);
                    trajeto.put("destinoEnd", destiny);
                    trajeto.put("participantes", BaseActivity.participantesAA.get(0));
                    trajeto.put("distancia", getEndLocationTitle(results));
                    trajeto.put("trajetoCorrente", 1);
                    trajeto.put("nomeTrajeto", "nome");
                    trajeto.put("pointerRecorrencia", object);
                    trajeto.put("origemNome", BaseActivity.nomeLocalInicial);
                    trajeto.put("destinoNome", BaseActivity.nomeDestino.get(0));

                    trajeto.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                Log.i("TRAJ.PARSE.ARR: ", "Trajeto Salvo OK");
                                Log.i("TRAJ.PARSE.ARR1: ", object.getObjectId());
                                Log.i("TRAJ.PARSE.ARR2: ", trajeto.getObjectId());
                                object.add("arrayTrajetos", trajeto);

                                object.saveInBackground();

                                //arrayListTrajetos.add(trajeto);

                            } else {
                                Log.i("TRAJ.PARSE.ARR: ", e.getMessage());
                            }
                        }
                    });

                    if (BaseActivity.enderecoDestino.size() > 1) {

                        recorrencia.fetchIfNeededInBackground(new GetCallback<ParseObject>() {
                            @Override
                            public void done(ParseObject object, ParseException e) {

                                for (int i = 0; i < (BaseActivity.enderecoDestino.size() - 1); i++) {
                                    String origem = BaseActivity.enderecoDestino.get(i);
                                    String destino = BaseActivity.enderecoDestino.get(i + 1);
                                    String nomeOrigem = BaseActivity.nomeDestino.get(i);
                                    String nomeDestino = BaseActivity.nomeDestino.get(i + 1);
                                    DirectionsResult results2 = getDirectionsDetails(origem, destino, TravelMode.DRIVING);

                                    ParseObject novosTrajetos = new ParseObject("Trajetos");

                                    novosTrajetos.put("motorista", motorista);
                                    novosTrajetos.put("origemEnd", origem);
                                    novosTrajetos.put("destinoEnd", destino);
                                    novosTrajetos.put("participantes", BaseActivity.participantesAA.get(i + 1));
                                    novosTrajetos.put("distancia", getEndLocationTitle(results2));
                                    novosTrajetos.put("trajetoCorrente", 1);
                                    novosTrajetos.put("nomeTrajeto", "nome");
                                    novosTrajetos.put("pointerRecorrencia", object);
                                    novosTrajetos.put("origemNome", nomeOrigem);
                                    novosTrajetos.put("destinoNome", nomeDestino);

                                    novosTrajetos.saveInBackground(new SaveCallback() {
                                        @Override
                                        public void done(ParseException e) {
                                            if (e == null) {
                                                Log.i("TRAJ.PARSE.NV.TRAJ: ", "Novos trajetos Salvos OK");
                                                Log.i("TRAJ.PARSE.NV.TRAJ1: ", object.getObjectId());
                                                Log.i("TRAJ.PARSE.NV.TRAJ2: ", novosTrajetos.getObjectId());
                                                object.add("arrayTrajetos", novosTrajetos);
                                                object.saveInBackground();
                                                //arrayListTrajetos.add(novosTrajetos);

                                            } else {
                                                Log.i("TRAJ.PARSE.NV.TRAJ.: ", e.getMessage());
                                            }
                                        }
                                    });
                                }
                            }
                        });
                    }
                } else {

                    Log.i("TRAJETOS_RECORR.PARSE", e.getMessage());
                }
            }
        });

//CÓDIGO PARA UTILIZAR JSON PARA SALVAR TRAJETOS E RECORRÊNCIAS.
/*
                        JSONObject mainJsonObject = new JSONObject();

                        try {
                            mainJsonObject.put("ID", 0);
                            //mainJsonObject.put("motorista", motorista);
                            mainJsonObject.put("origem", origin);
                            mainJsonObject.put("destino", destiny);
                            mainJsonObject.put("km", getEndLocationTitle(results));
                            mainJsonObject.put("participantes", new JSONArray(BaseActivity.participantesAA.get(0)));
                            //mainJsonObject.put("freq", freq);
                            //mainJsonObject.put("intervalo", interval);
                            //mainJsonObject.put("dias", byday);
                            jsonMainArray.put(mainJsonObject);

                        } catch (JSONException e){

                            e.printStackTrace();
                        }

                        if(BaseActivity.enderecoDestino.size() > 1){

                            for(int i = 0; i < (BaseActivity.enderecoDestino.size()-1); i++){

                                String origem2 = BaseActivity.enderecoDestino.get(i);
                                String destino2 = BaseActivity.enderecoDestino.get(i+1);
                                DirectionsResult results2 = getDirectionsDetails(origem2,destino2, TravelMode.DRIVING);

                                JSONObject mainJsonObject2 = new JSONObject();

                                try {
                                    mainJsonObject2.put("ID", i+1);
                                    //mainJsonObject2.put("motorista", motorista);
                                    mainJsonObject2.put("origem", origem2);
                                    mainJsonObject2.put("destino", destino2);
                                    mainJsonObject2.put("km", getEndLocationTitle(results2));
                                    mainJsonObject2.put("participantes", new JSONArray(BaseActivity.participantesAA.get(i+1)));
                                    //mainJsonObject2.put("freq", freq);
                                    //mainJsonObject2.put("intervalo", interval);
                                    //mainJsonObject2.put("dias", byday);
                                } catch (JSONException e){

                                    e.printStackTrace();
                                }

                                jsonMainArray.put(mainJsonObject2);

                                Log.i("Trajeto_SAVE2", getEndLocationTitle(results2).toString());
                            }
                        }

                        Log.i("Trajeto_SAVE3", jsonMainArray.toString());

                        ParseObject objetoGrupo = ParseObject.createWithoutData("GrupoCarona", BaseActivity.grupoSelecionadoId);

                        ParseObject novosTrajetos = new ParseObject("Trajetos");

                        novosTrajetos.put("motorista", motorista);
                        novosTrajetos.put("trajetosJSON", jsonMainArray);
                        novosTrajetos.put("pointerGrupoCarona", objetoGrupo);
                        novosTrajetos.put("frequencia", freq);
                        novosTrajetos.put("intervalo", interval);
                        novosTrajetos.put("diasDaSemana", byday);
                        novosTrajetos.put("trajetoCorrente", 1);
                        novosTrajetos.saveInBackground();

                        novosTrajetos.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {

                                if(e == null){
                                    Log.i("TRAJ.PARSE: ", "SALVO OK");
                                } else {
                                    Log.i("TRAJ.PARSE: ", e.getMessage());
                                }
                            }
                        });
*/
    }


    private void updateLabel() {

        String myFormat = "dd/MM/yyyy";

        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        editTextCalendario.setText(sdf.format(myCalendar.getTime()));

        Toast.makeText(Trajetos.this, sdf.format(myCalendar.getTime()), Toast.LENGTH_SHORT).show();
    }

    private String dataInicialSelecionada() {

        String myFormat = "yyyy-MM-dd";

        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        String dataInicial = sdf.format(myCalendar.getTime());

      /*  int year = myCalendar.get(Calendar.YEAR);
        int month = myCalendar.get(Calendar.MONTH);
        int day = myCalendar.get(Calendar.DAY_OF_MONTH);
        final String dia = String.format("%02d", day);
        final String mes = String.format("%02d", month + 1);
        final String ano = String.valueOf(year);
        String dataInicial = ano+"-"+mes+"-"+dia;*/

        return dataInicial;
    }

    public String setRecorrencia(String opcaoSelecionada) {

        switch (opcaoSelecionada) {
            case "Todo dia":
                freq = "DAILY";
                break;
            case "Toda semana":
                freq = "WEEKLY";
                break;
            case "Todo mês":
                freq = "MONTHLY";
                break;
            case "Todo ano":
                freq = "YEARLY";
                break;
            default:
                freq = "";
        }

        return opcaoSelecionada;
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

        Intent intent = new Intent(Trajetos.this, DiasSemana.class);

        startActivity(intent);

        finish();

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

    private String getKey() {

        String mapsKey = getText(R.string.google_maps_api_key).toString();
        return mapsKey;
    }

    private DirectionsResult getDirectionsDetails(String orign, String destination, TravelMode mode) {
        /*AndroidThreeTen.init(context); //Instancia do ThreeTen Android Backport, que permite utilizar os métodos do LocalDate em API's
                                        //mais antigas que a 26.

        //Instant now = Instant.now(); //Otra forma de pegar a data atual, porém mostra o dia e hora UTC.
        LocalDate date = LocalDate.parse("9999-12-31");
        Instant instant = date.atStartOfDay(ZoneId.of("America/Sao_Paulo")).toInstant();
        Log.i("DirectionsAPI5",instant.toString());*/
        try {
            return DirectionsApi.newRequest(getGeoContext())
                    .mode(mode)
                    .origin(orign)
                    .destination(destination)
                    .departureTimeNow()
                    .await();

        } catch (ApiException e) {
            e.printStackTrace();
            Log.i("DirectionsAPI1", e.toString());
            return null;
        } catch (InterruptedException e) {
            Log.i("DirectionsAPI2", e.toString());
            e.printStackTrace();
            Log.i("DirectionsAPI3", e.toString());
            return null;

        } catch (IOException e) {
            Log.i("DirectionsAPI4", e.toString());
            e.printStackTrace();
            return null;
        }
    }

    private Long getEndLocationTitle(DirectionsResult results) {
        return  /*"Time :"+ results.routes[overview].legs[overview].duration.humanReadable + " Distance :" +*/ results.routes[overview].legs[overview].distance.inMeters;
    }

    private GeoApiContext getGeoContext() {
        GeoApiContext geoApiContext = new GeoApiContext.Builder()
                .queryRateLimit(5)
                .apiKey(getKey())
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .writeTimeout(5, TimeUnit.SECONDS)
                .build();
        return geoApiContext;
    }

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



    public void queryObjectName(ArrayList<String> id) {

        for(int i = 0; i < id.size(); i++){

            ParseQuery<ParseUser> query = ParseUser.getQuery();

            query.whereEqualTo("objectId", id.get(i));

            query.getFirstInBackground(new GetCallback<ParseUser>() {
                @Override
                public void done(ParseUser object, ParseException e) {
                    if (e == null) {

                        BaseActivity.participantesAATemp.add(object.getString("nome"));
                        Log.i("TRAJETO1", BaseActivity.participantesAATemp.toString());

                    } else {

                        Log.i("ERRO.NOME.OBJETO", e.getMessage());
                    }
                }
            });
        }
    }

}