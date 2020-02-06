package com.minimallauncher;

import android.content.pm.ResolveInfo;

public class App
{
    private String applicationName;

    private String packageName;
    private boolean restricted = false;
    private boolean regular = false;
    private ResolveInfo app;


    public App(String applicationName, ResolveInfo app)
    {
        this.applicationName = applicationName;
        this.app = app;

    }

    public ResolveInfo getApp()
    {
        return app;
    }

    public void setApp(ResolveInfo app)
    {
        this.app = app;
    }


    public App(String applicationName, String packageName)
    {
        this.applicationName = applicationName;
        this.packageName = packageName;
    }


    public String getApplicationName()
    {
        return applicationName;
    }



    public boolean isRestricted()
    {
        return restricted;
    }

    public boolean isRegular()
    {
        return regular;
    }

    public void setApplicationName(String applicationName)
    {
        this.applicationName = applicationName;
    }



    public void setRestricted(boolean restricted)
    {
        this.restricted = restricted;
    }

    public void setRegular(boolean regular)
    {
        this.regular = regular;
    }


    @Override
    public boolean equals(Object v) {
        boolean retVal = false;

        if (v instanceof App){
            App ptr = (App) v;
            retVal = ptr.applicationName.equals(this.applicationName);
        }

        return retVal;
    }

    @Override
    public int hashCode() {
        return applicationName.hashCode();
    }

    public String getPackageName()
    {
        return packageName;
    }

    public void setPackageName(String packageName)
    {
        this.packageName = packageName;
    }
}
