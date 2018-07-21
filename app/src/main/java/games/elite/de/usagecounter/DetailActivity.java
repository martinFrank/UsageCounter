package games.elite.de.usagecounter;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class DetailActivity extends AppCompatActivity {


    private int pk = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        pk = getIntent().getIntExtra(MainActivity.EXTRA_INDEX, -1);
        final String text = getIntent().getStringExtra(MainActivity.EXTRA_TEXT);
        final int count = getIntent().getIntExtra(MainActivity.EXTRA_COUNT, 0);

        EditText editText = findViewById(R.id.textUpdate);
        editText.setText(text);

        EditText editCount = findViewById(R.id.counterUpdate);
        editCount.setText(Integer.toString(count));

        Button deleteButton = findViewById(R.id.delete);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = createIntent();
                returnIntent.putExtra(MainActivity.EXTRA_OPERATION, MainActivity.OPERATION_DELETE);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });

        Button updateButton = findViewById(R.id.update);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = createIntent();
                returnIntent.putExtra(MainActivity.EXTRA_OPERATION, MainActivity.OPERATION_UPDATE);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });


    }

    private Intent createIntent() {
        EditText editText = findViewById(R.id.textUpdate);
        String text = editText.getText().toString();

        EditText editCount = findViewById(R.id.counterUpdate);
        int count = Integer.parseInt(editCount.getText().toString());

        Intent intent = new Intent();
        intent.putExtra(MainActivity.EXTRA_INDEX, pk);
        intent.putExtra(MainActivity.EXTRA_TEXT, text);
        intent.putExtra(MainActivity.EXTRA_COUNT, count);
        return  intent;
    }


}
