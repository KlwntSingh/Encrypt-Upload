Encrypt-Upload
==============
Android Application to Encrypt your files and than you can upload them to Dropbox Cloud account or just keep them local on you device hence increasing your privacy and protecting your data from being compromised.


## Installation 
 directly install android apk given by name EncryptUpload.apk


## Building Apk From Source

1. You need to make changes in three files 
	a.Encrypt-Upload\src\app\dropbox\encryption\MainActivity.java
	b.Encrypt-Upload\src\app\dropbox\encryption\encryption.java
	c.AndroidMainifest.xml
	
   there will be two string type variables by the name of APP_KEY and APP_SECRET set them to values you have from your dropbox     developer console by creating app of dropbox api app. 
   and in android manifest file there will be line 
    <data android:scheme="db-" /> put the APP_KEY variable value after db.
	
Now you can make apk from code.

## Usage
   Encrypting -
   After you install the application when ever you click on any file there will be option to open it with EncryptUpload if you select this than android application will start asking you to login in your dropbox account and i will ask for Password which you want to encrypt your file and than you will be asked if you want to keep your file local or if you want to upload to your dropbox account.
   Decrypting -
   if your file is present in local you can select it and you decrypt file.
   if file is present in dropbox account than first you need to first download it and decrypt.
   
## Future Supports 
    will support google drive. 
    improvement in UserInterface.
    Desktop Application in nodeJs 
    
	
    
