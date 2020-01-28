package com.minimallauncher;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
{

    static Context context;
    static List<App> allApplications = new ArrayList<>();
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        getApplications();
        context = this;
        setContentView(R.layout.content_main);

    }

    @Override
    public void onBackPressed()
    {
        if (getSupportFragmentManager().getBackStackEntryCount() == 1){
            finish();
        }
        else {
            super.onBackPressed();
        }
    }

    public void getApplications()
    {

        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
       List< ResolveInfo> allApps = getApplicationContext().getPackageManager().queryIntentActivities(intent,0);

       for(int i = 0; i < allApps.size(); i++)
       {
           allApplications.add(new App(allApps.get(i).activityInfo.loadLabel(getPackageManager()).toString(),allApps.get(i),false,false));
       }

    }





}
