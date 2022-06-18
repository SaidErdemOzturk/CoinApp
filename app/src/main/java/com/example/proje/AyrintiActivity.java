package com.example.proje;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

public class AyrintiActivity extends AppCompatActivity {

    TextView coinName;
    TextView coinAsk;
    TextView coinBid;
    Button buton;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseFirestore mFirestore;
    String s3;
    private DocumentReference docRef2;
    int s=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ayrinti);
        coinName=(TextView)findViewById(R.id.coinNameText);
        coinAsk=(TextView)findViewById(R.id.askPrice);
        coinBid=(TextView)findViewById(R.id.bidPrice);
        buton=(Button)findViewById(R.id.button);
        Bundle coinName1 =getIntent().getExtras();
        double d1=coinName1.getDouble("coinAsk");
        double d2=coinName1.getDouble("coinBid");
        String s1=String.valueOf(d1);
        String s2=String.valueOf(d2);
        s3=coinName1.getString("coinName");
        coinAsk.setText(s1);
        coinBid.setText(s2);
        coinName.setText(s3);
        Button button =(Button)findViewById(R.id.button2);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AyrintiActivity.this,AnaSayfaActivity.class));
            }
        });

        buton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                mAuth = FirebaseAuth.getInstance();
                mUser = mAuth.getCurrentUser();
                mFirestore = FirebaseFirestore.getInstance();
                DocumentReference docRef = mFirestore.collection("Kullanicilar").document(mUser.getUid());
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {

                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                docRef2 = mFirestore.collection("Kullanicilar").document(mUser.getUid());

                                for (int a = 0; a < document.getData().size() - 5; a++) {
                                    s++;
                                    while (document.get("takip" + s) == null) {
                                        s++;
                                    }
                                    if (document.get("takip" + s).equals(s3)) {
                                        docRef2.update("takip" + s, FieldValue.delete());
                                        startActivity(new Intent(AyrintiActivity.this, AnaSayfaActivity.class));
                                    }
                                }
                            }

                        }
                    }
                });


            }
        });

    }

}