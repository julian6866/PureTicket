package com.pureiconsulting.tt;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.pureiconsulting.tt.service.Cache;
import com.pureiconsulting.tt.service.Ticket;

import java.util.ArrayList;

/**
 * Created by julianzhu on 12/1/15.
 */
public class PullService extends IntentService {

    public static final String BROADCAST_PULLING_START = "com.pureiconsulting.com.PULLING_START";
    public static final String BROADCAST_PULLING_END = "com.pureiconsulting.com.PULLING_END";

    private static boolean autoSync = false;

    private static int sleepTime = 0; // default 0 seconds (disabled)

    public PullService() {
        super(PullService.class.getName());
    }

    public static boolean isAutoSync() {
        return autoSync;
    }

    public static void setAutoSync(boolean theAutoSync) {
        autoSync = theAutoSync;
    }

    public static int getSleepTime() {
        return sleepTime;
    }

    public static void setSleepTime(int theTime) {
        sleepTime = theTime; // in miliseconds;
    }

    @Override
    protected void onHandleIntent(Intent workIntent) {

        // Gets data from the incoming Intent
        //String dataString = workIntent.getDataString();

        // do work here ..
        boolean isContinue = true;

        while(isContinue) {

            try {

                Thread.sleep(sleepTime);

                if(autoSync) {
                    Intent localIntent = new Intent(BROADCAST_PULLING_START);
                    // Puts the status into the Intent
                    // .putExtra(Constants.EXTENDED_DATA_STATUS, status);
                    // Broadcasts the Intent to receivers in this app.
                    LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);

                    // First of all, let's send all scanned ticket to the cloud:
                    Ticket.uploadTickets(getApplicationContext());

                    Ticket.loadTickets(getApplicationContext());

                    Intent localIntent2 = new Intent(BROADCAST_PULLING_END);
                    // Puts the status into the Intent
                    // .putExtra(Constants.EXTENDED_DATA_STATUS, status);
                    // Broadcasts the Intent to receivers in this app.
                    LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent2);

                    // now let's reload the tickets from the cloud
                    //Ticket.loadTickets(getApplicationContext());
                    //ArrayList<Ticket> tickets = loadTickets(getApplicationContext());
                }
            } catch(InterruptedException ire) {


            }

        }

    }

}
