package cursoandroid.whatsappandroid.com.br;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import cursoandroid.whatsappandroid.com.br.adapter.TabAdapter;
import cursoandroid.whatsappandroid.com.br.config.ConfiguracaoFirebase;
import cursoandroid.whatsappandroid.com.br.helper.Base64Custom;
import cursoandroid.whatsappandroid.com.br.helper.SlidingTabLayout;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private FirebaseAuth usuarioAutenticacao;

    private SlidingTabLayout slidingTabLayout;
    private ViewPager viewPager;

    private String identificadorDoContato;
    private DatabaseReference firebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usuarioAutenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("WattsApp");
        setSupportActionBar(toolbar);

        slidingTabLayout = findViewById(R.id.stl_taps);
        viewPager = findViewById(R.id.vp_pagina);

        //Distribui as abas no espaço do layout
        slidingTabLayout.setDistributeEvenly(true);
        slidingTabLayout.setSelectedIndicatorColors(ContextCompat.getColor(this, R.color.colorAccent));

        TabAdapter tabAdapter = new TabAdapter(getSupportFragmentManager());
        viewPager.setAdapter(tabAdapter);

        slidingTabLayout.setViewPager(viewPager);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.itemConfiguracoes:
                return true;

            case R.id.itemSair:
                deslogarUsuario();
                return true;

            case R.id.itemAdicionar:

                abrirCadastroContato();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void abrirCadastroContato() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setTitle("Novo Contato");
        alertDialog.setMessage("E-mail do usuário");
        alertDialog.setCancelable(false);

        final EditText editText = new EditText(MainActivity.this);
        alertDialog.setView(editText);

        // Configura botões
        alertDialog.setPositiveButton("Cadastrar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String emailContato = editText.getText().toString();
                if (emailContato.isEmpty()) {

                    Toast.makeText(MainActivity.this, "Preencha o e-mail!", Toast.LENGTH_SHORT).show();

                } else {

                    identificadorDoContato = Base64Custom.codificarBase64(emailContato);

                    firebase = ConfiguracaoFirebase.getFirebase();
                    firebase.child("usuarios").child(identificadorDoContato);

                    firebase.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            if (dataSnapshot.getValue() != null) {

                                firebase = ConfiguracaoFirebase.getFirebase();
                                //firebase.child("contatos").child();

                            } else {

                                Toast.makeText(MainActivity.this, "Usuário não possui cadastro!",Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }
        });

        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


            }
        });

        alertDialog.create();
        alertDialog.show();
    }

    private void deslogarUsuario() {

        usuarioAutenticacao.signOut();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

}
