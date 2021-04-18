package com.example.myapplication.CalendarManager;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.icu.util.Calendar;
import android.net.Uri;
import android.provider.CalendarContract;
import android.text.format.DateUtils;
import android.util.Log;

import com.example.myapplication.Models.EventModel;
import com.example.myapplication.PermissionsManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Pattern;

public class CalendarManager {
    Context context;

    Long calID;

    public Context getContext() {
        return context;
    }
    public CalendarManager(Context context) {
        this.context = context;
        //PermissionsManager permissionsManager = new PermissionsManager((Activity) this.getContext());
        //if(permissionsManager.calendarPermissionNotGranted()) permissionsManager.checkCalendarPermission();
        calID = getCaliID();

    }

    public Long getCaliID() { //gets first calendar, if there are not calendars, returns null
        final String[] EVENT_PROJECTION = new String[]{
                CalendarContract.Calendars._ID, // 0
                CalendarContract.Calendars.ACCOUNT_NAME, // 1
                CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, // 2
                CalendarContract.Calendars.OWNER_ACCOUNT // 3
        };
        // The indices for the projection array above.
        final int PROJECTION_ID_INDEX = 0;
        final int PROJECTION_ACCOUNT_NAME_INDEX = 1;
        final int PROJECTION_DISPLAY_NAME_INDEX = 2;
        final int PROJECTION_OWNER_ACCOUNT_INDEX = 3;
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
            ownerName = cur.getString(PROJECTION_OWNER_ACCOUNT_INDEX);

            Log.d("-------------------","---------------------------");
            Log.d("Cal id", " " + calID);
            Log.d("ACCOUNT_NAME", " " + accountName);
            Log.d("CALENDAR_DISPLAY_NAME", " " + displayName);
            Log.d("OWNER_NAME", " " + ownerName);
        }
        cur.moveToPosition(-1);
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

    public List<EventModel> getEvents() {
        return readCalendarEvent(context);
    }
    public List<EventModel> readCalendarEvent(Context context) {
        long now = new Date().getTime();

        Uri.Builder builder = Uri.parse("content://com.android.calendar/instances/when").buildUpon();
        ContentUris.appendId(builder, now - DateUtils.DAY_IN_MILLIS * 10000);
        ContentUris.appendId(builder, now + DateUtils.DAY_IN_MILLIS * 10000);

        Cursor cursor = context.getContentResolver().query(builder.build(),
                                            new String[]  {CalendarContract.Instances.TITLE,
                                                    CalendarContract.Instances.BEGIN,
                                                    CalendarContract.Instances.END,
                                                    CalendarContract.Instances.DESCRIPTION,
                                                    CalendarContract.Instances.EVENT_ID,},
                                           CalendarContract.Instances.CALENDAR_ID+"=" + this.calID,
                                            null, "startDay ASC");

        cursor.moveToFirst();
        // fetching calendars name

        List<EventModel> eventModelList = new ArrayList<>();
        // fetching calendars id
        if(cursor.moveToFirst()) {
            Log.d("EVENTS", "The events exist" );
            do {
                Log.d("EVENTS do while", "Enter" );
                final String title = cursor.getString(0);
                final Date begin = new Date(cursor.getLong(1));
                final Date end = new Date(cursor.getLong(2));
                final String description = cursor.getString(3);
                final Long eventId = cursor.getLong(4);

                Pattern p = Pattern.compile(" ");
                String[] items = p.split(begin.toString());

                String month = String.valueOf(begin.getDate());
                String day = String.valueOf(begin.getMonth()+1);
                String year = String.valueOf(begin.getYear()+1900);
                EventModel eventModel = new EventModel(title, description,year,month,day, eventId);

                eventModelList.add(eventModel);
            } while (cursor.moveToNext());
        }
        return eventModelList;
    }

    public void insertEvent(EventModel eventModel) {
        long startMillis = 0;
        long endMillis = 0;
        Calendar beginTime = Calendar.getInstance();
        int year = Integer.parseInt(eventModel.getYear());
        int month = Integer.parseInt(eventModel.getMonth())-1;
        int day = Integer.parseInt(eventModel.getDay());

        beginTime.set(year, month, day, 12, 0);
        startMillis = beginTime.getTimeInMillis();
        Calendar endTime = Calendar.getInstance();
        endTime.set(year, month, day, 12, 0);
        endMillis = endTime.getTimeInMillis();

        ContentResolver cr = getContext().getContentResolver();
        ContentValues values2 = new ContentValues();
        values2.put(CalendarContract.Events.DTSTART, startMillis);
        values2.put(CalendarContract.Events.DTEND, endMillis);
        values2.put(CalendarContract.Events.TITLE, eventModel.getTitle());
        values2.put(CalendarContract.Events.DESCRIPTION, eventModel.getDescription());
        values2.put(CalendarContract.Events.CALENDAR_ID, calID);
        values2.put(CalendarContract.Events.EVENT_TIMEZONE, "Europe/Madrid");
        Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values2);

        // get the event ID that is the last element in the Uri
        long eventID = Long.parseLong(uri.getLastPathSegment());
    }


    public boolean eventExists(String id) {
        List<EventModel> list = this.getEvents();
        for(EventModel model : list) {
            if(model.getTitle().equals(id)) return true;
        }
        return false;
    }

    public EventModel getEventbyID(String id) {
        List<EventModel> list = this.getEvents();
        for(EventModel model : list) {
            if(model.getTitle().equals(id)) return model;
        }
        return null;
    }

    public void deleteEventById(String id_forDelete) {
        EventModel eventModel = getEventbyID(id_forDelete);
        long eventID = eventModel.getEventID();
        ContentResolver cr = getContext().getContentResolver();
        Uri deleteUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, eventID);
        int rows = cr.delete(deleteUri, null, null);

    }
}
