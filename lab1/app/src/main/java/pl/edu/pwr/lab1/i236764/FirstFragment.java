package pl.edu.pwr.lab1.i236764;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

public class FirstFragment extends Fragment {

    private BMI bmiResult;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EditText mass = view.findViewById(R.id.massInput);
        EditText height = view.findViewById(R.id.heightInput);
        TextView result = view.findViewById(R.id.result);
        view.findViewById(R.id.button_first).setOnClickListener(view12 -> {
            if(isValid(mass, height)) {
                bmiResult = new BMI(Double.parseDouble(mass.getText().toString()), Double.parseDouble(height.getText().toString()));
                result.setTextColor(getResources().getColor(bmiResult.getAppropriateColor()));
                result.setText(String.format("%s%.2f", getString(R.string.bmi_result), bmiResult.getBmi()));
            }
            else{
                result.setText(R.string.invalidErr);
            }
        });

        view.findViewById(R.id.result).setOnClickListener(view1 -> {
            if(result.getText() != null) {
                FirstFragmentDirections.ActionFirstFragmentToSecondFragment action =
                        FirstFragmentDirections.actionFirstFragmentToSecondFragment();
                action.setBmiValue((float) bmiResult.getBmi());
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(action);
            }
            });
    }

    private boolean isValid(EditText mass, EditText height) {
        try {
            double m = Double.parseDouble(mass.getText().toString());
            double h = Double.parseDouble(height.getText().toString());
            if(m<3||m>300 || h<50 || h>250){
                return false;
            }
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

}