package cursoandroid.whatsappandroid.com.br.helper;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class Permissao {

    public static boolean validaPermissao(int requestCode, Activity activity, String[] permissoes) {

        if (Build.VERSION.SDK_INT >= 23) {

            List<String> listaPermiossoes = new ArrayList<String>();

            for (String permissao : permissoes) {

                boolean validado = ContextCompat.checkSelfPermission(activity, permissao) == PackageManager.PERMISSION_GRANTED;
                if (!validado) listaPermiossoes.add(permissao);
            }

            if (listaPermiossoes.isEmpty())
                return true;

            String[] aux = new String[ listaPermiossoes.size() ];
            listaPermiossoes.toArray(aux);

            ActivityCompat.requestPermissions(activity, aux, requestCode);
        }

        return true;
    }
}
