package com.pureiconsulting.tt;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.pureiconsulting.tt.service.Ticket;

/**
 * Created by julianzhu on 12/4/15.
 */
public class TicketUploading extends AsyncTask<String, Integer, Integer> {

    public interface TicketUploadingListener {
        void onUploadTaskStarted();
        void onUploadTaskFinished();
    }

    private final TicketUploadingListener listener;

    private Context context = null;

    public TicketUploading(Context context, TicketUploadingListener listener) {

        this.context = context;
        this.listener = listener;

    }

    @Override
    protected Integer doInBackground(String... params) {

        // do initialization here ...

        try {

            Ticket.uploadTickets(context);

        } catch(Exception ioe) {
            Log.e("Error", ioe.getMessage());
            ioe.printStackTrace();
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
    protected void onPreExecute() {
        super.onPreExecute();

        listener.onUploadTaskStarted();
        //ProgressBar pbHeaderProgress = (ProgressBar) context.get.findViewById(R.id.progressBarArticleLoading);

        //pbHeaderProgress.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onPostExecute(Integer integer) {

        super.onPostExecute(integer);
        // Notifiy whoever needs to be notified.
        listener.onUploadTaskFinished();

    }

}
