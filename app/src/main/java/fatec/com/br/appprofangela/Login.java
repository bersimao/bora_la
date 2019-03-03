package fatec.com.br.appprofangela;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
//import android.widget.EditText;

//import com.github.rtoshiro.util.format.SimpleMaskFormatter;
//import com.github.rtoshiro.util.format.text.MaskTextWatcher;

public class Login extends AppCompatActivity {

   /* private EditText cpfFunc;{

        //Mascara para CPF
        SimpleMaskFormatter simpleMaskFormatter = new SimpleMaskFormatter("NNN.NNN.NNN-NN");
        MaskTextWatcher masCPF = new MaskTextWatcher(cpfFunc, simpleMaskFormatter);
        cpfFunc.addTextChangedListener(masCPF);

    }*/

   public void mostrarSistema(){

       Intent intent = new Intent(this, GrupoCarona.class);

       startActivity(intent);
   }

    public void AbrirEsqSenha (View v) {

        Intent intent = new Intent (this, EsqueciSenha.class);

        startActivity(intent);
    }

    public void AbrirCadastro (View view) {

        Intent intent = new Intent (this, Cadastro.class);

        startActivity(intent);
    }

    public void entrarSistema(View view) {

        EditText login = findViewById(R.id.login);
        EditText senha = findViewById(R.id.senha);

        if(login.getText().toString().matches("") || senha.getText().toString().matches("")){

            Toast.makeText(this, "Inserir Nome de Usuário e Senha", Toast.LENGTH_SHORT).show();

        } else{

            ParseUser.logInInBackground(login.getText().toString(), senha.getText().toString(), new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {

                    if (user != null){

                        Log.i("Login", "Login Sucessful");
                        Toast.makeText(Login.this, "Login Efetuado!", Toast.LENGTH_SHORT).show();
                        mostrarSistema();

                    } else {

                        Toast.makeText(Login.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            });

        }

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setTitle("");

        //cpfFunc = findViewById(R.id.id_login);

/*
        ParseUser user = new ParseUser();

        user.setUsername("bernardo.simao");
        user.setPassword("1234");

        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {

                if (e == null){

                    Log.i("Sign Up", "Successful");

                } else {

                    Log.i("Sign Up", "Failed");

                }

            }
        });

*/

        /*
        if (ParseUser.getCurrentUser() != null){

            mostrarSistema();
        }

        */

        ParseAnalytics.trackAppOpenedInBackground(getIntent());


    }
}
