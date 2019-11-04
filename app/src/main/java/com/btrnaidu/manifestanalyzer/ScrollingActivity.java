package com.btrnaidu.manifestanalyzer;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

public class ScrollingActivity
    extends AppCompatActivity
{

    @Override
    protected void onCreate(
        Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        HashMap<String,String> installedApps = getInstalledPackages();
        String fbPackageName = installedApps.get("Facebook");
        Log.d("MA", "Facebook package name: " + fbPackageName);

        String fbManifestFile = getAppManifest(fbPackageName);

        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView tv_manifestViewer = findViewById(R.id.tv_manifestViewer);
        tv_manifestViewer.setText(fbManifestFile);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(
            new View.OnClickListener()
            {
                @Override
                public void onClick(
                    View view)
                {
                    Snackbar
                        .make(
                            view,
                            "Replace with your own action",
                            Snackbar.LENGTH_LONG)
                        .setAction(
                            "Action",
                            null)
                        .show();
                }
            });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
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

    public String getAppManifest(String packageName)
    {
        String manifestXML;
        String[] requestedPermissions = {};

        try {
            // Get the package info
            PackageInfo packageInfo = getPackageManager().getPackageInfo(packageName, PackageManager.GET_PERMISSIONS);
            requestedPermissions = packageInfo.requestedPermissions;
        } catch(Exception e)
        {
            Log.e("MA", e.getLocalizedMessage());
        }

        manifestXML = String.join("\n", requestedPermissions);
        return manifestXML;
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
            //Log.d("MA", label + " -> " + packageName);
        }
        return map;
    }


}
