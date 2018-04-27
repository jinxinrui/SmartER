package com.example.jxr.smarter;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jxr.smarter.RestWS.RestClient;

/**
 * Created by jxr on 25/4/18.
 */

public class MainFragment extends Fragment {
    private View vMain;
    private TextView currentTempView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vMain = inflater.inflate(R.layout.fragment_main, container, false);


        currentTempView = (TextView) vMain.findViewById(R.id.currentTempText);
        Factorial f = new Factorial();
        f.execute(new String[] {null});
        return vMain;
    }

    private class Factorial extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            return RestClient.findTemp(); // result in onPostExecute is returned by this
        }
        @Override
        protected void onPostExecute(String result) {
            String snippet = RestClient.getSnippet(result);
            currentTempView.setText(snippet);
        }
    }
}
