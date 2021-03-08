package pl.edu.pwr.lab1.i236764;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import static pl.edu.pwr.lab1.i236764.R.string.info;

public class MainActivity extends AppCompatActivity {

    static Boolean metricsInKgCm = true;
    EditText mass;
    EditText height;
    TextView result;
    TextView massLabel;
    TextView heightLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        metricsInKgCm = true;
         mass   = (EditText)findViewById(R.id.massInput);
         height = (EditText)findViewById(R.id.heightInput);
         result = (TextView)findViewById(R.id.result);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> Snackbar.make(view, info, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        massLabel   = (TextView)findViewById(R.id.massLabel);
        heightLabel = (TextView)findViewById(R.id.heightLabel);
        if (id == R.id.action_switch) {
            if (metricsInKgCm) {
                massLabel.setText(R.string.mass_lbs);
                heightLabel.setText(R.string.height_in);
                metricsInKgCm = false;
            }
            else {
                massLabel.setText(R.string.mass_kg);
                heightLabel.setText(R.string.height_cm);
                metricsInKgCm = true;
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}