package com.minimallauncher;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity
{

    static Context context;
    static Typeface font ;
    static List<App> allApplications = new ArrayList<App>(){
        public boolean add(App data)
        {
            super.add(data);
            Collections.sort(allApplications, new Comparator<App>()
            {
                @Override
                public int compare(App o1, App o2)
                {
                    return o1.getApplicationName().toLowerCase().compareTo(o2.getApplicationName().toLowerCase());
                }
            });
            return true;
        }
    };
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        getApplications();
        context = this;
        font = Typeface.createFromAsset(context.getAssets(), "fonts/Helvetica.ttc");
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
           allApplications.add(new App(allApps.get(i).activityInfo.loadLabel(getPackageManager()).toString(),allApps.get(i)));
       }

    }





}
