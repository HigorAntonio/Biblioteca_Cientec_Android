package com.biblioteca.cientec.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

    private Context context;
    private TextInputLayout edtEmail;
    private TextInputLayout edtPassword;
    private Button nextButton;
    private User user = new User();
    private ProgressDialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        edtEmail = view.findViewById(R.id.email_text_input);
        edtPassword = view.findViewById(R.id.password_text_input2);
        nextButton = view.findViewById(R.id.login_button);

        edtEmail.getEditText().addTextChangedListener(loginTextWatcher);
        edtPassword.getEditText().addTextChangedListener(loginTextWatcher);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextButton.setEnabled(false);

                dialog = new ProgressDialog(context);
                dialog.setTitle("");
                dialog.setMessage("Entrando...");
                dialog.show();

                Retrofit retrofit = new Retrofit.Builder()
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .baseUrl(BibliotecaCientecAPIService.BASE_URL)
                        .build();

                BibliotecaCientecAPIService service = retrofit.create(BibliotecaCientecAPIService.class);
                Call<String> stringCall = service.postAuthentication(edtEmail.getEditText().getText().toString(), edtPassword.getEditText().getText().toString());
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
                                    getActivity().finish();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(getActivity().getApplicationContext(),"Usuário ou senha invalido",Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                        nextButton.setEnabled(true);
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        dialog.dismiss();
                        nextButton.setEnabled(true);
                    }
                });
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        this.context = context;
        super.onAttach(context);
    }

    private boolean validateEmail(boolean print) {
        String emailInput = edtEmail.getEditText().getText().toString().trim();

        if (emailInput.isEmpty()) {
            if (print)
                edtEmail.setError("O campo Email não pode ficar vazio");
            return false;
        } else {
            if (print)
                edtEmail.setError(null);
            return true;
        }
    }

    private boolean validatePassword(boolean print) {
        String passwordInput = edtPassword.getEditText().getText().toString().trim();

        if (passwordInput.isEmpty()) {
            if (print)
                edtPassword.setError("O campo Senha não pode ficar vazio");
            return false;
        } else {
            if (print)
                edtPassword.setError(null);
            return true;
        }
    }

    private TextWatcher loginTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            boolean emailValido = validateEmail(false);
            boolean passwordValido = validatePassword(false);
            nextButton.setEnabled(emailValido && passwordValido);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

}
