# ManifestAnalyzer
## Description
This Android application (Java) retrieves the Android manifest of another application
installed on the same device. 9292ov app in this case. The app shows the content of
the manifest file available in a human readable format.

## Application logic
The app follows below steps to achieve the task
1. Extract the full package name of 9292ov.
2. Find out the full path of the apk location using the package name.
3. Read the manifest file using the apk of the app.
4. Decode the manifest file from binary to a text format.
5. Format the text string to human readable xml format.
6. Render the xml output in TextArea.

## How to use the app
Please make sure you have the 9292ov app installed.  
Please install the ManifestAnalyzer app.  
Launch the app to see the manifest of 9292ov app.  
