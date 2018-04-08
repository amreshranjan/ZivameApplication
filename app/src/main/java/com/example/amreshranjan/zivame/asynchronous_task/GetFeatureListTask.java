package com.example.amreshranjan.zivame.asynchronous_task;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ProgressBar;

import com.example.amreshranjan.zivame.model.Feature;
import com.example.amreshranjan.zivame.view.IResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by amreshranjan on 08/04/18.
 */

public class GetFeatureListTask extends AsyncTask<Void, Void, ArrayList<Feature>> {

    private Context mContext;
    private IResponse jResponse;
    private ProgressDialog progressDialog;

    public GetFeatureListTask(Context context, IResponse jResponse){
        this.mContext = context;
        this.jResponse = jResponse;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage("Please wait..");
        progressDialog.show();
    }

    @Override
    protected void onPostExecute(ArrayList<Feature> result) {
        super.onPostExecute(result);
        jResponse.onPostExecuteData(result);
        progressDialog.dismiss();
    }

    @Override
    protected ArrayList<Feature> doInBackground(Void... voids) {
        ArrayList<Feature> features = new ArrayList<>();
        try {
            InputStream inputStream = mContext.getAssets().open("Features.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            String result = new String(buffer, "UTF-8");
            return extractData(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private ArrayList<Feature> extractData(String response){
        ArrayList<Feature> features = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray values = jsonObject.getJSONArray("values");
            for (int index = 0; index < values.length() ; index ++){
                JSONObject object = values.getJSONObject(index);
                Feature feature = new Feature();
                feature.setName(object.getString("name"));
                feature.setDescription(object.getString("description"));
                Log.d("Name : ", object.getString("name"));
                features.add(feature);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return features;
    }
}
