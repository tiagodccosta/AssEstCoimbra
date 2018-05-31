package tiagodccosta.assestcoimbra;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class AdminActivity extends AppCompatActivity {

    private DrawerLayout mDrawer;
    private android.support.v7.widget.Toolbar toolbar;

    private ActionBarDrawerToggle toggle;

    FragmentTransaction ft;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        firebaseAuth = FirebaseAuth.getInstance();

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layoutAdmin);
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.flcontent2, new FeedFragAdmin());
        ft.commit();

        NavigationView nv = (NavigationView) findViewById(R.id.nView);
        toggle = new ActionBarDrawerToggle(AdminActivity.this, mDrawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.feed:
                        ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.flcontent2, new FeedFragAdmin());
                        ft.commit();
                        getSupportActionBar().setTitle(menuItem.getTitle());
                        menuItem.setChecked(true);
                        mDrawer.closeDrawers();
                        break;
                    case R.id.chat:
                        ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.flcontent2, new ChatAdmin());
                        ft.commit();
                        getSupportActionBar().setTitle("Q&A");
                        menuItem.setChecked(true);
                        mDrawer.closeDrawers();
                        Snackbar.make(getCurrentFocus(), "Responda ás perguntas dos utilizadores!", Snackbar.LENGTH_SHORT).show();
                        break;
                    case R.id.multimedia:
                        ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.flcontent2, new GaleryFragAdmin());
                        ft.commit();
                        getSupportActionBar().setTitle(menuItem.getTitle());
                        menuItem.setChecked(true);
                        mDrawer.closeDrawers();
                        break;
                    case R.id.study:
                        ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.flcontent2, new StudyFragAdmin());
                        ft.commit();
                        getSupportActionBar().setTitle(menuItem.getTitle());
                        menuItem.setChecked(true);
                        mDrawer.closeDrawers();
                        break;
                    case R.id.horarios:
                        ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.flcontent2, new ScheduleFragAdmin());
                        ft.commit();
                        getSupportActionBar().setTitle(menuItem.getTitle());
                        menuItem.setChecked(true);
                        mDrawer.closeDrawers();
                        break;
                    case R.id.logout:
                        firebaseAuth.signOut();
                        Toast.makeText(AdminActivity.this, "Sessão Terminada", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(AdminActivity.this, LoginPage.class));
                        finish();
                        break;

                }

                return true;
            }

        });
    }


    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if(toggle.onOptionsItemSelected(menuItem)) {
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

}
