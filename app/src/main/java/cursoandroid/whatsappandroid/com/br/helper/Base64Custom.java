package cursoandroid.whatsappandroid.com.br.helper;

import android.util.Base64;

public class Base64Custom {

    public static String codificarBase64(String texto) {

        String strBase64 = Base64.encodeToString(texto.getBytes(), Base64.DEFAULT);
        //Remover as quebras de linha que podem aparecer na base64
        strBase64 = strBase64.replaceAll("\\n|\\r", "");

        return strBase64;
    }

    public static String decodificarBase64(String textoCodificado) {

        byte[] str = Base64.decode(textoCodificado, Base64.DEFAULT);

        return new String(str);
    }
}
