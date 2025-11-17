package dam.projecte.pj6;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class PJ6Main extends AppCompatActivity {

    private ImageView imatgeJoc;

    private int[] imatgesJoc = {
            R.drawable.armoredcore,
            R.drawable.deltarune,
            R.drawable.eldenring,
            R.drawable.league
    };

    private String[] nomsCorrectes = {
            "Armored Core",
            "Deltarune",
            "Elden Ring",
            "League of Legends"
    };
    private int indexActual = 0;
    private TextInputEditText inputNom;
    private TextView textResultat, textHint;
    private Button btnConfirmar;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pj6main);

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
        imatgeJoc.setImageResource(imatgesJoc[indexActual]);

        // Botón que verifica la respuesta
        btnConfirmar.setOnClickListener(v -> comprovarResposta());
    }

    private void avanzarJuego() {
        indexActual++;

        if (indexActual >= imatgesJoc.length) {
            textResultat.setText("¡Has completado todos los juegos!");
            return;
        }

        imatgeJoc.setImageResource(imatgesJoc[indexActual]);
        inputNom.setText("");
        textHint.setText("");
    }

    private void mostrarPista() {
        switch(indexActual) {
            case 0:
                textHint.setText("Pista: Es un fontanero.");
                break;
            case 1:
                textHint.setText("Pista: Mundo hecho de cubos.");
                break;
            case 2:
                textHint.setText("Pista: En un reino muy lejano.");
                break;
            case 3:
                textHint.setText("Pista: Ciudad, crimen y coches.");
                break;
            default:
                textHint.setText("Sin pistas.");
                break;
        }
    }
    private void comprovarResposta() {
        String respostaUsuari = inputNom.getText().toString().trim();

        if (respostaUsuari.equalsIgnoreCase(nomsCorrectes[indexActual])) {
            textResultat.setText("¡Correcto!");
            avanzarJuego();
        } else {
            textResultat.setText("Incorrecto");
            mostrarPista();
        }
    }
}
