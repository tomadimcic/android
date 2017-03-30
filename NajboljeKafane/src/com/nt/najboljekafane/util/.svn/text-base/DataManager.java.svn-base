package com.nt.najboljekafane.util;

import java.util.ArrayList;

import android.os.AsyncTask;
import android.widget.TextView;

import com.nt.najboljekafane.db.DatabaseHandler;
import com.nt.najboljekafane.model.Desavanje;
import com.nt.najboljekafane.model.Kafana;
import com.nt.najboljekafane.model.Pesma;
import com.nt.najboljekafane.task.GetAktuelnaDesavanjaTask;
//import com.nt.najboljekafane.task.GetKafaneList;
import com.nt.najboljekafane.task.GetKafaneTask;

public class DataManager {
	
    
    private static DataManager instance;
    private ArrayList<Desavanje> desavanjaList = new ArrayList<Desavanje>();
    private ArrayList<Kafana> kafaneList = new ArrayList<Kafana>(); 	
    
    
    public static DataManager getInstance() {
	if(instance == null)
	    instance = new DataManager();
	return instance;
    }
    
    /*
    public static ArrayList<Kafana> getKafaneSearch() {
    	AsyncTask<Void, Void, ArrayList<Kafana>> kl = new GetKafaneList().execute();
	return new ArrayList<Kafana>();
    }
    /*
    public ArrayList<Kafana> getKafaneList(String search) {
    	TextView tv = null;
			
        	GetKafaneTask kafane = new GetKafaneTask(tv);
    	    try {
    		kafane.execute();
    	    } catch (Exception e) {
            	    // TODO: handle exception
    	    }
    	return new ArrayList<Kafana>();
    }
*/
    
    public ArrayList<Desavanje> getDesavanjaList() {
	// TODO Auto-generated method stub
	return new ArrayList<Desavanje>();
    }
    
    public ArrayList<Pesma> getPesmeList() {
	// TODO Auto-generated method stub
	return new ArrayList<Pesma>();
    }
    
    public void setAktuelnaDesavanja(TextView tv) {
	    GetAktuelnaDesavanjaTask desavanja = new GetAktuelnaDesavanjaTask(tv);
	    try {
		desavanja.execute();
	    } catch (Exception e) {
        	    // TODO: handle exception
	    }
    }
    
    public ArrayList<Desavanje> getAktuelnaDesavanjaList() {
	
	return desavanjaList;
    }
    
    public void setAktuelnaDesavanjaList(ArrayList<Desavanje> desavanjaList) {
	this.desavanjaList = desavanjaList;
    }

    ////
    public void setKafane(TextView tv) {
	    GetKafaneTask kafane = new GetKafaneTask(tv);
	    try {
		kafane.execute();
	    } catch (Exception e) {
        	    // TODO: handle exception
	    }
    }
    
    public ArrayList<Kafana> getKafane() {
	
	return kafaneList;
    }
    
    public void setKafaneList(ArrayList<Kafana> kafaneList) {
	this.kafaneList = kafaneList;
    }
}
