package com.pureiconsulting.tt.service;

import android.content.Context;
import android.os.Build;
import android.speech.tts.TextToSpeech;

import java.util.Locale;

/**
 * Created by julianzhu on 11/17/15.
 */
public class Voice {

    private static Voice instance = null;

    private TextToSpeech ttsEngine = null;

    private boolean canSpeak = false;

    private boolean isSpeaking = false;

    public Voice(Context context) {

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && ttsEngine == null) {
            ttsEngine = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    if(status != TextToSpeech.ERROR) {
                        //int result = ttsEngine.setLanguage(Locale.US);
                        int result = ttsEngine.setLanguage(Locale.CHINA);
                        if(result == TextToSpeech.LANG_MISSING_DATA ||
                                result == TextToSpeech.LANG_NOT_SUPPORTED){

                                canSpeak = false;
                        } else {
                            canSpeak = true;
                        }

                    }
                    //Log.i("Text to speech", "status: " + status);
                }
            });
            //ttsEngine.setLanguage(Locale.US);
            //t1.speak("We are here to help you!", TextToSpeech.QUEUE_FLUSH, null, null);
        }


    }

    public static Voice getInstance(Context context) {

        if(instance == null) {
            instance = new Voice(context);
        }

        return instance;
    }

    public boolean canSpeak() {
        return canSpeak;
    }


    public boolean isSpeaking() {

        return isSpeaking;

    }

    public void setSpeaking(boolean isSpeaking) {
        this.isSpeaking = isSpeaking;
    }

    public void stop() {

        if(ttsEngine != null) {
            ttsEngine.stop();
        }
        isSpeaking = false;

    }

    public TextToSpeech getEngine() {
        return ttsEngine;
    }

    public void speak(CharSequence toSpeak) {

        try {
            if(canSpeak) {
                this.isSpeaking = true;
                ttsEngine.speak(toSpeak, TextToSpeech.QUEUE_ADD, null, null);
            }
        } catch (Exception e) {

        }
    }

}
