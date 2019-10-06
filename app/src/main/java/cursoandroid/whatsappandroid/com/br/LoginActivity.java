package cursoandroid.whatsappandroid.com.br;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import cursoandroid.whatsappandroid.com.br.config.ConfiguracaoFirebase;
import cursoandroid.whatsappandroid.com.br.helper.Permissao;
import cursoandroid.whatsappandroid.com.br.model.Usuario;

public class LoginActivity extends AppCompatActivity {

    private EditText loginEmail;
    private EditText loginSenha;
    private Button btnLogar;
    private ImageView img;
    private TextView tvCadastre;
    private Usuario usuario;
    private FirebaseAuth autenticacao;

    private String[] permissoesNecessarias = new String[] {

            Manifest.permission.SEND_SMS,
            Manifest.permission.INTERNET
    };

    public void abrirCadastroUsuario(View view)
    {
        Intent intent = new Intent(LoginActivity.this, CadastroUsuarioActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        verificarUsuarioLogado();

        Permissao.validaPermissao(1,this, permissoesNecessarias);

        loginEmail = findViewById(R.id.edtLoginEmail);
        loginSenha = findViewById(R.id.edtLoginSenha);
        btnLogar = findViewById(R.id.btnLogar);
        img = findViewById(R.id.imageView);

        btnLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                usuario = new Usuario();
                usuario.setEmail(loginEmail.getText().toString());
                usuario.setSenha(loginSenha.getText().toString());
                validarLogin();

            }
        });

    }

    private void verificarUsuarioLogado() {

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        if (autenticacao.getCurrentUser() != null) {

            abrirTelaPrincipal();
        }
    }

    private void validarLogin() {

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.signInWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha())
            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {

                        abrirTelaPrincipal();
                        finish();

                    } else {

                        Toast.makeText(LoginActivity.this, "Erro ao efetuar o Login!", Toast.LENGTH_SHORT).show();
                    }

                }
            });
    }

    private void abrirTelaPrincipal() {

        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }

    private boolean enviaSMS(String fone, String msg) {

        Toast.makeText(LoginActivity.this, fone + " - " + msg, Toast.LENGTH_SHORT).show();
        try {

            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(fone, null, msg, null,null);

            return  true;
        } catch (Exception e) {

            e.printStackTrace();
        }

        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for (int resultado : grantResults) {

            if (resultado == PackageManager.PERMISSION_DENIED) {

                alertaValidacaoPermissao();
            }
        }
    }

    private void alertaValidacaoPermissao() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permissões negadas");
        builder.setMessage("Para utilizar esse app, é necessário aceitar as permissões!");

        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                finish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

//    @Override
//    public boolean dispatchKeyEvent(KeyEvent event) {
//
//        int action = event.getAction();
//        int keyCode = event.getKeyCode();
//
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 0);
//        }
//
//        tirarFoto();
//
//        switch (keyCode) {
//
//            case KeyEvent.KEYCODE_VOLUME_UP:
//                if (action == KeyEvent.ACTION_DOWN) {
//
//                    Toast.makeText(this, "Volume up", Toast.LENGTH_SHORT).show();
//                }
//
//                return true;
//
//            case KeyEvent.KEYCODE_VOLUME_DOWN:
//                if (action == KeyEvent.ACTION_DOWN) {
//
//                    Toast.makeText(this, "Volume down", Toast.LENGTH_SHORT).show();
//                }
//
//                return true;
//            default:
//                return super.dispatchKeyEvent(event);
//        }
//    }

    private void tirarFoto() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == 1 && resultCode == RESULT_OK) {

            Bundle extras = data.getExtras();
            Bitmap image = (Bitmap) extras.get("data");
            img.setImageBitmap(image);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
