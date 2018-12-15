package com.mrntlu.PassVault.Widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.mrntlu.PassVault.Offline.MailVault;
import com.mrntlu.PassVault.Offline.OtherAccounts;
import com.mrntlu.PassVault.R;
import com.mrntlu.PassVault.Offline.UserAccounts;

public class ButtonsWidgetProvider extends AppWidgetProvider {

    public static String MAIL_VAULT="com.mrntlu.PassVault.Widgets.mailvault";
    public static String USER_ACCOUNTS="com.mrntlu.PassVault.Widgets.useraccounts";
    public static String OTHER_ACCOUNTS="com.mrntlu.PassVault.Widgets.others";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.buttons_app_widget);

        Intent mailClickIntent=new Intent(context,ButtonsWidgetProvider.class);
        mailClickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,appWidgetId);
        mailClickIntent.setAction(MAIL_VAULT);
        PendingIntent pendingClick=PendingIntent.getBroadcast(context,0,mailClickIntent,0);
        views.setOnClickPendingIntent(R.id.mailAccounts,pendingClick);

        Intent userClickIntent=new Intent(context,ButtonsWidgetProvider.class);
        userClickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,appWidgetId);
        userClickIntent.setAction(USER_ACCOUNTS);
        PendingIntent pendingUserClick=PendingIntent.getBroadcast(context,0,userClickIntent,0);
        views.setOnClickPendingIntent(R.id.bankAccounts,pendingUserClick);

        Intent otherClickIntent=new Intent(context,ButtonsWidgetProvider.class);
        otherClickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,appWidgetId);
        otherClickIntent.setAction(OTHER_ACCOUNTS);
        PendingIntent pendingOtherClick=PendingIntent.getBroadcast(context,0,otherClickIntent,0);
        views.setOnClickPendingIntent(R.id.otherAccounts,pendingOtherClick);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equalsIgnoreCase(MAIL_VAULT)){
            Intent clickIntent=new Intent(context,MailVault.class);
            context.startActivity(clickIntent);
        }else if (intent.getAction().equalsIgnoreCase(USER_ACCOUNTS)){
            Intent clickIntent=new Intent(context,UserAccounts.class);
            context.startActivity(clickIntent);
        }else if (intent.getAction().equalsIgnoreCase(OTHER_ACCOUNTS)){
            Intent clickIntent=new Intent(context,OtherAccounts.class);
            context.startActivity(clickIntent);
        }

        super.onReceive(context, intent);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

