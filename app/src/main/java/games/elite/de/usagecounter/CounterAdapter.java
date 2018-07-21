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

import java.text.MessageFormat;
import java.util.List;

class CounterAdapter extends RecyclerView.Adapter<CounterAdapter.CounterViewHolder> {

    private final List<Counter> counters;

    public CounterAdapter(List<Counter> counters) {
        super();
        this.counters = counters;
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
                Intent intent = new Intent(v.getContext(), DetailActivity.class);
                intent.putExtra(MainActivity.EXTRA_INDEX, counter.getId());
                intent.putExtra(MainActivity.EXTRA_TEXT, counter.getText());
                intent.putExtra(MainActivity.EXTRA_COUNT, counter.getCount());
                ((Activity) v.getContext()).startActivityForResult(intent, 0, null);
                return true;
            }
        });
        holder.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter.add();
                holder.textView.setText(counter.getFormatted());
            }
        });

        holder.removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter.remove();
                holder.textView.setText(counter.getFormatted());
            }
        });
        holder.textView.setText(counter.getFormatted());
    }


    @Override
    public int getItemCount() {
        return counters.size();
    }

    public class CounterViewHolder extends RecyclerView.ViewHolder {

        private final TextView textView;
        private final Button addButton;
        private final Button removeButton;

        public CounterViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.usage_text);
            addButton = itemView.findViewById(R.id.usage_add);
            removeButton = itemView.findViewById(R.id.usage_remove);
        }
    }
}
