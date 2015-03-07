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

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.session.AppKeyPair;
import com.dropbox.client2.session.Session.AccessType;
import app.dropbox.encrytion.R;
import app.dropbox.encrytion.R.layout;

import android.R.string;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

	// now to check for authentication of access tokken stored in shared preference
	
	
	final static private String APP_KEY="";
	final static private String APP_SECRET="";
	final static private AccessType ACCESS_TYPE =AccessType.DROPBOX ;
	private DropboxAPI<AndroidAuthSession> mDBApi;

	String accessToken=null;
	SharedPreferences acctok;
	SharedPreferences enckey;
	String encryptKey=null;
	
	Intent it;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initializingVariables();
		checkToken();

	}
	
	protected void checkToken(){
		if(accessToken==null)
		{
			dropboxStarted();
		}
		else
		{
			if(encryptKey==null)
			nextEncryptionSetup();
			else nextHome();
     	}
		
	}
	
	protected void onRestart() 
	{
	    super.onRestart();
	    getNewToken();
	       
	   if(accessToken!=null)
	   {		 
		   if(encryptKey==null)
			 nextEncryptionSetup();
			else
			 nextHome();
	   }
	    else
	    {
	    	 finish();
	    }
	}
	
	protected void nextEncryptionSetup() {
		 
		it=new Intent(this,encryptionsetup.class);
		finish();
	    startActivity(it);
	}
	
	protected void nextHome() {
		
		it=new Intent(this,home.class);
		finish(); 
	    startActivity(it);
	}
	
	protected void initializingVariables() 
	{   
		acctok=getSharedPreferences("accesstoken",0);
		this.accessToken =acctok.getString("accesstoken", null);
	    enckey=getSharedPreferences("encryptkey", 0);
		encryptKey=enckey.getString("encryptkey", null);
	}
	
	protected void  forAuthentication() 
	{
		AppKeyPair appKeys = new AppKeyPair(APP_KEY, APP_SECRET);
		AndroidAuthSession session = new AndroidAuthSession(appKeys, ACCESS_TYPE);
        mDBApi = new DropboxAPI<AndroidAuthSession>(session);
    }
	
	protected  void saveTokenToPreference(String accesstoken) 
	{
		
		 Editor editor = acctok.edit();
         editor.putString("accesstoken", accesstoken);
         editor.commit();
		 
	}
	
	protected void loadTokentoacctok() {
		
		acctok=getSharedPreferences("accesstoken",0);
		accessToken =acctok.getString("accesstoken", null);
		
	}
	protected void removeSavedToken() 
	{
		 Editor editor = acctok.edit();
         editor.putString("accesstoken", null);
         editor.commit();
		
	}
	protected void setSavedTokenToSession() 
	{
		mDBApi.getSession().setOAuth2AccessToken(accessToken);	
	}
	
	protected void dropboxStarted() 
	{

		forAuthentication();
		mDBApi.getSession().startOAuth2Authentication(this);
		
		
	}
	
	protected void getNewToken() 
	{
		if (mDBApi.getSession().authenticationSuccessful()) {
	        try {
	            // Required to complete auth, sets the access token on the session
	            mDBApi.getSession().finishAuthentication();
	            this.accessToken=mDBApi.getSession().getOAuth2AccessToken();
	            saveTokenToPreference(this.accessToken);
	        }
	        catch (IllegalStateException e) {
	            Log.i("deeet", "Error authenticating", e);  }
	   } 
	}
}