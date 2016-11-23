package com.pureiconsulting.tt;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.pureiconsulting.tt.service.Ticket;

public class MainActivity extends AppCompatActivity implements
        TicketLoading.TicketLoadingListener, TicketUploading.TicketUploadingListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

            // Instantiates a new DownloadStateReceiver
        PullingStatusReceiver mDownloadStateReceiver = new PullingStatusReceiver();

        // The filter's action is BROADCAST_ACTION
        IntentFilter mStatusIntentFilter = new IntentFilter(PullService.BROADCAST_PULLING_START);
        // Registers the DownloadStateReceiver and its intent filters
        LocalBroadcastManager.getInstance(this).registerReceiver(mDownloadStateReceiver, mStatusIntentFilter);

        IntentFilter mStatusIntentFilter2 = new IntentFilter(PullService.BROADCAST_PULLING_END);
        LocalBroadcastManager.getInstance(this).registerReceiver(mDownloadStateReceiver, mStatusIntentFilter2);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_help) {
            loadHelp();
            return true;
        } else if(id == R.id.action_about) {
            loadAbout();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onStop() {
        super.onStop();  // Always call the superclass method first

    }

    @Override
    public void onLoadTaskStarted() {

        ProgressBar pb = (ProgressBar) findViewById(R.id.pbTicketLoading);
        pb.setVisibility(View.VISIBLE);

    }

    @Override
    public void onLoadTaskFinished() {


            ProgressBar pb = (ProgressBar) findViewById(R.id.pbTicketLoading);
            pb.setVisibility(View.GONE);

            TextView textViewUnScanned = (TextView) findViewById(R.id.textViewUnScanned);
            textViewUnScanned.setText("" + Ticket.getUnscannedTickets().size());

            TextView textViewScanned = (TextView) findViewById(R.id.textViewScanned);
            textViewScanned.setText("" + Ticket.getScannedTickets().size());

    }


    @Override
    public void onUploadTaskStarted() {

        ProgressBar pb = (ProgressBar) findViewById(R.id.pbTicketPulling);
        pb.setVisibility(View.VISIBLE);

    }

    @Override
    public void onUploadTaskFinished() {


        ProgressBar pb = (ProgressBar) findViewById(R.id.pbTicketPulling);
        pb.setVisibility(View.GONE);

        //TextView textViewUnScanned = (TextView) findViewById(R.id.textViewUnScanned);
        //textViewUnScanned.setText("" + Ticket.getUnscannedTickets().size());

        TextView textViewScanned = (TextView) findViewById(R.id.textViewScanned);
        textViewScanned.setText("" + Ticket.getScannedTickets().size());

    }


    private class PullingStatusReceiver extends BroadcastReceiver
    {
        // Prevents instantiation
        private PullingStatusReceiver() {
        }
        // Called when the BroadcastReceiver gets an Intent it's registered to receive
        @Override
        public void onReceive(Context context, Intent intent) {


            String action = intent.getAction();
            //Toast.makeText(context, action, Toast.LENGTH_SHORT).show();
            ProgressBar pb = null;
            ProgressBar pb1 = null;
            switch (action) {
                case PullService.BROADCAST_PULLING_START:

                    pb = (ProgressBar) findViewById(R.id.pbTicketPulling);
                    if(pb != null) pb.setVisibility(View.VISIBLE);


                    pb1 = (ProgressBar) findViewById(R.id.pbTicketLoading);
                    if(pb1 != null) pb1.setVisibility(View.VISIBLE);

                    break;
                case PullService.BROADCAST_PULLING_END:
                    pb = (ProgressBar) findViewById(R.id.pbTicketPulling);
                    if(pb != null) pb.setVisibility(View.GONE);

                    pb1 = (ProgressBar) findViewById(R.id.pbTicketLoading);
                    if(pb1 != null) pb1.setVisibility(View.GONE);

                    TextView textViewScanned = (TextView) findViewById(R.id.textViewScanned);
                    if(textViewScanned != null) textViewScanned.setText("" + Ticket.getScannedTickets().size());


                    TextView textViewUnScanned = (TextView) findViewById(R.id.textViewUnScanned);
                    if(textViewUnScanned !=null) textViewUnScanned.setText("" + Ticket.getUnscannedTickets().size());

                    break;
                default:
                    break;

            }


        }
    }

    public void loadAbout() {
        //WebViewFragment wv = WebViewFragment.getInstance(getString(R.string.WEBVIEW_URL_ABOUT));
        WebViewFragment wv = new WebViewFragment();

            /**/
        Bundle args = new Bundle();
        args.putString(getString(R.string.WEBVIEW_URL_KEY), getString(R.string.WEBVIEW_URL_ABOUT));
        wv.setArguments(args);
            /**/

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.fragmentMain, wv, null);
        //if(lastItemId != id)
        transaction.addToBackStack(null);
        transaction.commit();

    }

    public void loadHelp() {

        WebViewFragment wv = new WebViewFragment();

            /**/
        Bundle args = new Bundle();
        args.putString(getString(R.string.WEBVIEW_URL_KEY), getString(R.string.WEBVIEW_URL_HELP));
        wv.setArguments(args);
            /**/

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.fragmentMain, wv, null);
        //if(lastItemId != id)
        transaction.addToBackStack(null);
        transaction.commit();

    }


}
