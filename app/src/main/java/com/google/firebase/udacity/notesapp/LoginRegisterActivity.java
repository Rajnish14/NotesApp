package com.google.firebase.udacity.notesapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.auth.api.Auth;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class LoginRegisterActivity extends AppCompatActivity {
    private static final String TAG = "LoginRegisterActivity";
    //FirebaseUser user;
    static final int AUTHUI_REQUEST_CODE = 10001;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);

        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            startActivity(new Intent(this,MainActivity.class));
            finish();
        }
    }

    public void handleloginregister(View view) {
        List<AuthUI.IdpConfig> provider = Arrays.asList(
            new AuthUI.IdpConfig.EmailBuilder().build(),
            new AuthUI.IdpConfig.GoogleBuilder().build()
                //new AuthUI.IdpConfig.PhoneBuilder().build()
        );
        Intent intent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(provider)
                .setTosAndPrivacyPolicyUrls("https://example.com","https://example.com")
                .setLogo(R.drawable.notes)
                .setAlwaysShowSignInMethodScreen(true)
                .build();
        startActivityForResult(intent,AUTHUI_REQUEST_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == AUTHUI_REQUEST_CODE){
            if(resultCode == RESULT_OK){
                // We have signed in the user or we have a new user
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                Log.d(TAG,"onActivityResult: " + user.getEmail());
                if(user.getMetadata().getCreationTimestamp() == user.getMetadata().getLastSignInTimestamp()){
                    Toast.makeText(this,"Welcome new User",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(this,"Welcome back!",Toast.LENGTH_SHORT).show();
                }
                Intent intent = new Intent(this,MainActivity.class);
                startActivity(intent);
                this.finish();
            }
            else{
                // signing in failed
                IdpResponse idpResponse = IdpResponse.fromResultIntent(data);
                if(idpResponse == null){
                    Log.d(TAG,"onActivityResult: User cancelled the sign in request");
                }
                else{
                    Log.e(TAG,"onActivityResult:",idpResponse.getError());
                }
            }
        }

    }
}