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

        opcaoList.add("Ida");

        opcaoList.add("Volta");

        opcaoList.add("Ida e Volta");

        opcaoList.add("Remover Usuário");

        //ListView opcaoListView = findViewById(R.id.list_view_ida_volta_delete);
        //arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_single_choice, opcaoList);
        //arrayAdapter.notifyDataSetChanged();
        //opcaoListView.setAdapter(arrayAdapter);

        final TextView caroneiroNome = findViewById(R.id.caroneiro_nome_pesquisa);

        final RadioGroup opcaoRadioGroup = findViewById(R.id.radio_ida_volta);

        Button salvarButton = findViewById(R.id.button_salvar_ida_volta);

        for (int i = 0; i < opcaoList.size(); i++) {

            radioButton = new RadioButton(this);

            radioButton.setId(i);

            radioButton.setText(opcaoList.get(i));

            opcaoRadioGroup.addView(radioButton);
        }

        ParseQuery<ParseObject> grupoQuery = ParseQuery.getQuery("GrupoCarona");

        grupoQuery.whereEqualTo("objectId", grupoAtivo);

        grupoQuery.include("usuariosGrupo");

        grupoQuery.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {

                if (e == null) {

                    List<ParseObject> usuariosGrupo = object.getList("usuariosGrupo");

                    for (ParseObject user : usuariosGrupo) {

                        Log.i("BORALA_DEL1: ", user.getString("username"));
                        Log.i("BORALA_DEL2: ", caroneiroID);
                        Log.i("BORALA_DEL3: ", user.getObjectId());

                        if (user.getObjectId() == caroneiroID) {

                            //caroneiroNome.setText(user.getString("username"));
                            Log.i("BORALA_DEL", user.getString("username"));
                        }
                    }
                }
            }
        });

        final ParseObject grupoCarona = ParseObject.createWithoutData("GrupoCarona", grupoAtivo);

        ParseQuery<ParseUser> userParseQuery = ParseUser.getQuery();

        userParseQuery.whereEqualTo("objectId", caroneiroID);

        userParseQuery.getFirstInBackground(new GetCallback<ParseUser>() {
            @Override
            public void done(final ParseUser object, ParseException e) {

                if (e == null) {

                    caroneiroNome.setText(object.getUsername());

                    opcaoRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {

                            Log.i("BORA_IdaVolta", String.valueOf(checkedId));

                            //try {
                            if (checkedId == 3) {

                                ParseQuery<ParseObject> grupoQuery = ParseQuery.getQuery("GrupoCarona");
                                grupoQuery.whereEqualTo("objectId", grupoAtivo);
                                grupoQuery.include("usuariosGrupo");

                                grupoQuery.getFirstInBackground(new GetCallback<ParseObject>() {
                                    @Override
                                    public void done(ParseObject objectList, ParseException e) {

                                        if (e == null) {

                                            List<ParseObject> usuariosGrupo = objectList.getList("usuariosGrupo");
                                            usuariosGrupo.remove(object);
                                        }
                                    }
                                });

                                //object.remove("usuariosGrupo");
                            }

                            //} catch (Exception e){

                            //    Toast.makeText(CaroneiroIdaVolta.this, "Caroneiro Não Pertence ao Grupo.", Toast.LENGTH_SHORT).show();
                            // }

                            switch (checkedId) {

                                case 0:
                                    grupoCarona.addUnique("usuariosIda", object);
                                    grupoCarona.addUnique("usuariosGrupo", object);
                                    break;
                                case 1:
                                    grupoCarona.addUnique("usuariosVolta", object);
                                    grupoCarona.addUnique("usuariosGrupo", object);
                                    break;
                                case 2:
                                    grupoCarona.addUnique("usuariosGrupo", object);

                                default:
                            }
                        }
                    });

                } else {

                }
            }
        });

/*
        opcaoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                CheckedTextView checkedTextView = (CheckedTextView) view;
                //checkedTextView.setChecked(!checkedTextView.isChecked());

                for(int i = 0; i < 3; i++){

                    if(checkedTextView.isChecked()){
                        int ck = position;

                        checkedTextView.setChecked(true);

                    } else {
                        checkedTextView.setChecked(true);
                    }

                }
                    Log.i("BORA_IdaVolta", String.valueOf(position));
            }
        });
*/

        salvarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                grupoCarona.saveInBackground();

                Intent intentGrupoCarona = new Intent(CaroneiroIdaVolta.this, GrupoCarona.class);
                startActivity(intentGrupoCarona);
            }
        });
    }
}
