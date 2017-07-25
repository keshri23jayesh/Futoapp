package com.example.jayesh123.futooapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class ProfileSetup extends AppCompatActivity
{
    private StorageReference mStorage;
    EditText username, mobileno;
    Button setok;
    private FirebaseUser mAuth;
    private DatabaseReference mDatabase;
    private FirebaseAuth.AuthStateListener mAuthlistener;
    private DatabaseReference userData;
    private ImageButton propic;
    private Uri uri = null;
    private static final int GALLERY_INTENT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setup);
        mAuth = FirebaseAuth.getInstance().getCurrentUser();
        username = (EditText)findViewById(R.id.username);
        mobileno = (EditText)findViewById(R.id.mobileno);
        setok = (Button)findViewById(R.id.setok);
        propic = (ImageButton)findViewById(R.id.propic);
        mStorage = FirebaseStorage.getInstance().getReference();


        setok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Accountsetup();
            }
        });

        propic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryintent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryintent.setType("image/*");
                startActivityForResult(galleryintent, GALLERY_INTENT);
            }
            });
    }

    private void Accountsetup()
    {
        final String uname = username.getText().toString().trim();
        final String mobile = mobileno.getText().toString().trim();
        final String uid = mAuth.getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users Lists");
        StorageReference mref = mStorage.child("profilepic/" + uri.getLastPathSegment());
        mref.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>()
        {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
            {
                String downloaduri = taskSnapshot.getDownloadUrl().toString();
                mDatabase.child(uid).child("image").setValue(downloaduri);
                mDatabase.child(uid).child("phoneno").setValue(mobile);
                mDatabase.child(uid).child("username").setValue(uname);
                Toast.makeText(ProfileSetup.this, "Upload successful...", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener()
        {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                Toast.makeText(ProfileSetup.this, "Upload Unsuccessful...", Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK && data.getData() != null)
        {
            uri = data.getData();
            propic.setImageURI(uri);
        }
    }
}
