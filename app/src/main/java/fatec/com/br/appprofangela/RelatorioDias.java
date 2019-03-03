package fatec.com.br.appprofangela;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

import com.parse.CountCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RelatorioDias extends AppCompatActivity {

    ArrayList<String> caroneirosNome = new ArrayList<>();
    ArrayList<String> diasRelatorio = new ArrayList<>();
    String grupoAtivo = "";
    int diasUteis = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relatorio_dias);

        Intent intent = getIntent();

        caroneirosNome = intent.getStringArrayListExtra("caroneirosNome");

        grupoAtivo = intent.getStringExtra("IdGrupo");

        final ListView diasListView = (ListView) findViewById(R.id.list_resultado_dias);

        final Spinner dropdownMes = findViewById(R.id.spinner_mes);

        final Spinner dropdownAno = findViewById(R.id.spinner_ano);

        Button buttonPesquisar = findViewById(R.id.button_pesquisar);

        String[] meses = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"};

        String[] anos = new String[]{"2018", "2019", "2020", "2021", "2022", "2023", "2024", "2025", "2026", "2027", "2028", "2029"};

        ArrayAdapter<String> adapterMes = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, meses);

        ArrayAdapter<String> adapterAno = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, anos);

        dropdownMes.setAdapter(adapterMes);

        dropdownAno.setAdapter(adapterAno);



        buttonPesquisar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mesSelecionado = dropdownMes.getSelectedItem().toString();
                String anoSelecionado = dropdownAno.getSelectedItem().toString();

                switch (mesSelecionado){
                    case "1":
                        diasUteis = 22;
                        break;
                    case "2":
                        diasUteis = 22;
                        break;
                    case "3":
                        diasUteis = 22;
                        break;
                    case "4":
                        diasUteis = 22;
                        break;
                    case "5":
                        diasUteis = 22;
                        break;
                    case "6":
                        diasUteis = 22;
                        break;
                    case "7":
                        diasUteis = 22;
                        break;
                    case "8":
                        diasUteis = 19;
                        break;
                    case "9":
                        diasUteis = 16;
                        break;
                    case "10":
                        diasUteis = 19;
                        break;
                    case "11":
                        diasUteis = 18;
                        break;
                    case "12":
                        diasUteis = 17;
                        break;
                        default:
                            diasUteis = 30;
                }


                Log.i("BORALA_REL", anoSelecionado+mesSelecionado);

                ParseObject objGrupoCarona = ParseObject.createWithoutData("GrupoCarona", grupoAtivo);

                ParseQuery<ParseObject> query = ParseQuery.getQuery("Calendario");

                query.whereEqualTo("pointerGrupoCarona", objGrupoCarona );

                query.whereStartsWith("anoMes", anoSelecionado+mesSelecionado);

                query.include("naoFoi");

                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        if (e == null){

                            if (objects.size() > 0){

                                Log.i("BORALA_REL1", String.valueOf(objects.size()));

                                final List<Map<String, String>> diasData = new ArrayList<Map<String, String>>();

                                for (int i = 0; i < caroneirosNome.size(); i++) {
                                    int count = 0;

                                    for (ParseObject result : objects) {

                                        if (result.getList("naoFoi") != null || result.get("naoFoi") != null) {

                                            List<String> userName = result.getList("naoFoi");

                                            for (int n = 0; n < userName.size(); n++) {
                                                if (userName.get(n) != null) {
                                                    if (userName.get(n).equals(caroneirosNome.get(i))) {
                                                        count++;
                                                    }
                                                }
                                            }
                                        }
                                    }

                                        Log.i("BORALA_REL2", caroneirosNome.get(i));
                                        Log.i("BORALA_REL3", String.valueOf(count));

                                        Map<String, String> dataInfo = new HashMap<String, String>();
                                        dataInfo.put("username", "Caroneiro(a): " + caroneirosNome.get(i));
                                        dataInfo.put("dias", String.valueOf(diasUteis - count) + " dia(s) presente na carona.");

                                        diasData.add(dataInfo);


                                }
                                    SimpleAdapter simpleAdapter = new SimpleAdapter(RelatorioDias.this, diasData, android.R.layout.simple_list_item_2, new String[] {"username", "dias"}, new int[] {android.R.id.text1, android.R.id.text2});

                                    diasListView.setAdapter(simpleAdapter);

                            }
                        }
                    }
                });
            }
        });
    }
}
