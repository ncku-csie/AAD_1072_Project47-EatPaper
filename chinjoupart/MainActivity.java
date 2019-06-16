package com.yoyohung.eatpaper;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.yoyohung.eatpaper.model.Paper;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    // ==================
    private static final String TAG = "MainActivity";

    private FirebaseFirestore mFirestore;
    private DocumentReference mPaperRef;
    TextView mTextView_Content;
    private String documentID = "ylj7N7knsIakLbmiSbA3";
    DocumentSnapshot paper;
    // ===================

    private static final int RC_SIGN_IN = 9001; //any number you want RC_SIGN_IN
//    GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mAuth;
    List<AuthUI.IdpConfig> providers;
    Button btn_sign_out;

    Button check_papercool;
    Button new_paper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // ===
        mTextView_Content = (TextView) findViewById(R.id.textView_Content);
        // initializeFirestore ===
        initFirestore();
        // ====================
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // ===========================
                onPaperSelected(paper);
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
//        btn_sign_in=(Button)findViewById(R.id.btn_sign_in);
        btn_sign_out=(Button)findViewById(R.id.btn_sign_out);
        check_papercool=(Button)findViewById(R.id.check_papercool);
        new_paper=(Button)findViewById(R.id.new_paper);

        btn_sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Logout
                AuthUI.getInstance()
                        .signOut(MainActivity.this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                btn_sign_out.setEnabled(false);
//                                showSignInOptions();
                                startSignIn();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });

        check_papercool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //implement go to check the paper cool &I'm so cool

            }
        });

        new_paper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //implement go to new paper page

            }
        });






        // Init Provider
        providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build()

        );
//        showSignInOptions();
//        startSignIn();
//        Toast.makeText(MainActivity.this,""+FirebaseAuth.getInstance().getCurrentUser(),Toast.LENGTH_LONG).show();



//        AuthUI.getInstance()
//                .signOut(this)
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    public void onComplete(@NonNull Task<Void> task) {
//                        // ...
//                    }
//                });




    }

    // ======================

    @Override
    public void onStart() {
        super.onStart();

        // Start sign in if necessary
        if (shouldStartSignIn()) {
            startSignIn();
            return;
        }

//        // Apply filters
//        onFilter(mViewModel.getFilters());
//
//        // Start listening for Firestore updates
//        if (mAdapter != null) {
//            mAdapter.startListening();
//        }
    }

        private boolean shouldStartSignIn() {
        return (FirebaseAuth.getInstance().getCurrentUser() == null);
    }
    //    private void updateUI(FirebaseUser user) {
//        if (user != null) {
//            // Signed in
////            mStatusView.setText(getString(R.string.firebaseui_status_fmt, user.getEmail()));
////            mDetailView.setText(getString(R.string.id_fmt, user.getUid()));
//
////            findViewById(R.id.btn_sign_in).setVisibility(View.GONE);
//            findViewById(R.id.btn_sign_out).setVisibility(View.VISIBLE);
//        } else {
//            // Signed out
////            mStatusView.setText(R.string.signed_out);
////            mDetailView.setText(null);
//
////            findViewById(R.id.btn_sign_in).setVisibility(View.VISIBLE);
//            findViewById(R.id.btn_sign_out).setVisibility(View.GONE);
//        }
//    }
//    private void signOut() {
//        AuthUI.getInstance().signOut(this);
//        updateUI(null);
//    }







    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RC_SIGN_IN){
            IdpResponse response =IdpResponse.fromResultIntent(data);
            if(resultCode==RESULT_OK){
                //Get user
                FirebaseUser user =FirebaseAuth.getInstance().getCurrentUser();
                //Show email on Toast
//                Toast.makeText(this,""+user.getEmail(),Toast.LENGTH_SHORT).show();
                //Set Button signout
                btn_sign_out.setEnabled(true);
//                btn_sign_in.setVisibility(View.GONE);
                btn_sign_out.setVisibility(View.VISIBLE);
            }
            else{
                Toast.makeText(this,""+response.getError().getMessage(),Toast.LENGTH_SHORT).show();
            }


        }
    }

    private void startSignIn() {
        // Build FirebaseUI sign in intent. For documentation on this operation and all
        // possible customization see: https://github.com/firebase/firebaseui-android
        Intent intent = AuthUI.getInstance().createSignInIntentBuilder()
                .setIsSmartLockEnabled(!BuildConfig.DEBUG)
                .setAvailableProviders(providers)
                .setLogo(R.mipmap.ic_launcher)
                .build();

        startActivityForResult(intent, RC_SIGN_IN);



    }



    private void initFirestore() {
        mFirestore = FirebaseFirestore.getInstance();

        // Get reference to the paper
        mPaperRef = mFirestore.collection("paperStock").document(documentID);


        mPaperRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    paper = task.getResult();
                    if (paper.exists()) {
                        Paper paper = MainActivity.this.paper.toObject(Paper.class);
                        mTextView_Content.setText(paper.getPaperName() + paper.getCurrentQuantity() + paper.getUnit());
                        Log.d(TAG, "DocumentSnapshot data: " + MainActivity.this.paper.getData());
                    } else {
                        Log.d(TAG, "No such doc");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

    };
    public void onPaperSelected(DocumentSnapshot paper) {
        // Go to the details page for the selected paper
        Intent intent = new Intent(this, PaperDetailActivity.class);
//        intent.putExtra(PaperDetailActivity.KEY_PAPER_ID, paper.getId());

        startActivity(intent);
    }
    // ======================

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_tools) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
