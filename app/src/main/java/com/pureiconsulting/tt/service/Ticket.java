package com.pureiconsulting.tt.service;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by julianzhu on 12/1/15.
 */
public class Ticket {

    private String id = "";

    private static String authCode = "";

    // use this to store all new unscanned tickets
    private static ArrayList<Ticket> unscanned = new ArrayList<>();

    // use this to store all scanned tickets
    private static ArrayList<Ticket> scanned = new ArrayList<>();

    private static int successScans = 0;

    private static int failScans = 0;

    public Ticket(String id) {

        this.id = id;
    }

    public boolean equals(Ticket o) {
        return this.id.equals(o.getId()); // if Id matchese, they are the same ticket.
    }

    public static String getAuthCode() {
        return authCode;
    }

    public static void setAuthCode(String authCode) {
        Ticket.authCode = authCode;
    }

    public static void loadTickets(Context context) {

        Cache cache = Cache.getInstance(context);
        String str = cache.readFromURL("https://script.google.com/macros/s/AKfycbw2Oo3cC1vmRJvjAqU61GKBj9l8w7WGcbuZAJ5WuqjAteehiYNz/exec?code=" + authCode);
        //https://script.google.com/macros/s/AKfycbw2Oo3cC1vmRJvjAqU61GKBj9l8w7WGcbuZAJ5WuqjAteehiYNz/exec
        if(str == null) return; // do nothing if nothing is retrieved from server.

        synchronized (unscanned) {
            unscanned.clear();
            String[] tickets = str.split(":");
            for (int i = 0; i < tickets.length; i++) {

                if (tickets[i] != null && !tickets[i].trim().equals("") && !tickets[i].trim().equals("FAIL")) {
                    Ticket ticket = new Ticket(tickets[i]);
                    //ticket.setId(tickets[i]);

                    unscanned.add(ticket);
                }
            }
        }
        Log.i("str", str);
    }

    public static void uploadTickets(Context context) {
        //ArrayList<Ticket> tickets = Ticket.getScannedTickets();
        synchronized (scanned) {
            if (scanned.size() > 0) {

                String ticketStr = ":";
                for (int i = 0; i < scanned.size(); i++) {
                    ticketStr += scanned.get(i).getId() + ":";
                }

                Cache cache = Cache.getInstance(context);
                String url = "https://script.google.com/macros/s/AKfycbw2Oo3cC1vmRJvjAqU61GKBj9l8w7WGcbuZAJ5WuqjAteehiYNz/exec";
                String str = cache.readFromURL(url + "?a=scan&t=" + ticketStr + "&code=" + authCode);

                if(str != null && str.toUpperCase().contains("SUCCESS")) {
                    // clear it up only when server contact was successful
                    Ticket.clearScannedTickets();
                }
            }
        }

    }

    public static void countFailScan() {
        failScans ++;
    }

    public static void countSucessScan() {
        successScans ++;
    }

    public static int getFailScans() {
        return failScans;
    }

    public static int getSuccessScans() {
        return successScans;
    }

    public static void resetSuccessScans() {
        successScans = 0;
    }

    public static void resetFailScans() {
        failScans = 0;
    }

    // return true if successfully, false if not found
    public static boolean scan(Ticket ticket) {

        for(int i=0; i < unscanned.size(); i++) {
            Ticket obj = unscanned.get(i);
            if(ticket.equals(obj)) {
                synchronized (unscanned) {
                    unscanned.remove(i);
                }

                synchronized (scanned) {
                    scanned.add(ticket);
                }
                return true;
            }

        }
        return false;

    }

    public synchronized static ArrayList<Ticket> getUnscannedTickets() {
        return unscanned;
    }

    public synchronized static ArrayList<Ticket> getScannedTickets() {
        return scanned;
    }

    public synchronized static void clearScannedTickets() {
        scanned.clear();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
