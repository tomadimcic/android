package com.iotwear.wear.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpVersion;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;

import com.iotwear.wear.gui.AddDeviceActivity;
import com.iotwear.wear.gui.handlers.RetryHandler;
import com.iotwear.wear.model.PiDevice;

public class NetworkUtil {

	public static StringBuilder inputStreamToString(InputStream is)
			throws IOException {
		String line = "";
		StringBuilder total = new StringBuilder();

		BufferedReader rd = new BufferedReader(new InputStreamReader(is));

		while ((line = rd.readLine()) != null) {
			total.append(line);
		}
		rd.close();
		return total;
	}

	public static String getCurrentSsid(Context context) {
		String ssid = new String();
		ConnectivityManager connManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connManager
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (networkInfo.isConnected()) {
			final WifiManager wifiManager = (WifiManager) context
					.getSystemService(Context.WIFI_SERVICE);
			final WifiInfo connectionInfo = wifiManager.getConnectionInfo();
			if (connectionInfo != null
					&& !StringUtils.isBlank(connectionInfo.getSSID())) {
				ssid = connectionInfo.getSSID();
			}
		}
		return removeQuotationsInCurrentSSID(ssid);
	}
	
	public static String removeQuotationsInCurrentSSID(String ssid){
	     int deviceVersion= Build.VERSION.SDK_INT;

	     if (deviceVersion >= 17){
	         if (ssid.startsWith("\"") && ssid.endsWith("\"")){
	             ssid = ssid.substring(1, ssid.length()-1);
	         }
	     }

	     return ssid;

	 }

	public static boolean isWiFiEnabled(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(AddDeviceActivity.CONNECTIVITY_SERVICE);

		return manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
				.isConnectedOrConnecting();
	}

	public static boolean isMobileNetworkEnabled(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(AddDeviceActivity.CONNECTIVITY_SERVICE);

		return manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
				.isConnectedOrConnecting();
	}

	public static String getDeviceUrl(Context context, PiDevice pi) {
		if (isWiFiEnabled(context)) {
			String ssid = getCurrentSsid(context);
			// On the same WiFi as PiDevice...local mode
			ssid = ssid.replace("\"", "");
			if (ssid.equals(pi.getSsid()))
				return pi.getLocalIp() + ":" + pi.getPort();

			// Offline mode, when connected to PiDevice directly...offline mode
			else if (ssid.equals(PiDevice.DEVICE_SSID))
				return PiDevice.MASTER_AP_IP_ADDRESS + ":"
						+ PiDevice.MASTER_PORT_NUMBER;
		}

		// If on other wifi or mobile network...online mode
		return pi.getUrl() + ":" + pi.getPort();
	}

	public static String getColorUrlString(int color) {
		return "?red=" + Color.red(color) + "&green=" + Color.green(color)
				+ "&blue=" + Color.blue(color);
	}

	public static DefaultHttpClient getDefaultHttpClient() {
		HttpParams httpParams = new BasicHttpParams();
		int timeout = 2000;
		HttpConnectionParams.setConnectionTimeout(httpParams, timeout);
		HttpConnectionParams.setSoTimeout(httpParams, timeout);
		HttpConnectionParams.setTcpNoDelay(httpParams, true);
		DefaultHttpClient httpClient = new DefaultHttpClient(httpParams);
		httpClient.getParams().setParameter(CoreProtocolPNames.USER_AGENT, System.getProperty("http.agent"));
		httpClient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
		//httpClient.setHttpRequestRetryHandler(new RetryHandler());
		return httpClient;
	}
}
