package com.biblioteca.cientec.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.biblioteca.cientec.Models.User;
import com.biblioteca.cientec.R;
import com.biblioteca.cientec.fragments.HomeFragment;
import com.biblioteca.cientec.fragments.NewBookFragment;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Intent it;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private CircleImageView profileImage;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        it = getIntent();
        user = (User) it.getSerializableExtra("user");

        setUpToolbar();

        //SetUp NavigationDrawer
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.containerHome, new HomeFragment())
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        //alterar a imagem de usuário dinamicamente na Navigation Drawer
        profileImage = (CircleImageView) findViewById(R.id.header_profile_image);
        profileImage.setImageDrawable(getResources().getDrawable(R.drawable.user_picture2));

        //Coloca o nome e email do usuario na navigation drawer
        TextView nav_draw_user_name = findViewById(R.id.header_user_name);
        nav_draw_user_name.setText(user.getName());
        TextView nav_draw_user_email = findViewById(R.id.header_user_email);
        nav_draw_user_email.setText(user.getEmail());

        return true;
    }

    //Toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search) {
            Intent it = new Intent(getBaseContext(), SearchActivity.class);
            startActivity(it);
            return true;
        } else if (id == R.id.action_filter) {
            Toast.makeText(this, "Clicou no Filter", Toast.LENGTH_SHORT).show();
            return true;
        } else if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Navigation Drawer
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        if (id == R.id.home) {
            //O código do bloco if/else a seguir
            //verifica se existe um HomeFragment na backstack, se existir, abre o fragment existente;
            //se não existir cria um novo fragment e navega até ele
            Fragment fragment = getSupportFragmentManager().findFragmentByTag("HomeFragment");
            if (fragment == null) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.containerHome, new HomeFragment(), "HomeFragment")
                        .addToBackStack("HomeFragment")
                        .commit();
            } else {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.containerHome, fragment, "HomeFragment")
                        .addToBackStack("HomeFragment")
                        .commit();
            }
            onBackPressed(); //Recolhe a navDrawer se estiver aberta
        } else if (id == R.id.new_book) {
            //O código do bloco if/else a seguir
            //verifica se existe um NewBookFragment na backstack, se existir, abre o fragment existente;
            //se não existir cria um novo fragment e navega até ele
            Fragment fragment = getSupportFragmentManager().findFragmentByTag("NewBookFragment");
            if (fragment == null) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.containerHome, new NewBookFragment(), "NewBookFragment")
                        .addToBackStack("NewBookFragment")
                        .commit();
            } else {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.containerHome, fragment, "NewBookFragment")
                        .addToBackStack("HomeFragment")
                        .commit();
            }
            onBackPressed();//Recolhe a navDrawer se estiver aberta
        } else if (id == R.id.setting) {
            Toast.makeText(getBaseContext(), "setting", Toast.LENGTH_SHORT).show();
        }else if (id == R.id.log) {
            //Toast.makeText(getBaseContext(), "logOut", Toast.LENGTH_SHORT).show();
            finish();
        }
        return false;
    }

    public void onBackPressed() {
        DrawerLayout layout = (DrawerLayout)findViewById(R.id.drawer_layout);
        closeKeyboard();

        Fragment fr = getSupportFragmentManager().findFragmentByTag("NewBookFragment");

        if (layout.isDrawerOpen(GravityCompat.START)) { //Recolhe a navDrawer se estiver aberta
            layout.closeDrawer(GravityCompat.START);
        } else if (fr != null && fr.isVisible()) {
            //Se o usuário estiver na tela de cadastro de livros (NewBookFragment) e pressionar
            //o botão de voltar o app deve voltar para o início (HomeFragment)
            Fragment fragment = getSupportFragmentManager().findFragmentByTag("HomeFragment");
            if (fragment == null) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.containerHome, new HomeFragment(), "HomeFragment")
                        .addToBackStack("HomeFragment")
                        .commit();
            } else {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.containerHome, fragment, "HomeFragment")
                        .addToBackStack("HomeFragment")
                        .commit();
            }
        } else {
            super.onBackPressed();
        }
    }

    public ActionBarDrawerToggle getmToggle() {
        return this.mToggle;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
