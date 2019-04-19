package fatec.com.br.appprofangela;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

//import scala.util.parsing.combinator.testing.Str;

public class BaseActivity extends AppCompatActivity {

    public static String dataSelecionadaCalendario = "";

    public static ArrayList<String> caroneirosDoGrupo = new ArrayList<>();

    public static String nomeTrajeto = "";

    public static String localInicial = "";

    public static ArrayList<String> enderecoDestino = new ArrayList<>();

    public static String enderecoDestinoTemp = "";

    public static Integer opcaoShowDialog = 0;

    public static ArrayList<ArrayList<String>> participantesAA = new ArrayList<>();

    public static ArrayList<String> participantesTemp = new ArrayList<>();

    public static int situacaoRadioGroupRecorrencia = 1;

    public static String grupoSelecionadoId = "";

    //public static String freqParse = "";

    //public static List<String> listDiasDaSemanaParse = new ArrayList<>();

    public static void buscaCaroneirosGrupo (String grupoId){

        ParseObject grupoCarro = ParseObject.createWithoutData("GrupoCarona", grupoId);

        ParseRelation<ParseUser> relation = grupoCarro.getRelation("relationUser");

        ParseQuery query = relation.getQuery();

        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects1, ParseException e) {
                if (e == null) {
                    // "user" is now a list of the user who at the grupoAtivo

                    ArrayList<String> arrayCaroneiros = new ArrayList<>();

                    Log.i("Sucess", "Retrieved " + objects1.size() + " objects");

                    for (ParseObject caroneiros : objects1) {
                        String name = caroneiros.getString("username");
                        Log.i("Sucess", "Retrieved name: " + name);
                        arrayCaroneiros.add(caroneiros.getObjectId());
                    }

                } else {
                    // Something went wrong...
                    Log.i("NameErro: ", e.getMessage());
                }

            }
        });

    }
}