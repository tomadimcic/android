package com.dis.fiademo.db;

import java.util.ArrayList;

import com.dis.fiademo.model.Beacon;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
 
public class DatabaseHandler extends SQLiteOpenHelper {
 
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 4;
 
    // Database Name
    private static final String DATABASE_NAME = "proximitydetector"; 
    private static final String TABLE_BEACONS = "beacons";
    private static final String TABLE_IPADDRESS = "ip_address";
    
    // reports Table Columns names
    private static final String KEY_ID = "id";   
    private static final String BEACON_ID = "beacon_id";
    private static final String BEACON_TITLE = "title";
    private static final String BEACON_GPS = "gps";
    private static final String BEACON_DISTANCE = "distance";
    private static final String BEACON_EMAIL = "email";
    private static final String BEACON_SMS = "sms";
    private static final String BEACON_URL = "url";
    private static final String BEACON_IPV4 = "ipv4";
    private static final String BEACON_IPV6 = "ipv6";
    private static final String BEACON_ACTIVATED = "activated";
    private static final String BEACON_SENT = "sent";
    
    private static final String IPADDRESS_ID = "ipaddress_id";
    private static final String IPV4_CMS = "ipv4_cms";
    private static final String IPV6_CMS = "ipv6_cms";
    
    
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

	// Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PROXIMITY_TABLE = "CREATE TABLE if not exists " + TABLE_BEACONS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + BEACON_ID + " TEXT," + BEACON_TITLE + " TEXT,"
                + BEACON_GPS + " TEXT," + BEACON_DISTANCE + " TEXT," 
                + BEACON_EMAIL + " TEXT," + BEACON_SMS + " TEXT," 
                + BEACON_URL + " TEXT," + BEACON_IPV4 + " TEXT," + BEACON_IPV6 + " TEXT,"
                + BEACON_ACTIVATED + " TEXT," + BEACON_SENT + " TEXT" +")";
        
        String CREATE_IPADDRESS_TABLE = "CREATE TABLE if not exists " + TABLE_IPADDRESS + "("
                + IPADDRESS_ID + " TEXT," + IPV4_CMS + " TEXT," + IPV6_CMS + " TEXT"  +")";
        
        
        
        db.execSQL(CREATE_PROXIMITY_TABLE);
        db.execSQL(CREATE_IPADDRESS_TABLE);
        
    }
 
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BEACONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_IPADDRESS);
        
        // Create tables again
        onCreate(db);
    }
 
    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */
 
    public // Adding new beacon
    void addBeacon(Beacon beacon) {
	if(beacon.getActivated().equals(""))
	    beacon.setActivated("1");
	
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
       // values.put(KEY_ID, kafana.getId());
        values.put(BEACON_ID, beacon.getId());
        values.put(BEACON_TITLE, beacon.getTitle()); 
        values.put(BEACON_GPS, beacon.getGps());
        values.put(BEACON_DISTANCE, Integer.toString(beacon.getDistance()));         
        values.put(BEACON_EMAIL, beacon.getEmail());
        values.put(BEACON_SMS, beacon.getSms());
        values.put(BEACON_URL, beacon.getUrlCMS());        
        values.put(BEACON_IPV4, beacon.getIPv4CMS());
        values.put(BEACON_IPV6, beacon.getIPv6CMS());
        values.put(BEACON_ACTIVATED, beacon.getActivated());
        values.put(BEACON_SENT, "");
        
        
        db.insert(TABLE_BEACONS, null, values);
        db.close(); // Closing database connection
    }
    
    public ArrayList<Beacon> getAllBeacons() {
        ArrayList<Beacon> beaconList = new ArrayList<Beacon>();
        // Select All Query
        String selectQuery = "SELECT * FROM beacons";
        
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
 
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
            	Beacon beacon = new Beacon();
            	beacon.setId(cursor.getString(0));
            	//beacon.setId(cursor.getString(1));
            	beacon.setTitle(cursor.getString(2));
            	beacon.setGps(cursor.getString(3));
            	beacon.setDistance(cursor.getInt(4));
            	beacon.setEmail(cursor.getString(5));       
            	beacon.setSms(cursor.getString(6));
            	beacon.setUrlCMS(cursor.getString(7));
            	beacon.setIPv4CMS(cursor.getString(8));
            	beacon.setIPv6CMS(cursor.getString(9));
            	beacon.setActivated(cursor.getString(10));
            	beacon.setSent(cursor.getString(11));
                
                beaconList.add(beacon);
            } while (cursor.moveToNext());
        }
 
        // return report list
        return beaconList;
    }
    
    
    //Delete All beacons
    public void deleteAllBeacons() {
    	SQLiteDatabase db = this.getWritableDatabase();
    	db.delete(TABLE_BEACONS, null, null);
        
 
    }
    
    public Beacon getBeaconById(String beacon_id) {
        
    	SQLiteDatabase db = this.getReadableDatabase();
    	Cursor cursor = db.rawQuery("SELECT * FROM beacons where beacon_id = '"+ beacon_id +"'", null);
    	Beacon beacon = new Beacon();

    	
    	///cursor.moveFirst()
    	while(cursor.moveToNext()){ 
    		
    	    //beacon.setId(cursor.getString(cursor.getColumnIndex(KEY_ID)));
            	beacon.setId(cursor.getString(cursor.getColumnIndex(BEACON_ID)));
            	beacon.setTitle(cursor.getString(cursor.getColumnIndex(BEACON_TITLE)));
            	beacon.setGps(cursor.getString(cursor.getColumnIndex(BEACON_GPS)));
            	beacon.setDistance(Integer.getInteger(cursor.getString(cursor.getColumnIndex(BEACON_DISTANCE))));
            	beacon.setEmail(cursor.getString(cursor.getColumnIndex(BEACON_EMAIL)));       
            	beacon.setSms(cursor.getString(cursor.getColumnIndex(BEACON_SMS)));
            	beacon.setUrlCMS(cursor.getString(cursor.getColumnIndex(BEACON_URL)));
            	beacon.setIPv4CMS(cursor.getString(cursor.getColumnIndex(BEACON_IPV4)));
            	beacon.setIPv4CMS(cursor.getString(cursor.getColumnIndex(BEACON_IPV6)));
            	beacon.setActivated(cursor.getString(cursor.getColumnIndex(BEACON_ACTIVATED)));
            	beacon.setSent(cursor.getString(cursor.getColumnIndex(BEACON_SENT)));
    	}   	
    
	return beacon;
		
	}
    
    public int updateBeacon(Beacon beacon) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(BEACON_ID, beacon.getId());
        values.put(BEACON_TITLE, beacon.getTitle());
        values.put(BEACON_GPS, beacon.getGps());
        values.put(BEACON_DISTANCE, beacon.getDistance());
        values.put(BEACON_EMAIL, beacon.getEmail());
        values.put(BEACON_SMS, beacon.getSms());
        values.put(BEACON_URL, beacon.getUrlCMS());
        values.put(BEACON_IPV4, beacon.getIPv4CMS());
        values.put(BEACON_IPV6, beacon.getIPv6CMS());
        values.put(BEACON_ACTIVATED, beacon.getActivated());
        values.put(BEACON_SENT, beacon.getSent());
        
        // updating row
        int update = 0;
        try {
	    update = db.update(TABLE_BEACONS, values, KEY_ID + " = ?",
	            new String[] { String.valueOf(beacon.getId()) });
	} catch (Exception e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
        return update;
    }

	
    // Deleting single beacon
    public void deleteBeaconById(String beacon_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_BEACONS, BEACON_ID + " = ?",
                new String[] { beacon_id });
        db.close();
    }
    
    public void addIpAddress(String ipv4Address, String ipv6Address){
	SQLiteDatabase db = this.getWritableDatabase();
	 
        ContentValues values = new ContentValues();
       // values.put(KEY_ID, kafana.getId());
        values.put(IPADDRESS_ID, "1");
        values.put(IPV4_CMS, ipv4Address); 
        values.put(IPV6_CMS, ipv6Address);
        
        db.insert(TABLE_IPADDRESS, null, values);
        db.close(); // Closing database connection
    }
    
    public int updateIpv4Address(String ipAddress){
	SQLiteDatabase db = this.getWritableDatabase();
	 
        ContentValues values = new ContentValues();
        values.put(IPV4_CMS, ipAddress);
        
        // updating row
        return db.update(TABLE_IPADDRESS, values, IPADDRESS_ID + " = ?",
                new String[] { "1" });
    }
    
    public int updateIpv6Address(String ipAddress){
	SQLiteDatabase db = this.getWritableDatabase();
	 
        ContentValues values = new ContentValues();
        values.put(IPV6_CMS, ipAddress);
        
        // updating row
        return db.update(TABLE_IPADDRESS, values, IPADDRESS_ID + " = ?",
                new String[] { "1" });
    }
    
    public String getIpv4Address(){
	String id = null;
	String ipv4Address = "";
	String ipv6Address = null;
	
	SQLiteDatabase db = this.getReadableDatabase();
    	Cursor cursor = db.rawQuery("SELECT * FROM ip_address where ipaddress_id = '1'", null);
    	
    	///cursor.moveFirst()
    	while(cursor.moveToNext()){ 
    		
    	    id = cursor.getString(cursor.getColumnIndex(IPADDRESS_ID));
    	    ipv4Address = cursor.getString(cursor.getColumnIndex(IPV4_CMS));
    	    ipv6Address = cursor.getString(cursor.getColumnIndex(IPV6_CMS));
    	}   	
	
	return ipv4Address;
    }
    
    public String getIpv6Address(){
	String id = null;
	String ipv4Address = null;
	String ipv6Address = "";
	
	SQLiteDatabase db = this.getReadableDatabase();
    	Cursor cursor = db.rawQuery("SELECT * FROM ip_address where ipaddress_id = '1'", null);
    	
    	///cursor.moveFirst()
    	while(cursor.moveToNext()){ 
    		
    	    id = cursor.getString(cursor.getColumnIndex(IPADDRESS_ID));
    	    ipv4Address = cursor.getString(cursor.getColumnIndex(IPV4_CMS));
    	    ipv6Address = cursor.getString(cursor.getColumnIndex(IPV6_CMS));
    	}   	
	
	return ipv6Address;
    }
    
    
    /*public void setPrvoPokretanje(Boolean prvoPokretanje) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
       // values.put(KEY_ID, kafana.getId());
        values.put(SETTINGS_PRVO_POKRETANJE, prvoPokretanje);
           
        db.insert(TABLE_SETTINGS, null, values);
        db.close(); // Closing database connection
    }*/
    
    
    public boolean isTableExists(String tableName) {
    	SQLiteDatabase mDatabase = this.getWritableDatabase();
        Cursor cursor = mDatabase.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '"+tableName+"'", null);
        if(cursor!=null) {
            if(cursor.getCount()>0) {
                                cursor.close();
                return true;
            }
                        cursor.close();
        }
        return false;
    }
 

}