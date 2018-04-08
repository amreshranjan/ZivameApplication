package com.example.amreshranjan.zivame.view;

import com.example.amreshranjan.zivame.model.Feature;

import java.util.ArrayList;

/**
 * Created by amreshranjan on 08/04/18.
 */

public interface IResponse {
    void onPostExecuteData(ArrayList<Feature> features);
}
