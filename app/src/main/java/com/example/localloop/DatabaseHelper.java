package com.example.localloop;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.localloop.model.Category;
import com.example.localloop.model.Event;
import com.example.localloop.model.JoinRequest;
import com.example.localloop.model.User;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "LocalLoop.db";
    public static final int DATABASE_VERSION = 3;

    public static final String TABLE_USERS = "users";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";

    private static final String CREATE_TABLE_USERS =
            "CREATE TABLE " + TABLE_USERS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_USERNAME + " TEXT NOT NULL, " +
                    COLUMN_PASSWORD + " TEXT NOT NULL, " +
                    "firstname TEXT, " +
                    "role TEXT, " +
                    "disabled INTEGER DEFAULT 0);";


    public static final String TABLE_CATEGORIES = "categories";
    public static final String COLUMN_CAT_ID = "id";
    public static final String COLUMN_CAT_NAME = "name";

    private static final String CREATE_TABLE_CATEGORIES =
            "CREATE TABLE " + TABLE_CATEGORIES + " (" +
                    COLUMN_CAT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_CAT_NAME + " TEXT NOT NULL, " +
                    "description TEXT);";

    public static final String TABLE_EVENTS = "events";
    public static final String COLUMN_EVENT_ID = "id";
    public static final String COLUMN_EVENT_NAME = "name";
    public static final String COLUMN_EVENT_DESCRIPTION = "description";
    public static final String COLUMN_EVENT_FEE = "fee";
    public static final String COLUMN_EVENT_CATEGORY_ID = "categoryId";
    public static final String COLUMN_EVENT_DATE = "date";
    public static final String COLUMN_EVENT_TIME = "time";

    private static final String CREATE_TABLE_EVENTS =
            "CREATE TABLE " + TABLE_EVENTS + " (" +
                    COLUMN_EVENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_EVENT_NAME + " TEXT NOT NULL, " +
                    COLUMN_EVENT_DESCRIPTION + " TEXT, " +
                    COLUMN_EVENT_FEE + " REAL, " +
                    COLUMN_EVENT_CATEGORY_ID + " INTEGER, " +
                    COLUMN_EVENT_DATE + " TEXT, " +
                    COLUMN_EVENT_TIME + " TEXT, " +
                    "organizerUsername TEXT, " +
                    "FOREIGN KEY(" + COLUMN_EVENT_CATEGORY_ID + ") REFERENCES " +
                    TABLE_CATEGORIES + "(" + COLUMN_CAT_ID + "));";


    public static final String TABLE_EVENT_JOINS = "EventJoins";

    private static final String CREATE_TABLE_EVENT_JOINS =
            "CREATE TABLE IF NOT EXISTS " + TABLE_EVENT_JOINS + " (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "username TEXT, " +
                    "eventId INTEGER, " +
                    "status TEXT);";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_CATEGORIES);
        db.execSQL(CREATE_TABLE_EVENTS);
        db.execSQL(CREATE_TABLE_EVENT_JOINS); // new table for joins

        // Default admin account
        ContentValues admin = new ContentValues();
        admin.put(COLUMN_USERNAME, "admin");
        admin.put(COLUMN_PASSWORD, "XPI76SZUqyCjVxgnUjm0");
        admin.put("firstname", "Admin");
        admin.put("role", "admin");
        db.insert(TABLE_USERS, null, admin);

        // Default categories
        ContentValues cat1 = new ContentValues();
        cat1.put(COLUMN_CAT_NAME, "Sports");
        cat1.put("description", "Sports events");
        db.insert(TABLE_CATEGORIES, null, cat1);

        ContentValues cat2 = new ContentValues();
        cat2.put(COLUMN_CAT_NAME, "Tech");
        cat2.put("description", "Tech-related events");
        db.insert(TABLE_CATEGORIES, null, cat2);

        ContentValues cat3 = new ContentValues();
        cat3.put(COLUMN_CAT_NAME, "Music");
        cat3.put("description", "Musical events and shows");
        db.insert(TABLE_CATEGORIES, null, cat3);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENT_JOINS);
        onCreate(db);
    }

    public long insertUser(User user, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, user.getUsername());
        values.put(COLUMN_PASSWORD, password);
        values.put("firstname", user.getFirstName());
        values.put("role", user.getRole());
        long id = db.insert(TABLE_USERS, null, values);
        db.close();
        return id;
    }

    public long insertEvent(Event event) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_EVENT_NAME, event.getName());
        values.put(COLUMN_EVENT_DESCRIPTION, event.getDescription());
        values.put(COLUMN_EVENT_FEE, event.getFee());
        values.put(COLUMN_EVENT_CATEGORY_ID, event.getCategoryId());
        values.put(COLUMN_EVENT_DATE, event.getDate());
        values.put(COLUMN_EVENT_TIME, event.getTime());
        long id = db.insert(TABLE_EVENTS, null, values);
        db.close();
        return id;
    }

    public long insertCategory(Category category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CAT_NAME, category.getName());
        values.put("description", category.getDescription());
        long id = db.insert(TABLE_CATEGORIES, null, values);
        db.close();
        return id;
    }

    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CATEGORIES,
                new String[]{COLUMN_CAT_ID, COLUMN_CAT_NAME, "description"},
                null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Category category = new Category();
                category.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CAT_ID)));
                category.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CAT_NAME)));
                category.setDescription(cursor.getString(cursor.getColumnIndexOrThrow("description")));
                categories.add(category);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return categories;
    }

    public List<Event> getAllEvents() {
        List<Event> events = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_EVENTS, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Event event = new Event();
                event.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_EVENT_ID)));
                event.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EVENT_NAME)));
                event.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EVENT_DESCRIPTION)));
                event.setFee(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_EVENT_FEE)));
                event.setCategoryId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_EVENT_CATEGORY_ID)));
                event.setDate(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EVENT_DATE)));
                event.setTime(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EVENT_TIME)));
                events.add(event);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return events;
    }

    public int updateEvent(Event event) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_EVENT_NAME, event.getName());
        values.put(COLUMN_EVENT_DESCRIPTION, event.getDescription());
        values.put(COLUMN_EVENT_FEE, event.getFee());
        values.put(COLUMN_EVENT_CATEGORY_ID, event.getCategoryId());
        values.put(COLUMN_EVENT_DATE, event.getDate());
        values.put(COLUMN_EVENT_TIME, event.getTime());
        int rows = db.update(TABLE_EVENTS, values, COLUMN_EVENT_ID + " = ?", new String[]{String.valueOf(event.getId())});
        db.close();
        return rows;
    }

    public List<String> getAllCategoryNames() {
        List<String> names = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CATEGORIES, new String[]{COLUMN_CAT_NAME}, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                names.add(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CAT_NAME)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return names;
    }

    public User getUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE username=? AND password=?", new String[]{username, password});
        if (cursor.moveToFirst()) {
            User user = new User();
            user.setUsername(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USERNAME)));
            user.setFirstName(cursor.getString(cursor.getColumnIndexOrThrow("firstname")));
            user.setRole(cursor.getString(cursor.getColumnIndexOrThrow("role")));
            user.setDisabled(cursor.getInt(cursor.getColumnIndexOrThrow("disabled")) == 1);  // 👈 critical fix
            cursor.close();
            return user;
        }
        cursor.close();
        return null;
    }

    // Check if a user has joined an event
    public boolean hasUserJoinedEvent(String username, int eventId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_EVENT_JOINS + " WHERE username = ? AND eventId = ?", new String[]{username, String.valueOf(eventId)});
        boolean exists = cursor.moveToFirst();
        cursor.close();
        return exists;
    }

    // Add a user to an event
    public void joinEvent(String username, int eventId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("eventId", eventId);
        values.put("status", "pending");
        db.insert("EventJoins", null, values);
        db.close();
    }

    public void deleteCategoryById(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CATEGORIES, COLUMN_CAT_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public void deleteEventById(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_EVENTS, COLUMN_EVENT_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public List<Event> getEventsByOrganizer(String organizerUsername) {
        List<Event> eventList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Events WHERE organizerUsername = ?", new String[]{organizerUsername});
        if (cursor.moveToFirst()) {
            do {
                Event event = new Event();
                event.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                event.setName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
                event.setDescription(cursor.getString(cursor.getColumnIndexOrThrow("description")));
                event.setFee(cursor.getDouble(cursor.getColumnIndexOrThrow("fee")));
                event.setCategoryId(cursor.getInt(cursor.getColumnIndexOrThrow("categoryId")));
                event.setDate(cursor.getString(cursor.getColumnIndexOrThrow("date")));
                event.setTime(cursor.getString(cursor.getColumnIndexOrThrow("time")));
                event.setOrganizerUsername(cursor.getString(cursor.getColumnIndexOrThrow("organizerUsername")));
                eventList.add(event);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return eventList;
    }

    public List<JoinRequest> getJoinRequestsByOrganizer(String organizerUsername) {
        List<JoinRequest> requests = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT r.id, r.eventId, r.username, r.status " +
                "FROM EventJoins r INNER JOIN Events e ON r.eventId = e.id " +
                "WHERE e.organizerUsername = ?";

        Cursor cursor = db.rawQuery(query, new String[]{organizerUsername});
        if (cursor.moveToFirst()) {
            do {
                JoinRequest jr = new JoinRequest();
                jr.setId(cursor.getInt(0));
                jr.setEventId(cursor.getInt(1));
                jr.setAttendeeUsername(cursor.getString(2));
                jr.setStatus(cursor.getString(3));
                requests.add(jr);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return requests;
    }



    public void updateRequestStatus(int requestId, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("status", status);
        db.update("EventJoins", values, "id = ?", new String[]{String.valueOf(requestId)});
    }

    public void addEvent(Event event) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", event.getName());
        values.put("description", event.getDescription());
        values.put("fee", event.getFee());
        values.put("categoryId", event.getCategoryId());
        values.put("date", event.getDate());
        values.put("time", event.getTime());
        values.put("organizerUsername", event.getOrganizerUsername());

        db.insert("Events", null, values);
        db.close();
    }

    public int updateCategory(Category c) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", c.getName());
        values.put("description", c.getDescription());
        int rows = db.update(TABLE_CATEGORIES, values, COLUMN_CAT_ID + " = ?", new String[]{String.valueOf(c.getId())});
        db.close();
        return rows;
    }

    public Category getCategoryById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.query(TABLE_CATEGORIES, null,
                COLUMN_CAT_ID + "=?", new String[]{String.valueOf(id)},
                null, null, null);
        if (!cur.moveToFirst()) {
            cur.close();
            return null;
        }
        Category c = new Category(
                cur.getInt(cur.getColumnIndexOrThrow(COLUMN_CAT_ID)),
                cur.getString(cur.getColumnIndexOrThrow(COLUMN_CAT_NAME)),
                cur.getString(cur.getColumnIndexOrThrow("description")));
        cur.close();
        db.close();
        return c;
    }

    public boolean categoryNameExists(String name, int excludeId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT 1 FROM " + TABLE_CATEGORIES +
                        " WHERE " + COLUMN_CAT_NAME + "=? AND " + COLUMN_CAT_ID + "!=?",
                new String[]{name, String.valueOf(excludeId)});
        boolean exists = c.moveToFirst();
        c.close();
        db.close();
        return exists;
    }

    public List<User> getAllNonAdminUsers() {
        List<User> users = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Users WHERE role != 'admin'", null);
        while (cursor.moveToNext()) {
            User user = new User();
            user.setUsername(cursor.getString(cursor.getColumnIndexOrThrow("username")));
            user.setRole(cursor.getString(cursor.getColumnIndexOrThrow("role")));
            user.setDisabled(cursor.getInt(cursor.getColumnIndexOrThrow("disabled")) == 1);
            users.add(user);
        }
        cursor.close();
        return users;
    }

    public void setUserDisabled(String username, boolean disabled) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("disabled", disabled ? 1 : 0);
        db.update("Users", values, "username=?", new String[]{username});
    }

    public void deleteUserByUsername(String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("Users", "username=?", new String[]{username});
    }



}
