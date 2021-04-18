package com.example.myapplication.TextToSpeech;

import android.content.Context;
import android.speech.tts.TextToSpeechService;
import android.util.Log;

import com.example.myapplication.TextToSpeech.TextToSpeech;

import java.util.Locale;

public class TextToSpeechImpl implements TextToSpeech {

    private final Context context;
    android.speech.tts.TextToSpeech tts  = null;

    public TextToSpeechImpl(Context context) {
        this.context = context;
    }

    private void init_and_speak(String text) {
        if(tts == null) {
            tts = new android.speech.tts.TextToSpeech(context, new android.speech.tts.TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    if(status == android.speech.tts.TextToSpeech.SUCCESS) {
                        int result = tts.setLanguage(new Locale("es", "ES"));

                        if (result == android.speech.tts.TextToSpeech.LANG_MISSING_DATA
                                || result == android.speech.tts.TextToSpeech.LANG_NOT_SUPPORTED) {
                            Log.e("TTS", "This Language is not supported");
                        } else {
                            speak(text);
                        }
                    }
                }

                @Override
                protected void finalize() throws Throwable {
                    //destroy();
                    super.finalize();
                }
            });
        }
        else speak(text);

    }

    @Override
    public void speak(String text) {
        tts.speak(text, android.speech.tts.TextToSpeech.QUEUE_FLUSH, null, "text");
    }

    @Override
    public void askConfirmDelete() {
        this.init_and_speak("si para confirmar");
        //this.init_and_speak("Say Yes to confirm");
    }

    @Override
    public void sayElementDontExist() {
        this.init_and_speak("El elemento no existe");
        //this.init_and_speak("The element does not exist");
    }

    @Override
    public void taskDontExist() {
        this.init_and_speak("La tarea no existe");
        //this.init_and_speak("The task does not exist");
    }

    @Override
    public void didNotUnderstand() {
        this.init_and_speak("Comando incorrecto");
        //this.init_and_speak("Incorrect command. Please click the info button for help.");
    }

    @Override
    public void listDontExist() {
        this.init_and_speak("La lista no existe");
        //this.init_and_speak("The list does not exist");
    }

    @Override
    public void eventDontExist() {
        this.init_and_speak("El evento no existe");
        //this.init_and_speak("The event does not exist");
    }

    @Override
    public void destroy() {
        if(tts != null) {
            tts.stop();
            tts.shutdown();
        }
    }


}
