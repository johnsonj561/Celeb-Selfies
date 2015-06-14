package com.putty.celebselfie_v2;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class ThicknessDialog extends DialogFragment {

	//constructor is required for DialogFragment
	public ThicknessDialog(){
	}
		
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceOf){
		View v = inflater.inflate(R.layout.activity_thickness_dialog, container);
		getDialog().setTitle("Choose Pen Thickness");
		parentActivity = (GetSignature)getActivity();
		
		linkButtons(v);
		
		return v;
}
	
	private void linkButtons(View v){
		
		thickness1Button = (Button) v.findViewById(R.id.thickness1Button);
		thickness1Button.setOnClickListener(new OnClickListener(){
			public void onClick(View V){
				parentActivity.onThicknessDialogReturn(2);
				dismiss();
			}
		});
		thickness2Button = (Button) v.findViewById(R.id.thickness2Button);
		thickness2Button.setOnClickListener(new OnClickListener(){
			public void onClick(View V){
				parentActivity.onThicknessDialogReturn(4);
				dismiss();
			}
		});
		thickness3Button = (Button) v.findViewById(R.id.thickness3Button);
		thickness3Button.setOnClickListener(new OnClickListener(){
			public void onClick(View V){
				parentActivity.onThicknessDialogReturn(6);
				dismiss();
			}
		});
		thickness4Button = (Button) v.findViewById(R.id.thickness4Button);
		thickness4Button.setOnClickListener(new OnClickListener(){
			public void onClick(View V){
				parentActivity.onThicknessDialogReturn(8);
				dismiss();
			}
		});
		thickness5Button = (Button) v.findViewById(R.id.thickness5Button);
		thickness5Button.setOnClickListener(new OnClickListener(){
			public void onClick(View V){
				parentActivity.onThicknessDialogReturn(10);
				dismiss();
			}
		});
		thickness6Button = (Button) v.findViewById(R.id.thickness6Button);
		thickness6Button.setOnClickListener(new OnClickListener(){
			public void onClick(View V){
				parentActivity.onThicknessDialogReturn(12);
				dismiss();
			}
		});
		thickness7Button = (Button) v.findViewById(R.id.thickness7Button);
		thickness7Button.setOnClickListener(new OnClickListener(){
			public void onClick(View V){
				parentActivity.onThicknessDialogReturn(14);
				dismiss();
			}
		});
		thickness8Button = (Button) v.findViewById(R.id.thickness8Button);
		thickness8Button.setOnClickListener(new OnClickListener(){
			public void onClick(View V){
				parentActivity.onThicknessDialogReturn(16);
				dismiss();
			}
		});
		thickness9Button = (Button) v.findViewById(R.id.thickness9Button);
		thickness9Button.setOnClickListener(new OnClickListener(){
			public void onClick(View V){
				parentActivity.onThicknessDialogReturn(18);
				dismiss();
			}
		});
		
		
		
		
	}
	
	

	
	private GetSignature parentActivity;
	private Button thickness1Button;
	private Button thickness2Button;
	private Button thickness3Button;
	private Button thickness4Button;
	private Button thickness5Button;
	private Button thickness6Button;
	private Button thickness7Button;
	private Button thickness8Button;
	private Button thickness9Button;
	
}
