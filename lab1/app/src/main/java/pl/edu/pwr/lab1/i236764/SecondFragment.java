package pl.edu.pwr.lab1.i236764;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

public class SecondFragment extends Fragment {

    private double bmiResult;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.fragment_second, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bmiResult = SecondFragmentArgs.fromBundle(getArguments()).getBmiValue();
        String bmiText = String.format("%.2f", SecondFragmentArgs.fromBundle(getArguments()).getBmiValue());
        TextView bmiLabel  = view.findViewById(R.id.bmiLabel);
        TextView bmiDescription = view.findViewById(R.id.bmiDescription);

        if(bmiResult < 18.5){
            bmiLabel.setText(getString(R.string.bmiConstant) + bmiText + getString(R.string.underweight));
            bmiDescription.setText(R.string.underWeightDescription);
        }
        else if(bmiResult < 25){
            bmiLabel.setText(getString(R.string.bmiConstant) + bmiText + getString(R.string.normal));
            bmiDescription.setText(R.string.normalDescription);
        }
        else{
            bmiLabel.setText(getString(R.string.bmiConstant)  + bmiText + getString(R.string.overweight));
            bmiDescription.setText(R.string.overweightDesctiption);
        }
        view.findViewById(R.id.button_second).setOnClickListener(view1 -> {
            System.out.println(bmiResult);
            NavHostFragment.findNavController(SecondFragment.this)
                    .navigate(R.id.action_SecondFragment_to_FirstFragment);
        });
    }

}