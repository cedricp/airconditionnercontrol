package com.mycode.cedric.airconditionnercontrol; /**
 * Created by cedric on 1/12/17.
 */
import android.os.AsyncTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

/**
 *
 */
public class HttpRequest extends AsyncTask<String, Void, String>
{
    private onHttpRequestComplete listener;
    private String result;
    public Boolean finished = false;

    public HttpRequest(onHttpRequestComplete listener){
        this.listener = listener;
    }

    public interface onHttpRequestComplete {
        void onHttpRequestComplete(String s);
    }

    public void abort(){
        listener = null;
    }

    @Override
    protected String doInBackground(String... urls) {
        HttpParams httpParameters = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParameters, 10000);
        HttpResponse response = null;
        HttpClient client = new DefaultHttpClient();
        HttpGet httpget= new HttpGet(urls[0]);

        try {
            response = client.execute(httpget);
            HttpEntity resEntityGet = response.getEntity();
            if (response != null) {
                // do something with the response
                String resp = EntityUtils.toString(resEntityGet);
                this.result = resp;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            this.result = "NOK";
        }
        finished = true;
        return this.result;

    }

    @Override
    protected void onPostExecute(String s){
        if (listener != null)
            listener.onHttpRequestComplete(s);
        finished = true;
    }

}