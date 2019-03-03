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
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.parse.ParseUser;

import java.util.Arrays;

public class Trajetos extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    String diaSemana = "";

    TextView textViewTrajetos;

    private void showMyDialog(Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.caixa_dialogo_add_trajeto);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);

        TextView textView = (TextView) dialog.findViewById(R.id.txtTitle);
        ListView listView = (ListView) dialog.findViewById(R.id.listView);
        Button btnBtmLeft = (Button) dialog.findViewById(R.id.btnBtmLeft);
        ///Button btnBtmRight = (Button)dialog.findViewById(R.id.btmRight);

        btnBtmLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        /*
        btnBtmRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // do whatever you want here
            }
        });
        */

        /**
         * if you want the dialog to be specific size, do the following
         * this will cover 85% of the screen (85% width and 85% height)
         */
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int dialogWidth = (int)(displayMetrics.widthPixels * 0.85);
        int dialogHeight = (int)(displayMetrics.heightPixels * 0.55);
        dialog.getWindow().setLayout(dialogWidth, dialogHeight);

        dialog.show();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer_trajetos);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_trajetos);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();

        textViewTrajetos = findViewById(R.id.textViewTrajetos);

        diaSemana = intent.getStringExtra("diaSemana");

        textViewTrajetos.setText(diaSemana);

        final Context context = Trajetos.this;

        //============== DRAWER =============
        //==============BOTÃO FLUTUANTE PARA ADICIONAR GRUPOS DE CARONA =============
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent maps = new Intent(Trajetos.this, Maps.class);
                startActivity(maps);
                //showMyDialog(context);
            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_trajetos);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_trajetos);
        navigationView.setNavigationItemSelectedListener(this);
        //============== DRAWER =============

    }

    //============== MÉTODOS PARA A DRAWER ==============
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
}