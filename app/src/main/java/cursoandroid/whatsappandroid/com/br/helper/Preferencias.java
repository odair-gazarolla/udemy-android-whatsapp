package cursoandroid.whatsappandroid.com.br.helper;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class Preferencias {

    private Context contexto;
    private SharedPreferences preferences;
    private final String NOME_ARQUIVO = "whatsapp-preferencias";
    private final int MODE = 0; //somente meu app poderá utilizar esse sharedPreferences
    private SharedPreferences.Editor editor;

    private final String CHAVE_IDENTIFICADOR = "identificadorUsuarioLogado";

    public Preferencias (Context paramContexto) {

        contexto = paramContexto;
        preferences = contexto.getSharedPreferences(NOME_ARQUIVO, MODE);
        editor = preferences.edit();
    }

    public void salvarDados(String identificadorUsuarioLogao) {

        editor.putString(CHAVE_IDENTIFICADOR, identificadorUsuarioLogao);
        editor.commit();
    }

}
