package dunavnet.com.mojbac.model;

import java.util.Date;
import java.text.SimpleDateFormat;

/**
 * Created by Tomek on 22.9.2015.
 */
public class LogData {

    public static final String MP_HOST = "http://services.nsinfo.co.rs/rest/api/v1/";

    private String pname;  //The name of Complex service which the MP belongs to
    private int msgid; //Message id depending on operation type (
                   //1 - authentication
                   //2 - writing data
                   //3 - reading data
                   //)

    private String msg; //Message content relative to message id
    private String appname;  //The micro-proxy name
    private String action;  //The MP method that has been invoked
    private int status;  //Status number relative to operation result
    private String host;  //The host of micro-proxy
    private String userHost;  //The IP of the final user of the service
    private Date c_timestamp;  //The timestamp when the MP has been invoked
    //"c_kpi_msg":
    private String c_service_id;  //The ID of the Complex Service
    private String c_service_country;  //The country that provides the service
    private String c_auth_type; //The type of authentication used by final user "enum" : ["stork", "clips-idm", "other"]
    private boolean c_is_cb;  //is the service a cross-border service?
    //"c_cb_details
    private String c_cb_from;  //country from which the user leaves
    private String c_cb_to;  //country  to which the user arrives


    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public int getMsgid() {
        return msgid;
    }

    public void setMsgid(int msgid) {
        this.msgid = msgid;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getAppname() {
        return appname;
    }

    public void setAppname(String appname) {
        this.appname = appname;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getUserHost() {
        return userHost;
    }

    public void setUserHost(String userHost) {
        this.userHost = userHost;
    }

    public Date getC_timestamp() {
        return c_timestamp;
    }

    public void setC_timestamp(Date c_timestamp) {
        this.c_timestamp = c_timestamp;
    }

    public String getC_service_id() {
        return c_service_id;
    }

    public void setC_service_id(String c_service_id) {
        this.c_service_id = c_service_id;
    }

    public String getC_service_country() {
        return c_service_country;
    }

    public void setC_service_country(String c_service_country) {
        this.c_service_country = c_service_country;
    }

    public String getC_auth_type() {
        return c_auth_type;
    }

    public void setC_auth_type(String c_auth_type) {
        this.c_auth_type = c_auth_type;
    }

    public boolean isC_is_cb() {
        return c_is_cb;
    }

    public void setC_is_cb(boolean c_is_cb) {
        this.c_is_cb = c_is_cb;
    }

    public String getC_cb_from() {
        return c_cb_from;
    }

    public void setC_cb_from(String c_cb_from) {
        this.c_cb_from = c_cb_from;
    }

    public String getC_cb_to() {
        return c_cb_to;
    }

    public void setC_cb_to(String c_cb_to) {
        this.c_cb_to = c_cb_to;
    }

    public static String toJsonForSending(LogData log){
        String logData = "";

        logData = "{" +
            "\"log_message\": {" +
            "\"pname\": \"" + log.getPname() + "\"," +
                    "\"msgid\": " + log.getMsgid() + "," +
                    "\"msg\": \"" + log.getMsg() + "\"," +
                    "\"appname\": \"" + log.getAppname() + "\"," +
                    "\"action\": \"" + log.getAction() + "\","+
                    "\"status\": " + log.getStatus() + "," +
                    "\"host\": \"" + log.getHost() + "\"," +
                    "\"userHost\": \"" + log.getUserHost() + "\"," +
                    "\"c_timestamp\": \"" + log.getDate() + "\"," +
                    "\"c_kpi_msg\": {" +
                        "\"c_service_id\": \"" + log.getC_service_id() + "\"," +
                        "\"c_service_country\": \"" + log.getC_service_country() + "\"," +
                        "\"c_auth_type\": \"" + log.getC_auth_type() + "\"," +
                        "\"c_is_cb\": " + log.isC_is_cb() + "," +
                        "\"c_cb_details\": {" +
                            "\"c_cb_from\": \"" + log.getC_cb_from() + "\"," +
                            "\"c_cb_to\": \"" + log.getC_cb_to() + "\"" +
                        "}" +
                    "}" +
                "}" +
            "}";

        return logData;
    }

    public static LogData createLog(int type, String msg, String action, String appName, String ip, String service_id){
        LogData log = new LogData();
        log.setPname("NOS03");
        log.setMsgid(type);
        log.setMsg(msg);
        log.setAppname(appName);
        log.setAction(action);
        log.setStatus(200);
        log.setHost(MP_HOST);
        log.setUserHost(ip);
        log.setC_service_id(service_id);
        log.setC_service_country("RS");
        log.setC_auth_type("clips-idm");
        log.setC_is_cb(false);
        log.setC_cb_from("RS");
        log.setC_cb_to("RS");


        return log;
    }

    private String getDate() {
        //2012-04-23T18:25:43.511Z
        try {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentTimeStamp = dateFormat.format(new Date()); // Find todays date

            currentTimeStamp = currentTimeStamp.replace(" ", "T");
            currentTimeStamp += "Z";
            return currentTimeStamp;
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }
}
