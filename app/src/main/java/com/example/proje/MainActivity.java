package com.example.proje;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    private EditText editEmail, editSifre;
    private String txtEmail, txtSifre;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseFirestore mFirestore;
    private String deger;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth=FirebaseAuth.getInstance();
        editEmail = (EditText) findViewById(R.id.emailEditText);
        editSifre = (EditText) findViewById(R.id.sifreEditText);



    }

    public void kayitOl(View view) {
        startActivity(new Intent(MainActivity.this, KayitOlActivity.class));
    }


    public void girisYap(View view) {

        txtEmail = editEmail.getText().toString();
        txtSifre = editSifre.getText().toString();
        if (!TextUtils.isEmpty(txtEmail) && !TextUtils.isEmpty(txtSifre)) {
            mAuth.signInWithEmailAndPassword(txtEmail, txtSifre).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    {
                        if (task.isSuccessful()) {
                            mUser=mAuth.getCurrentUser();
                            Toast.makeText(MainActivity.this, "Giriş başarılı.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MainActivity.this,AnaSayfaActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(MainActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });

        } else {
            Toast.makeText(MainActivity.this, "Boş alan bırakmayınız", Toast.LENGTH_SHORT).show();
        }
    }
}