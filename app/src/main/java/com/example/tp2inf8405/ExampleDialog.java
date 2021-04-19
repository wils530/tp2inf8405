package com.example.tp2inf8405;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;
/*
 PopUp class
 */
public class ExampleDialog extends AppCompatDialogFragment {
    @NonNull
    public BtDevice bt;
    // PopUp constructor : argument bluetooth device
    public ExampleDialog(BtDevice btDevice) {
        this.bt=btDevice;
    }

    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        //build the popup
        builder.setTitle(bt.dName).setMessage(bt.mac+"\n"+bt.c.latitude+"\n"+bt.c.longitude).setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        //return the popup msg
        return builder.create();
    }
}
