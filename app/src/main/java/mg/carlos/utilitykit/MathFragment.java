package mg.carlos.utilitykit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MathFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_math, container, false);

        EditText editNombre = view.findViewById(R.id.edit_nombre);
        Button btnShow = view.findViewById(R.id.btn_show_table);
        TextView tvTitle = view.findViewById(R.id.tv_table_title);
        RecyclerView recycler = view.findViewById(R.id.recycler_table);

        recycler.setLayoutManager(new LinearLayoutManager(getContext()));

        btnShow.setOnClickListener(v -> {
            String input = editNombre.getText().toString().trim();
            if (input.isEmpty()) return;

            int nombre = Integer.parseInt(input);

            // Build rows 1–10
            List<String[]> rows = new ArrayList<>();
            for (int i = 1; i <= 10; i++) {
                rows.add(new String[]{
                        nombre + " × " + i + " =",
                        String.valueOf(nombre * i)
                });
            }

            tvTitle.setText("Table de " + nombre);
            tvTitle.setVisibility(View.VISIBLE);
            recycler.setVisibility(View.VISIBLE);

            recycler.setAdapter(new RecyclerView.Adapter<RecyclerView.ViewHolder>() {
                @NonNull @Override
                public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    View v = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.item_table_row, parent, false);
                    return new RecyclerView.ViewHolder(v) {};
                }

                @Override
                public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                    ((TextView) holder.itemView.findViewById(R.id.tv_operation))
                            .setText(rows.get(position)[0]);
                    ((TextView) holder.itemView.findViewById(R.id.tv_result))
                            .setText(rows.get(position)[1]);
                }

                @Override public int getItemCount() { return rows.size(); }
            });
        });

        return view;
    }
}