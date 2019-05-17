package fatec.com.br.appprofangela;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import scala.util.parsing.combinator.testing.Str;

//import scala.util.parsing.combinator.testing.Str;

public class BaseActivity extends AppCompatActivity {

    public static String[] spinnerMotoristas = new String[]{"Bernardo", "Jaime"}; //remover esta variável após a implementação no BD.

    public static String dataSelecionadaCalendario = "";

    public static ArrayList<String> caroneirosDoGrupo = new ArrayList<>();

    public static String localInicial = "";

    public static String nomeLocalInicial = "";

    public static String nomeGrupo = "";

    public static ArrayList<String> enderecoDestino = new ArrayList<>();

    public static String enderecoDestinoTemp = "";

    public static ArrayList<String> nomeDestino = new ArrayList<>();

    public static String nomeDestinoTemp = "";

    public static Integer opcaoShowDialog = 0;

    public static ArrayList<ArrayList<String>> participantesAA = new ArrayList<>();

    public static ArrayList<String> participantesAATemp = new ArrayList<>();

    public static ArrayList<String> participantesTemp = new ArrayList<>();

    public static int situacaoRadioGroupRecorrencia = 1;

    public static String grupoSelecionadoId = "";

    public static int diaDaSemanaSelecionado = 0;

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
                                List<String> nomes = new ArrayList<>();
                                List<String> objectId = new ArrayList<>();

                                for(ParseObject o : usuariosGrupo){

                                    nomes.add(o.getObjectId());
                                    objectId.add(o.getString("nome"));

                                }
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


/*    //MÉTODO PARA QUE O SCROLL DAS LISTVIEW'S FUNCIONE EM ACTIVITIES QUE POSSUEM SCROLL VIEW
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    } */

    /**** Method for Setting the Height of the ListView dynamically.
     **** Hack to fix the issue of not showing all the items of the ListView
     **** when placed inside a ScrollView  ****/
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            //view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

            view.measure(View.MeasureSpec.makeMeasureSpec(desiredWidth, View.MeasureSpec.AT_MOST), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    public static void adicionarCaroneiro(String caroneiroID) { //ParseObject objetoGrupo

        ParseQuery<ParseObject> queryGrupoCarona = ParseQuery.getQuery("GrupoCarona");

        queryGrupoCarona.whereEqualTo("objectId", BaseActivity.grupoSelecionadoId);

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



    public static void removerCaroneiro(String caroneiroID) { //ParseObject objetoGrupo

        ParseQuery<ParseObject> queryGrupoCarona = ParseQuery.getQuery("GrupoCarona");

        queryGrupoCarona.whereEqualTo("objectId", BaseActivity.grupoSelecionadoId);

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