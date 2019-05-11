package com.biblioteca.cientec.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.biblioteca.cientec.JSONPostTask;
import com.biblioteca.cientec.R;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_fragment, container, false);
        final EditText emailInput = (EditText) view.findViewById(R.id.email_text_input);
        final EditText passwordInput = (EditText) view.findViewById(R.id.password_text_input);
        Button nextButton = (Button) view.findViewById(R.id.login_button);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String json = "{\n" +
                        "\t\"email\": \"" + emailInput.getText().toString() + "\",\n" +
                        "\t\"password\": \"" + passwordInput.getText().toString() + "\"\n" +
                        "}";
                new PostTask().execute("http://10.0.2.2:3000/auth/authenticate", json);
            }
        });

        return view;
    }
    private class PostTask extends JSONPostTask {
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null) {
                JSONObject root = null;
                try {
                    root = new JSONObject(result);
                    String token = root.optString("token");
                    Toast.makeText(getActivity().getApplicationContext(), "token: "+token, Toast.LENGTH_LONG).show();
                    Log.i("cientec", "token: "+token);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
