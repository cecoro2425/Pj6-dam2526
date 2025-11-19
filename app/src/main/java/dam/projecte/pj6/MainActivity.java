package dam.projecte.pj6;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inicio);
    }

    public void enviarMissatge(View view) {
        Intent intent = new Intent(this, PJ6Main.class);
        intent.putExtra("MISSATGE_CLAU", "¡Que comience el juego!");
        startActivity(intent);
    }

    public void mostrarAyuda(View view){
        Intent intent = new Intent(this, AyudaActivity.class);
        startActivity(intent);
    }
}