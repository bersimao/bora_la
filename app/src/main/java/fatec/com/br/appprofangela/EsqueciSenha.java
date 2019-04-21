package fatec.com.br.appprofangela;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;

public class EsqueciSenha extends AppCompatActivity {

    private TextInputLayout textInputEmail;
    private Button btncontinuar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_esqueci_senha);
    }

    private boolean validateEmail(){
        String emailInput = textInputEmail.getEditText().getText().toString().trim();

        btncontinuar.setEnabled (true);

        if (emailInput.isEmpty()){
            textInputEmail.setError("É necessário informar um email.");
            return false;

        }else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()){
            textInputEmail.setError("Formato do Email inválido!");
            return false;

        }else {
            textInputEmail.setError(null);
            return true;
        }
    }

    public void retornoLogin (View view){
        Intent intent = new Intent(this, Login.class);

        startActivity(intent);
    }
}
