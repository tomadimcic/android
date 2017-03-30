package com.iotwear.wear.task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

import com.iotwear.wear.gui.AlarmActivity;
import com.iotwear.wear.model.PiDevice;
import com.iotwear.wear.task.interfaces.GetAlarmInterface;
import com.iotwear.wear.util.NetworkUtil;

public class GetAlarmTask extends AsyncTask<Void, Void, String> {

    private GetAlarmInterface mAlarmInterface;
    private AlarmActivity mActivity;
    private ProgressDialog progress;
    private PiDevice mSelectedDevice;
    PrintWriter out;
    BufferedReader rd;
    Socket clientSocket;

    public GetAlarmTask(GetAlarmInterface alarmInterface, Activity activity,
	    PiDevice selectedDevice) {
	mAlarmInterface = alarmInterface;
	mActivity = (AlarmActivity) activity;
	mSelectedDevice = selectedDevice;
    }

    @Override
    protected void onPreExecute() {
	super.onPreExecute();
	progress = ProgressDialog.show(mActivity, "Sync",
		"Getting alarms from device..");
    }

    @Override
    protected String doInBackground(Void... params) {
	try {
	    String urlPort = NetworkUtil.getDeviceUrl(mActivity,
		    mSelectedDevice);
	    InetAddress address = InetAddress.getByName(urlPort.split(":")[0]);
	    int port = Integer.parseInt(urlPort.split(":")[1]);
	    clientSocket = new Socket();
	    clientSocket.connect(new InetSocketAddress(address, port), 7000);
	    clientSocket.setSoTimeout(7000);
	    out = new PrintWriter(clientSocket.getOutputStream(), true);

	    // Send first message - Message is being correctly received
	    out.println("GET " + PiDevice.URL_ALARM + " HTTP/1.1\r\n");
	    out.println("Cache-Control: no-cache\r\n");
	    out.println("\r\n\r\n");
	    out.flush();

	    rd = new BufferedReader(new InputStreamReader(
		    clientSocket.getInputStream(), "UTF-8"));
	    String line = null;
	    String response = "";
	    while ((line = rd.readLine()) != null) {
		response += line;
	    }
	    System.out.println(response);
	    if (!response.equals("")) {
		out.close();
		rd.close();
		clientSocket.close();
		return response;
	    }
	    out.close();
	    rd.close();
	    clientSocket.close();
	} catch (Exception e) {
	    e.printStackTrace();
	    if (out != null)
		out.close();
	    try {
		if (rd != null)
		    rd.close();
		if (clientSocket != null)
		    clientSocket.close();
	    } catch (IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
		return "0";
	    }
	}
	return "0";
    }

    @Override
    protected void onPostExecute(String result) {
	progress.dismiss();
	if (result == null){
	    Toast.makeText(mActivity, "Alarms not received!",
		    Toast.LENGTH_SHORT).show();
	    result = "[{\"s\":13546,\"d\":0,\"i\":\"1+2+3\",\"v\":\"1+1+1\",\"t\":\"1+2+1\"},{\"s\":100,\"d\":0,\"i\":\"0\",\"v\":\"1\",\"t\":\"1\"},{\"s\":0,\"d\":0,\"i\":\"0\",\"v\":\"0\",\"t\":\"0\"},{\"s\":0,\"d\":0,\"i\":\"0\",\"v\":\"0\",\"t\":\"0\"},{\"s\":0,\"d\":0,\"i\":\"0\",\"v\":\"0\",\"t\":\"0\"},{\"s\":0,\"d\":0,\"i\":\"0\",\"v\":\"0\",\"t\":\"0\"}]";
	}
	else {
	    mAlarmInterface.onAlarmReceived(result);
	}
    }

}
