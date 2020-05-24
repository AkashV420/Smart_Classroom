package com.example.smartclasscontrolpanel;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.FragmentManager;

public class SceneDialog extends AppCompatDialogFragment {
    RadioGroup radioGroup;
    RadioButton radioButton;
    String light_state;
    public SceneDialog(String light_state){
        this.light_state=light_state;
    }
    private SceneDialogListener listener;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.scene_dialog,null);
        builder.setView(view)
                .setTitle("Select Scene")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int radioID=radioGroup.getCheckedRadioButtonId();
                        if (radioID==-1) {
                            listener.applyScene("None");
                        }
                        else {
                            Log.e("Selected RButton ", Integer.toString(radioID));
                            RadioButton rb = radioGroup.findViewById(radioID);
                            String state = rb.getText().toString();
                            //Change the light layout string according to the selected scene
                            // to change a layout just send string as a parameter to applyScene
                            //For custom layout the apply scene is called by CustomSceneDialog
                            if (state.equals("Custom")) {
                                CustomSceneDialog sd2 = new CustomSceneDialog(light_state);
                                sd2.show(getFragmentManager(), "Test Dialog2");
                            }
                            else
                                listener.applyScene("1010101010101010101");
                        }
                    }
                });
        radioGroup = view.findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton checkedButton=(RadioButton) group.findViewById(checkedId);
                Log.e("FINAL STATES",checkedButton.getText().toString());
            }
        });
        return builder.create();
    }
    public void checkButton(View v){
        int radioId= radioGroup.getCheckedRadioButtonId();
        radioButton=v.findViewById(radioId);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener=(SceneDialogListener)context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()+"Must implement Scene Dialog listener");
        }
    }

    public interface SceneDialogListener{
        void applyScene(String states);
    }
}
