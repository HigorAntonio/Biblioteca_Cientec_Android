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

public class UserRegisterFragment extends BaseFragment {

    private Context context;
    private TextInputLayout edtNome;
    private TextInputLayout edtEmail;
    private TextInputLayout edtSenha;
    private Button btnAvancar;
    private User user = new User();
    private ProgressDialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_register, container, false);

        edtNome =  view.findViewById(R.id.nome_text_input);
        edtEmail =  view.findViewById(R.id.email_text_input);
        edtSenha =  view.findViewById(R.id.password_text_input);
        btnAvancar = view.findViewById(R.id.avancar_button2);

        edtNome.getEditText().addTextChangedListener(nameTextWatcher);
        edtEmail.getEditText().addTextChangedListener(emailTextWatcher);
        edtSenha.getEditText().addTextChangedListener(passwordTextWatcher);

        btnAvancar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnAvancar.setEnabled(false);

                dialog = new ProgressDialog(context);
                dialog.setTitle("");
                dialog.setMessage("Entrando...");
                dialog.show();

                Retrofit retrofit = new Retrofit.Builder()
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .baseUrl(BibliotecaCientecAPIService.BASE_URL)
                        .build();

                BibliotecaCientecAPIService service = retrofit.create(BibliotecaCientecAPIService.class);
                Call<String> stringCall = service.postRegister(edtNome.getEditText().getText().toString(),
                        edtEmail.getEditText().getText().toString(), edtSenha.getEditText().getText().toString());
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
                            Toast.makeText(getActivity().getApplicationContext(),"Usuário existente",Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                        btnAvancar.setEnabled(true);
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        dialog.dismiss();
                        btnAvancar.setEnabled(true);
                        Toast.makeText(getActivity().getApplicationContext(),"Não foi possível completar o cadastro",Toast.LENGTH_SHORT).show();
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

    private boolean validateName(boolean print) {
        String nameInput = edtNome.getEditText().getText().toString().trim();

        if (nameInput.isEmpty()) {
            if (print)
                edtNome.setError("O campo Nome não pode ficar vazio");
            return false;
        } else {
            if (print)
                edtNome.setError(null);
            return true;
        }
    }

    private boolean validateEmail(boolean print) {
        String emailInput = edtEmail.getEditText().getText().toString().trim();
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        if (emailInput.isEmpty()) {
            if (print)
                edtEmail.setError("O campo Email não pode ficar vazio");
            return false;
        }else if (!emailInput.matches(regex)) {
             if (print)
                    edtEmail.setError("Esse email é inválido. Ele deveria ter um formato assim: exemplo@email.com.");
            return false;
        } else {
            if (print)
                edtEmail.setError(null);
            return true;
        }
    }

    private boolean validatePassword(boolean print) {
        String passwordInput = edtSenha.getEditText().getText().toString().trim();

        if (passwordInput.isEmpty()) {
            if (print)
                edtSenha.setError("O campo Senha não pode ficar vazio");
            return false;
        } else {
            if (print)
                edtSenha.setError(null);
            return true;
        }
    }

    public boolean confirmInput() {
        return validateName(true) && validateEmail(true) && validatePassword(true);
    }

    private TextWatcher nameTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String nameInput = edtNome.getEditText().getText().toString().trim();
            String emailInput = edtEmail.getEditText().getText().toString().trim();
            String passwordInput = edtSenha.getEditText().getText().toString().trim();

            validateName(true);
            btnAvancar.setEnabled(validateName(false) && validateEmail(false) && validatePassword(false));
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private TextWatcher emailTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String nameInput = edtNome.getEditText().getText().toString().trim();
            String emailInput = edtEmail.getEditText().getText().toString().trim();
            String passwordInput = edtSenha.getEditText().getText().toString().trim();

            validateEmail(true);
            btnAvancar.setEnabled(validateName(false) && validateEmail(false) && validatePassword(false));
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private TextWatcher passwordTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String nameInput = edtNome.getEditText().getText().toString().trim();
            String emailInput = edtEmail.getEditText().getText().toString().trim();
            String passwordInput = edtSenha.getEditText().getText().toString().trim();

            validatePassword(true);
            btnAvancar.setEnabled(validateName(false) && validateEmail(false) && validatePassword(false));
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
}
