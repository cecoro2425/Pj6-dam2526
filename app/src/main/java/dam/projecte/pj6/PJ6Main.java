package dam.projecte.pj6;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class PJ6Main extends AppCompatActivity {

    // ArrayList de juegos después de lectura XML
    private ArrayList<String> llistaJocs;

    private ImageView imatgeJoc;

    private int indexActual = 0;
    private int intentoJugada = 0;
    private TextInputEditText inputNom;
    private TextView textResultat, textHint;
    private Button btnConfirmar;
    private List<LecturaXMLUtility.Juego> llistaJuegos = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pj6main);

        // INICIO PRUEBA DE LECTURA DEL XML

        LecturaXMLUtility lecturaXML = new LecturaXMLUtility();

        AssetManager am = getAssets();
        InputStream is;
        try {
            is = am.open("juegos.xml"); //Archivo ubicado en assets
            llistaJuegos = lecturaXML.analitzarXML(is);

        } catch (IOException e) {

            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }

        llistaJocs = new ArrayList<>();
        for(LecturaXMLUtility.Juego juego : llistaJuegos){
            llistaJocs.add(juego.toString());
        }

        // Muestra por consola los juegos encontrados
        System.out.println(llistaJocs);

        // FIN DE PRUEBA

        Intent intent = getIntent();
        String missatge = intent.getStringExtra("MISSATGE_CLAU");

        TextView textView = findViewById(R.id.textResult);
        textView.setText(missatge);

        imatgeJoc = findViewById(R.id.ImatgeJoc);
        inputNom = findViewById(R.id.NameInput);
        textResultat = findViewById(R.id.textResult);
        textHint = findViewById(R.id.textHint);
        btnConfirmar = findViewById(R.id.ConfirmButton);

        // Mostrar primera imagen
        //imatgeJoc.setImageResource(imatgesJoc[indexActual]);
        Glide.with(this).load(llistaJuegos.get(indexActual).getImagenes().get(intentoJugada))
                .placeholder(R.drawable.loading)
                .error(R.drawable.error)
                .into(imatgeJoc);
        // Botón que verifica la respuesta
        btnConfirmar.setOnClickListener(v -> comprovarResposta());
    }

    private void avanzarJuego() {
        indexActual++;

        if (indexActual >= llistaJuegos.size()) {
            textResultat.setText("¡Has completado todos los juegos!");
            return;
        }

        Glide.with(this).load(llistaJuegos.get(indexActual).getImagenes().get(intentoJugada)).into(imatgeJoc);
        inputNom.setText("");
        textHint.setText("");
    }


    private void comprovarResposta() {
        String respostaUsuari = inputNom.getText().toString().trim();

        if (respostaUsuari.equalsIgnoreCase(llistaJuegos.get(indexActual).getNombre())) {
            textResultat.setText("¡Correcto!");
            avanzarJuego();
        } else {
            textResultat.setText("Incorrecto");
            textHint.setText(llistaJuegos.get(indexActual).getPistas().get(intentoJugada));
            if(intentoJugada<4) intentoJugada++;
            Glide.with(this).load(llistaJuegos.get(indexActual).getImagenes().get(intentoJugada))
                    .placeholder(R.drawable.loading)
                    .error(R.drawable.error)
                    .into(imatgeJoc);
            System.out.println(intentoJugada);
        }
    }
}
