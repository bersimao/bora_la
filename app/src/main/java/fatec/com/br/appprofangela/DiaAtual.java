package fatec.com.br.appprofangela;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TextInputEditText;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.LoginFilter;
import android.text.method.PasswordTransformationMethod;
import android.text.method.SingleLineTransformationMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
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

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import scala.util.regexp.Base;

import static java.time.Month.JANUARY;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

// TELA INICIAL DO GRUPO DE CARONA SELECIONADO
public class DiaAtual extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    String grupoAtivo = "";
    ArrayList<String> Caroneiros = new ArrayList<String>();
    ArrayList<String> CaroneirosId = new ArrayList<>();
    ArrayAdapter arrayAdapter;
    Date c = Calendar.getInstance().getTime();
    SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
    String formattedDate;
    TextView dataAtual;
    EditText editTextNomeGrupo;
    ImageButton imageButtonEdit;
    Button botaoMaps, botaoTrajetos, botaoAdicionaCaroneiro, botaoRelatorio;
    ListView caroneirosListView;


    boolean isPressed = false;


    //============== MÉTODOS DA GAVETA LATERAL ============= INÍCIO
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_dia_atual);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
        BaseActivity.dataSelecionadaCalendario = "";

        Intent intent = new Intent(DiaAtual.this, GrupoCarona.class);

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
            Toast.makeText(DiaAtual.this, "MEUS DADOS!", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_caroneiros) {
            Toast.makeText(DiaAtual.this, "CARONEIROS!", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_buscarcaroneiros) {
            Toast.makeText(DiaAtual.this, "ENCONTRAR CARONEIROS!", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_gruposcarona) {
            Intent intentGrupoCarona = new Intent(DiaAtual.this, GrupoCarona.class);
            startActivity(intentGrupoCarona);

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {
            ParseUser.logOut();
            sairSistema();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout_dia_atual);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
//============== MÉTODOS DA GAVETA LATERAL ============= FIM

    private void abrirMaps() {

        String uri = "http://maps.google.com/maps?saddr=" + "-23.013829" + "," + "-45.5384847" + "&daddr=" + "-23.013829" + "," + "-45.5384847";
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
        startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer_dia_atual);
        Toolbar toolbar = findViewById(R.id.toolbar_dia_atual);
        setSupportActionBar(toolbar);
        setTitle("Bora-lá");

        final Intent intent = getIntent();

//============== GAVETA LATERAL =============
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_dia_atual);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_dia_atual);
        navigationView.setNavigationItemSelectedListener(this);
//============== GAVETA LATERAL =============

        editTextNomeGrupo = findViewById(R.id.editText_nome_grupo);

        imageButtonEdit = findViewById(R.id.imageButton_edit);

        imageButtonEdit.setBackgroundResource(R.drawable.ic_baseline_create_24px_edit);

        editTextNomeGrupo.setEnabled(false);

        botaoMaps = findViewById(R.id.buttonMapas);

        botaoTrajetos = findViewById(R.id.buttonTrajetos);

        botaoRelatorio = findViewById(R.id.buttonRelatorio);

        botaoAdicionaCaroneiro = findViewById(R.id.button_adiciona_caroneiro);

        caroneirosListView = findViewById(R.id.ListaCaroneiros);

        BaseActivity.setListViewHeightBasedOnChildren(caroneirosListView);

//METODO PARA QUE O SCROLL DA LIST VIEW FUNCIONE DENTRO DE OUTRA SCROLL VIEW
        caroneirosListView.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        grupoAtivo = BaseActivity.grupoSelecionadoId; //intent.getStringExtra("IdGrupo");

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, Caroneiros);

        formattedDate = df.format(c);

        dataAtual = findViewById(R.id.textViewDia);

        //dataAtual.setText(formattedDate);

        dataAtual.setText("Página Inicial");

        CalendarView calendarView = findViewById(R.id.calendarView1);

        Log.i("DIA.ATUAL.VER1", BaseActivity.dataSelecionadaCalendario);


// QUERY PARA ENCONTRAR OS CARONEIROS DO GRUPO
        final ParseQuery<ParseObject> query = ParseQuery.getQuery("GrupoCarona");

        query.whereEqualTo("objectId", grupoAtivo);
        query.include("usuariosGrupo");

        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(final ParseObject object, ParseException e) {

                if (e == null) {

                    BaseActivity.nomeGrupo = object.getString("nomeGrupo");

                    editTextNomeGrupo.setText(BaseActivity.nomeGrupo);

                    imageButtonEdit.setOnClickListener(imgButtonHandler);

                    List<ParseObject> usuariosGrupo = object.getList("usuariosGrupo");

                    Collections.sort(usuariosGrupo, new Comparator<ParseObject>() { // COMPARAÇÃO DOS NOMES DOS USUÁRIOS PARA ORDENAÇÃO CRESCENTE
                        @Override
                        public int compare(ParseObject o1, ParseObject o2) {

                            String string1 = o1.getString("nome");
                            String string2 = o2.getString("nome");

                            return string1.compareToIgnoreCase(string2);
                        }
                    });

                    for (ParseObject user : usuariosGrupo) { //PERCORRE OS OBJETOS ENCOTRADOS

                        Caroneiros.add(user.getString("nome")); //ADICIONA O NOME DOS CARONEIROS NO ARRAY
                        CaroneirosId.add(user.getObjectId()); //ADICIONA O Id DOS CARONEIROS EM OUTRO ARRAY PARA CONTROLE

                        Log.i("BORALA_Pesq ", user.getObjectId());
                        Log.i("BORALA_Pesq ", user.getString("username"));
                    }

                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(DiaAtual.this, android.R.layout.simple_list_item_1, Caroneiros);
                    arrayAdapter.notifyDataSetChanged();
                    caroneirosListView.setAdapter(arrayAdapter);

                    findViewById(R.id.loadingPanel).setVisibility(View.GONE);

                } else {
                    Log.i("BORALA_DiaAtual", e.getMessage()); //LOG DE CONTROLE DE ERROS
                }
            }
        });


// TRATAMENTO DAS DATAS DO CALENDÁRIO
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                final String dia = String.format("%02d", dayOfMonth);
                final String mes = String.format("%02d", month + 1);
                final String ano = String.valueOf(year);

                Calendar calendar = Calendar.getInstance();

                calendar.set(year, month, dayOfMonth);

                int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

                BaseActivity.diaDaSemanaSelecionado = dayOfWeek;

                Log.i("DiaAtual_D.Semana", Integer.toString(dayOfWeek));

                BaseActivity.dataSelecionadaCalendario = ano + "-" + mes + "-" + dia;

                Log.i("DIA.ATUAL.VER", BaseActivity.dataSelecionadaCalendario);

                final Intent intentCaronaDoDia = new Intent().setClass(DiaAtual.this, CaronaDoDia.class);

                intentCaronaDoDia.putExtra("passdia", dia);
                intentCaronaDoDia.putExtra("passmes", mes);
                intentCaronaDoDia.putExtra("passano", ano);
                intentCaronaDoDia.putExtra("grupoAtivo", grupoAtivo);
                //intentCaronaDoDia.putExtra("caroneirosList", Caroneiros);

                //final ParseObject objetoGrupo = ParseObject.createWithoutData("GrupoCarona", grupoAtivo);

                //ParseQuery<ParseObject> query = ParseQuery.getQuery("Calendario");

                //query.whereEqualTo("pointerGrupoCarona", objetoGrupo);

                //query.whereFullText("data", ano + mes + dia);

      /*          query.getFirstInBackground(new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject object, ParseException e) {

                        if (object == null) {

                            Log.i("BoraLa-DiaAtual", "object == null");

                            ParseObject novaData = new ParseObject("Calendario");
                            novaData.put("data", ano + mes + dia);
                            novaData.put("anoMes", ano + mes);
                            novaData.put("pointerGrupoCarona", objetoGrupo);

                            //List<String> listaVazia = new ArrayList<>();  MUDANÇA EM 13/12/18
                            //novaData.addUnique("naoFoi", "vazia");

                            novaData.saveInBackground();
                            String novaDataId = novaData.getObjectId();

                            intentCaronaDoDia.putExtra("DataId", novaDataId);

                        } else {

                            Log.i("BoraLa-DiaAtual", "object != null");
                            String dataExistente = object.getObjectId();
                            intentCaronaDoDia.putExtra("DataId", dataExistente);

                        }

                    }
                }); */

                DiaAtual.this.startActivity(intentCaronaDoDia);
            }
        });

        botaoMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirMaps();
            }
        });

        botaoTrajetos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentTrajetos = new Intent(DiaAtual.this, DiasSemana.class);
                startActivity(intentTrajetos);
            }
        });

        botaoRelatorio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentRelatorioDias = new Intent(DiaAtual.this, RelatorioDias.class);
                intentRelatorioDias.putExtra("caroneirosNome", Caroneiros);
                intentRelatorioDias.putExtra("IdGrupo", grupoAtivo);
                startActivity(intentRelatorioDias);
            }
        });

        botaoAdicionaCaroneiro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentAdicionaCaroneiro = new Intent(DiaAtual.this, AdicionaCaroneiro.class);
                intentAdicionaCaroneiro.putExtra("caroneirosId", CaroneirosId);
                intentAdicionaCaroneiro.putExtra("IdGrupo", grupoAtivo);
                startActivity(intentAdicionaCaroneiro);
            }
        });

        caroneirosListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(DiaAtual.this, "Pressione e Segure para Excluir.", Toast.LENGTH_SHORT).show();
            }
        });

        caroneirosListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                new AlertDialog.Builder(DiaAtual.this)
                        .setTitle("Deletar Usuário")
                        .setMessage("Tem certeza que deseja excluir este usuário ?")
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(R.drawable.ic_warning_black_24dp) //setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                String stringUserID = CaroneirosId.get(position);

                                BaseActivity.removerCaroneiro(stringUserID);

                                Intent intentGrupoCarona = new Intent(DiaAtual.this, GrupoCarona.class);
                                startActivity(intentGrupoCarona);

                                Toast.makeText(DiaAtual.this, "Caroneiro Removido com Sucesso!", Toast.LENGTH_SHORT).show();

                            }
                        })

                        .show();


                return true;
            }
        });


        // CÓDIGO ANTIGO

        // CÓDIGO PARA BUSCA COM RELATIONS NO BANCO DE DADOS.
        /*
        ParseObject grupoCarro = ParseObject.createWithoutData("GrupoCarona", grupoAtivo);

        ParseRelation<ParseUser> relation = grupoCarro.getRelation("relationUser");

        ParseQuery query = relation.getQuery();

        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects1, ParseException e) {
                if (e == null) {
                    // "user" is now a list of the user who at the grupoAtivo
                    Log.i("Sucess", "Retrieved " + objects1.size() + " objects");

                    for (ParseObject caroneiros : objects1) {
                        String name = caroneiros.getString("username");
                        Log.i("Sucess", "Retrieved name: " + name);
                        Caroneiros.add(caroneiros.getString("username"));

                    }

                } else {
                    // Something went wrong...
                    Log.i("NameErro: ", e.getMessage());
                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, Caroneiros);
                caroneirosListView.setAdapter(arrayAdapter);
            }
        });
*/
    }

    View.OnClickListener imgButtonHandler = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (isPressed) {
                imageButtonEdit.setBackgroundResource(R.drawable.ic_baseline_create_24px_edit);
                BaseActivity.nomeGrupo = editTextNomeGrupo.getText().toString();
                editTextNomeGrupo.setEnabled(false);
                Log.i("PEN", BaseActivity.nomeGrupo);


                ParseQuery<ParseObject> query = ParseQuery.getQuery("GrupoCarona");

                query.whereEqualTo("objectId", grupoAtivo);

                query.getFirstInBackground(new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject object, ParseException e) {

                        object.put("nomeGrupo", BaseActivity.nomeGrupo);
                        object.saveInBackground();

                    }
                });

            } else {
                imageButtonEdit.setBackgroundResource(R.drawable.ic_baseline_done_24px_confirm);
                editTextNomeGrupo.setEnabled(true);
                //editTextNomeGrupo.setTransformationMethod(SingleLineTransformationMethod.getInstance());
                Log.i("CHECK", BaseActivity.nomeGrupo);
            }
            isPressed = !isPressed;
        }
    };
}