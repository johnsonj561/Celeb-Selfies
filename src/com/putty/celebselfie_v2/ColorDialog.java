package com.putty.celebselfie_v2;

import android.app.DialogFragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ColorDialog extends DialogFragment {

	//constructor is required for DialogFragment
	public ColorDialog(){
	}
		
	/**
	 * onCreateView inflates color dialog, displaying 9 different color options to user
	 * Each button triggers onColorDialogReturn, which in turn sets the color value
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceOf){
		View v = inflater.inflate(R.layout.activity_color_dialog, container);
		getDialog().setTitle("Choose Signature Color");
		parentActivity = (GetSignature)getActivity();
		
		blueButton = (Button) v.findViewById(R.id.blueButton);
		blueButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				parentActivity.onColorDialogReturn(0xff34b4e3);
				dismiss();
			}
		});
		
		greenButton = (Button) v.findViewById(R.id.greenButton);
		greenButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				parentActivity.onColorDialogReturn(0xff99cc00);
				dismiss();
			}
		});
		
		blackButton = (Button) v.findViewById(R.id.blackButton);
		blackButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				parentActivity.onColorDialogReturn(Color.BLACK);
				dismiss();
			}
		});
		
		purpleButton = (Button) v.findViewById(R.id.purpleButton);
		purpleButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				parentActivity.onColorDialogReturn(0xffaa66cc);
				dismiss();
			}
		});
		
		redButton = (Button) v.findViewById(R.id.redButton);
		redButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				parentActivity.onColorDialogReturn(0xffff4747);
				dismiss();
			}
		});
		
		yellowButton = (Button) v.findViewById(R.id.yellowButton);
		yellowButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				parentActivity.onColorDialogReturn(0xfffff314);
				dismiss();
			}
		});
		
		silverButton = (Button) v.findViewById(R.id.silverButton);
		silverButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				parentActivity.onColorDialogReturn(0xfff0f0f0);
				dismiss();
			}
		});
		
		pinkButton = (Button) v.findViewById(R.id.pinkButton);
		pinkButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				parentActivity.onColorDialogReturn(0xffff2ed2);
				dismiss();
			}
		});
		
		orangeButton = (Button) v.findViewById(R.id.orangeButton);
		orangeButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				parentActivity.onColorDialogReturn(0xffffbb33);
				dismiss();
			}
		});
		
		
		return v;
	}
	
	

	
	private GetSignature parentActivity;
	private Button blueButton;
	private Button purpleButton;
	private Button greenButton;
	private Button redButton;
	private Button yellowButton;
	private Button orangeButton;
	private Button pinkButton;
	private Button silverButton;
	private Button blackButton;
	
}
