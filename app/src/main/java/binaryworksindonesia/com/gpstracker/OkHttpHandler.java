package binaryworksindonesia.com.gpstracker;


import android.os.AsyncTask;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OkHttpHandler extends AsyncTask<Void, Void, byte[]> {

    private final  String _uri;

    public OkHttpHandler(String uri){
        this._uri=uri;
    }

    OkHttpClient httpClient = new OkHttpClient();

    @Override
    protected byte[] doInBackground(Void... params) {

        Request.Builder builder = new Request.Builder();
        builder.url(_uri);
        System.out.println("atas " + _uri + "  error ");
        Request request = builder.build();
        System.out.println("atas try " + _uri + "  error ");
        try {
            System.out.println("dalam try " + _uri + "  error ");
            Response response = httpClient.newCall(request).execute();
            return response.body().bytes();

        } catch (Exception e) {
            System.out.println("gagal load >> uri " + _uri + "  error "+ e.toString());
        }

        return null;
    }
}
