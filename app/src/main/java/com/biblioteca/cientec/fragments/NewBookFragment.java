package com.biblioteca.cientec.fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.biblioteca.cientec.BibliotecaCientecAPIService;
import com.biblioteca.cientec.Models.Author;
import com.biblioteca.cientec.Models.User;
import com.biblioteca.cientec.R;
import com.biblioteca.cientec.activity.HomeActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static android.app.Activity.RESULT_OK;

public class NewBookFragment extends BaseFragment implements GalleryOrCameraDialogFragment.OnDialogItemSelect {
    public static final int GALERIA_IMAGENS = 1;
    public static final int PERMISSAO_REQUEST = 2;
    public static final int CAMERA = 3;
    public static final int REQUEST_PERMISSIONS_CODE = 128;

    private File arquivoFoto = null;

    private Context context;
    private Intent it;
    private User user;
    private ImageView img_book_cover;
    private Button btn_select_cover;
    private Button btn_cadastrar;
    private Button btn_add_author;
    private Spinner spinner_autor;
    private int authorId = -1;
    private EditText edt_title;
    private EditText edt_original_title;
    private EditText edt_isbn;
    private EditText edt_publisher;
    private EditText edt_edition;
    private EditText edt_num_pages;
    private EditText edt_language;
    private EditText edt_description;
    private TextView txt_error_msgs;
    private LinearLayout btn_select_genres;
    private Button btn_add_genre;
    private ProgressDialog dialog;
    private File file;
    private ArrayList<Author> mAuthors = new ArrayList<>();
    //multiple choice list dialog
    private String[] listGenres;
    private int[] listGenresIds;
    private boolean[] checkedItems;
    ArrayList<Integer> mBookGenres = new ArrayList<>();
    private int[] genresId;

    @Override
    public void sendOption(String input) {
        if (input.equals("camera")) {
            callCamera();
        } else if (input.equals("galeria")) {
            callGallery();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_book, container, false);
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

        img_book_cover = view.findViewById(R.id.fr_new_book_cover);
        //Esconde o ImageView até que seja selecionada uma imagem
        img_book_cover.setVisibility(View.GONE);

        txt_error_msgs = view.findViewById(R.id.fr_new_book_error_msgs);
        txt_error_msgs.setVisibility(View.GONE);

        btn_select_cover = view.findViewById(R.id.fr_new_book_select_cover);
        btn_select_cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GalleryOrCameraDialogFragment gcDialog = new GalleryOrCameraDialogFragment();
                gcDialog.setTargetFragment(NewBookFragment.this, 1);
                gcDialog.show(getActivity().getSupportFragmentManager(), "gcDialog");
            }
        });

        spinner_autor = view.findViewById(R.id.fr_new_book_author);
        //Coloca um objeto Autor "inválido" como placeholder do spinner
        mAuthors.add(new Author(-1, "Selecionar Autor", ""));
        //Carrega os dados dos autores vindos da API
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(ScalarsConverterFactory.create())
                .baseUrl(BibliotecaCientecAPIService.BASE_URL)
                .build();

        BibliotecaCientecAPIService service = retrofit.create(BibliotecaCientecAPIService.class);
        Call<String> stringCall = service.getAuthors("Bearer "+user.getToken());
        stringCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    String responseString = response.body();

                    JSONArray root = null;
                    try {
                        root = new JSONArray(responseString);
                        mAuthors.clear();
                        //Coloca um objeto Autor "inválido" como placeholder do spinner
                        mAuthors.add(new Author(-1, "Selecionar Autor", ""));
                        for (int i = 0; i < root.length(); i++) {
                            JSONObject jsonBook = root.getJSONObject(i);
                            Author a = new Author();
                            a.setId(jsonBook.optInt("id"));
                            a.setName(jsonBook.optString("name"));
                            a.setAbout(jsonBook.optString("about"));
                            mAuthors.add(a);
                        }
                        if (getActivity() != null) {
                            it = getActivity().getIntent();
                            int newAuthorId = it.getIntExtra("newAuthorId", -1);
                            Log.d("NewBookFragment", "newAuthorId: " + newAuthorId);
                            if (newAuthorId != -1) {
                                setSpinnerByAuthorId(newAuthorId);
                                it.removeExtra("newAuthorId");
                                Log.d("NewBookFragment", "newAuthorId depois do removeExtra: " + it.getIntExtra("newAuthorId", -1));
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getActivity().getApplicationContext(),
                            "Não foi possível carregar a lista de autores. " +
                                    "Tente novamente mais tarde",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getActivity().getApplicationContext(),
                        "Não foi possível carregar a lista de autores. Verifique sua conexão" +
                                " e tente novamente",
                        Toast.LENGTH_SHORT).show();
            }
        });
        ArrayAdapter<Author> adapter = new ArrayAdapter<Author>(context,
                android.R.layout.simple_spinner_item, mAuthors);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_autor.setAdapter(adapter);
        spinner_autor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Author a = (Author) parent.getSelectedItem();
                if (position > 0) { //O primeiro autor do spinner é o placeholder e não pode ser usado
                    Log.d("NewBookFragment", "id: "+ a.getId() +
                            "\nnome: " + a.getName() +
                            "\nsobre: " + a.getAbout());
                    authorId = a.getId();
                } else {
                    authorId = -1;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        btn_add_author = view.findViewById(R.id.fr_new_book_add_author);
        btn_add_author.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyboard();
                Fragment myFragment = new NewAuthorFragment();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.containerHome, myFragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        //Gêneros
        btn_select_genres = view.findViewById(R.id.fr_new_book_genres);
        stringCall = service.getGenres("Bearer "+user.getToken());
        stringCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    String responseString = response.body();

                    JSONArray root = null;
                    try {
                        root = new JSONArray(responseString);
                        listGenres = new String[root.length()];
                        listGenresIds = new int[root.length()];
                        checkedItems = new boolean[root.length()];
                        for (int i = 0; i < root.length(); i++) {
                            JSONObject jsonGenre = root.getJSONObject(i);
                            listGenres[i] = jsonGenre.optString("name");
                            listGenresIds[i] = jsonGenre.optInt("id");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getActivity().getApplicationContext(),
                            "Não foi possível carregar a lista de gêneros. " +
                                    "Tente novamente mais tarde",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getActivity().getApplicationContext(),
                        "Não foi possível carregar a lista de gêneros. Verifique sua conexão" +
                                " e tente novamente",
                        Toast.LENGTH_SHORT).show();
            }
        });
        //listGenres = new String[] {"Épico", "Fábula", "Epopeia", "Novela", "Conto", "Crônica", "Ensaio", "Romance"};
        //checkedItems = new boolean[listGenres.length];
        genresId = new int[0];
        btn_select_genres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyboard();
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
                mBuilder.setTitle("Selecionar Gêneros");
                mBuilder.setMultiChoiceItems(listGenres, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position, boolean isChecked) {
                        if (isChecked) {
                            if (!mBookGenres.contains(position)) {
                                mBookGenres.add(position);
                                Log.d("GenrerChoice", "Add option "+position);
                            }
                        } else if (mBookGenres.contains(position)) {
                            mBookGenres.remove(mBookGenres.indexOf(position));
                            Log.d("GenrerChoice", "Remove option "+position);
                        }
                    }
                });
                mBuilder.setCancelable(false);
                mBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //String item = "";
                        genresId = new int[mBookGenres.size()];
                        for (int i = 0; i < mBookGenres.size(); i++) {
                            //item += listGenresIds[mBookGenres.get(i)] + ". " + listGenres[mBookGenres.get(i)];
                            genresId[i] = listGenresIds[mBookGenres.get(i)];
                            //if (i != mBookGenres.size()-1) {
                            //    item += ", ";
                            //}
                        }
                        //Toast.makeText(context, item, Toast.LENGTH_SHORT).show();
                    }
                });
                mBuilder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                mBuilder.setNeutralButton("Limpar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for (int i = 0; i < checkedItems.length; i++) {
                            checkedItems[i] = false;
                            mBookGenres.clear();
                            genresId = new int[0];
                        }
                    }
                });
                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });
        btn_add_genre = view.findViewById(R.id.fr_new_book_add_genre);
        btn_add_genre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyboard();
                Fragment myFragment = new NewGenreFragment();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.containerHome, myFragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        edt_title = view.findViewById(R.id.fr_new_book_title);
        edt_original_title = view.findViewById(R.id.fr_new_book_original_title);
        edt_isbn = view.findViewById(R.id.fr_new_book_isbn);
        edt_publisher = view.findViewById(R.id.fr_new_book_publisher);
        edt_edition = view.findViewById(R.id.fr_new_book_edition);
        edt_num_pages = view.findViewById(R.id.fr_new_book_num_pages);
        edt_language = view.findViewById(R.id.fr_new_book_language);
        edt_description = view.findViewById(R.id.fr_new_book_description);
        btn_cadastrar = view.findViewById(R.id.fr_new_book_cadastrar);
        btn_cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyboard();
                if (validaCadastro()) {
                    btn_cadastrar.setEnabled(false);

                    dialog = new ProgressDialog(context);
                    dialog.setTitle("");
                    dialog.setMessage("Cadastrando livro...");
                    dialog.show();

                    Retrofit retrofit = new Retrofit.Builder()
                            .addConverterFactory(ScalarsConverterFactory.create())
                            .baseUrl(BibliotecaCientecAPIService.BASE_URL)
                            .build();

                    BibliotecaCientecAPIService service = retrofit.create(BibliotecaCientecAPIService.class);
                    MultipartBody.Part filePart = MultipartBody.Part.createFormData("cover", file.getName(), RequestBody.create(MediaType.parse("image/jpeg"), file));
                    Call<String> stringCall = service.postNewBook(
                            "Bearer "+user.getToken(),
                            filePart, edt_isbn.getText().toString(), edt_title.getText().toString(),
                            edt_edition.getText().toString(), edt_publisher.getText().toString(),
                            authorId, edt_original_title.getText().toString(), edt_description.getText().toString(),
                            edt_num_pages.getText().toString(), edt_language.getText().toString()
                    );
                    stringCall.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            if (response.isSuccessful()) {
                                String responseString = response.body();

                                JSONObject root = null;
                                try {
                                    root =  new JSONObject(responseString);
                                    JSONObject jsonBook = root.optJSONObject("book");
                                    int bookId = jsonBook.optInt("id");

                                    //Chamada da API para cadastrar os gêneros do livro
                                    Retrofit retrofit = new Retrofit.Builder()
                                            .addConverterFactory(ScalarsConverterFactory.create())
                                            .baseUrl(BibliotecaCientecAPIService.BASE_URL)
                                            .build();

                                    BibliotecaCientecAPIService service = retrofit.create(BibliotecaCientecAPIService.class);
                                    Call<String> stringCall = service.postBookGenre(
                                            "Bearer "+user.getToken(),
                                            bookId, genresId
                                    );
                                    stringCall.enqueue(new Callback<String>() {
                                        @Override
                                        public void onResponse(Call<String> call, Response<String> response) {
                                            if (response.isSuccessful()) {
                                                Toast.makeText(getActivity().getApplicationContext(),
                                                        "Livro cadastrado com sucesso",
                                                        Toast.LENGTH_SHORT).show();
                                                limpaPreenchimento();
                                            } else {
                                                Toast.makeText(getActivity().getApplicationContext(),
                                                        "Não foi possível cadastrar o livro",
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                        @Override
                                        public void onFailure(Call<String> call, Throwable t) {
                                            Toast.makeText(getActivity().getApplicationContext(),
                                                    "Não foi possível cadastrar o livro",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                Toast.makeText(getActivity().getApplicationContext(),
                                        "Não foi possível cadastrar o livro",
                                        Toast.LENGTH_SHORT).show();
                            }
                            dialog.dismiss();
                            btn_cadastrar.setEnabled(true);
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            dialog.dismiss();
                            Toast.makeText(getActivity().getApplicationContext(),
                                    "Não foi possível cadastrar o livro",
                                    Toast.LENGTH_SHORT).show();
                            btn_cadastrar.setEnabled(true);
                        }
                    });
                } else {
                    Toast.makeText(getActivity().getApplicationContext(),
                            "Preencha os campos corretamente",
                            Toast.LENGTH_SHORT).show();
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
        ((HomeActivity)getActivity()).getSupportActionBar().setTitle("Cadastrar Livro");
        super.onCreateOptionsMenu(menu, inflater);
    }

    //Métodos para manipulação da galeria e da câmera
    private void callGallery() {
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PERMISSAO_REQUEST);
            } else {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PERMISSAO_REQUEST);
            }
        } else {
            openGallery();
        }
    }

    private void callCamera() {
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.CAMERA)) {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.CAMERA},
                        PERMISSAO_REQUEST);
            } else {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.CAMERA},
                        PERMISSAO_REQUEST);
            }
        } else {
            if (ContextCompat.checkSelfPermission(context,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            PERMISSAO_REQUEST);
                } else {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            PERMISSAO_REQUEST);
                }
            } else {
                if (hasCameraSupport()) {
                    openCamera();
                } else {
                    Toast.makeText(context, "Nenhuma câmera detectada", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void openGallery() {
        Intent it = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(it, GALERIA_IMAGENS);
    }

    public boolean hasCameraSupport() {
        boolean hasSupport = false;
        if(getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            hasSupport = true;
        }
        return hasSupport;
    }

    private void openCamera() {
        Intent it = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (it.resolveActivity(getActivity().getPackageManager()) != null) {
            try {
                arquivoFoto = criaArquivo();
            } catch (IOException e) {}
            if (arquivoFoto != null) {
                Uri photoURI = FileProvider.getUriForFile(context,
                        context.getApplicationContext().getPackageName() +
                                ".provider", arquivoFoto);
                it.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(it, CAMERA);
            }
        }
    }

    private File criaArquivo() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        File pasta = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File imagem = new File(pasta.getPath() + File.separator
                + "JPG_" + timeStamp + ".jpg");

        return imagem;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == GALERIA_IMAGENS) {
            //Exibe a imagem escolhida no ImageView
            img_book_cover.setVisibility(View.VISIBLE);

            Uri selectedImage = data.getData();
            String[] filePath = { MediaStore.Images.Media.DATA };
            Cursor c = context.getContentResolver().query(selectedImage, filePath, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePath[0]);
            String picturePath = c.getString(columnIndex);
            c.close();
            Bitmap imagemGaleria = (BitmapFactory.decodeFile(picturePath));
            imagemGaleria = rotateBitmap(imagemGaleria);
            file = new File(picturePath); // Enviar esse arquivo pelo multipartformdata
            verificaTamanhoImagem(file, imagemGaleria);
        }

        if (resultCode == RESULT_OK && requestCode == CAMERA) {
            context.sendBroadcast(new Intent(
                    Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                    Uri.fromFile(arquivoFoto)
            ));
            exibirImagem();
        }

        if (file == null) {
            //Esconde o ImageView até que seja selecionada uma imagem
            img_book_cover.setVisibility(View.GONE);
        }
    }

    private void exibirImagem() {
        //Exibe a imagem escolhida no ImageView
        img_book_cover.setVisibility(View.VISIBLE);

        int targetW = 110;
        int targetH = 162;

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(arquivoFoto.getAbsolutePath(), bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;

        Bitmap bitmap =
                BitmapFactory.decodeFile(arquivoFoto.getAbsolutePath(), bmOptions);
        bitmap = rotateBitmap(bitmap);
        file = new File(arquivoFoto.getAbsolutePath());
        verificaTamanhoImagem(file, bitmap);
    }

    private Bitmap rotateBitmap(Bitmap bitmap) {
        if (bitmap.getWidth() > bitmap.getHeight()) {
            Log.i("image", "image");
            Matrix matrix = new Matrix();
            matrix.postRotate(90);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        }
        return bitmap;
    }

    private void verificaTamanhoImagem(File imageFile, Bitmap imageBitmap) {
        long tamanho = imageFile.length();
        if (tamanho < (2 * 1024 * 1024)) {
            img_book_cover.setImageBitmap(imageBitmap);
        } else {
            img_book_cover.setVisibility(View.GONE);
            Toast.makeText(context, "A imagem selecionada deve ter no máximo 2Mb", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validaCadastro() {
        boolean valido = true;
        String error_msg = "";
        if (file == null) {
            valido = false;
            error_msg += "Selecione uma Imagem de Capa\n";
        }
        if (edt_title.getText().toString().equals("")) {
            valido = false;
            error_msg += "Informe o Título do livro\n";
        }
        if (edt_original_title.getText().toString().equals("")) {
            valido = false;
            error_msg += "Informe o Título Original do livro\n";
        }
        if (edt_description.getText().toString().equals("")) {
            valido = false;
            error_msg += "Informe uma Descrição para o livro\n";
        }
        if (edt_num_pages.getText().toString().equals("")) {
            valido = false;
            error_msg += "Informe o Número de Páginas do livro\n";
        }
        if (edt_language.getText().toString().equals("")) {
            valido = false;
            error_msg += "Informe o Idioma do livro\n";
        }
        if (authorId <= 0) {
            valido = false;
            error_msg += "Selecione o Autor do livro\n";
        }
        if (genresId.length == 0) {
            valido = false;
            error_msg += "Selecione os Gêneros do livro\n";
        }
        if (error_msg.equals("")) {
            txt_error_msgs.setVisibility(View.GONE);
        } else {
            txt_error_msgs.setVisibility(View.VISIBLE);
        }
        txt_error_msgs.setText(error_msg);
        return valido;
    }

    private void limpaPreenchimento() {
        img_book_cover.setVisibility(View.GONE);
        file = null;
        img_book_cover.setImageResource(0);
        edt_title.setText("");
        edt_original_title.setText("");
        edt_isbn.setText("");
        edt_publisher.setText("");
        edt_edition.setText("");
        edt_description.setText("");
        edt_num_pages.setText("");
        edt_language.setText("");
        spinner_autor.setSelection(0);
        //limpa a seleção de gêneros
        for (int i = 0; i < checkedItems.length; i++) {
            checkedItems[i] = false;
            mBookGenres.clear();
        }
        genresId = new int[0];
    }

    private void setSpinnerByAuthorId(int id) {
        for(int i = 0; i<mAuthors.size(); i++) {
            if (mAuthors.get(i).getId() == id) {
                spinner_autor.setSelection(i, true);
                Log.d("NewBookFragment", mAuthors.get(i).getId() +" == "+ id +" i = "+i);
                break;
            }
        }
    }
}
