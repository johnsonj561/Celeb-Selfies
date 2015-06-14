package com.putty.celebselfie_v2;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;


public class PhotoSavedDialog extends DialogFragment{
	
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
		
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Photo Saved")
        .setPositiveButton("Home", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                	   Intent i = new Intent(getActivity(), Home.class);
                	   i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                	   startActivity(i);
                   }
               });
        this.setCancelable(false);//Disable back button - forces user to press "Home" button
        return builder.create();
    }
	

}
