package com.minimallauncher;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity
{

    static Context context;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
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

    static String getApplicationName(ApplicationInfo ai)
    {

        try {
            ai = context.getPackageManager().getApplicationInfo( ai.packageName, 0);
        } catch (final PackageManager.NameNotFoundException e) {
            ai = null;
        }
        return  (String) (ai != null ? context.getPackageManager().getApplicationLabel(ai) : "(unknown)");
    }

    static boolean isSystemPackage(ApplicationInfo applicationInfo) {
        return ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
    }
}
