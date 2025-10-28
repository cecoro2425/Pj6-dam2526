package dam.projecte.pj6;

import android.os.Bundle;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

public class AyudaActivity extends AppCompatActivity {
        WebView navegador;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            setContentView(R.layout.ayuda);
            navegador = (WebView) findViewById(R.id.webkit);

            navegador.getSettings().setJavaScriptEnabled(true);

            //per geolocalització de HTML5
            //navegador.getSettings().setAppCacheEnabled(true);
            navegador.getSettings().setDatabaseEnabled(true);
            navegador.getSettings().setDomStorageEnabled(true);
            //navegador.getSettings().setAllowContentAccess(true);
            //navegador.getSettings().setAllowFileAccess(true);
            navegador.getSettings().setAllowFileAccess(true);
            navegador.setWebChromeClient(new WebChromeClient() {
                public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                    callback.invoke(origin, true, false);
                }
            });
            //per defecte ens obrirà Chrome, cal canviar-ho
            navegador.setWebViewClient(new WebViewClient());
            //navegador.loadUrl("https://www.fje.edu/");
            navegador.loadUrl("file:///android_asset/ayuda.html");
        }
}
