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

public class UserRegisterFragment extends BaseFragment {
    private EditText edtNome;
    private EditText edtEmail;
    private EditText edtSenha;
    private Button btnAvancar;
    private Button btnCancelar;
    private User user = new User();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_register_fragment, container, false);

        edtNome = (EditText) view.findViewById(R.id.nome_text_input);
        edtEmail = (EditText) view.findViewById(R.id.email_text_input);
        edtSenha = (EditText) view.findViewById(R.id.password_text_input);
        btnAvancar = (Button) view.findViewById(R.id.avancar_button2);
        btnCancelar = (Button) view.findViewById(R.id.cancel_button2);

        btnAvancar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtNome.getText().toString().equals("") ||
                    edtEmail.getText().toString().equals("") ||
                        edtSenha.getText().toString().equals("")) {
                    Toast.makeText(getActivity().getApplicationContext(),"Preencha todos os campos",Toast.LENGTH_SHORT).show();
                    return;
                }

                Retrofit retrofit = new Retrofit.Builder()
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .baseUrl(BibliotecaCientecAPIService.BASE_URL)
                        .build();

                BibliotecaCientecAPIService service = retrofit.create(BibliotecaCientecAPIService.class);
                Call<String> stringCall = service.postRegister(edtNome.getText().toString(),
                        edtEmail.getText().toString(), edtSenha.getText().toString());
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
                            Toast.makeText(getActivity().getApplicationContext(),"Usuário existente",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Toast.makeText(getActivity().getApplicationContext(),"Não foi possível completar o cadastro",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        return view;
    }
}
