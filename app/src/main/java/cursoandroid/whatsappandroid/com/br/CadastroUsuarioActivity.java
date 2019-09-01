package cursoandroid.whatsappandroid.com.br;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

import cursoandroid.whatsappandroid.com.br.config.ConfiguracaoFirebase;
import cursoandroid.whatsappandroid.com.br.model.Usuario;

public class CadastroUsuarioActivity extends AppCompatActivity {

    private EditText edtNome;
    private EditText edtEmail;
    private EditText edtSenha;
    private Button btnCadastrar;
    private Usuario usuario;
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);

        edtNome = findViewById(R.id.edtCadNome);
        edtEmail = findViewById(R.id.edtCadEmail);
        edtSenha = findViewById(R.id.edtCadSenha);
        btnCadastrar = findViewById(R.id.btnCadCadastrar);

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                usuario = new Usuario();
                usuario.setNome(edtNome.getText().toString());
                usuario.setEmail(edtEmail.getText().toString());
                usuario.setSenha(edtSenha.getText().toString());
                cadastrarUsuario();
            }
        });

    }

    public void cadastrarUsuario() {

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.createUserWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha()
        ).addOnCompleteListener(CadastroUsuarioActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

                    Toast.makeText(CadastroUsuarioActivity.this, "Sucesso ao cadastrar o usuario!", Toast.LENGTH_SHORT).show();

                    FirebaseUser firebaseUser = task.getResult().getUser();
                    usuario.setId(firebaseUser.getUid());

                    usuario.salvar();
                    autenticacao.signOut();

                    finish();

                } else {

                    String erro = "";

                    try {

                        throw task.getException();

                    } catch (FirebaseAuthWeakPasswordException e) {

                        erro = "Digite uma senha mais forte!";

                    } catch (FirebaseAuthInvalidCredentialsException e) {

                        erro = "Informe um email válido";

                    } catch (FirebaseAuthUserCollisionException e) {

                        erro = "Já existe uma conta registrada para o email informado!";

                    } catch (Exception e) {

                        erro = "Erro ao cadastrar o usuario!";
                    }

                    Toast.makeText(CadastroUsuarioActivity.this, erro, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
