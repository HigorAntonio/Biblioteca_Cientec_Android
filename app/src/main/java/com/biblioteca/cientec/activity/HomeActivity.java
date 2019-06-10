package com.biblioteca.cientec.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.biblioteca.cientec.R;
import com.biblioteca.cientec.fragments.HomeFragment;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private CircleImageView profileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

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

        //alterar a imagem de usuário dinamicamente
        profileImage = (CircleImageView) findViewById(R.id.header_profile_image);
        profileImage.setImageDrawable(getResources().getDrawable(R.drawable.user_picture2));

        return true;
    }


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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        if (id == R.id.home) {
            Toast.makeText(getBaseContext(), "home", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.setting) {
            Toast.makeText(getBaseContext(), "setting", Toast.LENGTH_SHORT).show();
        }else if (id == R.id.log) {
            Toast.makeText(getBaseContext(), "logOut", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    public void onBackPressed() {
        DrawerLayout layout = (DrawerLayout)findViewById(R.id.drawer_layout);
        if (layout.isDrawerOpen(GravityCompat.START)) {
            layout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
