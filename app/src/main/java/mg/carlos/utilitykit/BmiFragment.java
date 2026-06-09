package mg.carlos.utilitykit;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

public class BmiFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bmi, container, false);

        EditText editPoids  = view.findViewById(R.id.edit_poids);
        EditText editTaille = view.findViewById(R.id.edit_taille);
        Button btnCalc    = view.findViewById(R.id.btn_calc_bmi);
        CardView cardResult = view.findViewById(R.id.card_result);
        TextView tvValue    = view.findViewById(R.id.tv_bmi_value);
        TextView tvCategory = view.findViewById(R.id.tv_bmi_category);
        TextView tvAdvice   = view.findViewById(R.id.tv_bmi_advice);

        btnCalc.setOnClickListener(v -> {
            String sPoids  = editPoids.getText().toString().trim();
            String sTaille = editTaille.getText().toString().trim();
            if (sPoids.isEmpty() || sTaille.isEmpty()) return;

            double poids  = Double.parseDouble(sPoids);
            double taille = Double.parseDouble(sTaille) / 100.0;
            double bmi    = poids / (taille * taille);

            tvValue.setText(String.format("%.1f", bmi));

            String category, advice, color;
            if (bmi < 18.5) {
                category = "Maigreur";
                advice   = "Pensez à consulter un médecin.";
                color    = "#E8A838";
            } else if (bmi < 25.0) {
                category = "Poids normal";
                advice   = "Continuez comme ça !";
                color    = "#4CAF50";
            } else if (bmi < 30.0) {
                category = "Surpoids";
                advice   = "Une activité physique régulière aide.";
                color    = "#E8A838";
            } else {
                category = "Obésité";
                advice   = "Consultez un professionnel de santé.";
                color    = "#E53935";
            }

            tvCategory.setText(category);
            tvCategory.setTextColor(Color.parseColor(color));
            tvAdvice.setText(advice);
            cardResult.setVisibility(View.VISIBLE);
        });

        return view;
    }
}
