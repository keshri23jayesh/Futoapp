package com.example.jayesh123.futooapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Signup extends BaseActivity implements View.OnClickListener
{

    private static final String TAG = "EmailPassword";

    private EditText mEmailField;
    private EditText mPasswordField;
    private TextView mStatusTextView;

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //Views
        mEmailField = (EditText) findViewById(R.id.email);
        mPasswordField = (EditText) findViewById(R.id.pass);
        mStatusTextView = (TextView) findViewById(R.id.textView);
        //buttons
        findViewById(R.id.Login).setOnClickListener(this);
        findViewById(R.id.Signup).setOnClickListener(this);

        //ImageButtons
        findViewById(R.id.facebook).setOnClickListener(this);
        findViewById(R.id.google).setOnClickListener(this);

        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]
    }


    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }
    // [END on_start_check_user]

    // [ Register new user ]
    private void createAccount(String email, String password)
    {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm())
        {
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                if(task.isSuccessful())
                {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success");
                    FirebaseUser user = mAuth.getCurrentUser();
                    updateUI(user);
                }
                else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                    Toast.makeText(Signup.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                    updateUI(null);
                }
                // [START_EXCLUDE]
                hideProgressDialog();
                // [END_EXCLUDE]
            }
            // [END create_user_with_email]
        });

        showProgressDialog();

    }
    // [End_Login new user]

    // [sign new user]
    private void signIn(String email, String password)
    {
        Log.d(TAG, "signIn:" + email);
        if (!validateForm()) {
            return;
        }
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this , new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                if(task.isSuccessful())
                {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success");
                    FirebaseUser user = mAuth.getCurrentUser();
                    updateUI(user);
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                    Toast.makeText(Signup.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                    updateUI(null);
                }

                // [START_EXCLUDE]
                if (!task.isSuccessful()) {
                    mStatusTextView.setText(R.string.auth_failed);
                }
                hideProgressDialog();
                // [END_EXCLUDE]
            }
        });
        // [END sign_in_with_email]
    }
    // [End Sign new user]

    // [sign out]
    // private void signOut()
    //   {
    // mAuth.signOut();
    //updateUI(null);
    //}
    // [end sign out]

    //[send verfication email]
    //private void sendEmailVerification()
    //{
    //KUCH DIN KE BAAD
    //}
    //[end send verfication email]

    //[validate form]
    private boolean validateForm()
    {
        boolean valid = true;

        String email = mEmailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailField.setError("Required.");
            valid = false;
        } else {
            mEmailField.setError(null);
        }

        String password = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError("Required.");
            valid = false;
        } else {
            mPasswordField.setError(null);
        }

        return valid;

    }
    //[end validate form]

    //[update UI]
    private void updateUI(FirebaseUser user)
    {
        hideProgressDialog();
        if (user != null)
        {
            Intent i = new Intent(this ,ProfileSetup.class);
            startActivity(i);
                /* mStatusTextView.setText(getString(R.string.emailpassword_status_fmt, user.getEmail(), user.isEmailVerified()));
                mDetailTextView.setText(getString(R.string.firebase_status_fmt, user.getUid()));
                findViewById(R.id.email_password_buttons).setVisibility(View.GONE);
                findViewById(R.id.email_password_fields).setVisibility(View.GONE);
                findViewById(R.id.signed_in_buttons).setVisibility(View.VISIBLE);
                findViewById(R.id.verify_email_button).setEnabled(!user.isEmailVerified());  */
        }
        else
        {
            CharSequence text = "Hello toast!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(Signup.this, text, duration);
            toast.show();
                /*mStatusTextView.setText(R.string.signed_out);
                mDetailTextView.setText(null);
                findViewById(R.id.email_password_buttons).setVisibility(View.VISIBLE);
                findViewById(R.id.email_password_fields).setVisibility(View.VISIBLE);
                findViewById(R.id.signed_in_buttons).setVisibility(View.GONE);*/
        }
    }
    //[end update UI]
    @Override
    public void onClick(View v)
    {
        int i = v.getId();
        if (i == R.id.Signup) {
            createAccount(mEmailField.getText().toString(), mPasswordField.getText().toString());
        } else if (i == R.id.Login) {
            signIn(mEmailField.getText().toString(), mPasswordField.getText().toString());
        }else if (i == R.id.facebook)
        {
            Intent j = new Intent(this ,GoogleSignInActivity.class);
            startActivity(j);
        }
        else if (i == R.id.google)
        {
            Intent j = new Intent(this ,GoogleSignInActivity.class);
            startActivity(j);
        }
        /**else if (i == R.id.sign_out_button) {
         signOut();
         } else if (i == R.id.verify_email_button) {
         sendEmailVerification();
         }**/
    }
}
