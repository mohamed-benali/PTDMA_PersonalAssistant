package com.example.myapplication.TextToSpeech;

public interface TextToSpeech {
    void speak(String text);
    void askConfirmDelete();

    void sayElementDontExist();

    void destroy();

    void taskDontExist();

    void didNotUnderstand();

    void listDontExist();

}
