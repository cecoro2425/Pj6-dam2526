package dam.projecte.pj6;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Set;


public class PJ6Main extends AppCompatActivity {

    // ArrayList de juegos después de lectura XML
    private ArrayList<String> llistaJocs;

    private ImageView imatgeJoc;
    private ImageView imatgeStar;

    private int indexActual = 0;
    private int intentoJugada = 0;
    private TextInputEditText inputNom;
    private TextView textResultat, textHint;
    private Button btnConfirmar;
    private Button btnCalendari;
    private Button btnTornarJoc;

    private List<Integer> indiceRandom = new ArrayList<>();

    private List<LecturaXMLUtility.Juego> llistaJuegos = null;
    //Tiempo
    private long tiempoInicio;

    private long tiempoTotal;

    //Variables de calendario
    private static final int PERMISSIONS_REQUEST_READ_CALENDAR = 100;

    private ContentResolver contentResolver;
    private Set<String> calendaris = new HashSet<String>();
    private List<String> events = new ArrayList<String>();

    public void pressBtnCalendari(View view){
        //obtenirCalendaris();
        //Log.i(getClass().getName(), calendaris.toString());
        //registrarCalendario();
        mostrarSelectorCalendario();
    }


    protected void onCreate(Bundle savedInstanceState) {
        //Permisos de calendario
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_CALENDAR)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_CALENDAR},
                        PERMISSIONS_REQUEST_READ_CALENDAR);
            }
        }
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CALENDAR)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_CALENDAR},
                        PERMISSIONS_REQUEST_READ_CALENDAR);
            }
        }

        contentResolver = getContentResolver();

        for(int i = 0;i<6;){
            int numero = (int)Math.floor(Math.random()*10);
            if (indiceRandom.isEmpty()){
                indiceRandom.add(numero);
                i++;
            }
            if (!indiceRandom.contains(numero)) {
                indiceRandom.add(numero);
                i++;

            }
        }

        System.out.println(indiceRandom);

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
        btnCalendari = findViewById(R.id.btnCalendari);
        btnTornarJoc = findViewById(R.id.btnTornarJoc);



        // Mostrar primera imagen
        //imatgeJoc.setImageResource(imatgesJoc[indexActual]);
        Glide.with(this).load(llistaJuegos.get(indiceRandom.get(indexActual)).getImagenes().get(intentoJugada))
                .placeholder(R.drawable.loading)
                .error(R.drawable.error)
                .into(imatgeJoc);

        cambiarEstrella();
        // Botón que verifica la respuesta
        btnConfirmar.setOnClickListener(v -> comprovarResposta());
    }

    private void cambiarEstrella(){
        int[] estrellas = {
                R.id.intents1,
                R.id.intents2,
                R.id.intents3,
                R.id.intents4,
                R.id.intents5
        };

        for (int i = 0; i < estrellas.length; i++) {
            ImageView estrella = findViewById(estrellas[i]);

            if (i < intentoJugada) {
                estrella.setImageResource(android.R.drawable.btn_star_big_on);
            } else {
                estrella.setImageResource(android.R.drawable.btn_star_big_off);
            }
        }
    }

    private void avanzarJuego() {
        indexActual++;
        intentoJugada = 0;

        //prueba de random
        //if (indexActual >= llistaJuegos.size()) {
        if (indexActual >= indiceRandom.size()) {
                long tiempoFin = System.currentTimeMillis();
                tiempoTotal = (tiempoFin - tiempoInicio) / 1000; // segundos(TIEMPO)
                textResultat.setText("¡Has completado todos los juegos!");
                btnConfirmar.setEnabled(false);
                btnTornarJoc.setVisibility(TextView.VISIBLE);
                btnCalendari.setVisibility(TextView.VISIBLE);
                guardarPuntuacion(tiempoTotal);
                mostrarPuntuacionesConsola();
                return;
        }

        Glide.with(this).load(llistaJuegos.get(indiceRandom.get(indexActual)).getImagenes().get(intentoJugada))
                .placeholder(R.drawable.loading)
                .error(R.drawable.error)
                .into(imatgeJoc);
        cambiarEstrella();
        inputNom.setText("");
        textHint.setText("");
    }


    private void comprovarResposta() {
        String respostaUsuari = inputNom.getText().toString().trim();

        if (respostaUsuari.equalsIgnoreCase(llistaJuegos.get(indiceRandom.get(indexActual)).getNombre())) {
            textResultat.setText("¡Correcto!");
            intentoJugada = 0;
            avanzarJuego();
        } else {
            if(intentoJugada < 4){
                textResultat.setText("Incorrecto");
                textHint.setText(llistaJuegos.get(indiceRandom.get(indexActual)).getPistas().get(intentoJugada));
                if(intentoJugada<4) intentoJugada++;
                Glide.with(this).load(llistaJuegos.get(indiceRandom.get(indexActual)).getImagenes().get(intentoJugada))
                        .placeholder(R.drawable.loading)
                        .error(R.drawable.error)
                        .into(imatgeJoc);
                cambiarEstrella();

            }else{
                textResultat.setText("Has perdido esta ronda");
                textHint.setText("La respuesta era: " + llistaJuegos.get(indiceRandom.get(indexActual)).getNombre());
                intentoJugada = 0;
                avanzarJuego();
            }

        }
    }

    // BASE DE DATOS - Guardar puntuaciones
       private void guardarPuntuacion(long tiempo) {

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

        cursor.close();
    }


    // IMPLEMENTACIÓN DE CALENDARIO

    private void registrarCalendario(){
        ContentValues esdeveniment = new ContentValues();
        esdeveniment.put(CalendarContract.Events.CALENDAR_ID, obtenirCalendariPerId(getContentResolver(), "cocolobo21@outlook.com")); // Tipus de calendari
        esdeveniment.put(CalendarContract.Events.TITLE, "¡Nueva puntuación! -" + tiempoTotal);
        esdeveniment.put(CalendarContract.Events.DTSTART, Calendar.getInstance().getTimeInMillis());
        esdeveniment.put(CalendarContract.Events.DTEND, Calendar.getInstance().getTimeInMillis()+60*60*1000);
        esdeveniment.put(CalendarContract.Events.EVENT_TIMEZONE, "Europe/Madrid");


        Uri uri = contentResolver.insert(CalendarContract.Events.CONTENT_URI, esdeveniment);

        // La URI conté el contentProvider i retorna el id del event creat
        int id = Integer.parseInt(uri.getLastPathSegment());
        Toast.makeText(getApplicationContext(), "Esdeveniment creat amb codi" + id,
                Toast.LENGTH_SHORT).show();
        obtenirCalendaris();
    }

    private void registrarCalendarioConId(int calendarioId) {
        ContentValues esdeveniment = new ContentValues();
        esdeveniment.put(CalendarContract.Events.CALENDAR_ID, calendarioId);
        esdeveniment.put(CalendarContract.Events.TITLE, "¡Nueva puntuación! - " + tiempoTotal);
        esdeveniment.put(CalendarContract.Events.DTSTART, System.currentTimeMillis());
        esdeveniment.put(CalendarContract.Events.DTEND, System.currentTimeMillis() + 60 * 60 * 1000);
        esdeveniment.put(CalendarContract.Events.EVENT_TIMEZONE, "Europe/Madrid");

        Uri uri = contentResolver.insert(CalendarContract.Events.CONTENT_URI, esdeveniment);

        if (uri != null) {
            Toast.makeText(this, "Evento creado correctamente", Toast.LENGTH_SHORT).show();
        }
    }

    private void obtenirCalendaris() {
        //la URI dels calendaris és content://com.android.calendar/calendars
        Uri uri = CalendarContract.Calendars.CONTENT_URI;
        String[] projeccio = {CalendarContract.Calendars.NAME,
                CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,
                CalendarContract.Calendars.CALENDAR_COLOR,
                CalendarContract.Calendars.VISIBLE};
        Cursor cursor = contentResolver.query(uri, projeccio, null, null, null);

        try {
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    String nomIntern = cursor.getString(0);
                    String nomMostrat = cursor.getString(1);
                    @SuppressLint("Range") String color = cursor.getString(cursor.getColumnIndex(CalendarContract.Calendars.CALENDAR_COLOR));
                    Boolean seleccionat = !cursor.getString(3).equals("0");
                    calendaris.add(nomMostrat);
                }
            }
        } catch (AssertionError ex) {
        }
    }

    @SuppressLint("Range")
    private int obtenirCalendariPerId(ContentResolver contentResolver, String calendarName) {
        Uri uri = CalendarContract.Calendars.CONTENT_URI;
        String[] projection = new String[]{
                CalendarContract.Calendars._ID,
                CalendarContract.Calendars.CALENDAR_DISPLAY_NAME
        };
        String selection = CalendarContract.Calendars.CALENDAR_DISPLAY_NAME + " = ?";
        String[] selectionArgs = new String[]{calendarName};

        Cursor cursor = contentResolver.query(uri, projection, selection, selectionArgs, null);
        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    return cursor.getInt(cursor.getColumnIndex(CalendarContract.Calendars._ID));
                }
            } finally {
                cursor.close();
            }
        }
        return -1; // Return -1 if calendar not found
    }

    private void mostrarSelectorCalendario() {
        calendaris.clear();
        obtenirCalendaris();

        if (calendaris.isEmpty()) {
            Toast.makeText(this, "No se encontraron calendarios", Toast.LENGTH_SHORT).show();
            return;
        }

        // Convertimos el Set a array SOLO para mostrarlo
        String[] nombresCalendarios = calendaris.toArray(new String[0]);

        new AlertDialog.Builder(this)
                .setTitle("Elige un calendario")
                .setItems(nombresCalendarios, (dialog, which) -> {

                    String nombreElegido = nombresCalendarios[which];

                    int calendarioId = obtenirCalendariPerId(contentResolver, nombreElegido);

                    if (calendarioId != -1) {
                        registrarCalendarioConId(calendarioId);
                    } else {
                        Toast.makeText(this,
                                "No se pudo obtener el calendario",
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .show();
    }
}
