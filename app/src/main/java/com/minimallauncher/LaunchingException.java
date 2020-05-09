package com.minimallauncher;

public class LaunchingException extends Exception
{
    private String message;
    LaunchingException(String s)
    {
        super(s);
        message = s;

    }

    @Override
    public String getMessage()
    {
        return message;
    }
}
