package mg.carlos.utilitykit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        View rowBmi       = view.findViewById(R.id.row_bmi);
        View rowTable     = view.findViewById(R.id.row_table);
        View rowConverter = view.findViewById(R.id.row_converter);

        rowBmi.setOnClickListener(v -> navigateTo(new BmiFragment()));
        rowTable.setOnClickListener(v -> navigateTo(new MathFragment()));
        rowConverter.setOnClickListener(v -> navigateTo(new MoneyFragment()));
    }

    private void navigateTo(Fragment fragment) {
        requireActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }
}