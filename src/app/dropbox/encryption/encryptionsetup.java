package app.dropbox.encryption;

/*
Copyright [2014] KlwntSingh

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/


import org.apache.http.util.LangUtils;
import org.bouncycastle.asn1.cmp.ProtectedPart;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.session.AppKeyPair;
import com.dropbox.client2.session.Session.AccessType;

import app.dropbox.encrytion.R;
import app.dropbox.encrytion.R.layout;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class encryptionsetup extends Activity {
	
	SharedPreferences enckey;
	SharedPreferences fileEncryptPref;
	SharedPreferences acctok;
	
	String encryptKey=null;
	String accessToken=null;
	String fileToEncrypt=null;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.encryption_setup);
		
		initilizingVariables();
		if(encryptKey!=null)
		{
			if(fileToEncrypt!=null)
			 launchEncryption();
			else 
			 launchnext();
		}
		else{
//			acctok=getSharedPreferences("accesstoken",0);
//			accessToken =acctok.getString("accesstoken", null);
//			if(accessToken==null){
//				goback();
//				}
//		    
		final EditText key=(EditText) findViewById(R.id.editText1);
				
		ImageButton encrypt=(ImageButton) findViewById(R.id.imageButton1);
		encrypt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
		    encryptKey=	key.getText().toString();
		    if(keyValidator(encryptKey))
			{key.setText("");
			
		    setKeyToPreferences();
		    
			if(fileToEncrypt!=null)
		    launchEncryption();
			else launchnext();
			}
		    else {
		    	Toast toast=Toast.makeText(encryptionsetup.this, "Enter key between 5 to 16 Characters", 2000);
		    	toast.setGravity(Gravity.CENTER, 0, 0);
		    	toast.show();
		    }
			
			
			}
			
			
		});
		}
		
	}
	
	protected boolean keyValidator(String key){
		if(key.length()>5&&key.length()<16)return true;
		else return false;
		
	}
	
	protected void goback() {
		Intent it=new Intent(this,MainActivity.class);
		finish();
		startActivity(it);
	}
	
	protected void launchEncryption() {
		Intent it=new Intent(this,encryption.class);
		finish();
		startActivity(it);
		
	}

	protected void launchnext() 
	{
			Intent intent=new Intent(this,home.class);
			finish();
			startActivity(intent);
	}
	
	protected void initilizingVariables()
	{
		
		enckey=getSharedPreferences("encryptkey",  0);
		encryptKey=enckey.getString("encryptkey", null);
		fileEncryptPref=getSharedPreferences("filetoencrypt",  0);
		fileToEncrypt=fileEncryptPref.getString("filetoencrypt", null);
		
	}
	
	protected void setKeyToPreferences() 
	{
		 Editor editor = enckey.edit();
         editor.putString("encryptkey", encryptKey);
         editor.commit();
       	} 
	

}
