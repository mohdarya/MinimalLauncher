package com.minimallauncher;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;






public class MainActivity extends AppCompatActivity
{
    private Thread vibrateThraed;
    public static String categoryLaunched = "";
    public static String SHARED_PREFS = "sharedPrefs";
    public static String DATA = "data1";
    private static boolean activityVisible;
    static int fragmentLaunched = 0;
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
        context = this;
        loadData();
        getApplications();
        font = Typeface.createFromAsset(context.getAssets(), "fonts/Helvetica.ttc");
        setContentView(R.layout.content_main);
        setVibrateThraed();

    }



    @Override
    public void onBackPressed()
    {
        if (fragmentLaunched > 0){
            super.onBackPressed();
            fragmentLaunched--;
        }

    }

    public void getApplications()
    {

        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
       List< ResolveInfo> allApps = getApplicationContext().getPackageManager().queryIntentActivities(intent,0);


            for (int i = 0; i < allApps.size(); i++)
            {
                App temp = new App(allApps.get(i).activityInfo.loadLabel(getPackageManager()).toString(), allApps.get(i).activityInfo.packageName);
                if(!allApplications.contains(temp))
                {
                    allApplications.add(temp);
                }
            }


    }

    @Override
    protected void onResume() {

        super.onResume();
        MainActivity.activityResumed();
        if(categoryLaunched.equals("R"))
        {
            vibrateThraed.interrupt();
            setVibrateThraed();
        }
        categoryLaunched = "";
    }

    @Override
    protected void onPause() {

        super.onPause();
        MainActivity.saveData();
        MainActivity.activityPaused();
        if(categoryLaunched.equals("R"))
        {
            vibrateThraed.start();
        }


    }

    public static boolean isActivityVisible() {
        return activityVisible;
    }

    public static void activityResumed() {
        activityVisible = true;
    }

    public static void activityPaused() {
        activityVisible = false;
    }

    private void setVibrateThraed()
    {

        final Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        Runnable runnable = new Runnable()
        {
            @Override
            public void run()
            {
                try
                {

                    boolean once = false;
                    while(!Thread.currentThread().isInterrupted())
                    {

                        KeyguardManager km = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
                        boolean locked = km.inKeyguardRestrictedInputMode();

                            if(!locked && once)
                            {
                                v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                            }
                            Thread.sleep(300000);
                            once = true;

                    }
                }
                catch(InterruptedException consumed)
                {
                    Log.e("Thread", "Thraed Stopped");
                }
            }
        };

       vibrateThraed = new Thread(runnable);
    }

public static void saveData()
{
    SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
    SharedPreferences.Editor editor = sharedPreferences.edit();
    Gson gson = new Gson();
    Type appListType = new TypeToken<ArrayList<App>>(){}.getType();
    String json = gson.toJson(MainActivity.allApplications, appListType);
    editor.putString(DATA, json);
    editor.commit();

}

public static void loadData()
{
    SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
    Gson gson = new Gson();
    String json = sharedPreferences.getString(DATA,"");
    Type appListType = new TypeToken<ArrayList<App>>(){}.getType();
    if(!json.equals(""))
    {
        MainActivity.allApplications = gson.fromJson(json, appListType);
    }
}

private void removeApplication(String appName)
{
    for(int i = 0; i < allApplications.size(); i++)
    {
        if(allApplications.get(i).getApplicationName().equals(appName))
        {
            allApplications.remove(i);
        }
    }
}

}
