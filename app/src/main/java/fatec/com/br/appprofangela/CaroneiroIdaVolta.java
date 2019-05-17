package fatec.com.br.appprofangela;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

// TELA PARA ADICIONAR O CARONEIRO NO GRUPO SELECIONADO
public class CaroneiroIdaVolta extends AppCompatActivity {

    String caroneiroID = "";

    String grupoAtivo = "";

    ArrayList<String> opcaoList = new ArrayList<>();

    RadioButton radioButton;

    //ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caroneiro_ida_volta);

        final Intent intent = getIntent();

        caroneiroID = intent.getStringExtra("CaroneiroID");

        grupoAtivo = intent.getStringExtra("grupoAtivo");

        opcaoList.add("Adicionar Usuário");

        opcaoList.add("Remover Usuário");

        //ListView opcaoListView = findViewById(R.id.list_view_ida_volta_delete);
        //arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_single_choice, opcaoList);
        //arrayAdapter.notifyDataSetChanged();
        //opcaoListView.setAdapter(arrayAdapter);

        final TextView caroneiroNome = findViewById(R.id.caroneiro_nome_pesquisa);

        final RadioGroup opcaoRadioGroup = findViewById(R.id.radio_ida_volta);

        Button salvarButton = findViewById(R.id.button_salvar_ida_volta);

        ParseQuery<ParseUser> queryUser = ParseUser.getQuery();

        queryUser.whereEqualTo("objectId", caroneiroID);

        queryUser.getFirstInBackground(new GetCallback<ParseUser>() {
            @Override
            public void done(ParseUser object, ParseException e) {
                if (e == null) {

                    caroneiroNome.setText(object.getUsername());

                } else {

                    caroneiroNome.setText("Nome do Caroneiro(a)");
                    Log.i("ADIC.CARONEIRO", e.getMessage());

                }
            }
        });

        for (int i = 0; i < opcaoList.size(); i++) {

            radioButton = new RadioButton(this);

            radioButton.setId(i);

            radioButton.setText(opcaoList.get(i));

            opcaoRadioGroup.addView(radioButton);
        }

        salvarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int opcao = opcaoRadioGroup.getCheckedRadioButtonId();

                switch (opcao) {

                    case 0:
                        adicionarCaroneiro();
                        Log.i("ADIC.CARONEIRO", "Radio 0");
                        Toast.makeText(CaroneiroIdaVolta.this, "Adicionado com sucesso!", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        removerCaroneiro();
                        Log.i("ADIC.CARONEIRO", "Radio 1");
                        Toast.makeText(CaroneiroIdaVolta.this, "Removido com sucesso!", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(CaroneiroIdaVolta.this, "Você não selecionou nenhuma opção", Toast.LENGTH_SHORT).show();
                }

                Intent intentGrupoCarona = new Intent(CaroneiroIdaVolta.this, GrupoCarona.class);
                startActivity(intentGrupoCarona);
            }
        });
    }

    public void adicionarCaroneiro() { //ParseObject objetoGrupo

        ParseQuery<ParseObject> queryGrupoCarona = ParseQuery.getQuery("GrupoCarona");

        queryGrupoCarona.whereEqualTo("objectId", grupoAtivo);

        queryGrupoCarona.include("usuariosGrupo");

        queryGrupoCarona.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject objectGrupo, ParseException e) {

                if (e == null) {


                    ParseQuery<ParseUser> userParseQuery = ParseUser.getQuery();

                    userParseQuery.whereEqualTo("objectId", caroneiroID);

                    userParseQuery.getFirstInBackground(new GetCallback<ParseUser>() {
                        @Override
                        public void done(ParseUser objectUser, ParseException e) {

                            if (e == null) {

                                objectGrupo.addUnique("usuariosGrupo", objectUser);
                                objectGrupo.saveInBackground();

                            }
                        }
                    });
                }

                objectGrupo.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {

                        if (e == null) {
                            Log.i("ADIC.CARONEIRO", "Caroneiro Removido");
                        } else {
                            Log.i("ADIC.CARONEIRO", e.getMessage());
                        }
                    }
                });
            }
        });
    }

    public void removerCaroneiro() { //ParseObject objetoGrupo

        ParseQuery<ParseObject> queryGrupoCarona = ParseQuery.getQuery("GrupoCarona");

        queryGrupoCarona.whereEqualTo("objectId", grupoAtivo);

        queryGrupoCarona.include("usuariosGrupo");

        queryGrupoCarona.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject objectGrupo, ParseException e) {

                if (e == null) {

                    ParseQuery<ParseUser> userParseQuery = ParseUser.getQuery();

                    userParseQuery.whereEqualTo("objectId", caroneiroID);

                    userParseQuery.getFirstInBackground(new GetCallback<ParseUser>() {
                        @Override
                        public void done(ParseUser objectUser, ParseException e) {

                            if (e == null) {

                                Log.i("ADC.CARONEIRO", objectUser.getUsername());

                                List<ParseUser> usuariosGrupo = objectGrupo.getList("usuariosGrupo");

                                JSONArray novaLista = new JSONArray();

                                Log.i("ADC.CARONEIRO", usuariosGrupo.toString());

                                for (int i = 0; i < usuariosGrupo.size(); i++) {

                                    Log.i("ADC.CARONEIRO", "Caroneiros da Lista: " + usuariosGrupo.get(i).getUsername());

                                    if (!usuariosGrupo.get(i).getObjectId().equals(objectUser.getObjectId())) {

                                        novaLista.put(usuariosGrupo.get(i));

                                        Log.i("ADC.CARONEIRO", "Usuário da nova lista: " + usuariosGrupo.get(i).getUsername());

                                    } else {

                                        Log.i("ADC.CARONEIRO", "A List irá remover o objeto " + usuariosGrupo.get(i).getUsername());
                                    }
                                }

                                objectGrupo.remove("usuariosGrupo");

                                objectGrupo.put("usuariosGrupo", novaLista);

                                objectGrupo.saveInBackground();
                            }
                        }
                    });
                }

                objectGrupo.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {

                        if (e == null) {
                            Log.i("ADIC.CARONEIRO", "Caroneiro Removido");
                        } else {
                            Log.i("ADIC.CARONEIRO", e.getMessage());
                        }
                    }
                });
            }
        });
    }
}