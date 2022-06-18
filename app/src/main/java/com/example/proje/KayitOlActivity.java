package com.example.proje;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
public class KayitOlActivity extends AppCompatActivity {
    private FirebaseFirestore mFirestore;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private String txtAd, txtEmail, txtSifre, txtSifreTekrar;
    private EditText editAd, editEmail, editSifre, editSifreTekrar;
    private Kullanici mKullanici;
    private Uri myUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kayit_ol);
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        editAd = (EditText) findViewById(R.id.adEditText);
        editEmail = (EditText) findViewById(R.id.emailEditText2);
        editSifre = (EditText) findViewById(R.id.sifreEditText2);
        editSifreTekrar = (EditText) findViewById(R.id.sifreEditText22);
    }

    public void kayitOl2(View view) {
        txtAd = editAd.getText().toString();
        txtEmail = editEmail.getText().toString();
        txtSifre = editSifre.getText().toString();
        txtSifreTekrar = editSifreTekrar.getText().toString();
        if (!TextUtils.isEmpty(txtAd)) {
            if (!TextUtils.isEmpty(txtEmail)) {
                if (!TextUtils.isEmpty(txtSifre)) {
                    if (!TextUtils.isEmpty(txtSifreTekrar)) {
                        if (txtSifre.equals(txtSifreTekrar)) {
                            mAuth.createUserWithEmailAndPassword(txtEmail, txtSifre).addOnCompleteListener(KayitOlActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        mUser = mAuth.getCurrentUser();
                                        if (mUser != null) {
                                            mKullanici = new Kullanici(txtAd,txtEmail, txtSifre, mUser.getUid()
                                                    ,"https://w7.pngwing.com/pngs/841/727/png-transparent-computer-icons-user-profile-synonyms-and-antonyms-android-android-computer-wallpaper-monochrome-sphere.png");
                                            mFirestore.collection("Kullanicilar").document(mUser.getUid()).set(mKullanici).
                                                    addOnCompleteListener(KayitOlActivity.this, new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()){
                                                    Toast.makeText(KayitOlActivity.this, "Kayıt olma işlemi başarılı", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(KayitOlActivity.this,MainActivity.class);
                                                    startActivity(intent);
                                                    }
                                                }
                                            });
                                        }
                                    } else {
                                        Toast.makeText(KayitOlActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                        else{
                            Toast.makeText(KayitOlActivity.this,"Şifreler uyuşmuyor. Lütfenm şifreleri aynı giriniz.", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(KayitOlActivity.this, "Şifreyi terkar giriniz.", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(KayitOlActivity.this, "Şifreyi giriniz.", Toast.LENGTH_SHORT).show();
                }


            } else {
                Toast.makeText(KayitOlActivity.this, "Email giriniz.", Toast.LENGTH_SHORT).show();
            }

        }
        else {
            Toast.makeText(KayitOlActivity.this, "Adınızı giriniz.", Toast.LENGTH_SHORT).show();
        }
    }

    public void geriDon(View view){
        startActivity(new Intent (KayitOlActivity.this,MainActivity.class));
    }
}