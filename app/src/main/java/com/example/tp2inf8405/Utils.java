package com.example.tp2inf8405;
import android.app.Activity;
import android.content.Intent;
public class Utils
{
    private static int sTheme;
    public final static int THEME_LIGHT = 0;
    public final static int THEME_BLACK = 1;


    public static void changeToTheme(Activity activity, int theme)
    {
        sTheme = theme;
        activity.finish();
        activity.startActivity(new Intent(activity, activity.getClass()));
    }

    public static void onActivityCreateSetTheme(Activity activity)
    {
        switch (sTheme)
        {
            default:
            case THEME_LIGHT:
                activity.setTheme(android.R.style.Theme_Light);
                break;
            case THEME_BLACK:
                activity.setTheme(android.R.style.Theme_Black);
                break;
        }
    }
}
