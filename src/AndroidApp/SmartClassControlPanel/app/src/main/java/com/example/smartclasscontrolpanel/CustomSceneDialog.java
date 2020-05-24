package com.example.smartclasscontrolpanel;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class CustomSceneDialog extends AppCompatDialogFragment implements View.OnClickListener {
    ViewGroup layout;
    private CustomSceneDialogListener listener;
    String seats = "__O_O_N_N_N_N_M_M__/"
            + "______P_P_Q_R______/"
            + "O_O_O_N_N_N_N_M_M_M/"
            + "______P_S_S_R______//"
            + "___________________//"
            + "A_A_A_A_A_A_A/"
            + "C_D_D_D_D_C_C/"
            + "E_F_F_F_F_E_E/"
            + "H_H_G_G_H_G_G/"
            + "J_J_I_I_J_I_I/"
            + "K_L_L_L_K_L_K/"
            + "__B_B_B_B_B__/";

    StringBuilder seatsStatus ;
    int count = 0;
    List<TextView> seatViewList = new ArrayList<>();
    int seatSize = 100;
    int seatGaping = 10;

    //    int STATUS_AVAILABLE = 1;
    int STATUS_BOOKED = 2;
    int STATUS_RESERVED = 3;
    int STATUS_OFF = 0;
    int STATUS_ON = 1;
    String selectedIds = "";

    public CustomSceneDialog(String light_state){
        seatsStatus=new StringBuilder(light_state);
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view_main = inflater.inflate(R.layout.custom_scene_dialog, null);
        builder.setView(view_main)
                .setTitle("Custom Scene Selection")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.applyScene(seatsStatus.toString());
                    }
                });
        layout = view_main.findViewById(R.id.layoutSeat);

        seats = "/" + seats;

        LinearLayout layoutSeat = new LinearLayout(getContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutSeat.setOrientation(LinearLayout.VERTICAL);
        layoutSeat.setLayoutParams(params);
        layoutSeat.setPadding(8 * seatGaping, 8 * seatGaping, 8 * seatGaping, 8 * seatGaping);
        layout.addView(layoutSeat);

        LinearLayout layout = null;

        count = 0;
//        int count = 0;

        for (int index = 0; index < seats.length(); index++) {
            if (seats.charAt(index) == '/') {
                layout = new LinearLayout(getContext());
                layout.setOrientation(LinearLayout.HORIZONTAL);
                layoutSeat.addView(layout);
            } else if (seats.charAt(index) >= 'A' && seats.charAt(index) <= 'L') {
                count++;
                TextView view = new TextView(getContext());
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(seatSize, seatSize);
                layoutParams.setMargins(seatGaping, seatGaping, seatGaping, seatGaping);
                view.setLayoutParams(layoutParams);
                view.setPadding(0, 0, 0, 2 * seatGaping);
                view.setId(count);
                view.setGravity(Gravity.CENTER);
                if (seatsStatus.charAt(seats.charAt(index) - 'A') == '0')
                    view.setBackgroundResource(R.drawable.square_light_off);
                else
                    view.setBackgroundResource(R.drawable.square_light_on);
                view.setTag(seats.charAt(index));
                view.setTextColor(Color.BLACK);
                view.setText(count + "");
                view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 9);
                layout.addView(view);
                seatViewList.add(view);
                view.setOnClickListener(this);
            } else if (seats.charAt(index) >= 'M' && seats.charAt(index) <= 'O') {
                count++;
                TextView view = new TextView(getContext());
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(seatSize - 20, seatSize - 20, 1.0f);
                layoutParams.setMargins(seatGaping, seatGaping, seatGaping, seatGaping);
                view.setLayoutParams(layoutParams);
                view.setPadding(0, 0, 0, 2 * seatGaping);
                view.setId(count);
                view.setGravity(Gravity.CENTER);
                if (seatsStatus.charAt(seats.charAt(index) - 'A') == '0')
                    view.setBackgroundResource(R.drawable.round_light_off);
                else
                    view.setBackgroundResource(R.drawable.round_light_on);
                view.setTag(seats.charAt(index));
                view.setTextColor(Color.BLACK);
                view.setText(count + "");
                view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 9);
                layout.addView(view);
                seatViewList.add(view);
                view.setOnClickListener(this);
            } else if (seats.charAt(index) >= 'P' && seats.charAt(index) <= 'S') {
                count++;
                TextView view = new TextView(getContext());
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(seatSize - 20, seatSize - 20, 1.0f);
                layoutParams.setMargins(seatGaping, seatGaping, seatGaping, seatGaping);
                view.setLayoutParams(layoutParams);
                view.setPadding(0, 0, 0, 2 * seatGaping);
                view.setId(count);
                view.setGravity(Gravity.CENTER);
                if (seatsStatus.charAt(seats.charAt(index) - 'A') == '0')
                    view.setBackgroundResource(R.drawable.new_torch_off);
                else
                    view.setBackgroundResource(R.drawable.new_torch_on);
                view.setTag(seats.charAt(index));
                view.setTextColor(Color.BLACK);
                view.setText(count + "");
                view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 9);
                layout.addView(view);
                seatViewList.add(view);
                view.setOnClickListener(this);
            } else if (seats.charAt(index) == '_') {
                TextView view = new TextView(getContext());
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(seatSize - 20, seatSize - 20, 1.0f);
                layoutParams.setMargins(seatGaping, seatGaping, seatGaping, seatGaping);
                view.setLayoutParams(layoutParams);
                view.setBackgroundColor(Color.TRANSPARENT);
                view.setText("");
                layout.addView(view);
            }
        }
        return builder.create();
    }


    public boolean toggleLight(int pos) {
        if (seatsStatus.charAt(pos) == '0')
            seatsStatus.setCharAt(pos, '1');
        else
            seatsStatus.setCharAt(pos, '0');
        return true;
    }

    public void onClick(View view) {
        if (toggleLight((char) view.getTag() - 'A') == true) {
            char tag = (char) view.getTag();
            char new_status = seatsStatus.charAt(tag - 'A');
            Log.e("Current View ",view.toString());

            for (int i = 1; i <= count; i++) {
                TextView curView = (TextView) layout.findViewById(i);
                if ((char) curView.getTag() == tag) {
                    char light_Group = (char) curView.getTag();
                    if (light_Group >= 'A' && light_Group <= 'L') {
                        if (new_status == '0')
                            curView.setBackgroundResource(R.drawable.square_light_off);
                        else
                            curView.setBackgroundResource(R.drawable.square_light_on);
                    } else if (light_Group >= 'M' && light_Group <= 'O') {
                        if (new_status == '0')
                            curView.setBackgroundResource(R.drawable.round_light_off);
                        else
                            curView.setBackgroundResource(R.drawable.round_light_on);
                    } else {
                        if (new_status == '0')
                            curView.setBackgroundResource(R.drawable.new_torch_off);
                        else
                            curView.setBackgroundResource(R.drawable.new_torch_on);
                    }
                }
            }
        }
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener=(CustomSceneDialog.CustomSceneDialogListener)context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()+"Must implement Scene Dialog listener");
        }
    }

    public interface CustomSceneDialogListener{
        void applyScene(String states);
    }
}
