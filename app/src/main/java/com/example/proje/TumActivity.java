package com.example.proje;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Array;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class TumActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseFirestore mFirestore;
    private String deger;
    private ImageView imageView;
    private BottomNavigationView bottomNavigationView;
    String yanit;
    StringBuffer response;
    URL url;
    private ProgressDialog progressDialog;
    private ArrayList<Coin> coinler = new ArrayList<>();
    private String path="https://www.binance.com/api/v3/ticker/bookTicker";
    //-------------------------------------------------------------------------------------

    private RecyclerView recyclerView;
    private NewCoinAdapter newCoinAdapter;
    ArrayList<Coin>likeList=new ArrayList<>();
    private FirebaseFirestore mFirestore2;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tum);
        bottomNavigationView = (BottomNavigationView)findViewById(R.id.main_activity_bottomView);
        imageView=(ImageView)findViewById(R.id.imageViewAnaSayfa);

        recyclerView=findViewById(R.id.list);
        mAuth = FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();
        mFirestore= FirebaseFirestore.getInstance();
        DocumentReference docRef = mFirestore.collection("Kullanicilar").document(mUser.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        deger=document.get("photoUrl").toString();
                        Picasso.with(TumActivity.this).load(deger).into(imageView);
                    }
                }
            }
        });
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.bottom_nav_ic_takip:
                        startActivity(new Intent(TumActivity.this,AnaSayfaActivity.class));
                    case R.id.bottom_nav_ic_tumu:
                }
                return false;
            }
        });

        new TumActivity.GetServerData().execute();


    }
    class GetServerData extends AsyncTask {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();


            progressDialog=new ProgressDialog(TumActivity.this);
            progressDialog.setMessage("Cripto para bilgileri getiriliyor");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            return GetWebServiceResponseData();
        }
        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            if ( progressDialog.isShowing()) {
                progressDialog.dismiss();
                recyclerViewAtama();
            }
        }
    }
    protected Void GetWebServiceResponseData(){
        try {
            url=new URL(path);
            HttpURLConnection conn=(HttpURLConnection)url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            int yanitkodu = conn.getResponseCode();
            if (yanitkodu== HttpsURLConnection.HTTP_OK){
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String output;
                response = new StringBuffer();
                while ((output=in.readLine())!=null){
                    response.append(output);
                }
                in.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        yanit=response.toString();
        try {
            JSONArray jsonArray = new JSONArray(yanit);
            for (int i = 0;i<jsonArray.length();i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Coin coin1 = new Coin(jsonObject.getString("symbol"),jsonObject.getDouble("askPrice"),jsonObject.getDouble("bidPrice"));
                coinler.add(coin1);
            }
        }
        catch (JSONException e){
            e.printStackTrace();
        }
        return null;
    }

    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.secenekler_profil:
                startActivity(new Intent(TumActivity.this,ProfilActivity.class));
                return true;
            case R.id.secenekler_cikis: {
                mAuth.signOut();
                startActivity(new Intent(TumActivity.this,MainActivity.class));
                Toast.makeText(TumActivity.this, "Çıkış yapıldı", Toast.LENGTH_LONG).show();
            }
            return true;
            default:
                return false;
        }
    }


    public void showPopup(View view){
        PopupMenu popup = new PopupMenu(this, view);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.secenekler_menu);
        popup.show();
    }
    public void recyclerViewAtama(){
        mFirestore2=FirebaseFirestore.getInstance();
        mFirestore2.collection("Kullanicilar").document(mUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    int s=0;
                    for (int a =0;a<document.getData().size()-5;a++){
                        do{
                            s++;
                        }while (document.get("takip"+s)==null);
                        for (int b=0;b<coinler.size();b++){
                            if (document.get("takip"+s).equals(coinler.get(b).getParaAdi())){
                                Coin coinEkle=new Coin(document.get("takip"+s).toString(),coinler.get(b).getaFiyat(),coinler.get(b).getsFiyat());
                                likeList.add(coinEkle);
                            }
                        }
                    }
                }
            }
        });
        newCoinAdapter=new NewCoinAdapter(this,coinler,likeList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(newCoinAdapter);
    }
}