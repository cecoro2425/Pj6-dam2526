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

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;
import android.util.Log;


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
    //Tiempo
    private long tiempoInicio;

    protected void onCreate(Bundle savedInstanceState) {
       /*tiempo*/ tiempoInicio = System.currentTimeMillis();
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

            long tiempoFin = System.currentTimeMillis();
            long tiempoTotal = (tiempoFin - tiempoInicio) / 1000; // segundos(TIEMPO)
            textResultat.setText("¡Has completado todos los juegos!");
            guardarPuntuacion(tiempoTotal);
            //mostrarPuntuacionesConsola();
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
       private void guardarPuntuacion(long tiempo) {
        /*
        Puntuacions utilitatDB =
                new Puntuacions(getBaseContext());

        SQLiteDatabase db = utilitatDB.getWritableDatabase();

        ContentValues valors = new ContentValues();
        valors.put(PuntuacionsContract.TaulaPuntuacio.COLUMNA_TEMPS, tiempo);
        valors.put(PuntuacionsContract.TaulaPuntuacio.COLUMNA_DATA,
                String.valueOf(System.currentTimeMillis()));

        db.insert(
                PuntuacionsContract.TaulaPuntuacio.NOM_TAULA,
                PuntuacionsContract.TaulaPuntuacio.COLUMNA_NULL,
                valors
        );
    }


    private void mostrarPuntuacionesConsola(){
        Puntuacions utilitatDB = new Puntuacions(getBaseContext());
        SQLiteDatabase db = utilitatDB.getReadableDatabase();

        String[] projeccio = {
                PuntuacionsContract.TaulaPuntuacio._ID,
                PuntuacionsContract.TaulaPuntuacio.COLUMNA_TEMPS,
                PuntuacionsContract.TaulaPuntuacio.COLUMNA_DATA
        };

        Cursor cursor = db.query(
                PuntuacionsContract.TaulaPuntuacio.NOM_TAULA, // tabla
                projeccio,                                  // columnas
                null,                                       // WHERE
                null,                                       // args
                null,                                       // GROUP BY
                null,                                       // HAVING
                null                                        // ORDER
        );

        while (cursor.moveToNext()) {

            long temps = cursor.getLong(cursor.getColumnIndexOrThrow(PuntuacionsContract.TaulaPuntuacio.COLUMNA_TEMPS));

            String data = cursor.getString(cursor.getColumnIndexOrThrow(PuntuacionsContract.TaulaPuntuacio.COLUMNA_DATA));

            Log.i("PUNTUACIO", "Temps: " + temps + " segons | Data: " + data);
        }

        cursor.close();*/
    }

}
