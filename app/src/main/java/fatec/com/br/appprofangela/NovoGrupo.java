package fatec.com.br.appprofangela;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;


public class NovoGrupo extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    public void MostrarGruposCarona(){
        Intent intent = new Intent(NovoGrupo.this, GrupoCarona.class);
        startActivity(intent);
    }


    public void NovoGrupo(View view){

        EditText nomeGrupo = findViewById(R.id.nomeGrupo);

        if(nomeGrupo.getText().toString().matches("")){
            Toast.makeText(this, "Insira um bom nome para o seu Grupo de Carona!", Toast.LENGTH_SHORT).show();
        } else {

            ParseObject grupoCaronas = new ParseObject("GrupoCarona");

            grupoCaronas.put("nomeGrupo", nomeGrupo.getText().toString());

            grupoCaronas.saveInBackground();

            grupoCaronas.addUnique("usuariosGrupo", ParseUser.getCurrentUser());

            //CODIGO PARA INSERIR O RELACIONAMENTO - START
            /*
            ParseRelation<ParseUser> relation = grupoCaronas.getRelation("relationUser");

            relation.add(ParseUser.getCurrentUser());

            //CODIGO PARA INSERIR O RELACIONAMENTO - END
            */

            grupoCaronas.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {

                    if(e == null){

                        MostrarGruposCarona();
                        Toast.makeText(NovoGrupo.this, "Grupo Cadastrado.", Toast.LENGTH_SHORT).show();

                    } else {

                        Toast.makeText(NovoGrupo.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }
    }


    //============== DRAWER METHODS =============
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_meusdados) {
            // Handle the camera action
            Toast.makeText(NovoGrupo.this, "MEUS DADOS!", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_caroneiros) {
            Toast.makeText(NovoGrupo.this, "CARONEIROS!", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_buscarcaroneiros) {
            Toast.makeText(NovoGrupo.this, "ENCONTRAR CARONEIROS!", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_gruposcarona) {
            Intent intentGrupoCarona = new Intent(NovoGrupo.this, GrupoCarona.class);
            startActivity(intentGrupoCarona);

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {
            ParseUser.logOut();
            sairSistema();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    //============== DRAWER METHODS =============

    public void sairSistema(){
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer_novo_grupo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Bora-lá");

        //============== DRAWER =============
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //============== DRAWER =============

    }
}
