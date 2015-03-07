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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.DropboxAPI.Entry;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.session.AppKeyPair;
import com.dropbox.client2.session.Session.AccessType;

import app.dropbox.encrytion.R;
import app.dropbox.encrytion.R.layout;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class encryption extends Activity {
	
	SharedPreferences enckey;
	SharedPreferences filetoencrypt;
	String path=null;
	
	String accessToken=null;
	SharedPreferences acctok;
	String encryptKey=null;
	
	final static private String APP_KEY="";
	final static private String APP_SECRET="";
	final static private AccessType ACCESS_TYPE =AccessType.DROPBOX ;
	private DropboxAPI<AndroidAuthSession> mDBApi;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.encryption);
		
	
		initializingVariables();
		
		
	  	
        if(path==null)
		{
       
        path=getIntent().getData().getPath();
		
		Editor editor = filetoencrypt.edit();
        editor.putString("filetoencrypt", path);
        editor.commit();
        }
      
        Log.d("deeer", ""+path);
        
        if(allWell()){
        
        setFilepathPreference();
		gettingFileAndEncrypting();
		}
	}
	
	protected void  setFilepathPreference() {
		
		Editor editor = filetoencrypt.edit();
        editor.putString("filetoencrypt", null);
        editor.commit();
		
	}
	
	protected void initializingVariables() 
	{   
		
		acctok=getSharedPreferences("accesstoken",0);
		this.accessToken =acctok.getString("accesstoken", null);
	    enckey=getSharedPreferences("encryptkey", 0);
		encryptKey=enckey.getString("encryptkey", null);
		filetoencrypt=getSharedPreferences("filetoencrypt",  0);
		//encryptkey=filetoencrypt.getString("filetoencrypt", null);
		path=filetoencrypt.getString("filetoencrypt", null);
		
	}
	
	protected boolean allWell() {
		
		if(accessToken!=null&&encryptKey!=null)
		        return true;
		else {
			if(this.accessToken==null)nextDropbox();
			else if(encryptKey==null)nextEncryptionSetup();
			return false;
			}
	    
	}
	
	void nextEncryptionSetup(){
		
		finish();
		Intent it=new Intent(this,encryptionsetup.class);
		startActivity(it);
	}
	
	void nextDropbox(){
		finish();
		Intent it=new Intent(this,MainActivity.class);
		startActivity(it);
		
	}
	
	 public void open(View view){
	      AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
	      alertDialogBuilder.setMessage("Dropbox Upload");
	      alertDialogBuilder.setPositiveButton("Upload", 
	      new DialogInterface.OnClickListener() {
			
	         @Override
	         public void onClick(DialogInterface arg0, int arg1) {
	        	 forAuthentication();
	        	 setSavedTokenToSession();
	        	 final File file = new File(path);
	        	 
	        		 Thread th=new Thread(new Runnable() {
						
						@Override
						public void run() {
							
							// TODO Auto-generated method stub
							 FileInputStream inputStream;
							try {
								inputStream = new FileInputStream(file);
								Entry response = mDBApi.putFile("Encrypt&Upload/"+path.split("/")[path.split("/").length-1], inputStream,file.length(), null, null);
									
							} catch (FileNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (DropboxException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
				        	}	
					});
	        		 th.start();
	        		 
	        		 Log.i("DbExampleLog", "The uploaded file's rev is: " + path.split("/")[1]);
	        	// finish();
	        	
	         }
	      });
	      alertDialogBuilder.setNegativeButton("KeepLocal", 
	      new DialogInterface.OnClickListener() {
				
	         @Override
	         public void onClick(DialogInterface dialog, int which) {
	            }
	      });
		    
	      AlertDialog alertDialog = alertDialogBuilder.create();
	      alertDialog.show();
		    
	   }
	
	void gettingFileAndEncrypting(){
		
		Button encrypt=(Button) findViewById(R.id.button1);
		encrypt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				CryptoUtilsTest cut=new CryptoUtilsTest();
				cut.key=encryptKey;
				cut.path=path;
				
				
			    path=cut.encrypt();
			  
		        open(v);
		       // path=null;
				
				//Log.d("deeeer","f"+ path2.charAt(6));
				
				
			}
		});
		Button decryt=(Button) findViewById(R.id.button2);
		decryt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				CryptoUtilsTest cut=new CryptoUtilsTest();
				cut.key=encryptKey;
				cut.path=path;
				
			    cut.decrypt();
				
				finish();
			}
		});
	}
	
	protected void setSavedTokenToSession() 
	{
		mDBApi.getSession().setOAuth2AccessToken(accessToken);	
	}
	
	protected void  forAuthentication() 
	{
		AppKeyPair appKeys = new AppKeyPair(APP_KEY, APP_SECRET);
		AndroidAuthSession session = new AndroidAuthSession(appKeys, ACCESS_TYPE);
        mDBApi = new DropboxAPI<AndroidAuthSession>(session);
    }

}

class CryptoUtilsTest {
	
	static String key ;
	String path;
	
	//static File decryptedFile = new File("/storage/emulated/0/Encup&Upload/derypted/");
	
	String encrypt() 
	{
		File inputFile =new File(path);
		//SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss");
	    //String date = dateFormat.format(new Date());
	    String fileName=path.split("/")[path.split("/").length-1];
		File encryptedFile = new File("/storage/emulated/0/EncryptUpload/encrypted/");
		encryptedFile.mkdirs();
		int len=fileName.length();
		
		String absolutePath=encryptedFile.getPath()+"/"+fileName;//date+"."+fileName.charAt(len-3)+""+fileName.charAt(len-2)+""+fileName.charAt(len-1);
		
		File file=new File(absolutePath);
		try {
			CryptoUtils.encrypt(key, inputFile, file);
			} catch (CryptoException ex) {
			System.out.println(ex.getMessage());
			ex.printStackTrace();
		}
		
		return absolutePath;
	}
	void decrypt()
	{
		File inputFile =new File(path);
		String fileName=path.split("/")[path.split("/").length-1];
		File decryptedFile = new File("/storage/emulated/0/EncryptUpload/decrypted/");
		decryptedFile.mkdirs();
		int len=fileName.length();
		String name=fileName.substring(0,len-5);
		File file=new File(decryptedFile.getPath()+"/"+name+"."+fileName.charAt(len-3)+""+fileName.charAt(len-2)+""+fileName.charAt(len-1));
		
		try {
			CryptoUtils.decrypt(key, inputFile, file);
			
		} catch (CryptoException ex) {
			System.out.println(ex.getMessage());
			ex.printStackTrace();
		}
		
	
}
}

