package fatec.com.br.appprofangela;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class DiasSemana extends AppCompatActivity {

    ArrayList<String> diasSemana = new ArrayList<>();

    ListView diasSemanaListView;

    ArrayAdapter arrayAdapter;

    Button buttonNovoTrajeto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dias_semana);

        diasSemanaListView = findViewById(R.id.list_view_dias_semana);

        buttonNovoTrajeto = findViewById(R.id.button_adicionar_novo_trajeto);

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, diasSemana);

        diasSemana.add("Segunda-feira");
        diasSemana.add("Terça-feira");
        diasSemana.add("Quarta-feira");
        diasSemana.add("Quinta-feira");
        diasSemana.add("Sexta-feira");
        diasSemana.add("Sábado");
        diasSemana.add("Domingo");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(DiasSemana.this, android.R.layout.simple_list_item_1, diasSemana);

        diasSemanaListView.setAdapter(arrayAdapter);

        diasSemanaListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //String diaSemana = diasSemana.get(position);

                String diaSemana = "";

                switch (position){
                    case 0:
                        diaSemana = "MO";
                        break;
                    case 1:
                        diaSemana = "TU";
                        break;
                    case 2:
                        diaSemana = "WE";
                        break;
                    case 3:
                        diaSemana = "TH";
                        break;
                    case 4:
                        diaSemana = "FR";
                        break;
                    case 5:
                        diaSemana = "SA";
                        break;
                    case 6:
                        diaSemana = "SU";
                        break;
                    default:
                        diaSemana = "";
                        break;
                }

                Intent diaSemanaSelecionado = new Intent(DiasSemana.this, DiaSemanaSelecionado.class);
                diaSemanaSelecionado.putExtra("diaSemana", diaSemana);
                startActivity(diaSemanaSelecionado);
            }
        });

        buttonNovoTrajeto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BaseActivity.participantesAA.clear();

                BaseActivity.participantesTemp.clear();

                BaseActivity.enderecoDestino.clear();

                BaseActivity.enderecoDestinoTemp = "";

                BaseActivity.nomeDestino.clear();

                BaseActivity.nomeDestinoTemp = "";

                BaseActivity.localInicial = "";

                BaseActivity.nomeLocalInicial = "";

                BaseActivity.situacaoRadioGroupRecorrencia = 1;

                Intent novoTrajeto = new Intent(DiasSemana.this, Trajetos.class);

                startActivity(novoTrajeto);

            }
        });



// CÓDIGO PARA ADICIONAR O BOTÃO FLUTUANTE NESTE ACTIVITY
/*
        //==============BOTÃO FLUTUANTE PARA ADICIONAR NOVOS TRAJETOS =============
        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentNovoGrupo = new Intent(DiasSemana.this, Trajetos.class);
                startActivity(intentNovoGrupo);
            }
        });
        //==============BOTÃO FLUTUANTE PARA ADICIONAR NOVOS TRAJETOS =============
*/

    }
}