package dam.projecte.pj6;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inicio);

    }

    public void onClick(View arg0){ mostrarAyuda(arg0);}

    public void mostrarAyuda(View view){
        Intent intent = new Intent(this, AyudaActivity.class);
        startActivity(intent);
    }
}