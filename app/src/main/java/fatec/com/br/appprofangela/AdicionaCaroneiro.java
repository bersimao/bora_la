package fatec.com.br.appprofangela;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
// TELA DE BUSCA E ADIÇÃO DE CARONEIRO AO GRUPO DE CARONA SELECIONADO
public class AdicionaCaroneiro extends AppCompatActivity {

    ArrayList<String> caroneirosPesquisa = new ArrayList<>();
    ArrayList<String> caroneirosResultado = new ArrayList<>();
    ArrayList<String> caroneirosId = new ArrayList<>();
    ArrayList<String> userID = new ArrayList<String>();
    String grupoAtivo = "";
    Button botaoPesquisaCaroneiro;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adiciona_caroneiro);

        final Intent intent = getIntent();

        caroneirosId = intent.getStringArrayListExtra("caroneirosId");

        grupoAtivo = intent.getStringExtra("IdGrupo");

        botaoPesquisaCaroneiro = findViewById(R.id.button_pesquisa_caroneiro);

        final ListView listViewPesquisaCaroneiro = findViewById(R.id.list_view_pesquisa_caroneiro);

        caroneirosPesquisa.clear();

        botaoPesquisaCaroneiro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                caroneirosPesquisa.clear();

                final ParseQuery<ParseObject> query = ParseQuery.getQuery("GrupoCarona");// Primeira busca para saber os usuários que já estão no grupo.

                query.whereEqualTo("objectId", grupoAtivo);

                query.include("usuariosGrupo");

                query.getFirstInBackground(new GetCallback<ParseObject>() {
                    @Override
                    public void done(final ParseObject object, ParseException e) {

                        if (e == null) {

                            List<ParseObject> usuariosGrupo = object.getList("usuariosGrupo");

                            for (ParseObject user : usuariosGrupo) {

                                String id = user.getObjectId();

                                caroneirosResultado.add(id);
                            }

                            ParseQuery<ParseUser> queryUser = ParseUser.getQuery(); // Segunda busca de todos os usuários do app, exceto os do grupo.

                            queryUser.whereNotContainedIn("objectId", caroneirosResultado);

                            queryUser.findInBackground(new FindCallback<ParseUser>() {
                                @Override
                                public void done(List<ParseUser> objects, ParseException e) {

                                    if (e == null) {

                                        if (objects.size() > 0) {

                                            for (ParseUser user : objects) {

                                                caroneirosPesquisa.add(user.getUsername());
                                                userID.add(user.getObjectId());

                                            }
                                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(AdicionaCaroneiro.this, android.R.layout.simple_list_item_1, caroneirosPesquisa);
                                            arrayAdapter.notifyDataSetChanged();
                                            listViewPesquisaCaroneiro.setAdapter(arrayAdapter);
                                        }

                                    } else {
                                        Log.i("BORALA_AdcCaroneiro", e.getMessage());//LOG DE CONTROLE DE ERROS
                                    }
                                }
                            });
                        }
                    }
                });
            }
        });

        listViewPesquisaCaroneiro.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String stringUserID = userID.get(position);

                Intent intentCaroneiroIdaVolta = new Intent(AdicionaCaroneiro.this, CaroneiroIdaVolta.class);

                intentCaroneiroIdaVolta.putExtra("CaroneiroID", stringUserID);

                intentCaroneiroIdaVolta.putExtra("grupoAtivo", grupoAtivo);

                startActivity(intentCaroneiroIdaVolta);
            }
        });
    }
}