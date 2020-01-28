package com.minimallauncher;

import android.content.pm.ResolveInfo;

public class App
{
    private String applicationName;
    private ResolveInfo app;
    private boolean restricted;
    private boolean regular;

    public App(String applicationName, ResolveInfo app, boolean restricted, boolean regular)
    {
        this.applicationName = applicationName;
        this.app = app;
        this.restricted = restricted;
        this.regular = regular;
    }

    public String getApplicationName()
    {
        return applicationName;
    }

    public ResolveInfo getApp()
    {
        return app;
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

    public void setApp(ResolveInfo app)
    {
        this.app = app;
    }

    public void setRestricted(boolean restricted)
    {
        this.restricted = restricted;
    }

    public void setRegular(boolean regular)
    {
        this.regular = regular;
    }
}
