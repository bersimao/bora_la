package fatec.com.br.appprofangela;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.regex.Pattern;

/*Codificacao respeitando os requisitos do controle TRELLO RF002B*/

public class Login extends AppCompatActivity {

    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +   //Pelo menos um ditito
                    "(?=.*[a-z])" +   //Pelo menos uma letra minuscula
                    "(?=.*[A-Z])" +   //Pelo menos uma letra Maiuscula
                    "(?=\\S+$)" +     //Sem espaço em branco
                    ".{6,}" +         //no minimo 6 caracteres
                    "$");

    private TextInputLayout textInputEmail;
    private TextInputLayout textInputPassword;
    private Button btnentrar;
    private Button btncadastre;

    private boolean validateEmail(){
        String emailInput = textInputEmail.getEditText().getText().toString().trim();

        btnentrar.setEnabled (true);

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

    private boolean validatePassword(){
        String passwordInput = textInputPassword.getEditText().getText().toString().trim();

        if (passwordInput.isEmpty()){
            textInputPassword.setError("É necessário informar uma senha.");
            return false;

        } else if (!PASSWORD_PATTERN.matcher(passwordInput).matches()){
            textInputPassword.setError("A senha deve contar ao menos 6 caracteres, sendo ao menos uma letra maiúscula e um número.");
            return false;

        }else {
            textInputPassword.setError(null);
            return true;
        }
    }

    public void entrarSistema(View v){

        textInputEmail = findViewById(R.id.text_input_email);
        textInputPassword = findViewById(R.id.text_input_password);
        btnentrar = findViewById(R.id.entrar);
        btncadastre = findViewById(R.id.button2);


        if (!validateEmail() || !validatePassword()){

            btnentrar.setEnabled (true);
            btncadastre.setEnabled(true);

            return;
        } else{

            btnentrar.setEnabled (false);

            Log.i("teste_conexao", textInputEmail.getEditText().getText().toString() + textInputPassword.getEditText().getText().toString());

            ParseUser.logInInBackground(textInputEmail.getEditText().getText().toString(), textInputPassword.getEditText().getText().toString(), new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {

                    if (user != null ){

                        Log.i("Login", "Login Sucessful");
                        Toast.makeText(Login.this, "Seja bem vindo ao Bóra Lá  ;)", Toast.LENGTH_SHORT).show();
                        mostrarSistema();

                    } else {

                        btnentrar.setEnabled (true);

                        Toast.makeText(Login.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }

    public void mostrarSistema(){

        Intent intent = new Intent(this, GrupoCarona.class);

        startActivity(intent);

    }

    public void AbrirEsqSenha (View v) {

        Intent intent = new Intent (this, EsqueciSenha.class);

        startActivity(intent);
    }

    public void AbrirCadastro (View view) {

        btncadastre = findViewById(R.id.button2);
        btnentrar = findViewById(R.id.entrar);

        Intent intent = new Intent (this, Cadastro.class);

        btncadastre.setEnabled (false);
        btnentrar.setEnabled(true);

        startActivity(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setTitle("");

        ParseAnalytics.trackAppOpenedInBackground(getIntent());
    }
}