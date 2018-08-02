package games.elite.de.usagecounter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private CounterAdapter counterAdapter;
    private DatabaseAdapter databaseAdapter;
    private List<Counter> counters;

    public static final String EXTRA_ID = "games.elite.de.usagecounter.MainActivity.extra.id";
    public static final String EXTRA_TEXT = "games.elite.de.usagecounter.MainActivity.extra.text";
    public static final String EXTRA_COUNT = "games.elite.de.usagecounter.MainActivity.extra.count";
    public static final String EXTRA_OPERATION = "games.elite.de.usagecounter.MainActivity.extra.operation";
    public static final String OPERATION_DELETE = "DELETE";
    public static final String OPERATION_UPDATE = "UPDATE";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupDB();
        counters = databaseAdapter.getAllCounter();
        if(counters == null){
            counters = new ArrayList<>();
        }

        counterAdapter = new CounterAdapter(counters, new DbHook(databaseAdapter));

        RecyclerView recyclerView = findViewById(R.id.recycle_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(counterAdapter);
    }

    private void setupDB() {
        databaseAdapter = new DatabaseAdapter(getApplicationContext());
        databaseAdapter = databaseAdapter.open();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK){
            Counter counter = findCounter(data.getIntExtra(EXTRA_ID, -1));
            if(counter != null) {
                int updateIndex = counters.indexOf(counter);
                counter.setText(data.getStringExtra(EXTRA_TEXT));
                counter.setCount(data.getIntExtra(EXTRA_COUNT, -1));

                String operation = data.getStringExtra(EXTRA_OPERATION);
                if (OPERATION_DELETE.equals(operation)) {
                    databaseAdapter.deleteTimeStampByCounterId(counter.getId());
                    databaseAdapter.deleteCounterById(counter.getId());
                    counters.remove(counter);
                    counterAdapter.notifyItemRangeRemoved(updateIndex, 1);
                }
                if (OPERATION_UPDATE.equals(operation)) {
                    counterAdapter.notifyItemChanged(updateIndex);
                }
            }
        }
    }

    private Counter findCounter(int index) {
        for (Counter counter: counters){
            if (counter.getId() == index){
                return counter;
            }
        }
        return null;
    }

    public void addCounter(View view){
        Counter counter = new Counter("new Counter: {0}", 0);
        counter.setId ((int) databaseAdapter.insertCounter(counter));
        counters.add(counter);
        counterAdapter.notifyItemInserted(counters.size());
        TimeStamp timeStamp = new TimeStamp(counter.getId(), 0, System.currentTimeMillis());
        databaseAdapter.insertTimeStamp(timeStamp);
    }

    @Override
    protected void onStop() {
        super.onStop();
        for (Counter counter: counters){
            databaseAdapter.updateCounter(counter);
        }
    }


}
