package takuma.idei.ideiapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class Dashboard extends Fragment implements View.OnClickListener{
    private EditText editText;
    private ImageButton button;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        editText = getView().findViewById(R.id.edittext);
        editText.setText("440");
        button = getView().findViewById(R.id.sendButton);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputHz = editText.getText().toString();
                double hz = Double.parseDouble(inputHz);

                double A = hz;
                double D = A / 3 * 2;
                double G = D / 3 * 2 * 2;
                double C = G / 3 * 2;
                double F = C / 3 * 2 * 2;
                //ラレソドファ
                double E = A / 4 * 3;
                double B = E / 4 * 3 * 2;


                TextView textView = getView().findViewById(R.id.textView);
                textView.setText(
                        "\nC = " + C + "hz" +
                                "\nD = " + D + "hz" + "\nE = " + E + "hz" + "\nF = " + F + "hz" +
                                "\nG = " + G + "hz" + "\nA = " + A + "hz" + "\nB = " + B + "hz");
            }
        });
    }

    public void onClick(View view) {
    }

}

