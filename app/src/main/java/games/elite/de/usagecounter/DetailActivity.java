package games.elite.de.usagecounter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.Date;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    private enum ButtonType {UPDATE, DELETE}
    private DatabaseAdapter databaseAdapter;
    private int id = -1;
    private int count = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        id = getIntent().getIntExtra(MainActivity.EXTRA_ID, -1);
        final String text = getIntent().getStringExtra(MainActivity.EXTRA_TEXT);
        count = getIntent().getIntExtra(MainActivity.EXTRA_COUNT, 0);

        //let's assert the db has already been opened on MainActivity.onCreate
//        databaseAdapter = new DatabaseAdapter(getApplicationContext());
        setupDB();
        List<TimeStamp> timeStamps = databaseAdapter.getAllTimeStampsByCounterId(id);
        calculateAbsoluteCounts(timeStamps);

        GraphView graphView = findViewById(R.id.graph);
        graphView.removeAllSeries();
//        graphView.getViewport().setXAxisBoundsManual(true);
//        graphView.getViewport().setScalableY(true);
//        graphView.getViewport().setScrollable(true);
        graphView.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(this));
        graphView.getGridLabelRenderer().setNumHorizontalLabels(3); // only 4 because of the space
        graphView.getGridLabelRenderer().setHumanRounding(false);


        LineGraphSeries<DataPoint> series = createLineGraphFrom(timeStamps);
        graphView.addSeries(series);
        graphView.getViewport().calcCompleteRange();
//        graphView.computeScroll();

        EditText editText = findViewById(R.id.textUpdate);
        editText.setText(text);

        EditText editCount = findViewById(R.id.counterUpdate);
        editCount.setText(Integer.toString(count));

        Button deleteButton = findViewById(R.id.delete);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleButtonPushEvents(ButtonType.DELETE);
            }
        });

        Button updateButton = findViewById(R.id.update);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleButtonPushEvents(ButtonType.UPDATE);
            }
        });
    }



    private void setupDB() {
        databaseAdapter = new DatabaseAdapter(getApplicationContext());
        databaseAdapter = databaseAdapter.open();
    }

    private void handleButtonPushEvents(ButtonType buttonType) {
        Intent returnIntent = new Intent();
        updateIntendValues(returnIntent);
        String operation = getOperation(buttonType);
        insertDeltaIfRequired();
        returnIntent.putExtra(MainActivity.EXTRA_OPERATION, operation);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    private void insertDeltaIfRequired() {
        int delta = calculateDelta();
        if(delta != 0){
            databaseAdapter.insertTimeStamp( new TimeStamp(id, delta, System.currentTimeMillis()));
        }
    }

    private int calculateDelta() {
        int newCountValue = getCountFromInputField();
        return newCountValue - count;
    }

    private int getCountFromInputField() {
        EditText editCount = findViewById(R.id.counterUpdate);
        return Integer.parseInt(editCount.getText().toString());
    }

    private String getOperation(ButtonType buttonType) {
        switch (buttonType) {
            case DELETE:
                return MainActivity.OPERATION_DELETE;
            case UPDATE:
                return MainActivity.OPERATION_UPDATE;
            default:
                return "";
        }
    }


    private void calculateAbsoluteCounts(List<TimeStamp> timeStamps) {
        int before = 0;
        int after;
        for (TimeStamp timeStamp: timeStamps){
            after = before + timeStamp.getDelta();
            timeStamp.setBefore(before);
            timeStamp.setAfter(after);
            before = after;
        }
    }

    private LineGraphSeries<DataPoint> createLineGraphFrom(List<TimeStamp> timeStamps) {
        DataPoint[] dPoints = new DataPoint[timeStamps.size()+1]; //one addition 'now' value
        int lastValue = 0;
        for(int i = 0; i < timeStamps.size(); i ++){
            lastValue = timeStamps.get(i).getAfter();
            dPoints[i] = new DataPoint(new Date(timeStamps.get(i).getTime()), lastValue);
        }
        dPoints[timeStamps.size()] = new DataPoint(new Date(System.currentTimeMillis()),lastValue);
        return new LineGraphSeries<>(dPoints);
    }

    private void updateIntendValues(Intent intent) {
        EditText editText = findViewById(R.id.textUpdate);
        String text = editText.getText().toString();

        int count = getCountFromInputField();

        intent.putExtra(MainActivity.EXTRA_ID, id);
        intent.putExtra(MainActivity.EXTRA_TEXT, text);
        intent.putExtra(MainActivity.EXTRA_COUNT, count);
    }


}
