package com.biblioteca.cientec.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;

public class GalleryOrCameraDialogFragment extends DialogFragment {
    private static final String TAG  = "GalleryOrCameraDialog";

    public interface OnDialogItemSelect {
        void sendOption(String input);
    }
    public OnDialogItemSelect mOnDialogItemSelect;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String[] options = {"Câmera","Galeria"};
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Origem da imagem")
                .setItems(options, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String input = "";
                        switch (which) {
                            case 0:
                                //Toast.makeText(getContext(),"Câmera", Toast.LENGTH_SHORT).show();
                                input = "camera";
                                break;
                            case 1:
                                //Toast.makeText(getContext(),"Galeria", Toast.LENGTH_SHORT).show();
                                input = "galeria";
                                break;
                        }

                        mOnDialogItemSelect.sendOption(input);
                    }
                });

        // Create the AlertDialog object and return it
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mOnDialogItemSelect = (OnDialogItemSelect) getTargetFragment();
        } catch (ClassCastException e) {
            Log.e(TAG, "onAttach: ClassCastException: " + e.getMessage());
        }
    }
}