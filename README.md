# Encrypt-Upload
Android Application to Encrypt your files and than you can upload them to Dropbox Cloud account or just keep them local on you device hence increasing your privacy and protecting your data from being compromised.




how to build from code 

1. You need to make changes in three files 
	a.Encrypt-Upload\src\app\dropbox\encryption\MainActivity.java
	b.Encrypt-Upload\src\app\dropbox\encryption\encryption.java
	c.AndroidMainifest.xml
	
   there will be two string type variables by the name of APP_KEY and APP_SECRET set them to values you have from your dropbox developer
   console by creating app of dropbox api app.
   and in android manifest file there will be line 
    <data android:scheme="db-" /> put the APP_KEY variable value after db.
	
Now you can make apk from code.
or you run apk directly provided on you Android Smartphone.