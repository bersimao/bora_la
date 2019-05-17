package fatec.com.br.appprofangela;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GrupoCarona extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ParseUser currentUser = ParseUser.getCurrentUser();


    //============== MÉTODOS PARA A DRAWER ==============
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

        Intent intent = new Intent(GrupoCarona.this, Login.class);

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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_meusdados) {
            // Handle the camera action
            Toast.makeText(GrupoCarona.this, "MEUS DADOS!", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_caroneiros) {
            Toast.makeText(GrupoCarona.this, "CARONEIROS!", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_buscarcaroneiros) {
            Toast.makeText(GrupoCarona.this, "ENCONTRAR CARONEIROS!", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_gruposcarona) {
            Intent intentGrupoCarona = new Intent(GrupoCarona.this, GrupoCarona.class);
            startActivity(intentGrupoCarona);

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {
            ParseUser.logOut();
            sairSistema();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void sairSistema() {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_drawer_grupo_carona);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Bora-lá");

        BaseActivity.dataSelecionadaCalendario = "";

        //============== DRAWER =============
        //==============BOTÃO FLUTUANTE PARA ADICIONAR GRUPOS DE CARONA =============
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentNovoGrupo = new Intent(GrupoCarona.this, NovoGrupo.class);
                startActivity(intentNovoGrupo);
            }
        });
        //==============BOTÃO FLUTUANTE PARA ADICIONAR GRUPOS DE CARONA =============

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //View hView = navigationView.getHeaderView(0);

        //TextView

        //============== DRAWER =============

        //INÍCIO DO TRATAMENTO DA LISTA DOS GRUPOS DE CARONA
        final ListView GruposCaronaListView = findViewById(R.id.GruposCarona);
        final ArrayList<String> Carona = new ArrayList<String>();
        final ArrayList<String> GrupoID = new ArrayList<String>();

        ParseUser user = ParseUser.getCurrentUser();

        final ParseQuery<ParseObject> query = ParseQuery.getQuery("GrupoCarona");

        //query.whereEqualTo("relationUser", user);
        query.whereEqualTo("usuariosGrupo", user);

        query.orderByAscending("nomeGrupo");

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {

                if (e == null) {

                    if (objects.size() > 0) {

                        for (ParseObject nomeGrupo : objects) {

                            Carona.add(nomeGrupo.getString("nomeGrupo"));
                            GrupoID.add(nomeGrupo.getObjectId());

                        }

                        //ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(GrupoCarona.this, android.R.layout.simple_list_item_1, Carona);

                        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                                (GrupoCarona.this, android.R.layout.simple_list_item_1, Carona){
                            @Override
                            public View getView(int position, View convertView, ViewGroup parent){
                                // Get the current item from ListView
                                View view = super.getView(position,convertView,parent);

                                // Get the Layout Parameters for ListView Current Item View
                                ViewGroup.LayoutParams params = view.getLayoutParams();

                                // Set the height of the Item View
                                params.height = 150;
                                view.setLayoutParams(params);

                                return view;
                            }
                        };

                        findViewById(R.id.loadingPanel).setVisibility(View.GONE);

                        arrayAdapter.notifyDataSetChanged();

                        GruposCaronaListView.setAdapter(arrayAdapter);


                    }
                }
            }
        });


        GruposCaronaListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //final Intent intentDiaAtual = new Intent(GrupoCarona.this, DiaAtual.class);

                final String grupoId = GrupoID.get(position);

                //ParseObject grupoCarro2 = ParseObject.createWithoutData("GrupoCarona", grupoId);

                ParseObject idGrupo = ParseObject.createWithoutData("GrupoCarona", grupoId);

                idGrupo.addUnique("usuariosGrupo", ParseUser.getCurrentUser());

                idGrupo.saveInBackground();

                query.whereEqualTo("objectId", grupoId);
                query.selectKeys(Arrays.asList("usuariosGrupo"));
                query.include("usuariosGrupo");


                query.getFirstInBackground(new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject object, ParseException e) {

                        if (e == null) {
                            // "user" is now a list of the user who at the grupoAtivo

                            Intent intentDiaAtual = new Intent(GrupoCarona.this, DiaAtual.class);

                            ArrayList<String> Caroneiros = new ArrayList<>();
                            ArrayList<String> CaroneirosId = new ArrayList<>();

                            List<ParseObject> usuariosGrupo = object.getList("usuariosGrupo");

                            for (ParseObject user : usuariosGrupo) {

                                Caroneiros.add(user.getString("nome"));
                                CaroneirosId.add(user.getObjectId());

                                String nome = user.getObjectId();
                                String nome2 = user.getString("nome");
                                Log.i("BORALA_ARRAY2", nome + nome2);

                            }

                            intentDiaAtual.putExtra("caroneirosList", Caroneiros);
                            intentDiaAtual.putExtra("caroneirosId", CaroneirosId);
                            intentDiaAtual.putExtra("IdGrupo", grupoId);

                            BaseActivity.caroneirosDoGrupo = Caroneiros;

                            BaseActivity.grupoSelecionadoId = grupoId;

                            startActivity(intentDiaAtual);

                        } else {
                            Log.i("BORA_GpoCarona", e.getMessage());
                        }
                    }

                });

                GruposCaronaListView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {


                        return true;
                    }
                });


/*
               //BUSCA DE USUARIOS DO GRUPO SELECIONADO, UTILIZANDO RELATION NO LUGAR DE ARRAYS
                ParseObject grupoCarro = ParseObject.createWithoutData("GrupoCarona", grupoId);

                ParseRelation<ParseUser> relation = grupoCarro.getRelation("relationUser");

                ParseQuery query = relation.getQuery();

                query.findInBackground(new FindCallback<ParseObject>() {
                    public void done(List<ParseObject> objects1, ParseException e) {
                        if (e == null) {
                            // "user" is now a list of the user who at the grupoAtivo

                            //TESTE CLASSE BaseActivity

                            //BaseActivity.buscaCaroneirosGrupo(grupoId);
                            //FIM TESTE CLASSE BUSCA

                            //Intent intentDiaAtual = new Intent(GrupoCarona.this, DiaAtual.class);


                            //ArrayList<String> Caroneiros = new ArrayList<>();
                            //ArrayList<String> CaroneirosId = new ArrayList<>();

                            Log.i("Sucess", "Retrieved " + objects1.size() + " objects");

                            for (ParseObject caroneiros : objects1) {

                                //Caroneiros.add(caroneiros.getString("username"));
                                //CaroneirosId.add(caroneiros.getObjectId());
                            }
                            //intentDiaAtual.putExtra("caroneirosList", Caroneiros);
                            //intentDiaAtual.putExtra("caroneirosId", CaroneirosId);
                            //intentDiaAtual.putExtra("IdGrupo", grupoId);

                            //startActivity(intentDiaAtual);

                        } else {
                            // Something went wrong...
                            Log.i("NameErro: ", e.getMessage());
                        }
                    }
                });
*/
            }
        });
    }
}
