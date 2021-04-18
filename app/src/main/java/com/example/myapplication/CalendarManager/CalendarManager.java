package com.example.myapplication.CalendarManager;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.icu.util.Calendar;
import android.net.Uri;
import android.provider.CalendarContract;
import android.util.Log;

import com.example.myapplication.Models.EventModel;

import java.util.List;

public class CalendarManager {
    Context context;

    public Context getContext() {
        return context;
    }
    public CalendarManager(Context context) {
        this.context = context;
    }

    public List<EventModel> getEvents() {
        Calendar beginTime = Calendar.getInstance();

        return null;

    }

    public Long getCaliID() { //gets first calendar, if there are not calendars, returns null
        final String[] EVENT_PROJECTION = new String[]{
                CalendarContract.Calendars._ID, // 0
                CalendarContract.Calendars.ACCOUNT_NAME, // 1
                CalendarContract.Calendars.CALENDAR_DISPLAY_NAME // 2
        };
        // The indices for the projection array above.
        final int PROJECTION_ID_INDEX = 0;
        final int PROJECTION_ACCOUNT_NAME_INDEX = 1;
        final int PROJECTION_DISPLAY_NAME_INDEX = 2;
        // Run query
        Cursor cur = null;
        ContentResolver cr = getContext().getContentResolver();
        Uri uri = CalendarContract.Calendars.CONTENT_URI;
        cur = cr.query(uri, EVENT_PROJECTION, null, null, null);

        while (cur.moveToNext()) {
            long calID = 0;
            String displayName = null;
            String accountName = null;
            String ownerName = null;

            // Get the field values
            calID = cur.getLong(PROJECTION_ID_INDEX);
            displayName = cur.getString(PROJECTION_DISPLAY_NAME_INDEX);
            accountName = cur.getString(PROJECTION_ACCOUNT_NAME_INDEX);

            Log.d("Cal id", " " + calID);
            Log.d("ACCOUNT_NAME", " " + accountName);
            Log.d("CALENDAR_DISPLAY_NAME", " " + displayName);
            return calID;
        }
        return null;
    }

    public void insertEvent(EventModel eventModel) {
        Long calID = this.getCaliID();

        long startMillis = 0;
        long endMillis = 0;
        Calendar beginTime = Calendar.getInstance();
        beginTime.set(2021, 4, 14, 7, 30);
        startMillis = beginTime.getTimeInMillis();
        Calendar endTime = Calendar.getInstance();
        endTime.set(2021, 4, 14, 8, 45);
        endMillis = endTime.getTimeInMillis();

        ContentResolver cr = getContext().getContentResolver();
        ContentValues values2 = new ContentValues();
        values2.put(CalendarContract.Events.DTSTART, startMillis);
        values2.put(CalendarContract.Events.DTEND, endMillis);
        values2.put(CalendarContract.Events.TITLE, "Jazzercise");
        values2.put(CalendarContract.Events.DESCRIPTION, "Group workout");
        values2.put(CalendarContract.Events.CALENDAR_ID, calID);
        values2.put(CalendarContract.Events.EVENT_TIMEZONE, "America/Los_Angeles");
        Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values2);

        // get the event ID that is the last element in the Uri
        long eventID = Long.parseLong(uri.getLastPathSegment());
    }


}
