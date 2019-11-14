package com.btrnaidu.manifestanalyzer;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;


public class ScrollingActivity
    extends AppCompatActivity
{

    @Override
    protected void onCreate(
        Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        HashMap<String,String> installedApps = getInstalledPackages();
        String appToShow = "9292";
        String packageName = installedApps.get(appToShow);
        Log.d("MA", appToShow + " package name: " + packageName);

        String apkFilePath = getApkFilePath(packageName);
        Log.d("MA", apkFilePath);

        DecompressXML dx = new DecompressXML();
        String xml = dx.decompressXML(getByteXNL(apkFilePath)); //"testing"; //

        String prittyXML = new XmlFormatter().prettyFormat(xml);

        String appDetailsFromManifest = String.join("\n","Manifest of " + appToShow + " app\n", prittyXML);

        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView tv_manifestViewer = findViewById(R.id.tv_manifestViewer);
        tv_manifestViewer.setText(appDetailsFromManifest);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private String getApkFilePath(String packageName)
    {
        String apkFilePath = null;

        PackageManager pm = getApplicationContext().getPackageManager();
        try {
            ApplicationInfo ai = pm.getApplicationInfo(packageName, 0);
            apkFilePath = ai.publicSourceDir;
        } catch (Throwable x) {
        }

        return apkFilePath;
    }

    private byte[] getByteXNL(String path)
    {
        InputStream is = null;
        ZipFile zip = null;

        byte[] buf = new byte[20480];

        try
        {
            if (path.endsWith(".apk") || path.endsWith(".zip")) {

                zip = new ZipFile(path);
                ZipEntry mft = zip.getEntry("AndroidManifest.xml");
                is = zip.getInputStream(mft);

            } else {
                is = new FileInputStream(path);
            }

            int bytesRead = is.read(buf);

            is.close();
            if (zip != null) {
                zip.close();
            }
        } catch(Exception e)
        {
            e.printStackTrace();
        }

        return buf;
    }


    // Custom method to get all installed package names
    protected HashMap<String,String> getInstalledPackages()
    {
        PackageManager packageManager = getPackageManager();

        // Initialize a new intent
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        // Set the intent category
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        // Set the intent flags
        intent. setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        |Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);

        // Initialize a new list of resolve info
        List<ResolveInfo> resolveInfoList = getPackageManager().queryIntentActivities(intent, 0);

        // Initialize a new hash map of package names and application label
        HashMap<String,String> map = new HashMap<>();

        // Loop through the resolve info list
        for(ResolveInfo resolveInfo : resolveInfoList)
        {
            // Get the activity info from resolve info
            ActivityInfo activityInfo = resolveInfo.activityInfo;

            // Get the package name
            String packageName = activityInfo.applicationInfo.packageName;

            // Get the application label
            String label = (String) packageManager.getApplicationLabel(activityInfo.applicationInfo);

            // Put the package name and application label to hash map
            map.put(label, packageName);
            Log.d("MA", label + " -> " + packageName);
        }
        return map;
    }


}
