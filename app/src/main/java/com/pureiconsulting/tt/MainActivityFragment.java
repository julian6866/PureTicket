package com.pureiconsulting.tt;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.pureiconsulting.tt.service.Ticket;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);

    }

    boolean isContinue = false;

    @Override
    public void onResume() {

        super.onResume();
        View view = getView();

        final TextView textViewUnScanned = (TextView) view.findViewById(R.id.textViewUnScanned);
        textViewUnScanned.setText("" + Ticket.getUnscannedTickets().size());
        //textViewUnScanned.setText("Test");

        final TextView textViewScanned = (TextView) view.findViewById(R.id.textViewScanned);
        textViewScanned.setText("" + Ticket.getScannedTickets().size());
        //textViewUnScanned.setText("Test");

        final TextView textViewFail = (TextView) view.findViewById(R.id.textViewFail);
        textViewFail.setText("" + Ticket.getFailScans());

        final TextView textViewSuccess = (TextView) view.findViewById(R.id.textViewSuccess);
        textViewSuccess.setText("" + Ticket.getSuccessScans());

        final TextView tvPullingTime = (TextView) view.findViewById(R.id.tvPullingTime);
        tvPullingTime.setText("" + PullService.getSleepTime());

        final TextView tvAlert = (TextView) view.findViewById(R.id.tvAlert);


        int msec = PullService.getSleepTime();
        int min = msec/60000;
        int sec = (msec/1000 - min*60);
        String secStr = sec + " secs";
        String minStr = (min > 0)?min + " mins and ":"";
        String display = (min == 0 && sec == 0)?"Sync Off":"Sync every " + minStr + secStr;
        tvPullingTime.setText(display);

        final ImageButton bnDownload = (ImageButton) view.findViewById(R.id.bnDownload);
        bnDownload.setOnClickListener(new CompoundButton.OnClickListener() {
            @Override
            public void onClick(View v) {

                //NewsListAdapter adatper = (NewsListAdapter) getListAdapter();
                //Toast.makeText(getContext(), "Article Speaking .... called: ", Toast.LENGTH_SHORT).show();

                //Toast.makeText(getContext(), "Start Scanning", Toast.LENGTH_SHORT).show();
                //scanQRCode();
                //Ticket.loadTickets(v.getContext());
                new TicketLoading(v.getContext(), (TicketLoading.TicketLoadingListener) getActivity()).execute();

            }
        });

        final ImageButton bnUpload = (ImageButton) view.findViewById(R.id.bnUpload);
        bnUpload.setOnClickListener(new CompoundButton.OnClickListener() {
            @Override
            public void onClick(View v) {

                //NewsListAdapter adatper = (NewsListAdapter) getListAdapter();
                //Toast.makeText(getContext(), "Article Speaking .... called: ", Toast.LENGTH_SHORT).show();

                //Toast.makeText(getContext(), "Start Scanning", Toast.LENGTH_SHORT).show();
                //scanQRCode();
                //Ticket.loadTickets(v.getContext());
                //new TicketLoading(v.getContext(), (TicketLoading.TicketLoadingListener) getActivity()).execute();
               // Ticket.uploadTickets(v.getContext());

                new TicketUploading(v.getContext(), (TicketUploading.TicketUploadingListener) getActivity()).execute();

            }
        });

        final ImageButton bnScan = (ImageButton) view.findViewById(R.id.bnScan);
        bnScan.setOnClickListener(new CompoundButton.OnClickListener() {
            @Override
            public void onClick(View v) {

                //NewsListAdapter adatper = (NewsListAdapter) getListAdapter();
                //Toast.makeText(getContext(), "Article Speaking .... called: ", Toast.LENGTH_SHORT).show();

                //Toast.makeText(getContext(), "Start Scanning", Toast.LENGTH_SHORT).show();
                tvAlert.setText("");

                scanQRCode();

            }
        });

        SeekBar sb = (SeekBar) view.findViewById(R.id.sbPullSleepTime);
        sb.setProgress(PullService.getSleepTime() / 1000);    // convert to seconds
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                PullService.setSleepTime(1000 * progress);
                if (progress == 0) {
                    PullService.setAutoSync(false);
                } else {
                    PullService.setAutoSync(true);
                }

                int msec = PullService.getSleepTime();
                int min = msec/60000;
                int sec = (msec/1000 - min*60);
                String secStr = sec + " secs";
                String minStr = (min > 0)?min + " mins and ":"";
                String display = (min == 0 && sec == 0)?"Sync Off":"Sync every " + minStr + secStr;
                tvPullingTime.setText(display);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        final TextView tvAuthCode = (TextView) view.findViewById(R.id.tvAuthCode);
        tvAuthCode.setOnClickListener(new CompoundButton.OnClickListener() {
            @Override
            public void onClick(View v) {

                final EditText authCodeEdit = new EditText(v.getContext());

                // Set the default text to a link of the Queen
                authCodeEdit.setHint("AUTH CODE");

                new AlertDialog.Builder(v.getContext())
                        .setTitle("Auth Code")
                        .setMessage("Please input auth code")
                        .setView(authCodeEdit)
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                String authCode = authCodeEdit.getText().toString();
                                //moustachify(null, url);
                                //Log.i("Got you", url);
                                Ticket.setAuthCode(authCode);
                                new TicketLoading(getContext(), (TicketLoading.TicketLoadingListener) getActivity()).execute();

                                Ticket.resetFailScans();
                                Ticket.resetSuccessScans();
                                textViewFail.setText("0");
                                textViewSuccess.setText("0");
                                tvAlert.setText("");

                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                            }
                        })
                        .show();

            }
        });

        tvAlert.setOnClickListener(new CompoundButton.OnClickListener() {
            @Override
            public void onClick(View v) {

                tvAlert.setText("");

            }
        });
    }

    public void scanQRCode() {

        PackageManager pm = getActivity().getPackageManager();
        Intent intent = new Intent("com.google.zxing.client.android.SCAN");
        if(intent.resolveActivity(pm) != null) {
            intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
            startActivityForResult(intent, 0);
        } else {
            //Toast.makeText(getContext(), "Not installed.", Toast.LENGTH_SHORT).show();
            new android.support.v7.app.AlertDialog.Builder(getContext())
                    .setTitle("Sorry")
                    .setMessage("You don't have QR Code Scanner app installed. \n\nPlease go to Google Play Store and download & install an QR Code Scanner App.\n\n")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    /** .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing

                    }
                    }) **/
               .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();

        }
    }

    // handle when scanning results return
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

        if (requestCode == 0) {

            if (resultCode == Activity.RESULT_OK) {
                String contents = intent.getStringExtra("SCAN_RESULT"); // This will contain your scan result
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");

                //Toast.makeText(getContext(), contents, Toast.LENGTH_SHORT).show();

                Ticket ticket = new Ticket(contents);
                final TextView tvAlert = (TextView) getView().findViewById(R.id.tvAlert);

                if (Ticket.scan(ticket)) {
                    // scanned successfully
                    //scanQRCode();
                    //isContinue = false;
                    Ticket.countSucessScan();
                    tvAlert.setTextColor(Color.parseColor("#008800"));
                    tvAlert.setText("OK");

                } else {

                    //isContinue = false;

                    Ticket.countFailScan();
                    tvAlert.setTextColor(Color.RED);
                    tvAlert.setText("Invalid");

                    /**
                    RelativeLayout layout = (RelativeLayout) getView().findViewById(R.id.layoutMain);
                    if(layout != null)
                        layout.setBackgroundColor(Color.RED);
                    **/
                }
            }
        } else {

            isContinue = false;
        }
    }




}
