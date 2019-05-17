package fatec.com.br.appprofangela;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.CountCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import scala.Int;
import scala.util.parsing.combinator.testing.Str;

public class RelatorioDias extends AppCompatActivity {

    private JSONArray jsonArrayCalendario = new JSONArray();

    private JSONArray jsonArrayCalendarioMaster = new JSONArray();

    private JSONArray jsonArrayRelatorioMaster = new JSONArray();

    private JSONArray jsonArrayRelatorio = new JSONArray();

    private JSONObject jsonObjectParticipantesMaster = new JSONObject();

    private JSONObject jsonObjectRelatorioMaster = new JSONObject();

    private JSONObject jsonObjectRelatorioSuper = new JSONObject();

    private JSONObject jsonObjectQtdDiasCaroneirosNaoFoi = new JSONObject();

    private ArrayList<String> arrayListParticipantesGrupo = new ArrayList<>();

    private ArrayList<String> arrayListMotoristasGrupo = new ArrayList<>();

    ArrayList<String> caroneirosNome = new ArrayList<>();

    ArrayList<String> diasRelatorio = new ArrayList<>();

    private String mesSelecionadoString = "";

    String grupoAtivo = "";

    private Integer anoSelecionado = 0;

    TextView textViewMes;

    TextView textViewAno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relatorio_dias);

        Intent intent = getIntent();

        caroneirosNome = intent.getStringArrayListExtra("caroneirosNome");

        grupoAtivo = intent.getStringExtra("IdGrupo");

        //final ListView diasListView = (ListView) findViewById(R.id.list_resultado_dias);

        final Spinner dropdownMes = findViewById(R.id.spinner_mes);

        final Spinner dropdownAno = findViewById(R.id.spinner_ano);

        Button buttonPesquisar = findViewById(R.id.button_pesquisar);

        TextView textViewCabecalhoRelatorio = findViewById(R.id.textView_cabecalho_relatorio);

        textViewMes = findViewById(R.id.textView_mes_pesquisa);

        textViewAno = findViewById(R.id.textView_ano_pesquisa);

        //Integer[] meses = new Integer[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};//"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"};

        String[] mesesString = new String[]{"Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"};

        Integer[] anos = new Integer[]{2018, 2019, 2020, 2021, 2022, 2023, 2024, 2025, 2026};//"2018", "2019", "2020", "2021", "2022", "2023", "2024", "2025", "2026", "2027", "2028", "2029"};

        ArrayAdapter<String> adapterMes = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, mesesString);

        ArrayAdapter<Integer> adapterAno = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, anos);

        dropdownMes.setAdapter(adapterMes);

        dropdownAno.setAdapter(adapterAno);

        buttonPesquisar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int mesSelecionado = 0; //(Integer) dropdownMes.getSelectedItem();

                mesSelecionadoString = dropdownMes.getSelectedItem().toString();

                switch (mesSelecionadoString) {
                    case "Janeiro":
                        mesSelecionado = 0;
                        break;
                    case "Fevereiro":
                        mesSelecionado = 1;
                        break;
                    case "Março":
                        mesSelecionado = 2;
                        break;
                    case "Abril":
                        mesSelecionado = 3;
                        break;
                    case "Maio":
                        mesSelecionado = 4;
                        break;
                    case "Junho":
                        mesSelecionado = 5;
                        break;
                    case "Julho":
                        mesSelecionado = 6;
                        break;
                    case "Agosto":
                        mesSelecionado = 7;
                        break;
                    case "Setembro":
                        mesSelecionado = 8;
                        break;
                    case "Outubro":
                        mesSelecionado = 9;
                        break;
                    case "Novembro":
                        mesSelecionado = 10;
                        break;
                    case "Dezembro":
                        mesSelecionado = 11;
                        break;
                }

                anoSelecionado = (Integer) dropdownAno.getSelectedItem();

                jsonArrayCalendarioMaster = new JSONArray(new ArrayList<String>());

                jsonArrayRelatorioMaster = new JSONArray();

                arrayListMotoristasGrupo = new ArrayList<>();

                jsonObjectRelatorioMaster = new JSONObject();

                int nDias = RecurringDates.numberOfDays(anoSelecionado, mesSelecionado);

                Log.i("REL.N.DIAS.MES", String.valueOf(nDias));

                final String dia = String.format("%02d", nDias);
                final String mes = String.format("%02d", mesSelecionado + 1);
                final String ano = String.valueOf(anoSelecionado);

                final String diaFinalMes = ano + "-" + mes + "-" + dia;

                queryCalendario(ano, mes, diaFinalMes);
            }
        });
    }


    public void queryCalendario(String ano, String mes, String diaFinalMes) {

        String dataFinalQuery = ano + mes + diaFinalMes.substring(8, 10);

        ParseObject objetoGrupo2 = ParseObject.createWithoutData("GrupoCarona", BaseActivity.grupoSelecionadoId);

        ParseQuery<ParseObject> queryCalendario = ParseQuery.getQuery("Calendario");

        queryCalendario.whereEqualTo("pointerGrupoCarona", objetoGrupo2);

        queryCalendario.whereLessThan("data", dataFinalQuery);

        queryCalendario.whereStartsWith("anoMes", ano + mes);

        queryCalendario.include("pointerGrupoCarona");

        queryCalendario.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {

                if (e == null) {

                    if (objects.size() > 0) {

                        ParseObject grupoCarona = objects.get(0).getParseObject("pointerGrupoCarona");

                        //Log.i("RELATORIO.OBJ", objects.get(0).getParseObject("pointerGrupoCarona").toString());

                        List<ParseUser> participantesGrupo = grupoCarona.getList("usuariosGrupo");

                        List<ParseUser> motoristasGrupo = grupoCarona.getList("motoristas");

                        //Log.i("RELATORIO.OBJ", participantesGrupo.toString());

                        Log.i("RELATORIO", String.valueOf(objects.size()));

                        jsonObjectRelatorioSuper = new JSONObject();

                        int dia = 0;





                        for (ParseUser objectMotorista : motoristasGrupo) {
                            try {
                                objectMotorista.fetchIfNeeded();
                                arrayListMotoristasGrupo.add(objectMotorista.getString("nome"));

                                jsonObjectParticipantesMaster = new JSONObject();

                                arrayListParticipantesGrupo.clear();

                                for (ParseUser objectUser : participantesGrupo) {
                                    try {
                                        objectUser.fetchIfNeeded();
                                        arrayListParticipantesGrupo.add(objectUser.getString("nome"));

                                        //JSONObject jsonObjectParticipante = new JSONObject();

                                        try {
                                            //jsonObjectParticipante.put("nome", objectUser.getString("nome"));
                                            //jsonObjectParticipante.put("valor", 0);
                                            //jsonArray.put(jsonObjectParticipante);
                                            jsonObjectQtdDiasCaroneirosNaoFoi.put(objectUser.getString("nome"), 0);

                                            JSONObject jsonObjectIner = new JSONObject();

                                            jsonObjectIner.put("valor", 0.00);
                                            jsonObjectIner.put("distancia", 0.00);

                                            //MUDANÇA EM 13/05/2019

                                            jsonObjectParticipantesMaster.put(objectUser.getString("nome"), jsonObjectIner);

                                        } catch (JSONException e1) {
                                            e1.printStackTrace();
                                        }
                                    } catch (ParseException e1) {
                                        e1.printStackTrace();
                                    }
                                }
                                try {
                                    //jsonObjectParticipantesMaster.put("motorista", objectMotorista.getString("nome")); COMENTADO EM 13/05/2019
                                    //jsonObjectParticipantesMaster.put("array", jsonArray);

                                    jsonObjectRelatorioSuper.put(objectMotorista.getString("nome"), jsonObjectParticipantesMaster);

                                    //jsonArrayRelatorioMaster.put(jsonObjectParticipantesMaster); COMENTADO EM 13/05/2019

                                    //jsonArrayRelatorioMaster.put(jsonObjectSuper);

                                    //jsonObjectRelatorioMaster.put(objectMotorista.getString("nome"), jsonArrayRelatorioMaster); COMENTADO EM 13/05/2019

                                } catch (JSONException e1) {
                                    e1.printStackTrace();
                                }
                            } catch (ParseException e1) {
                                e1.printStackTrace();
                            }
                        }




                        for (ParseObject object : objects) {

                            List<String> listNaoFoi = object.getList("naoFoi");

                            jsonArrayCalendario = new JSONArray(new ArrayList<String>());

                            for (int i = 0; i < object.getList("naoFoiDistancia").size(); i++) {

                                jsonArrayCalendario.put(object.getList("naoFoiDistancia").get(i));

                            }
                            jsonArrayCalendarioMaster.put(jsonArrayCalendario);

                            //naoFoiAA.add(object.getList("naoFoi"));

                            //arrayListParticipantesGrupoTemp.removeAll(Collections.singletonList(jo.getString("nome")));

                            for(int j = 0; j < listNaoFoi.size(); j++){

                                try {
                                    dia = jsonObjectQtdDiasCaroneirosNaoFoi.getInt(listNaoFoi.get(j));
                                    dia++;
                                    jsonObjectQtdDiasCaroneirosNaoFoi.put(listNaoFoi.get(j), dia);

                                } catch (JSONException e1) {
                                    e1.printStackTrace();
                                }

                                Log.i("CALENDARIO.10", "listNaoFoi: " + jsonObjectQtdDiasCaroneirosNaoFoi.toString());
                            }
                        }


                        Log.i("RELATORIO", arrayListParticipantesGrupo.toString());

                        Log.i("RELATORIO.MOT.", arrayListMotoristasGrupo.toString());

                        Log.i("RELATORIO.JS.MS", jsonArrayRelatorioMaster.toString()); //jsonObjectRelatorioMaster

                        //Log.i("RELATORIO.AA", naoFoiAA.toString());
                        Log.i("RELATORIO.REL.MS", jsonArrayCalendarioMaster.toString());

                        Log.i("RELATORIO.REL.SUPER", jsonObjectRelatorioSuper.toString());

                        queryRecorrencia(diaFinalMes);
                    }
                }
            }
        });
    }


    public void queryRecorrencia(String diaFinalMes) {

        ParseObject objetoGrupo2 = ParseObject.createWithoutData("GrupoCarona", BaseActivity.grupoSelecionadoId);

        ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Recorrencia");

        query2.whereEqualTo("pointerGrupoCarona", objetoGrupo2);

        query2.include("arrayTrajetos");

        query2.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {

                if (e == null) {

                    Log.i("REL.N.DIAS.MES", "e == null");

                    if (objects.size() > 0) {

                        Log.i("REL.N.DIAS.MES", "objects > 0");

                        LinearLayout linearLayout = findViewById(R.id.linearLayout_resultado_pesquisa);

                        linearLayout.removeAllViews();

                        for (ParseObject o : objects) {

                            String frequenciaSemEspacos = o.getString("frequencia").replaceAll("\\s", "");

                            String intervaloSemEspacos = o.getString("intervalo").replaceAll("\\s", "");

                            List<String> listDiasDaSemana = o.getList("diasDaSemana");

                            String diasDaSemanaSemEspacos = listDiasDaSemana.toString().substring(1, listDiasDaSemana.toString().length() - 1);

                            diasDaSemanaSemEspacos = diasDaSemanaSemEspacos.replaceAll("\\s", "");

                            Log.i("REL.N.DIAS.MES", "ObjetoId: " + o.getObjectId());
                            Log.i("REL.N.DIAS.MES", "Frequencia: " + frequenciaSemEspacos);
                            Log.i("REL.N.DIAS.MES", "Intervalo: " + intervaloSemEspacos);
                            Log.i("REL.N.DIAS.MES", "Dias da Semana: " + diasDaSemanaSemEspacos);
                            Log.i("REL.N.DIAS.MES", "DiaSelecionado: " + diaFinalMes);

                            try {

                                boolean check = RecurringDates.rrule(o.getString("frequencia"),
                                        o.getString("intervalo"),
                                        listDiasDaSemana, o.getString("dataInicial"),
                                        diaFinalMes);

                                int diasRec = RecurringDates.getDiasRecorrencia();

                                double consumo = 10;

                                double mediaCombustivel = 3.99;

                                Log.i("REL.N.DIAS.MES", "get " + (RecurringDates.getDiasRecorrencia()));

                                List<ParseObject> trajetos = o.getList("arrayTrajetos");

                                if (trajetos != null) {

                                    if (trajetos.size() > 0) {

                                        for (ParseObject oTrajeto : trajetos) { //LOOP POR CADA TRAJETO.

                                            double distancia = oTrajeto.getInt("distancia");

                                            List<String> participantesArray = oTrajeto.getList("participantes");

                                            int qtddParticipantes = participantesArray.size();

                                            int reducaoDias = 0;

                                            //reducaoDias++;

                                            double valorDiasComFaltantes = 0;
                                            double distanciaComFaltantes = 0;

                                            for (int x = 0; x < jsonArrayCalendarioMaster.length(); x++) { //LOOP POR CADA DIA QUE POSSUI PESSOAS QUE NÃO FORAM.


                                                reducaoDias++;

                                                ArrayList<String> arrayListParticipantesGrupoTemp = new ArrayList<>(participantesArray);

                                                try {

                                                    JSONArray jsonArrayTemp = jsonArrayCalendarioMaster.getJSONArray(x);

                                                    Log.i("RELATORIO.INNER", jsonArrayCalendarioMaster.getJSONArray(x).toString() + String.valueOf(jsonArrayTemp.length()));


                                                    for (int y = 0; y < jsonArrayTemp.length(); y++) { //LOOP POR CADA CARONEIRO DO DIA

                                                        Log.i("RELATORIO.INNER2", jsonArrayTemp.get(y).toString());

                                                        JSONObject jo = new JSONObject(jsonArrayTemp.get(y).toString());

                                                        //String id = jo.getString("objectId");


                                                        if (jo.getString("objectId").equals(oTrajeto.getObjectId())) {

                                                            arrayListParticipantesGrupoTemp.removeAll(Collections.singletonList(jo.getString("nome")));

                                                            //AQUI REMOVE O CARONEIRO DO GRUPO DE CARONA DE UM DIA !!!!

                                                        }

                                                        //Log.i("RELATORIO.INNER2.1", id);

                                                    } // FINAL DO LOOP DE TODOS OS CARONEIROS DO DIA


                                                    //double valorDiasComFaltantes = 0;


                                                    if (arrayListParticipantesGrupoTemp.size() <= 0) {

                                                        valorDiasComFaltantes = 0;

                                                    } else {

                                                        valorDiasComFaltantes = (((distancia / consumo) * mediaCombustivel) / arrayListParticipantesGrupoTemp.size()) / 1000;
                                                        distanciaComFaltantes = (distancia) / 1000;

                                                        Log.i("RELATORIO.REL.SUPER.8", "Distancia: " + String.valueOf(distancia));

                                                        Log.i("RELATORIO.REL.SUPER.8", "Consumo: " + String.valueOf(consumo));

                                                        Log.i("RELATORIO.REL.SUPER.8", "Media Combustivel: " + String.valueOf(mediaCombustivel));

                                                        Log.i("RELATORIO.REL.SUPER.8", "arrayListParticipantesGrupoTemp.size(): " + String.valueOf(arrayListParticipantesGrupoTemp.size()));

                                                        Log.i("RELATORIO.REL.SUPER.8", "ValorDiasComFaltantes: " + String.valueOf(valorDiasComFaltantes));

                                                        double valor = 0;
                                                        double distanciaCarona = 0;

                                                        Log.i("RELATORIO.REL.SUPER.7.1", "ValorDiasComFaltantes: " + String.valueOf(valor));

                                                        for (int i = 0; i < arrayListParticipantesGrupoTemp.size(); i++) {

                                                            Log.i("RELATORIO.REL.SUPER.7.2", "ValorDiasComFaltantes: " + String.valueOf(valor));

                                                            JSONObject jsonObjectBase = new JSONObject(jsonObjectRelatorioSuper.get(oTrajeto.getString("motorista")).toString());

                                                            JSONObject jsonObjectBase2 = new JSONObject(jsonObjectBase.getJSONObject(arrayListParticipantesGrupoTemp.get(i)).toString());
                                                            //valor = jsonObjectBase.getDouble(arrayListParticipantesGrupoTemp.get(i));
                                                            valor = jsonObjectBase2.getDouble("valor");
                                                            distanciaCarona = jsonObjectBase2.getDouble("distancia");



                                                            //Log.i("RELATORIO.REL.SUPER.7.3", "ValorDiasComFaltantes: " + String.valueOf(valor) + " - " +arrayListParticipantesGrupoTemp.get(i));

                                                            //Log.i("RELATORIO.REL.SUPER.1", "Caroneiro: " + arrayListParticipantesGrupoTemp.get(i) + " Valor 1: " + String.valueOf(valor) + " antes.");
                                                            //Log.i("RELATORIO.REL.SUPER.1", "Caroneiro: " + arrayListParticipantesGrupoTemp.get(i) + " Valor Faltantes: " + String.valueOf(valorDiasComFaltantes) + " antes.");
                                                            valor = valor + valorDiasComFaltantes;
                                                            distanciaCarona = distanciaCarona + distanciaComFaltantes;


                                                            Log.i("RELATORIO.REL.SUPER.7.4", "ValorDiasComFaltantes: " + String.valueOf(valor));

                                                            Log.i("RELATORIO.REL.SUPER.1", "Caroneiro: " + arrayListParticipantesGrupoTemp.get(i) + " Valor: " + String.valueOf(valor) + " depois.");

                                                            //jsonObjectBase.put(arrayListParticipantesGrupoTemp.get(i), valor);
                                                            jsonObjectBase2.put("valor", valor);
                                                            jsonObjectBase2.put("distancia", distanciaCarona);
                                                            jsonObjectBase.put(arrayListParticipantesGrupoTemp.get(i), jsonObjectBase2);


                                                            Log.i("RELATORIO.REL.SUPER.7.5", "ValorDiasComFaltantes: " + String.valueOf(valor));

                                                            jsonObjectRelatorioSuper.put(oTrajeto.getString("motorista"), jsonObjectBase);
                                                        }
                                                        Log.i("RELATORIO.REL.SUPER.7.6", "ValorDiasComFaltantes: " + String.valueOf(valor));
                                                    }

                                                } catch (JSONException e1) {
                                                    e1.printStackTrace();
                                                }
                                            } //FINAL DO LOOP DE CADA PESSOA DOS DIAS QUE POSSUI PESSOAS QUE NÃO FORAM.

                                            Log.i("RELATORIO.REL.SUPER.2", String.valueOf(reducaoDias + " size " + String.valueOf(trajetos.size())));

                                            double valorTotalMes = ((((distancia / consumo) * mediaCombustivel) / qtddParticipantes) * (diasRec - (reducaoDias))) / 1000;
                                            double distanciaTotalMes = ((distancia) * (diasRec - (reducaoDias))) / 1000;

                                            for (int j = 0; j < participantesArray.size(); j++) {

                                                JSONObject jsonObjectBase = null;
                                                JSONObject jsonObjectBase2 = null;
                                                try {
                                                    jsonObjectBase = new JSONObject(jsonObjectRelatorioSuper.get(oTrajeto.getString("motorista")).toString());
                                                    jsonObjectBase2 = new JSONObject(jsonObjectBase.getJSONObject(participantesArray.get(j)).toString());

                                                    //double valor = jsonObjectBase.getDouble(participantesArray.get(j));
                                                    double valor = jsonObjectBase2.getDouble("valor");
                                                    double distanciaCarona = jsonObjectBase2.getDouble("distancia");

                                                    Log.i("RELATORIO.REL.SUPER.3", "Caroneiro: " + participantesArray.get(j) + " Valor: " + String.valueOf(valor) + " antes.");

                                                    valor = valor + valorTotalMes;
                                                    distanciaCarona = distanciaCarona + distanciaTotalMes;

                                                    Log.i("RELATORIO.REL.SUPER.3", "Caroneiro: " + participantesArray.get(j) + " Valor: " + String.valueOf(valor) + " depois.");

                                                    jsonObjectBase2.put("valor", valor);
                                                    jsonObjectBase2.put("distancia", distanciaCarona);


                                                    //jsonObjectBase.put(participantesArray.get(j), valor);

                                                    jsonObjectBase.put(participantesArray.get(j), jsonObjectBase2);

                                                    jsonObjectRelatorioSuper.put(oTrajeto.getString("motorista"), jsonObjectBase);

                                                } catch (JSONException e1) {
                                                    e1.printStackTrace();
                                                }
                                            }
                                        } //FINAL DO LOOP DE CADA TRAJETO

                                        Log.i("RELATORIO.JS.MS.2", jsonArrayRelatorioMaster.toString());

                                        Log.i("RELATORIO.REL.SUPER.9", jsonObjectRelatorioSuper.toString());

                                        textViewMes.setVisibility(View.VISIBLE);

                                        textViewAno.setVisibility(View.VISIBLE);

                                        textViewMes.setText(mesSelecionadoString);

                                        textViewAno.setText(String.valueOf(anoSelecionado));

                                        int count = 0;

                                        for (int l = 0; l < arrayListMotoristasGrupo.size(); l++) {

                                            for (int m = 0; m < arrayListParticipantesGrupo.size(); m++) {

                                                count++;

                                                try {
                                                    JSONObject jsonObjectRelatorio = new JSONObject(jsonObjectRelatorioSuper.get(arrayListMotoristasGrupo.get(l)).toString());

                                                    JSONObject jsonObjectRelatorio2 = new JSONObject(jsonObjectRelatorio.getJSONObject(arrayListParticipantesGrupo.get(m)).toString());

                                                    //double valorRelatorio = jsonObjectRelatorio.getDouble(arrayListParticipantesGrupo.get(m));
                                                    double valorRelatorio = jsonObjectRelatorio2.getDouble("valor");
                                                    double distanciaRelatorio = jsonObjectRelatorio2.getDouble("distancia");
                                                    int diasNaoFoiRelatorio = jsonObjectQtdDiasCaroneirosNaoFoi.getInt(arrayListParticipantesGrupo.get(m));
                                                    int diasQueFoiRelatorio = diasRec - diasNaoFoiRelatorio;
                                                    double valorMedioRelatorio = valorRelatorio / diasQueFoiRelatorio;

                                                    DecimalFormat df = new DecimalFormat("##.##");

                                                    String valorString = df.format(valorRelatorio);
                                                    String distanciaString = df.format(distanciaRelatorio);
                                                    String valorMedioString = df.format(valorMedioRelatorio);

                                                    String continuacao = "";

                                                    if(valorRelatorio <= 0){

                                                        continuacao = "\n\nNão há dias de carona para este participante e motorista.";

                                                    } else {

                                                        continuacao =
                                                                "\nVocê esteve em " + String.valueOf(diasQueFoiRelatorio) + " de " + String.valueOf(diasRec) + " dias de Carona" +
                                                                "\nE compartilhou " + distanciaString + " km este mês !" +
                                                                "\n\nValor Médio Diário: R$ " + valorMedioString +
                                                                "\nValor Total Devido: R$ " + valorString;


                                                    }

                                                    String textoFormatado = "Motorista: " + arrayListMotoristasGrupo.get(l) +
                                                            "\nCarona: " + arrayListParticipantesGrupo.get(m) +
                                                            continuacao;



                                                    TextView rowTextView = new TextView(RelatorioDias.this);

                                                    rowTextView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                                                    rowTextView.setId(count);
                                                    rowTextView.setTextSize(17);
                                                    rowTextView.setGravity(Gravity.START);
                                                    rowTextView.setLines(7);
                                                    rowTextView.setSingleLine(false);
                                                    rowTextView.setPadding(20, 30, 20, 30);
                                                    rowTextView.setText(textoFormatado);

                                                    View viewDivider = new View(RelatorioDias.this);

                                                    viewDivider.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1));

                                                    viewDivider.setBackgroundColor(Color.parseColor("#737373"));

                                                    linearLayout.addView(rowTextView);

                                                    linearLayout.addView(viewDivider);


                                                } catch (JSONException e1) {
                                                    e1.printStackTrace();
                                                }
                                            }
                                        }
                                    }
                                }

                                if (check) {
                                    Log.i("REL.N.DIAS.MES", "check");
                                } else {
                                    Log.i("REL.N.DIAS.MES", "false check");
                                }
                            } catch (java.text.ParseException e1) {
                                e1.printStackTrace();
                            }
                        }
                    } else {
                        Log.i("REL.N.DIAS.MES", "objects < 0");
                    }
                } else {
                    Log.i("REL.N.DIAS.MES", "e != null");
                }
            }
        });
    }
}
