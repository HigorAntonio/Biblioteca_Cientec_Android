package com.biblioteca.cientec.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.biblioteca.cientec.BibliotecaCientecAPIService;
import com.biblioteca.cientec.Models.User;
import com.biblioteca.cientec.R;
import com.biblioteca.cientec.activity.HomeActivity;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class LoginFragment extends BaseFragment {
    private EditText emailInput;
    private EditText passwordInput;
    private Button nextButton;
    private Button cancelButton;
    private User user = new User();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_fragment, container, false);

        emailInput = (EditText) view.findViewById(R.id.email_text_input);
        passwordInput = (EditText) view.findViewById(R.id.password_text_input);
        nextButton = (Button) view.findViewById(R.id.login_button);
        cancelButton = (Button) view.findViewById(R.id.cancel_button);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Retrofit retrofit = new Retrofit.Builder()
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .baseUrl(BibliotecaCientecAPIService.BASE_URL)
                        .build();

                BibliotecaCientecAPIService service = retrofit.create(BibliotecaCientecAPIService.class);
                Call<String> stringCall = service.postAuthentication(emailInput.getText().toString(), passwordInput.getText().toString());
                stringCall.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (response.isSuccessful()) {
                            String responseString = response.body();

                            JSONObject root = null;
                            try {
                                root = new JSONObject(responseString);
                                JSONObject obj = root.getJSONObject("user");
                                user.setId(obj.optInt("id"));
                                user.setName(obj.optString("name"));
                                user.setEmail(obj.optString("email"));
                                user.setToken(root.optString("token"));
                                String TAG = "cientec";
                                Log.i(TAG, "id: "+user.getId());
                                Log.i(TAG, "name: "+user.getName());
                                Log.i(TAG, "email: "+user.getEmail());
                                Log.i(TAG, "token"+user.getToken());


                                if (user.getToken() != null) {
                                    getFragmentManager().popBackStack();
                                    Intent it = new Intent(getContext(), HomeActivity.class);
                                    it.putExtra("user", user);
                                    startActivity(it);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(getActivity().getApplicationContext(),"Usuário ou senha invalido",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                    }
                });
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        return view;
    }
}
