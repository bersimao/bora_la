package fatec.com.br.appprofangela;

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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;

// TELA DO DIA SELECIONADO NO CALENDÁRIO DO GRUPO DE CARONA
public class CaronaDoDia extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ArrayList<String> caroneirosList = new ArrayList<>();

    ArrayAdapter arrayAdapter;

    TextView diaCarona;

    String calendarioDia, dia, mes, ano, DataId, grupoAtivo;

    //============== MÉTODOS DA GAVETA LATERAL ============= INÍCIO
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_carona_do_dia);
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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_carona_do_dia);
        setSupportActionBar(toolbar);
        setTitle("Bora-lá");

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

        caroneirosList = intent.getStringArrayListExtra("caroneirosList");

        calendarioDia = ano + mes + dia;

/*
        try {
            RecurringDates.rrule(ano+"-"+mes+"-"+dia);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
*/

        diaCarona.setText(dia + "/ " + mes + "/ " + ano);

        final ListView listView = findViewById(R.id.list_view_with_checkbox);

        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice, caroneirosList);

        listView.setAdapter(arrayAdapter);

        ParseObject dataId = ParseObject.createWithoutData("Calendario", DataId);

        dataId.fetchInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {

                if (object == null) {

                    Log.i("BORALA_CarDoDia", " data null");
                    ArrayList<String> vazio = new ArrayList<>();
                    vazio.add("vazio");

                    //ParseObject data = new ParseObject("Calendario");
                    //data.put("data", calendarioDia);
                    //data.saveInBackground();
                    //data.put("pointerGrupoCarona", grupoAtivo);

                    Log.i("BoraLa-CaronaDoDia", e.getMessage());
                    //object.addUnique("naoFoi", vazio);
                    //object.saveInBackground();
                } else {

                }

                ParseObject grupoDoDia = ParseObject.createWithoutData("GrupoCarona", grupoAtivo);

                ParseQuery<ParseObject> query = ParseQuery.getQuery("Calendario");

                query.whereFullText("data", calendarioDia);

                query.whereEqualTo("pointerGrupoCarona", grupoDoDia);

                query.include("naoFoi");

                query.getFirstInBackground(new GetCallback<ParseObject>() {
                    @Override
                    public void done(final ParseObject object, ParseException e) {

                        if (e == null) {

                            if (object == null) {

                            } else {

                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                        CheckedTextView checkedTextView = (CheckedTextView) view;

                                        if (checkedTextView.isChecked()) {

                                            //Log.i("BoraLaTeste", "Linha selecionada");
                                            object.addAllUnique("naoFoi", Arrays.asList(caroneirosList.get(position)));
                                            object.saveInBackground();

                                        } else {

                                            //Log.i("BoraLaTeste", "Linha desmarcada");
                                            object.removeAll("naoFoi", Arrays.asList(caroneirosList.get(position)));
                                            object.saveInBackground();

                                        }
                                    }
                                });

                                for (String username : caroneirosList) {

                                    if (object.getList("naoFoi") != null) {
                                        if (object.getList("naoFoi").contains(username)) {
                                            listView.setItemChecked(caroneirosList.indexOf(username), true);
                                        }
                                    }
                                }
                            }

                        } else {

                            Log.i("BoraLa-CaronaDoDia", " e != null " + e.getMessage());
                        }
                    }
                });
            }
        });
    }
}
