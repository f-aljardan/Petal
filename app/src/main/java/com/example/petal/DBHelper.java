package com.example.petal;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DBNAME = "PETAL.db";
    public static final String USERSTB = "USERS";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String PLANTS = "PLANTS";
    public static final String ITEMID = "id";
    public static final String NAME = "name";
    public static final String MOBILE = "mobile";
    public static final String DESCRIPTION = "description";
    public static final String SIZE = "size";
    public static final String SUN_EXPOSURE = "sun_exposure";
    public static final String SOIL_TYPE = "soil_type";
    public static final String IMAGE = "image";

    public static final String OWNER = "owner";
    public static final String RENTAL_ORDERS = "rental_orders";
    public static final String RENTED_ITEM = "rented_item";
    public static final String RENTER = "renter";
    public static final String ORDER_DATE = "orderDate";
    public static final String ORDERID = "tid";
    public static final String IS_RENTED = "isRENTED";
    public static final String RENT = "rent_plant";


    public DBHelper(@Nullable Context context) {
        super(context, DBNAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create Table " + USERSTB + "(" + USERNAME + " VARCHAR(20) primary key, " + PASSWORD + " VARCHAR(20), " + MOBILE + " VARCHAR(10));");
        sqLiteDatabase.execSQL("create Table " + PLANTS + "(" + ITEMID + " INTEGER primary key AUTOINCREMENT, " + NAME + " VARCHAR(20), " + DESCRIPTION + " VARCHAR(150), " + SIZE + " VARCHAR(20), " + SUN_EXPOSURE + " VARCHAR(20), " + SOIL_TYPE + " VARCHAR(20), " + IMAGE + " BLOB, " + IS_RENTED + " boolean default 'false', " + OWNER + " VARCHAR(20), foreign key (" + OWNER + ") references " + USERSTB + "(" + USERNAME + ")  );");
        sqLiteDatabase.execSQL("create table " + RENTAL_ORDERS + " (" + ORDERID + " INTEGER primary key autoincrement, " + RENTED_ITEM + " integer, " + RENTER + " varchar(20), " + ORDER_DATE + " datetime DEFAULT(current_timestamp) , foreign key (" + RENTER + ") references " + USERSTB + " (" + USERNAME + "), foreign key (" + RENTED_ITEM + ") references " + PLANTS + " (" + ITEMID + ")); ");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + RENTAL_ORDERS);
        db.execSQL("DROP TABLE IF EXISTS " + PLANTS);
        db.execSQL("DROP TABLE IF EXISTS " + USERSTB);
        onCreate(db);
    }

    public long insertPlant(Plant plant) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NAME, plant.getName());
        values.put(DESCRIPTION, plant.getDescription());
        values.put(SIZE, plant.getSize());
        values.put(SUN_EXPOSURE, plant.getSunExposure());
        values.put(SOIL_TYPE, plant.getSoilType());
        values.put(IMAGE, plant.getImageBytes());
        values.put(OWNER, plant.getUsername());
        long id = db.insert(PLANTS, null, values);
        Cursor cursor = db.rawQuery("Select * from " + PLANTS, new String[]{});
        System.out.println("Inserted Plant correctly With Id" + id);
        System.out.println("Current plants in app = " + cursor.getCount());
        db.close();
        return id;
    }


    public boolean deletePlant(int id) {
        SQLiteDatabase db = getWritableDatabase();
        int result = db.delete(PLANTS, ITEMID + "=?", new String[]{String.valueOf(id)});
        db.close();
        return result > 0;
    }


    public Boolean registerUser(String username, String password, String mobile) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USERNAME, username);
        contentValues.put(PASSWORD, password);
        contentValues.put(MOBILE, mobile);
        long result = MyDB.insert(USERSTB, null, contentValues);
        if (result == -1) return false;
        return true;
    }

    public Boolean checkUsername(String username) {//checks if username exists in db
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from " + USERSTB + " where " + USERNAME + " = ?", new String[]{username});
        if (cursor.getCount() > 0) return true;
        return false;
    }

    @SuppressLint("Range")
    public String getUsername(String username, String password) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from users where username = ? and password = ?", new String[]{username, password});
        if (cursor.getCount() > 0) {

            if (cursor.moveToFirst()) {
                return cursor.getString(cursor.getColumnIndex(USERNAME));
            }

        }
        return "";
    }

    public Boolean checkPlant(String plantName, String owner) {//checks if plant exists for a owner
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from " + PLANTS + " where " + OWNER + " = ?  AND " + NAME + " = ?", new String[]{owner, plantName});
        if (cursor.getCount() > 0) return true;
        return false;
    }

    public ArrayList<Plant> getPlants() {
        ArrayList<Plant> plants = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = IS_RENTED + "=?";
        String[] selectionArgs = {"false"};
        Cursor cursor = db.query(PLANTS, null, selection, selectionArgs, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(ITEMID));
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(NAME));
                @SuppressLint("Range") String description = cursor.getString(cursor.getColumnIndex(DESCRIPTION));
                @SuppressLint("Range") byte[] imageBytes = cursor.getBlob(cursor.getColumnIndex(IMAGE));
                Plant plant = new Plant();
                plant.setId(id);
                plant.setName(name);
                plant.setDescription(description);
                plant.setImageBytes(imageBytes);
                plants.add(plant);
            }
            cursor.close();
        }
        db.close();
        return plants;
    }

    public Plant getPlantDetails(String plantId) {
        SQLiteDatabase db = this.getReadableDatabase();
        System.out.printf("%s", plantId);
        String[] projection = {
                ITEMID,
                NAME,
                DESCRIPTION,
                SIZE,
                SUN_EXPOSURE,
                SOIL_TYPE,
                IMAGE,
                OWNER
        };

        String selection = ITEMID + "=?";
        String[] selectionArgs = {plantId};

        Cursor cursor = db.query(
                PLANTS,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        Plant plant = null;

        if (cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow(NAME));
            String description = cursor.getString(cursor.getColumnIndexOrThrow(DESCRIPTION));
            String size = cursor.getString(cursor.getColumnIndexOrThrow(SIZE));
            String sunExposure = cursor.getString(cursor.getColumnIndexOrThrow(SUN_EXPOSURE));
            String soilType = cursor.getString(cursor.getColumnIndexOrThrow(SOIL_TYPE));
            byte[] imageBytes = cursor.getBlob(cursor.getColumnIndexOrThrow(IMAGE));
            String owner = cursor.getString(cursor.getColumnIndexOrThrow(OWNER));

            plant = new Plant(name, description, size, sunExposure, soilType, imageBytes, owner);
        }

        cursor.close();

        return plant;
    }


    public List<Plant> getPlantsByOwner(String owner) {
        List<Plant> plantList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String[] columns = {ITEMID, NAME, DESCRIPTION, SIZE, SUN_EXPOSURE, SOIL_TYPE, IMAGE};
        String selection = OWNER + "=?";
        String[] selectionArgs = {owner};
        Cursor cursor = db.query(PLANTS, columns, selection, selectionArgs, null, null, null);
        while (cursor.moveToNext()) {
            @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(ITEMID));
            @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(NAME));
            @SuppressLint("Range") String description = cursor.getString(cursor.getColumnIndex(DESCRIPTION));
            @SuppressLint("Range") String size = cursor.getString(cursor.getColumnIndex(SIZE));
            @SuppressLint("Range") String sunExposure = cursor.getString(cursor.getColumnIndex(SUN_EXPOSURE));
            @SuppressLint("Range") String soilType = cursor.getString(cursor.getColumnIndex(SOIL_TYPE));
            @SuppressLint("Range") byte[] imageBytes = cursor.getBlob(cursor.getColumnIndex(IMAGE));
            Plant plant = new Plant(name, description, size, sunExposure, soilType, imageBytes, owner);
            plant.setId(id);
            plantList.add(plant);
        }
        cursor.close();
        db.close();
        return plantList;
    }

    public List<Plant> getPlantsByRenter(String renter) {
        List<Plant> plantList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String[] columns = {RENTED_ITEM, ORDER_DATE};
        String selection = RENTER + "=?";
        String[] selectionArgs = {renter};
        Cursor cursor = db.query(RENTAL_ORDERS, columns, selection, selectionArgs, null, null, null);
        while (cursor.moveToNext()) {
            @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(RENTED_ITEM));
            @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex(ORDER_DATE));
            Plant rented_plant = getPlantDetails(Integer.toString(id));
            rented_plant.setRentalDate(date);
            rented_plant.setId(id);
            plantList.add(rented_plant);
        }
        cursor.close();
        db.close();
        return plantList;
    }

    public boolean returnPlant(int id) {
        SQLiteDatabase db = getWritableDatabase();
        int result = db.delete(RENTAL_ORDERS, RENTED_ITEM + "=?", new String[]{String.valueOf(id)});
        if (result > 0) {
            Cursor cursor = db.rawQuery("Update " + PLANTS + " SET " + IS_RENTED + " = ? WHERE " + ITEMID + " = ?", new String[]{"false", String.valueOf(id)});
            System.out.println("Updated isRENTED, cursor count: " + cursor.getCount());
            cursor.close();
            db.close();
            return true;
        }
        return false;
    }

    public long rentPlant(String pid, String renter) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(RENTED_ITEM, pid);
        values.put(RENTER, renter);
        long id = db.insert(RENTAL_ORDERS, null, values);
        if (id > 0) {
            Cursor cursor = db.rawQuery("Update " + PLANTS + " set " + IS_RENTED + " = 'true' where " + ITEMID + " = " + pid + "; Select * from " + RENTAL_ORDERS, new String[]{});
            System.out.println("Rental Order has been placed successfully With Id " + id);
            System.out.println("Current Rental Order in app: " + cursor.getCount());
        }
        db.close();
        return id;
    }

}
