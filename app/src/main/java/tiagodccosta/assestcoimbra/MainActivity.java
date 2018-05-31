package tiagodccosta.assestcoimbra;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ToolbarWidgetWrapper;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawer;
    private android.support.v7.widget.Toolbar toolbar;
    private ActionBarDrawerToggle mToggle;

    FragmentTransaction ft;

    FirebaseAuth mfirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mfirebaseAuth = FirebaseAuth.getInstance();

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.TOOLBAR);

        setSupportActionBar(toolbar);

        ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.flcontent, new FeedFrag());
        ft.commit();

        NavigationView nv = (NavigationView) findViewById(R.id.nView);
        mToggle = new ActionBarDrawerToggle(this, mDrawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(mToggle);
        mToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.profile:
                        ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.flcontent, new ProfileFrag());
                        ft.commit();
                        getSupportActionBar().setTitle(menuItem.getTitle());
                        menuItem.setChecked(true);
                        mDrawer.closeDrawers();
                        break;
                    case R.id.feed:
                        ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.flcontent, new FeedFrag());
                        ft.commit();
                        getSupportActionBar().setTitle(menuItem.getTitle());
                        menuItem.setChecked(true);
                        mDrawer.closeDrawers();
                        break;
                    case R.id.chat:
                        ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.flcontent, new MessagesFrag());
                        ft.commit();
                        getSupportActionBar().setTitle("Q&A");
                        menuItem.setChecked(true);
                        mDrawer.closeDrawers();
                        Snackbar.make(getCurrentFocus(), "Faça perguntas á Associação de Estudantes!", Snackbar.LENGTH_SHORT).show();
                        break;
                    case R.id.multimedia:
                        ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.flcontent, new GaleryFrag());
                        ft.commit();
                        getSupportActionBar().setTitle(menuItem.getTitle());
                        menuItem.setChecked(true);
                        mDrawer.closeDrawers();
                        break;
                    case R.id.study:
                        ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.flcontent, new StudyFrag());
                        ft.commit();
                        getSupportActionBar().setTitle(menuItem.getTitle());
                        menuItem.setChecked(true);
                        mDrawer.closeDrawers();
                        break;
                    case R.id.horarios:
                        ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.flcontent, new ScheduleFrag());
                        ft.commit();
                        getSupportActionBar().setTitle(menuItem.getTitle());
                        menuItem.setChecked(true);
                        mDrawer.closeDrawers();
                        break;
                    case R.id.logout:
                        logOut();
                        break;

                }

                return true;
            }

            });
    }


    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if(mToggle.onOptionsItemSelected(menuItem)) {
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    private void logOut() {
        startActivity(new Intent(MainActivity.this, LoginPage.class));
        finish();
        mfirebaseAuth.signOut();
        Toast.makeText(MainActivity.this, "Sessão Terminada", Toast.LENGTH_SHORT).show();
    }

}
