package fatec.com.br.appprofangela;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class DiasSemana extends AppCompatActivity {

    ArrayList<String> diasSemana = new ArrayList<>();

    ListView diasSemanaListView;

    ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dias_semana);

        diasSemanaListView = findViewById(R.id.list_view_dias_semana);

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
                String diaSemana = diasSemana.get(position);
                Intent diaSemanaSelecionado = new Intent(DiasSemana.this, Trajetos.class);
                diaSemanaSelecionado.putExtra("diaSemana", diaSemana);
                startActivity(diaSemanaSelecionado);
            }
        });

    }
}
