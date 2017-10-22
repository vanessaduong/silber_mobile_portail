package esipe.fisa.silber.utils;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;



public class HttpUtil{

    public final static String GET = "GET";
    public final static String POST = "POST";


    private HttpURLConnection urlConnection;
    private URL url;
    private String method;

    public void initConnection(String url) throws IOException {
        this.url = new URL(url);
        this.urlConnection = (HttpURLConnection)  this.url.openConnection();
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void connect() throws IOException {
        urlConnection.setRequestMethod(method);
        urlConnection.setReadTimeout(((int) TimeUnit.MINUTES.toMillis(10)) /* milliseconds */ );
        urlConnection.setConnectTimeout((int) TimeUnit.MINUTES.toMillis(15) /* milliseconds */ );
        urlConnection.setDoOutput(true);
        urlConnection.connect();
    }


    public Object getCookie() {
        urlConnection.
    }

    public StringBuilder readData() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(this.url.openStream()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line + "\n");
        }
        br.close();
        return sb;
    }

    public JSONObject readDataToJson() throws JSONException, IOException {
        StringBuilder sb = this.readData();
        JSONObject jsonObject = new JSONObject(sb.toString());
        return jsonObject;
    }

    public HttpURLConnection getUrlConnection() {
        return urlConnection;
    }

    public int getHttpCode() throws IOException {
        return this.urlConnection.getResponseCode();
    }



}
