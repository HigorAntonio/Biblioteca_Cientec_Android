package com.biblioteca.cientec.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

public class NewGenreFragment extends BaseFragment {
    private Context context;
    private Intent it;
    private User user;
    private EditText edt_genre_name;
    private Button btn_cadastrar;
    private ProgressDialog dialog;
    private TextView txt_error_msgs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_genre, container, false);
        it = getActivity().getIntent();
        user = (User) it.getSerializableExtra("user");

        //Torna possível trocar o menu da ActionBar
        setHasOptionsMenu(true);

        //Volta o título e o subtítulo da Actionbar para seus valores iniciais
        ((HomeActivity)getActivity()).getSupportActionBar().setTitle(R.string.app_name);
        ((HomeActivity)getActivity()).getSupportActionBar().setSubtitle("");

        //Troca o menu hamburguer da Navigation Drawer pela seta de voltar
        ActionBarDrawerToggle mToggle = ((HomeActivity)getActivity()).getmToggle();
        mToggle.setDrawerIndicatorEnabled(false);

        txt_error_msgs = view.findViewById(R.id.fr_new_genre_error_msgs);
        txt_error_msgs.setVisibility(View.GONE);

        edt_genre_name = view.findViewById(R.id.fr_new_genre_name);
        btn_cadastrar = view.findViewById(R.id.fr_new_genre_cadastrar);
        btn_cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validaCadastro()) {
                    btn_cadastrar.setEnabled(false);

                    dialog = new ProgressDialog(context);
                    dialog.setTitle("");
                    dialog.setMessage("Cadastrando Gênero...");
                    dialog.show();

                    Retrofit retrofit = new Retrofit.Builder()
                            .addConverterFactory(ScalarsConverterFactory.create())
                            .baseUrl(BibliotecaCientecAPIService.BASE_URL)
                            .build();

                    BibliotecaCientecAPIService service = retrofit.create(BibliotecaCientecAPIService.class);
                    Call<String> stringCall = service.postNewGenre(
                            "Bearer "+user.getToken(),
                            edt_genre_name.getText().toString()
                    );
                    stringCall.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(getActivity().getApplicationContext(),"Gênero cadastrado com sucesso",Toast.LENGTH_SHORT).show();
                                closeKeyboard();
                                getActivity().onBackPressed();
                            } else {
                                Toast.makeText(getActivity().getApplicationContext(),"Não foi possível cadastrar o gênero",Toast.LENGTH_SHORT).show();
                            }
                            dialog.dismiss();
                            btn_cadastrar.setEnabled(true);
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            dialog.dismiss();
                            Toast.makeText(getActivity().getApplicationContext(),"Não foi possível cadastrar o gênero",Toast.LENGTH_SHORT).show();
                            btn_cadastrar.setEnabled(true);
                        }
                    });
                } else {
                    Toast.makeText(getActivity().getApplicationContext(),"Preencha os campos corretamente",Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        this.context = context;
        super.onAttach(context);
    }

    //Altera o menu da ActionBar
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        ((HomeActivity)getActivity()).getSupportActionBar().setTitle("Cadastrar Gênero");
        super.onCreateOptionsMenu(menu, inflater);
    }

    private boolean validaCadastro() {
        boolean valido = true;
        String error_msg = "";
        if (edt_genre_name.getText().toString().equals("")) {
            valido = false;
            error_msg += "Informe o Nome do gênero\n";
        }
        if (error_msg.equals("")) {
            txt_error_msgs.setVisibility(View.GONE);
        } else {
            txt_error_msgs.setVisibility(View.VISIBLE);
        }
        txt_error_msgs.setText(error_msg);
        return valido;
    }
}
