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

    private final String CHAVE_NOME = "nome";
    private final String CHAVE_FONE = "fone";
    private final String CHAVE_TOKEN = "token";

    public Preferencias (Context paramContexto) {

        contexto = paramContexto;
        preferences = contexto.getSharedPreferences(NOME_ARQUIVO, MODE);
        editor = preferences.edit();
    }

    public void salvarPreferencias(String nome, String fone, String token) {

        editor.putString(CHAVE_NOME, nome);
        editor.putString(CHAVE_FONE, fone);
        editor.putString(CHAVE_TOKEN, token);
        editor.commit();
    }

    public HashMap<String, String> getDadosUsuario() {

        HashMap<String, String> dadosUsuario = new HashMap<>();

        dadosUsuario.put(CHAVE_NOME, preferences.getString(CHAVE_NOME, null));
        dadosUsuario.put(CHAVE_FONE, preferences.getString(CHAVE_FONE, null));
        dadosUsuario.put(CHAVE_TOKEN, preferences.getString(CHAVE_TOKEN, null));

        return dadosUsuario;
    }

}
