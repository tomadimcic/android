package com.dis.fiademo.util;

import java.util.ArrayList;

import com.dis.fiademo.db.DatabaseHandler;
import com.dis.fiademo.model.Beacon;


public class DataManager {
    
    private static DataManager instance;
    
    public static DataManager getInstance() {
	if(instance == null)
	    instance = new DataManager();
	return instance;
}

    public ArrayList<Beacon> getBeaconsList(DatabaseHandler db) {
	//ArrayList<Beacon> beaconList = testList();
	ArrayList<Beacon> beaconList = db.getAllBeacons();
	return beaconList;
    }
    
    public ArrayList<Beacon> testList(){
	ArrayList<Beacon> beaconList = new ArrayList<Beacon>();
	
	Beacon b1 = new Beacon();
	b1.setId("1");
	b1.setDistance(5000);
	b1.setEmail("email1@server.com");
	b1.setGps("44.87730901;20.65765212");
	b1.setIPv4CMS("192.168.1.2");
	b1.setIPv6CMS("2001:5612:2::1593");
	b1.setSms("+38458921158");
	b1.setTitle("Beacon1");
	b1.setUrlCMS("somedomain.com");
	b1.setActivated("1");
	
	Beacon b2 = new Beacon();
	b2.setId("2");
	b2.setDistance(5);
	b2.setEmail("email2@server.com");
	b2.setGps("44.87730901;20.65765212");
	b2.setIPv4CMS("192.168.1.3");
	b2.setIPv6CMS("2001:5612:2::1594");
	b2.setSms("+38458921158");
	b2.setTitle("Beacon2");
	b2.setUrlCMS("someotherdomain.com");
	b2.setActivated("0");
	
	Beacon b3 = new Beacon();
	b3.setId("3");
	b3.setDistance(250);
	b3.setEmail("email3@server.com");
	b3.setGps("37.987;23.726");
	b3.setIPv4CMS("192.168.1.4");
	b3.setIPv6CMS("2001:5612:2::1595");
	b3.setSms("+38458921158");
	b3.setTitle("Beacon3");
	b3.setUrlCMS("someotherdomain1.com");
	b3.setActivated("1");
	
	beaconList.add(b1);
	beaconList.add(b2);
	beaconList.add(b3);
	
	return beaconList;
    }

}
