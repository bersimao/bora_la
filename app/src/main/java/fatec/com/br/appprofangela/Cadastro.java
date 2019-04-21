package fatec.com.br.appprofangela;

import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

// TELA DE CADASTRO DE NOVOS USUÁRIOS
public class Cadastro extends AppCompatActivity {

    public void splashCarregamento() {
        Intent intent = new Intent(this, SplashCarregamento.class);
        startActivity(intent);
    }

    public void CadastroUsuario(View view) {

        EditText nome = findViewById(R.id.nome);
        EditText sobrenome = findViewById(R.id.sobrenome);
        EditText cpf = findViewById(R.id.cpf);
        EditText telefone = findViewById(R.id.telefone);
        EditText email = findViewById(R.id.email2);
        EditText senha = findViewById(R.id.senha2);

        if (nome.getText().toString().matches("") || sobrenome.getText().toString().matches("") || cpf.getText().toString().matches("") || telefone.getText().toString().matches("") || email.getText().toString().matches("") || senha.getText().toString().matches("")) {

            Toast.makeText(this, "Preencha Todos os Dados", Toast.LENGTH_SHORT).show();

        } else {

            ParseUser user = new ParseUser();

            user.setUsername(nome.getText().toString());

            user.put("sobrenome", sobrenome.getText().toString());

            user.setPassword(senha.getText().toString());

            user.setEmail(email.getText().toString());

            user.put("cpf", cpf.getText().toString());

            user.put("telefone", telefone.getText().toString());

            user.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {

                    if (e == null) {

                        Log.i("Sign Up", "Successful");
                        splashCarregamento();

                    } else {

                        Log.i("Sign Up", "Failed");
                        Toast.makeText(Cadastro.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
    }
}