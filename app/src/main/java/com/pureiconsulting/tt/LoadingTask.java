package com.pureiconsulting.tt;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.pureiconsulting.tt.service.Ticket;

/**
 * Created by julianzhu on 11/15/15.
 */
public class LoadingTask extends AsyncTask<String, Integer, Integer> {

    public interface LoadingTaskFinishedListener {
        void onTaskFinished();
    }

    private final LoadingTaskFinishedListener finishedListener;

    private Context context = null;

    public LoadingTask(Context context, LoadingTaskFinishedListener finishedListener) {

        this.context = context;
        this.finishedListener = finishedListener;
    }

    @Override
    protected Integer doInBackground(String... params) {

        // do initialization here ...

        try {

            //ActivityHelper.init(context);
            //Log.i("Test", "Loading config ...");
            //Ticket.loadTickets(context);



            Thread.sleep(800);


        } catch (InterruptedException ignore) {

        }

        // could also update progress status here.
        return 0;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {

        super.onProgressUpdate(values);
        // Could add a progress bar to update status ....
    }

    @Override
    protected void onPostExecute(Integer integer) {

        super.onPostExecute(integer);
        // Notifiy whoever needs to be notified.
        finishedListener.onTaskFinished();
    }
}