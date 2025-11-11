package dam.projecte.pj6;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class PJ6Main extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pj6main);

        Intent intent = getIntent();
        String missatge = intent.getStringExtra("MISSATGE_CLAU");

        TextView textView = findViewById(R.id.textResult);
        textView.setText(missatge);
    }
}
