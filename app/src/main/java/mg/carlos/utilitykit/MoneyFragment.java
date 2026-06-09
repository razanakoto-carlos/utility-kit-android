package mg.carlos.utilitykit;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class MoneyFragment extends Fragment {

    private static final double BASE_RATE = 4800.0; // 1 EUR = 4800 Ar
    private double currentRate = BASE_RATE;
    private boolean isUpdating = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_money, container, false);

        EditText editTop      = view.findViewById(R.id.edit_top);
        EditText editBottom   = view.findViewById(R.id.edit_bottom);
        Spinner  spinnerTop   = view.findViewById(R.id.spinner_top);
        Spinner  spinnerBottom= view.findViewById(R.id.spinner_bottom);
        TextView tvRate       = view.findViewById(R.id.text_rate_display);
        TextView tvRateValue  = view.findViewById(R.id.text_rate_value);
        TextView iconTop      = view.findViewById(R.id.icon_top);
        TextView iconBottom   = view.findViewById(R.id.icon_bottom);
        SeekBar  seekBar      = view.findViewById(R.id.seekbar_rate);
        ImageButton btnSwap   = view.findViewById(R.id.btn_swap);

        String[] currencies = {"EUR", "MGA"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getContext(), android.R.layout.simple_spinner_item, currencies);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTop.setAdapter(adapter);
        spinnerBottom.setAdapter(adapter);

        // Default: top = EUR, bottom = MGA
        spinnerTop.setSelection(0);
        spinnerBottom.setSelection(1);

        // Auto-swap bottom when top changes
        spinnerTop.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, View v, int pos, long id) {
                if (isUpdating) return;
                isUpdating = true;
                // bottom is always the opposite
                spinnerBottom.setSelection(pos == 0 ? 1 : 0);
                updateIcons(iconTop, iconBottom, spinnerTop, spinnerBottom);
                updateRate(tvRate, tvRateValue, spinnerTop);
                convertTopToBottom(editTop, editBottom, spinnerTop);
                isUpdating = false;
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });

        spinnerBottom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, View v, int pos, long id) {
                if (isUpdating) return;
                isUpdating = true;
                spinnerTop.setSelection(pos == 0 ? 1 : 0);
                updateIcons(iconTop, iconBottom, spinnerTop, spinnerBottom);
                updateRate(tvRate, tvRateValue, spinnerTop);
                convertTopToBottom(editTop, editBottom, spinnerTop);
                isUpdating = false;
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Live conversion as user types
        editTop.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int i, int c, int a) {}
            @Override public void onTextChanged(CharSequence s, int i, int b, int c) {}
            @Override public void afterTextChanged(Editable s) {
                if (isUpdating) return;
                convertTopToBottom(editTop, editBottom, spinnerTop);
            }
        });

        editBottom.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int i, int c, int a) {}
            @Override public void onTextChanged(CharSequence s, int i, int b, int c) {}
            @Override public void afterTextChanged(Editable s) {
                if (isUpdating) return;
                convertBottomToTop(editTop, editBottom, spinnerTop);
            }
        });

        // Swap button
        btnSwap.setOnClickListener(v -> {
            isUpdating = true;
            int topPos = spinnerTop.getSelectedItemPosition();
            spinnerTop.setSelection(topPos == 0 ? 1 : 0);
            spinnerBottom.setSelection(topPos == 0 ? 0 : 1);
            updateIcons(iconTop, iconBottom, spinnerTop, spinnerBottom);
            updateRate(tvRate, tvRateValue, spinnerTop);
            // Swap values
            String tmp = editTop.getText().toString();
            editTop.setText(editBottom.getText().toString());
            editBottom.setText(tmp);
            isUpdating = false;
        });

        // SeekBar: range 3000–7000 Ar
        seekBar.setMax(4000);
        seekBar.setProgress(1800); // default ~4800
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override public void onProgressChanged(SeekBar sb, int progress, boolean fromUser) {
                currentRate = 3000 + progress;
                tvRateValue.setText(String.format("%,.0f Ar", currentRate));
                updateRate(tvRate, tvRateValue, spinnerTop);
                convertTopToBottom(editTop, editBottom, spinnerTop);
            }
            @Override public void onStartTrackingTouch(SeekBar sb) {}
            @Override public void onStopTrackingTouch(SeekBar sb) {}
        });

        // Init
        updateIcons(iconTop, iconBottom, spinnerTop, spinnerBottom);
        updateRate(tvRate, tvRateValue, spinnerTop);

        return view;
    }

    private void convertTopToBottom(EditText editTop, EditText editBottom, Spinner spinnerTop) {
        isUpdating = true;
        String input = editTop.getText().toString().trim();
        if (input.isEmpty()) { editBottom.setText(""); isUpdating = false; return; }
        try {
            double amount = Double.parseDouble(input);
            double result = spinnerTop.getSelectedItemPosition() == 0
                    ? amount * currentRate   // EUR → MGA
                    : amount / currentRate;  // MGA → EUR
            editBottom.setText(String.format("%.2f", result));
        } catch (NumberFormatException ignored) {}
        isUpdating = false;
    }

    private void convertBottomToTop(EditText editTop, EditText editBottom, Spinner spinnerTop) {
        isUpdating = true;
        String input = editBottom.getText().toString().trim();
        if (input.isEmpty()) { editTop.setText(""); isUpdating = false; return; }
        try {
            double amount = Double.parseDouble(input);
            double result = spinnerTop.getSelectedItemPosition() == 0
                    ? amount / currentRate   // MGA → EUR
                    : amount * currentRate;  // EUR → MGA
            editTop.setText(String.format("%.2f", result));
        } catch (NumberFormatException ignored) {}
        isUpdating = false;
    }

    private void updateIcons(TextView iconTop, TextView iconBottom, Spinner spinnerTop, Spinner spinnerBottom) {
        iconTop.setText(spinnerTop.getSelectedItemPosition() == 0 ? "🇪🇺" : "🇲🇬");
        iconBottom.setText(spinnerBottom.getSelectedItemPosition() == 0 ? "🇪🇺" : "🇲🇬");
    }

    private void updateRate(TextView tvRate, TextView tvRateValue, Spinner spinnerTop) {
        if (spinnerTop.getSelectedItemPosition() == 0) {
            tvRate.setText(String.format("1 EUR = %,.0f Ar", currentRate));
        } else {
            tvRate.setText(String.format("1 Ar = %.6f EUR", 1.0 / currentRate));
        }
        tvRateValue.setText(String.format("%,.0f Ar", currentRate));
    }
}