package com.iotwear.wear.model;

public class WiFiNetwork {
    public static final String PSK = "PSK";
    public static final String WEP = "WEP";
    public static final String EAP = "EAP";
    public static final String OPEN = "Open";

    public String ssid;
    public String capabilities;

    public String getScanResultSecurity() {
	final String[] securityModes = { WEP, PSK, EAP };
	for (int i = securityModes.length - 1; i >= 0; i--) {
	    if (capabilities.contains(securityModes[i])) {
		return securityModes[i];
	    }
	}

	return OPEN;
    }
}
