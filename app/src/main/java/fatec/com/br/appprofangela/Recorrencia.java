package fatec.com.br.appprofangela;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Recorrencia extends AppCompatActivity {

    String resumo = "", freq = "", interval = "1", byday = "";
    ArrayList byday2 = new ArrayList();
    EditText editTextIntervalo;
    Button buttonSalvarRecorrencia, buttonCancelarRecorrencia;
    CheckBox checkBoxSeg, checkBoxTer, checkBoxQua, checkBoxQui, checkBoxSexta, checkBoxSab, checkBoxDom;
    Spinner dropdownFrequencia;
    ArrayAdapter arrayAdapterSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recorrencia);

        String[] spinnerRecorrencias = new String[]{"dia", "semana", "mês", "ano"};

        editTextIntervalo = findViewById(R.id.edit_text_quantidades_recorrencias);

        buttonSalvarRecorrencia = findViewById(R.id.dialog_btnSalvarRecorrencia);

        buttonCancelarRecorrencia = findViewById(R.id.dialog_btnCancelarRecorrencia);

        dropdownFrequencia = findViewById(R.id.spinner_recorrencias_dias_semana);

        arrayAdapterSpinner = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, spinnerRecorrencias);

        dropdownFrequencia.setAdapter(arrayAdapterSpinner);

        dropdownFrequencia.setSelection(1);

        addListenerOnSaveButton();

        buttonCancelarRecorrencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BaseActivity.situacaoRadioGroupRecorrencia = 1;

                Intent intent = new Intent(Recorrencia.this, Trajetos.class);

                startActivity(intent);
            }
        });
    }

    public void addListenerOnSaveButton() {

        ArrayList<String> diasSemanaExtenso = new ArrayList<>();

        ArrayList<String> diaSemana = new ArrayList<>();

        checkBoxSeg = findViewById(R.id.checkBox_segunda);
        checkBoxTer = findViewById(R.id.checkBox_terca);
        checkBoxQua = findViewById(R.id.checkBox_quarta);
        checkBoxQui = findViewById(R.id.checkBox_quinta);
        checkBoxSexta = findViewById(R.id.checkBox_sexta);
        checkBoxSab = findViewById(R.id.checkBox_sabado);
        checkBoxDom = findViewById(R.id.checkBox_domingo);

        buttonSalvarRecorrencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Recorrencia.this, Trajetos.class);

                if (checkBoxSeg.isChecked()) {

                    diaSemana.add("MO");
                    diasSemanaExtenso.add("segunda");
                }
                if (checkBoxTer.isChecked()) {

                    diaSemana.add("TU");
                    diasSemanaExtenso.add("terça");
                }
                if (checkBoxQua.isChecked()) {

                    diaSemana.add("WE");
                    diasSemanaExtenso.add("quarta");
                }
                if (checkBoxQui.isChecked()) {

                    diaSemana.add("TH");
                    diasSemanaExtenso.add("quinta");
                }
                if (checkBoxSexta.isChecked()) {

                    diaSemana.add("FR");
                    diasSemanaExtenso.add("sexta");
                }
                if (checkBoxSab.isChecked()) {

                    diaSemana.add("SA");
                    diasSemanaExtenso.add("sábado");
                }
                if (checkBoxDom.isChecked()) {

                    diaSemana.add("SU");
                    diasSemanaExtenso.add("domingo");
                }

                ArrayList<String> diaSemanaUnico = removeDuplicates(diaSemana);

                Set<String> uniqueDiasSemanaExtenso = new HashSet<>(diasSemanaExtenso);

                String s = dropdownFrequencia.getSelectedItem().toString();

                switch(s) {
                    case "dia":
                        freq = "DAILY";
                        break;
                    case "semana":
                        freq = "WEEKLY";
                        break;
                    case "mês":
                        freq = "MONTHLY";
                        break;
                    case "ano":
                        freq = "YEARLY";
                        break;
                    default:
                        freq = "WEEKLY";
                        break;
                }

                editTextIntervalo.getText().toString();

                dropdownFrequencia.getSelectedItem().toString();

                BaseActivity.situacaoRadioGroupRecorrencia = 0;

                interval = editTextIntervalo.getText().toString();

                resumo = "Repete a cada " + interval + " " + dropdownFrequencia.getSelectedItem().toString() + ", ás/ aos " + uniqueDiasSemanaExtenso.toString().substring(1, uniqueDiasSemanaExtenso.toString().length() -1) + ".";

                intent.putExtra("resumoRecorrencia", resumo);
                intent.putExtra("freqRecorrencia", freq);
                intent.putExtra("intervalRecorrencia", interval);
                intent.putExtra("bydayRecorrencia", diaSemanaUnico);

                Log.i("RECORR","Numero: " + editTextIntervalo.getText().toString() + "Tipo: " + dropdownFrequencia.getSelectedItem().toString());

                if (!checkBoxSeg.isChecked() && !checkBoxTer.isChecked() && !checkBoxQua.isChecked() && !checkBoxQui.isChecked() && !checkBoxSexta.isChecked() && !checkBoxSab.isChecked() && !checkBoxDom.isChecked()){

                    Toast.makeText(Recorrencia.this, "Selecione pelo menos um dia da semana,", Toast.LENGTH_SHORT).show();

                } else {

                    Toast.makeText(Recorrencia.this, "Dia da Semana: " + diaSemanaUnico, Toast.LENGTH_SHORT).show();

                    startActivity(intent);
                }
            }
        });
    }

    public static <T> ArrayList<T> removeDuplicates(ArrayList<T> list) {
        // Create a new LinkedHashSet
        Set<T> set = new LinkedHashSet<>();

        // Add the elements to set
        set.addAll(list);

        // Clear the list
        list.clear();

        // add the elements of set
        // with no duplicates to the list
        list.addAll(set);

        // return the list
        return list;
    }
}
