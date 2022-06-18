package com.example.proje;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class ProfilActivity extends AppCompatActivity  {
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseFirestore mFirestore;
    private EditText txtAd,txtEmail,txtSifre;
    private DocumentReference docRef;
    private static String ad,email,sifre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);
        txtAd=(EditText) findViewById(R.id.adEditTextProfil);
        txtEmail=(EditText) findViewById(R.id.emailEditTextProfil);
        txtSifre=(EditText) findViewById(R.id.sifreEditTextProfil);
        mAuth=FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();
        mFirestore=FirebaseFirestore.getInstance();
        docRef = mFirestore.collection("Kullanicilar").document(mUser.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful())
                {

                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        txtAd.setText(document.get("displayName").toString());
                        txtEmail.setText(document.get("eMail").toString());
                        txtSifre.setText(document.get("password").toString());
                    }

                }
            }
        });
    }


    public void guncelle(View view){
        mAuth=FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();
        ad=txtAd.getText().toString();
        email=txtEmail.getText().toString();
        sifre=txtSifre.getText().toString();
        HashMap<String,Object>map = new HashMap<>();
        map.put("displayName",ad);
        map.put("eMail",email);
        map.put("password",sifre);
        System.out.println(map);

        mFirestore.collection("Kullanicilar").document(mUser.getUid()).update(map).addOnCompleteListener(this,new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    mUser.updateEmail(email);
                    mUser.updatePassword(sifre);
                    Toast.makeText(ProfilActivity.this, "Güncelleme işlemi yapıldı.", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(ProfilActivity.this, "hata", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void geriDon2(View view){
        startActivity(new Intent(ProfilActivity.this,AnaSayfaActivity.class));
    }

}