package games.elite.de.usagecounter;


import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

class CounterAdapter extends RecyclerView.Adapter<CounterAdapter.CounterViewHolder> {

    private enum PushType {INCREASE, DECREASE}

    private final List<Counter> counters;
    private final DbHook dbHook;

    CounterAdapter(List<Counter> counters, DbHook dbHook) {
        super();
        this.counters = counters;
        this.dbHook = dbHook;
    }

    @NonNull
    @Override
    public CounterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.usage, parent, false);
        return new CounterViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final CounterViewHolder holder, final int position) {
        final Counter counter = counters.get(position);
        holder.textView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Activity activity = ((Activity) v.getContext());
                return openEditorActivity(activity, counter);
            }
        });
        holder.increaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleButtonPushEvent(PushType.INCREASE, counter, holder.textView);
            }
        });

        holder.decreaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleButtonPushEvent(PushType.DECREASE, counter, holder.textView);
            }
        });
        holder.textView.setText(counter.getFormatted());
    }

    private void handleButtonPushEvent(PushType eventType, Counter counter, TextView textView) {
        int delta = 0;
        switch (eventType) {
            case DECREASE: {
                delta = counter.decrease();
                break;
            }
            case INCREASE: {
                delta = counter.increase();
                break;
            }
        }
        dbHook.addTimeStamp(counter.getId(), delta);
        textView.setText(counter.getFormatted());
    }

    private boolean openEditorActivity(Activity activity, Counter counter) {
        Intent intent = new Intent(activity, DetailActivity.class);
        intent.putExtra(MainActivity.EXTRA_ID, counter.getId());
        intent.putExtra(MainActivity.EXTRA_TEXT, counter.getText());
        intent.putExtra(MainActivity.EXTRA_COUNT, counter.getCount());
        activity.startActivityForResult(intent, 0, null);
        return true;
    }


    @Override
    public int getItemCount() {
        return counters.size();
    }

    class CounterViewHolder extends RecyclerView.ViewHolder {

        private final TextView textView;
        private final Button increaseButton;
        private final Button decreaseButton;

        CounterViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.counter_text);
            increaseButton = itemView.findViewById(R.id.button_increase_count);
            decreaseButton = itemView.findViewById(R.id.button_decrease_count);
        }
    }
}
