package fatec.com.br.appprofangela;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

//import scala.util.regexp.Base;

public class CaixaDialogo extends AppCompatActivity {

    ArrayList<String> participantesArray = new ArrayList<>();
    ArrayList<String> participantesId = new ArrayList<>();
    ArrayAdapter arrayAdapter;
    ListView dialogListView;
    //StringBuilder sb = new StringBuilder(); //String Builder para a forma de armazenamento em uma única String. Não está sendo utilizado pois os participantes são inseridos num Array.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caixa_dialogo);

        //getWindow().setLayout(85,65);

        //setWindowHeight(75);

        //setWindowWidth(85);

        TextView dialogTextView = findViewById(R.id.dialog_txtTitle);
        EditText destino11 = findViewById(R.id.dialog_edit_text_destino11);
        dialogListView = findViewById(R.id.dialog_listView_participantes_trajeto);
        //ImageButton buttonEndereco = findViewById(R.id.dialog_button_pesquisa_dest11);
        Button buttonCancelar = findViewById(R.id.dialog_btnLeft);
        Button buttonSalvar = findViewById(R.id.dialog_btmRight);

        if (BaseActivity.opcaoShowDialog == 1){

            destino11.setText(BaseActivity.enderecoDestinoTemp);
        }


        buscaCaroneirosGrupo(BaseActivity.grupoSelecionadoId);


        dialogListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                CheckedTextView checkedTextView = (CheckedTextView) view;

                if (checkedTextView.isChecked()){

                    BaseActivity.participantesTemp.add(participantesArray.get(position)); //era participantesArray
                    //Toast.makeText(CaixaDialogo.this, participantesArray.get(position), Toast.LENGTH_SHORT).show();

                } else {

                    BaseActivity.participantesTemp.remove(participantesId.get(position));
                }
            }
        });

        destino11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BaseActivity.participantesTemp.clear();

                int destCount = 2;

                Intent maps = new Intent(CaixaDialogo.this, Maps.class);

                maps.putExtra("destCount", destCount);

                startActivity(maps);
            }
        });

        buttonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BaseActivity.opcaoShowDialog = 0;

                Intent intent = new Intent(CaixaDialogo.this, Trajetos.class);

                startActivity(intent);
            }
        });

        buttonSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (BaseActivity.participantesTemp.isEmpty()){

                    Toast.makeText(CaixaDialogo.this, "Você não adicionou nenhum participante.", Toast.LENGTH_SHORT).show();
                }

                if (BaseActivity.enderecoDestinoTemp.equals("")){

                    Toast.makeText(CaixaDialogo.this, "Você não adicionou nenhum destino.", Toast.LENGTH_SHORT).show();
                }

                if (!BaseActivity.participantesTemp.isEmpty() && !BaseActivity.enderecoDestinoTemp.equals("")) {

                    BaseActivity.opcaoShowDialog = 0;

                    //sb.setLength(0); //String Builder para a forma de armazenamento em uma única String. Não está sendo utilizado pois os participantes são inseridos num Array.

                    ArrayList<String> list = new ArrayList<>();

                    BaseActivity.participantesAA.add(list); //a Adição da lista tem que ocorrer antes, pois o que é adicionado ao participantesAA é a referencia da lista, que depois é alimentada com os valores.

                    for(int i = 0; i < BaseActivity.participantesTemp.size(); i++){

                        list.add(BaseActivity.participantesTemp.get(i));

                    }

                    BaseActivity.enderecoDestino.add(BaseActivity.enderecoDestinoTemp);

                    BaseActivity.nomeDestino.add(BaseActivity.enderecoDestinoTemp);

                    Log.i("CXDIALOGO_endDest", BaseActivity.enderecoDestino.toString());

                    Log.i("CXDIALOGO_ArrArr", BaseActivity.participantesAA.toString());

                    Intent intent = new Intent(CaixaDialogo.this, Trajetos.class);

                    startActivity(intent);
                }
            }
        });
    }

    private void setWindowHeight(int heightPercent){

        DisplayMetrics metrics = new DisplayMetrics();

        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int screenHeight = metrics.heightPixels;

        WindowManager.LayoutParams params = getWindow().getAttributes();

        params.height = (screenHeight*heightPercent/100);

        this.getWindow().setAttributes(params);
    }

    private void setWindowWidth(int widthPercent){

        DisplayMetrics metrics = new DisplayMetrics();

        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int screenWidth = metrics.widthPixels;

        WindowManager.LayoutParams params = getWindow().getAttributes();

        params.width = (screenWidth*widthPercent/100);

        this.getWindow().setAttributes(params);
    }

    public void buscaCaroneirosGrupo (String grupoId){

        ParseQuery<ParseObject> queryGrupo = new ParseQuery<ParseObject>("GrupoCarona");

        queryGrupo.include("usuariosGrupo");

        queryGrupo.whereEqualTo("objectId", grupoId);

        queryGrupo.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {

                if(e == null){

                    object.fetchIfNeededInBackground(new GetCallback<ParseObject>() {
                        @Override
                        public void done(ParseObject object, ParseException e) {

                            if (e == null){

                                List<ParseObject> usuariosGrupo = object.getList("usuariosGrupo");

                                Collections.sort(usuariosGrupo, new Comparator<ParseObject>() { // COMPARAÇÃO DOS NOMES DOS USUÁRIOS PARA ORDENAÇÃO CRESCENTE
                                    @Override
                                    public int compare(ParseObject o1, ParseObject o2) {

                                        String string1 = o1.getString("nome");
                                        String string2 = o2.getString("nome");

                                        return string1.compareToIgnoreCase(string2);
                                    }
                                });

                                for(ParseObject o : usuariosGrupo){

                                    participantesId.add(o.getObjectId());
                                    participantesArray.add(o.getString("nome"));

                                }

                                setNameArrayLists(participantesArray);
                            } else {
                                Log.i("NameErro: ", e.getMessage());
                            }
                        }
                    });
                } else {
                    Log.i("NameErro: ", e.getMessage());
                }
            }
        });
    }

    public void setNameArrayLists(ArrayList<String> nomesParticipantes){

        //participantesArray = BaseActivity.caroneirosDoGrupo;

        dialogListView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice, nomesParticipantes);

        dialogListView.setAdapter(arrayAdapter);

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(CaixaDialogo.this, Trajetos.class);

        startActivity(intent);

        finish();
    }
}