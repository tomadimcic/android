package com.nt.najboljekafane.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.nt.najboljekafane.model.Desavanje;
import com.nt.najboljekafane.model.Glas;
import com.nt.najboljekafane.model.Kafana;
import com.nt.najboljekafane.model.Pesma;
import com.nt.najboljekafane.model.Settings;
 
public class DatabaseHandler extends SQLiteOpenHelper {
 
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 26;
 
    // Database Name
    private static final String DATABASE_NAME = "najboljekafane"; 
    private static final String TABLE_KAFANE = "kafane";
    private static final String TABLE_PESME = "pesme";
    private static final String TABLE_GLASOVI = "glasovi";
    private static final String TABLE_DESAVANJA = "desavanja";
    private static final String TABLE_SETTINGS = "settings";
    
    // reports Table Columns names
    private static final String KEY_ID = "id";   
    private static final String KAFANA_ID = "kafana_id";
    private static final String KAFANE_NAZIV = "naziv";
    private static final String KAFANE_GRAD = "grad";
    private static final String KAFANE_ADRESA = "adresa";
    private static final String KAFANE_WEBSAJT = "websajt";
    private static final String KAFANE_EMAIL = "email";
    private static final String KAFANE_OPIS = "opis";
    private static final String KAFANE_OCENA = "ocena_kafane";
    private static final String KAFANE_ATMOSFERA = "ocena_atmosfere";
    private static final String KAFANE_LAT = "lat";
    private static final String KAFANE_LNG = "lng";
    private static final String KAFANE_VIP = "vip";
    private static final String KAFANE_VERZIJA = "verzija";    
    private static final String KAFANE_TELEFON = "telefon";
    private static final String KAFANE_SLIKA1 = "slika1";
    private static final String KAFANE_SLIKA2 = "slika2";
    private static final String KAFANE_SLIKA3 = "slika3";
    private static final String KAFANE_SLIKA4 = "slika4";
    private static final String KAFANE_SLIKA5 = "slika5";    
    private static final String KAFANE_TIMESTAMP = "timestamp";
    private static final String KAFANE_REDOSLED = "redosled";
    private static final String KAFANE_THUMB = "thumb";
    private static final String KAFANE_VRSTA = "vrsta";
    private static final String KAFANE_BROJ_GLASOVA = "brojGlasova";
    private static final String KAFANE_RADNO_VREME = "radno_vreme";
    
    private static final String PESMA_ID = "pesma_id";
    private static final String PESMA_NASLOV = "naslov";
    private static final String PESMA_IZVODJAC ="izvodjac";
    private static final String PESMA_TEXT ="text";
    private static final String PESMA_BR_GLASOVA ="broj_glasova";
    private static final String PESMA_TIMESTAMP ="timestamp";
    private static final String PESMA_OMILJENA ="omiljena";
    private static final String PESMA_YT ="youtube_link";
    
    private static final String GLASOVI_TIP ="glasovi_vrsta";
    private static final String GLASOVI_TIP_ID ="glasovi_tip_id";
    private static final String GLASOVI_TIMESTAMP ="timestamp";
    private static final String GLASOVI_DEVICEUUID = "deviceuuid";
    
    private static final String DESAVANJA_ID = "desavanja_id";
    private static final String DESAVANJA_GRAD = "desavanja_grad";
    private static final String DESAVANJA_OPIS = "desavanja_opis";
    private static final String DESAVANJA_VREME = "desavanja_vreme";
    private static final String DESAVANJA_KAFANA_ID = "kafana_id";
    private static final String DESAVANJA_KAFANA = "kafana";
    private static final String DESAVANJA_DAN = "desavanja_dan";
    private static final String DESAVANJA_TIMESTAMP = "timestamp";
    private static final String DESAVANJA_NASLOV = "naslov";
    private static final String DESAVANJA_VIP = "vip";
    
    private static final String SETTINGS_DEVICEUUID = "deviceuuid";
    
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

	// Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_KAFANE_TABLE = "CREATE TABLE if not exists " + TABLE_KAFANE + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KAFANA_ID + " TEXT," + KAFANE_NAZIV + " TEXT,"
                + KAFANE_GRAD + " TEXT," + KAFANE_ADRESA + " TEXT," 
                + KAFANE_WEBSAJT + " TEXT," + KAFANE_EMAIL + " TEXT," 
                + KAFANE_OPIS + " TEXT," + KAFANE_OCENA + " DOUBLE," + KAFANE_ATMOSFERA + " DOUBLE," 
                + KAFANE_LAT + " TEXT,"  + KAFANE_LNG + " TEXT," + KAFANE_VIP + " TEXT," + KAFANE_VERZIJA + " TEXT,"
                + KAFANE_TELEFON + " TEXT," + KAFANE_SLIKA1 + " TEXT," + KAFANE_SLIKA2 + " TEXT,"+ KAFANE_SLIKA3 + " TEXT,"
                + KAFANE_SLIKA4 + " TEXT,"+ KAFANE_SLIKA5 + " TEXT," + KAFANE_TIMESTAMP + " TEXT," + KAFANE_REDOSLED + " TEXT,"
                + KAFANE_THUMB + " TEXT," + KAFANE_VRSTA + " TEXT," + KAFANE_BROJ_GLASOVA + " TEXT," + KAFANE_RADNO_VREME + " TEXT" +" )";

        String CREATE_PESME_TABLE = "CREATE TABLE if not exists " + TABLE_PESME + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + PESMA_ID + " TEXT," + PESMA_NASLOV + " TEXT,"
                + PESMA_IZVODJAC + " TEXT," + PESMA_TEXT + " TEXT," + PESMA_BR_GLASOVA + " INTEGER,"
                + PESMA_TIMESTAMP + " TEXT," + PESMA_OMILJENA + " TEXT," + PESMA_YT + " TEXT"+ ")";

        String CREATE_GLASOVI_TABLE = "CREATE TABLE if not exists " + TABLE_GLASOVI + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + GLASOVI_TIP + " TEXT," + GLASOVI_TIP_ID + " TEXT,"
                + GLASOVI_TIMESTAMP + " TEXT," + GLASOVI_DEVICEUUID + " TEXT" + ")";
        
        String CREATE_DESAVANJA_TABLE = "CREATE TABLE if not exists " + TABLE_DESAVANJA + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + DESAVANJA_ID + " TEXT," + DESAVANJA_GRAD + " TEXT,"
                + DESAVANJA_VREME + " TEXT," + DESAVANJA_KAFANA + " TEXT," + DESAVANJA_KAFANA_ID + " TEXT,"
                + DESAVANJA_DAN + " TEXT," + DESAVANJA_OPIS + " TEXT,"  + DESAVANJA_TIMESTAMP + " TEXT,"  +
                DESAVANJA_NASLOV + " TEXT," + DESAVANJA_VIP + " TEXT" +")";
        
        String CREATE_SETTINGS_TABLE = "CREATE TABLE if not exists " + TABLE_SETTINGS + "("
                + SETTINGS_DEVICEUUID + " TEXT" +")";
        
        db.execSQL(CREATE_KAFANE_TABLE);
        db.execSQL(CREATE_PESME_TABLE);
        db.execSQL(CREATE_GLASOVI_TABLE);
        db.execSQL(CREATE_DESAVANJA_TABLE);
        db.execSQL(CREATE_SETTINGS_TABLE);
    }
 
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_KAFANE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PESME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GLASOVI);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DESAVANJA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SETTINGS);
        // Create tables again
        onCreate(db);
    }
 
    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */
 
    public // Adding new kafana
    void addKafana(Kafana kafana) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
       // values.put(KEY_ID, kafana.getId());
        values.put(KAFANA_ID, kafana.getKafanaId());
        values.put(KAFANE_NAZIV, kafana.getNaziv()); 
        values.put(KAFANE_GRAD, kafana.getGrad());
        values.put(KAFANE_ADRESA, kafana.getAdresa());         
        values.put(KAFANE_WEBSAJT, kafana.getWebsajt());
        values.put(KAFANE_EMAIL, kafana.getEmail());
        values.put(KAFANE_OPIS, kafana.getOpis());        
        values.put(KAFANE_OCENA, kafana.getOcenaKafane());
        values.put(KAFANE_ATMOSFERA, kafana.getOcenaAtmosfere());
        values.put(KAFANE_LAT, kafana.getLat());
        values.put(KAFANE_LNG, kafana.getLng());
        values.put(KAFANE_VIP, kafana.getVip());
        values.put(KAFANE_VERZIJA, kafana.getVerzijaBaze());
        values.put(KAFANE_TELEFON, kafana.getTelefon());
        values.put(KAFANE_SLIKA1, kafana.getSlika1());
        values.put(KAFANE_SLIKA2, kafana.getSlika2());
        values.put(KAFANE_SLIKA3, kafana.getSlika3());
        values.put(KAFANE_SLIKA4, kafana.getSlika4());
        values.put(KAFANE_SLIKA5, kafana.getSlika5());        
        values.put(KAFANE_TIMESTAMP, kafana.getTimestamp());
        values.put(KAFANE_REDOSLED, kafana.getRedosled());
        values.put(KAFANE_THUMB, kafana.getThumb());
        values.put(KAFANE_VRSTA, kafana.getVrsta());
        values.put(KAFANE_BROJ_GLASOVA, kafana.getBrojGlasova());
        values.put(KAFANE_RADNO_VREME, kafana.getRadnoVreme());
        
        db.insert(TABLE_KAFANE, null, values);
        db.close(); // Closing database connection
    }
    
    public ArrayList<Kafana> getAllKafana(String vrsta) {
        ArrayList<Kafana> reportList = new ArrayList<Kafana>();
        // Select All Query
        String selectQuery = "SELECT "+KEY_ID+","+KAFANA_ID+","+KAFANE_NAZIV+","+KAFANE_GRAD+"," +
        		" "+KAFANE_ADRESA+"," +KAFANE_WEBSAJT+ ", "+KAFANE_EMAIL+", "+KAFANE_OPIS+", "+KAFANE_OCENA+"," +
        		" "+KAFANE_ATMOSFERA+","+KAFANE_LAT+", "+KAFANE_LNG+", "+KAFANE_VIP+", "+KAFANE_VERZIJA+", "+KAFANE_TELEFON+"," +
        		" "+KAFANE_SLIKA1+","+KAFANE_SLIKA2+","+KAFANE_SLIKA3+","+KAFANE_SLIKA4+","+KAFANE_SLIKA5+","+KAFANE_TIMESTAMP+"," +
        		" "+KAFANE_REDOSLED+", "+KAFANE_THUMB+", "+KAFANE_BROJ_GLASOVA+", "+KAFANE_RADNO_VREME+", " +
        				"(CASE WHEN thumb = 'no_photo' THEN thumb = '0'  ELSE '1' END) as have_thumb," +
        				"(CASE WHEN vip = 'da' THEN vip = '1'  ELSE '0' END) as vip_order, ((ocena_atmosfere + ocena_kafane)/2) as ocena " +
        				"FROM kafane WHERE "+KAFANE_VRSTA+" = '"+vrsta+"' ORDER BY vip_order ='0',redosled,have_thumb ='1' DESC, ocena DESC";
        						//"case when have_thumb ='1' then ocena END DESC, " +
        						//"case when have_thumb ='1' then have_thumb END DESC, " +
        						//"case when have_thumb ='0' then have_thumb END DESC, " +
        						//"case when have_thumb ='0' then ocena END DESC";
        
 
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
 
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
            	Kafana kafana = new Kafana();
                kafana.setId(Integer.parseInt(cursor.getString(0)));
                kafana.setKafanaId(cursor.getString(1));
                kafana.setNaziv(cursor.getString(2));
                kafana.setGrad(cursor.getString(3));
                kafana.setAdresa(cursor.getString(4));
                kafana.setWebsajt(cursor.getString(5));
                kafana.setEmail(cursor.getString(6));                
                kafana.setOpis(cursor.getString(7));                
                kafana.setOcenaKafane(Float.valueOf(cursor.getString(8)));
                kafana.setOcenaAtmosfere(Float.valueOf(cursor.getString(9)));
                kafana.setLat(cursor.getString(10));
                kafana.setLng(cursor.getString(11));
                kafana.setVip(cursor.getString(12));
                kafana.setVerzijaBaze(cursor.getString(13));
                kafana.setTelefon(cursor.getString(14));
                kafana.setSlika1(cursor.getString(15));
                kafana.setSlika2(cursor.getString(16));
                kafana.setSlika3(cursor.getString(17));
                kafana.setSlika4(cursor.getString(18));
                kafana.setSlika5(cursor.getString(19));
                kafana.setTimestamp(cursor.getString(20));
                kafana.setRedosled(cursor.getString(21));
                kafana.setThumb(cursor.getString(22));
                kafana.setBrojGlasova(cursor.getString(23));
                kafana.setRadnoVreme(cursor.getString(24));
                
                reportList.add(kafana);
            } while (cursor.moveToNext());
        }
 
        // return report list
        return reportList;
    }
    
    
    //Delete All kafanas
    public void DeleteAllKafana() {
    	SQLiteDatabase db = this.getWritableDatabase();
    	db.delete(TABLE_KAFANE, null, null);
        
 
    }


    public String getKafanaLastTimestamp() {
    
    	SQLiteDatabase db = this.getReadableDatabase();
    	Cursor cursor = db.rawQuery("SELECT * FROM kafane order by timestamp DESC LIMIT 1", null);
    	

    	String timestamp = "";
    	///cursor.moveFirst()
    	while(cursor.moveToNext()){ 
    		
    		timestamp= cursor.getString(cursor.getColumnIndex("timestamp"));
    		
    	}   	
    
		return timestamp;
		
	}
    
    public Kafana getKafanaByKafanaId(String kafana_id) {
        
    	SQLiteDatabase db = this.getReadableDatabase();
    	Cursor cursor = db.rawQuery("SELECT * FROM kafane where kafana_id = '"+ kafana_id +"'", null);
    	Kafana kafana = new Kafana();

    	
    	///cursor.moveFirst()
    	while(cursor.moveToNext()){ 
    		
    		kafana.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_ID))));
    		kafana.setKafanaId(cursor.getString(cursor.getColumnIndex(KAFANA_ID)));
    		kafana.setAdresa(cursor.getString(cursor.getColumnIndex(KAFANE_ADRESA)));
    		kafana.setGrad(cursor.getString(cursor.getColumnIndex(KAFANE_GRAD)));
    		kafana.setNaziv(cursor.getString(cursor.getColumnIndex(KAFANE_NAZIV)));
    		kafana.setWebsajt(cursor.getString(cursor.getColumnIndex(KAFANE_WEBSAJT)));
    		kafana.setEmail(cursor.getString(cursor.getColumnIndex(KAFANE_EMAIL)));
    		kafana.setOpis(cursor.getString(cursor.getColumnIndex(KAFANE_OPIS)));
    		kafana.setOcenaKafane(Float.valueOf(cursor.getString(cursor.getColumnIndex(KAFANE_OCENA))));
    		kafana.setOcenaAtmosfere(Float.valueOf(cursor.getString(cursor.getColumnIndex(KAFANE_ATMOSFERA))));
    		kafana.setLat(cursor.getString(cursor.getColumnIndex(KAFANE_LAT)));
    		kafana.setLng(cursor.getString(cursor.getColumnIndex(KAFANE_LNG)));
    		kafana.setVip(cursor.getString(cursor.getColumnIndex(KAFANE_VIP)));
    		kafana.setVerzijaBaze(cursor.getString(cursor.getColumnIndex(KAFANE_VERZIJA)));
    		kafana.setTelefon(cursor.getString(cursor.getColumnIndex(KAFANE_TELEFON)));
    		kafana.setSlika1(cursor.getString(cursor.getColumnIndex(KAFANE_SLIKA1)));
    		kafana.setSlika2(cursor.getString(cursor.getColumnIndex(KAFANE_SLIKA2)));
    		kafana.setSlika3(cursor.getString(cursor.getColumnIndex(KAFANE_SLIKA3)));
    		kafana.setSlika4(cursor.getString(cursor.getColumnIndex(KAFANE_SLIKA4)));
    		kafana.setSlika5(cursor.getString(cursor.getColumnIndex(KAFANE_SLIKA5)));
    		kafana.setTimestamp(cursor.getString(cursor.getColumnIndex(KAFANE_TIMESTAMP)));
    		kafana.setRedosled(cursor.getString(cursor.getColumnIndex(KAFANE_REDOSLED)));
    		kafana.setThumb(cursor.getString(cursor.getColumnIndex(KAFANE_THUMB)));
    		kafana.setVrsta(cursor.getString(cursor.getColumnIndex(KAFANE_VRSTA)));
    		kafana.setBrojGlasova(cursor.getString(cursor.getColumnIndex(KAFANE_BROJ_GLASOVA)));
    		kafana.setRadnoVreme(cursor.getString(cursor.getColumnIndex(KAFANE_RADNO_VREME)));
    		
    		return kafana;
    	}   	

    
		return kafana;
		
	}
    
    public int updateKafana(Kafana kafana) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(KAFANA_ID, kafana.getKafanaId());
        values.put(KAFANE_ADRESA, kafana.getAdresa());        
        values.put(KAFANE_GRAD, kafana.getGrad());
        values.put(KAFANE_NAZIV, kafana.getNaziv());
        values.put(KAFANE_WEBSAJT, kafana.getWebsajt());
        values.put(KAFANE_EMAIL, kafana.getEmail());
        values.put(KAFANE_OPIS, kafana.getOpis());
        values.put(KAFANE_OCENA, kafana.getOcenaKafane());
        values.put(KAFANE_ATMOSFERA, kafana.getOcenaAtmosfere());
        values.put(KAFANE_LAT, kafana.getLat());
        values.put(KAFANE_LNG, kafana.getLng());
        values.put(KAFANE_VIP, kafana.getVip());
        values.put(KAFANE_VERZIJA, kafana.getVerzijaBaze());
        values.put(KAFANE_TELEFON, kafana.getTelefon());
        values.put(KAFANE_SLIKA1, kafana.getSlika1());
        values.put(KAFANE_SLIKA2, kafana.getSlika2());
        values.put(KAFANE_SLIKA3, kafana.getSlika3());
        values.put(KAFANE_SLIKA4, kafana.getSlika4());
        values.put(KAFANE_SLIKA5, kafana.getSlika5());
        values.put(KAFANE_TIMESTAMP, kafana.getTimestamp());
        values.put(KAFANE_REDOSLED, kafana.getRedosled());
        values.put(KAFANE_THUMB, kafana.getThumb());
        values.put(KAFANE_VRSTA, kafana.getVrsta());
        values.put(KAFANE_BROJ_GLASOVA, kafana.getBrojGlasova());
        values.put(KAFANE_RADNO_VREME, kafana.getRadnoVreme());
        
        // updating row
        return db.update(TABLE_KAFANE, values, KEY_ID + " = ?",
                new String[] { String.valueOf(kafana.getId()) });
    }
    public int updateKafanaByKafanaId(Kafana kafana) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(KAFANA_ID, kafana.getKafanaId());
        values.put(KAFANE_ADRESA, kafana.getAdresa());        
        values.put(KAFANE_GRAD, kafana.getGrad());
        values.put(KAFANE_NAZIV, kafana.getNaziv());
        values.put(KAFANE_WEBSAJT, kafana.getWebsajt());
        values.put(KAFANE_EMAIL, kafana.getEmail());
        values.put(KAFANE_OPIS, kafana.getOpis());
        values.put(KAFANE_OCENA, kafana.getOcenaKafane());
        values.put(KAFANE_ATMOSFERA, kafana.getOcenaAtmosfere());
        values.put(KAFANE_LAT, kafana.getLat());
        values.put(KAFANE_LNG, kafana.getLng());
        values.put(KAFANE_VIP, kafana.getVip());
        values.put(KAFANE_VERZIJA, kafana.getVerzijaBaze());
        values.put(KAFANE_TELEFON, kafana.getTelefon());
        values.put(KAFANE_SLIKA1, kafana.getSlika1());
        values.put(KAFANE_SLIKA2, kafana.getSlika2());
        values.put(KAFANE_SLIKA3, kafana.getSlika3());
        values.put(KAFANE_SLIKA4, kafana.getSlika4());
        values.put(KAFANE_SLIKA5, kafana.getSlika5());
        values.put(KAFANE_TIMESTAMP, kafana.getTimestamp());
        values.put(KAFANE_REDOSLED, kafana.getRedosled());
        values.put(KAFANE_THUMB, kafana.getThumb());
        values.put(KAFANE_VRSTA, kafana.getVrsta());
        values.put(KAFANE_BROJ_GLASOVA, kafana.getBrojGlasova());
        values.put(KAFANE_RADNO_VREME, kafana.getRadnoVreme());
        // updating row
        return db.update(TABLE_KAFANE, values, KAFANA_ID + " = ?",
                new String[] { kafana.getKafanaId() });
    }

	
    // Deleting single kafana
    public void deleteKafanaByKafanaId(String kafana_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_KAFANE, KAFANA_ID + " = ?",
                new String[] { kafana_id });
        db.close();
    }
    
    public ArrayList<Pesma> getAllPesme() {
        ArrayList<Pesma> pesmeList = new ArrayList<Pesma>();
        // Select All Query
        String selectQuery = "SELECT * FROM pesme ORDER BY broj_glasova DESC";
 
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
 
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
            	Pesma pesma = new Pesma();
                pesma.setId(Integer.parseInt(cursor.getString(0)));
                pesma.setPesmaId(cursor.getString(1));
                pesma.setNaziv(cursor.getString(2));
                pesma.setIzvodjac(cursor.getString(3));
                pesma.setTekst(cursor.getString(4));
                pesma.setBrojglasova(Integer.parseInt(cursor.getString(5)));       
                pesma.setTimestamp(cursor.getString(6));
                pesma.setOmiljena(cursor.getString(7));
                pesma.setYoutubeLink(cursor.getString(8));
                
                pesmeList.add(pesma);
            } while (cursor.moveToNext());
        }
 
        
        return pesmeList;
    }
    
    public // Adding new pesme
    void addPesme(Pesma pesma) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
       // values.put(KEY_ID, kafana.getId());
        values.put(PESMA_ID, pesma.getPesmaId());
        values.put(PESMA_NASLOV, pesma.getNaziv()); 
        values.put(PESMA_IZVODJAC, pesma.getIzvodjac());
        values.put(PESMA_TEXT, pesma.getTekst());         
        values.put(PESMA_BR_GLASOVA, pesma.getBrojglasova());
        values.put(PESMA_TIMESTAMP, pesma.getTimestamp()); 
        values.put(PESMA_YT, pesma.getYoutube()); 
        
        db.insert(TABLE_PESME, null, values);
        db.close(); // Closing database connection
    }
    
    public int updatePesmaByPesmaId(Pesma pesma) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(PESMA_ID, pesma.getPesmaId());
        values.put(PESMA_NASLOV, pesma.getNaziv());        
        values.put(PESMA_IZVODJAC, pesma.getIzvodjac());
        values.put(PESMA_TEXT, pesma.getTekst());
        values.put(PESMA_BR_GLASOVA, pesma.getBrojglasova());
        values.put(PESMA_YT, pesma.getYoutube());
        
        return db.update(TABLE_PESME, values, PESMA_ID + " = ?",
                new String[] { pesma.getPesmaId() });
    }
    
    public int setOmiljenPesmaByPesmaId(Pesma pesma) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(PESMA_ID, pesma.getPesmaId());
        values.put(PESMA_NASLOV, pesma.getNaziv());        
        values.put(PESMA_IZVODJAC, pesma.getIzvodjac());
        values.put(PESMA_TEXT, pesma.getTekst());
        values.put(PESMA_BR_GLASOVA, pesma.getBrojglasova());
        values.put(PESMA_OMILJENA, pesma.getOmiljena());
        values.put(PESMA_YT, pesma.getYoutube());
        // updating row
        return db.update(TABLE_PESME, values, PESMA_ID + " = ?",
                new String[] { pesma.getPesmaId() });
    }
 
    public int updateBrojGlasovaByPesmaId(Pesma pesma) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(PESMA_ID, pesma.getPesmaId());
        values.put(PESMA_BR_GLASOVA, pesma.getBrojglasova());
        values.put(PESMA_TIMESTAMP, pesma.getTimestamp());
        // updating row
        return db.update(TABLE_PESME, values, PESMA_ID + " = ?",
                new String[] { pesma.getPesmaId() });
    }
    
    public Pesma getPesmaByPesmaId(String pesma_id) {
        
    	SQLiteDatabase db = this.getReadableDatabase();
    	Cursor cursor = db.rawQuery("SELECT * FROM pesme where pesma_id = '"+ pesma_id +"'", null);
    	Pesma pesma = new Pesma();

    	while(cursor.moveToNext()){ 
    		
    		//pesma.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_ID))));
    		pesma.setPesmaId(cursor.getString(cursor.getColumnIndex(PESMA_ID)));
    		pesma.setNaziv(cursor.getString(cursor.getColumnIndex(PESMA_NASLOV)));
    		pesma.setIzvodjac(cursor.getString(cursor.getColumnIndex(PESMA_IZVODJAC)));
    		pesma.setTekst(cursor.getString(cursor.getColumnIndex(PESMA_TEXT)));
    		pesma.setBrojglasova(Integer.parseInt(cursor.getString(cursor.getColumnIndex(PESMA_BR_GLASOVA))));
    		pesma.setOmiljena(cursor.getString(cursor.getColumnIndex(PESMA_OMILJENA)));
    		pesma.setYoutube(cursor.getString(cursor.getColumnIndex(PESMA_YT)));
    		
    		return pesma;
    	}   	
    	return pesma;
		
	}
    
    public String getPesmaLastTimestamp() {
        
    	SQLiteDatabase db = this.getReadableDatabase();
    	Cursor cursor = db.rawQuery("SELECT * FROM pesme order by timestamp DESC LIMIT 1", null);
    	

    	String timestamp = "";
    	///cursor.moveFirst()
    	while(cursor.moveToNext()){ 
    		
    		timestamp = cursor.getString(cursor.getColumnIndex("timestamp"));
    		Log.d("timestamp pesma dbhandler", ""+timestamp);
    	}   	
    
		return timestamp;
		
	}
    
    public 
    void addGlas(Glas glas) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(GLASOVI_TIP, glas.getTip());
        values.put(GLASOVI_TIP_ID, glas.getTipId()); 
        values.put(GLASOVI_TIMESTAMP, glas.getTimestamp()); 
        values.put(GLASOVI_DEVICEUUID, glas.getDeviceUUID()); 
        db.insert(TABLE_GLASOVI, null, values);
        db.close(); // Closing database connection
    }
    
    public String getGlasLastTimestamp(String tip, String tip_id, String deviceUUID) {
    	Cursor cursor;
    	SQLiteDatabase db = this.getReadableDatabase();
    	if(tip.equals("pesma")){
    		cursor = db.rawQuery("SELECT * FROM "+TABLE_GLASOVI+" WHERE "+GLASOVI_TIP+" = '"+ tip +"' and " +
        			""+GLASOVI_TIP_ID+" = '"+tip_id+"' order by timestamp DESC LIMIT 1", null);
        	
    	}
    	else {
    		cursor = db.rawQuery("SELECT * FROM "+TABLE_GLASOVI+" WHERE " +
    				" deviceuuid ='"+deviceUUID+"' order by timestamp DESC LIMIT 1", null);
    	}
    	
    	String timestamp = "";
    	///cursor.moveFirst()
    	while(cursor.moveToNext()){ 
    		
    		timestamp = cursor.getString(cursor.getColumnIndex("timestamp"));
    		Log.d("timestamp dbhandler", ""+timestamp);
    	}   	
    
		return timestamp;
		
	}
    
    public // Adding new desavanje
    void addDesavanje(Desavanje desavanje) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
       // values.put(KEY_ID, kafana.getId());
        values.put(DESAVANJA_ID, desavanje.getDesavanjeId());
        values.put(DESAVANJA_GRAD, desavanje.getGrad()); 
        values.put(DESAVANJA_VREME, desavanje.getDatum());
        values.put(DESAVANJA_KAFANA, desavanje.getKafana());         
        values.put(DESAVANJA_KAFANA_ID, desavanje.getKafanaId());
        values.put(DESAVANJA_DAN, desavanje.getDan());
        values.put(DESAVANJA_OPIS, desavanje.getOpis());        
        values.put(DESAVANJA_TIMESTAMP, desavanje.getTimestamp());
        values.put(DESAVANJA_NASLOV, desavanje.getNaslov());
        values.put(DESAVANJA_VIP, desavanje.getVip());
        
        db.insert(TABLE_DESAVANJA, null, values);
        db.close(); // Closing database connection
    }
    
    public ArrayList<Desavanje> getAllDesavanja(String tip) {
        ArrayList<Desavanje> reportList = new ArrayList<Desavanje>();
        // Select All Query
        String selectQuery = "SELECT d."+DESAVANJA_ID+", d."+DESAVANJA_GRAD+", d."+DESAVANJA_VREME+"," +
        		"d."+DESAVANJA_KAFANA+", d."+DESAVANJA_KAFANA_ID+", d."+DESAVANJA_DAN+", d."+DESAVANJA_OPIS+", " +
        				"d."+DESAVANJA_TIMESTAMP+ ", d."+DESAVANJA_NASLOV+ " ,d."+DESAVANJA_VIP+"" +
        				" FROM "+TABLE_DESAVANJA+" d,"+TABLE_KAFANE+" k " +
        				" WHERE k."+KAFANA_ID+" = d."+DESAVANJA_KAFANA_ID+
        				" ORDER BY d."+DESAVANJA_VREME+",  k.vip, k.redosled DESC";
      /*  String selectQuery = "SELECT "+DESAVANJA_ID+", "+DESAVANJA_GRAD+", "+DESAVANJA_VREME+"," +
            		""+DESAVANJA_KAFANA+", "+DESAVANJA_KAFANA_ID+", "+DESAVANJA_DAN+", "+DESAVANJA_OPIS+","+DESAVANJA_TIMESTAMP+
            				" FROM "+TABLE_DESAVANJA+" ORDER BY VIP DESC";
 */
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
 
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
            	Desavanje desavanje = new Desavanje();
                desavanje.setDesavanjeId(cursor.getString(0));
                desavanje.setGrad(cursor.getString(1));
                desavanje.setDatum(cursor.getString(2));
                desavanje.setKafana(cursor.getString(3));
                desavanje.setKafanaId(cursor.getString(4));
                desavanje.setDan(cursor.getString(5));
                desavanje.setOpis(cursor.getString(6));                
                desavanje.setTimestamp(cursor.getString(7));                
                desavanje.setNaslov(cursor.getString(8));
                desavanje.setVip(cursor.getString(9));
                
                reportList.add(desavanje);
            } while (cursor.moveToNext());
        }
 
        // return report list
        return reportList;
    }
    
    public int updateDesavanjeByDesavanjeId(Desavanje desavanje) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(DESAVANJA_ID, desavanje.getDesavanjeId());
        values.put(DESAVANJA_GRAD, desavanje.getGrad());        
        values.put(DESAVANJA_VREME, desavanje.getDatum());
        values.put(DESAVANJA_KAFANA, desavanje.getKafana());
        values.put(DESAVANJA_NASLOV, desavanje.getNaslov());
        values.put(DESAVANJA_KAFANA_ID, desavanje.getKafanaId());
        values.put(DESAVANJA_DAN, desavanje.getDan());
        values.put(DESAVANJA_OPIS, desavanje.getOpis());
        values.put(DESAVANJA_TIMESTAMP, desavanje.getTimestamp());
        values.put(DESAVANJA_VIP, desavanje.getVip());
        // updating row
        return db.update(TABLE_DESAVANJA, values, DESAVANJA_ID + " = ?",
                new String[] { desavanje.getDesavanjeId() });
    }
    
    public Desavanje getDesavanjeByDesavanjeId(String desavanje_id) {
        
    	SQLiteDatabase db = this.getReadableDatabase();
    	Cursor cursor = db.rawQuery("SELECT * FROM "+TABLE_DESAVANJA+" where " + DESAVANJA_ID + " = '"+ desavanje_id +"'", null);
    	Desavanje desavanje = new Desavanje();

    	while(cursor.moveToNext()){ 
    		
    		//pesma.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_ID))));
    		desavanje.setDesavanjeId(cursor.getString(cursor.getColumnIndex(DESAVANJA_ID)));
    		desavanje.setGrad(cursor.getString(cursor.getColumnIndex(DESAVANJA_GRAD)));
    		desavanje.setDatum(cursor.getString(cursor.getColumnIndex(DESAVANJA_VREME)));
    		desavanje.setKafana(cursor.getString(cursor.getColumnIndex(DESAVANJA_KAFANA)));
    		desavanje.setKafanaId(cursor.getString(cursor.getColumnIndex(DESAVANJA_KAFANA_ID)));
    		desavanje.setDan(cursor.getString(cursor.getColumnIndex(DESAVANJA_DAN)));
    		desavanje.setOpis(cursor.getString(cursor.getColumnIndex(DESAVANJA_OPIS)));
    		desavanje.setTimestamp(cursor.getString(cursor.getColumnIndex(DESAVANJA_TIMESTAMP)));
    		desavanje.setVip(cursor.getString(cursor.getColumnIndex(DESAVANJA_VIP)));
    		    		
    		return desavanje;
    	}   	
    	return desavanje;
		
	}
    
 // Deleting single kafana
    public void deleteDesavanjeByDesavanjeId(String desavanje_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_DESAVANJA, DESAVANJA_ID + " = ?",
                new String[] { desavanje_id });
        db.close();
    }
    
    public String getDesavanjeLastTimestamp() {
        
    	SQLiteDatabase db = this.getReadableDatabase();
    	Cursor cursor = db.rawQuery("SELECT * FROM "+ TABLE_DESAVANJA +" order by timestamp DESC LIMIT 1", null);
    	

    	String timestamp = "";
    	///cursor.moveFirst()
    	while(cursor.moveToNext()){ 
    		
    		timestamp= cursor.getString(cursor.getColumnIndex("timestamp"));
    		
    	}   	
    
		return timestamp;
		
	}
    
    /*
    public // Adding new pesme
    void setDeviceUUID(String deviceUUID) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
       // values.put(KEY_ID, kafana.getId());
        values.put(SETTINGS_DEVICEUUID, deviceUUID);
           
        db.insert(TABLE_SETTINGS, null, values);
        db.close(); // Closing database connection
    }
    */
    
    public // Adding new kafana
    void addDeviceUUID(Settings settings) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
       // values.put(KEY_ID, kafana.getId());
        values.put(SETTINGS_DEVICEUUID, settings.getDeviceUUID());
        
        db.insert(TABLE_SETTINGS, null, values);
        db.close(); // Closing database connection
    }
    
    
    public String getDeviceUUID() {
        
    	SQLiteDatabase db = this.getReadableDatabase();
    	Cursor cursor = db.rawQuery("SELECT deviceuuid FROM "+ TABLE_SETTINGS, null);
    	

    	String deviceUUID = "";
    	///cursor.moveFirst()
    	while(cursor.moveToNext()){ 
    		
    		deviceUUID = cursor.getString(cursor.getColumnIndex("deviceuuid"));
    		
    	}   	
    
		return deviceUUID;
		
	}
    
    
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