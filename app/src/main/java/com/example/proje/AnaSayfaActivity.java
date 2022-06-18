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
import android.widget.ListAdapter;
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
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class AnaSayfaActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseFirestore mFirestore;
    private String deger;
    private ImageView imageView;
    private BottomNavigationView bottomNavigationView;
    //-----------------------------------------------------------------------------
    String yanit;
    StringBuffer response;
    URL url;
    ArrayList coinList=new ArrayList();
    RecyclerView recyclerView;
    private ProgressDialog progressDialog;
    private CoinAdapter coinAdapter;
    private ArrayList<Coin> coinler = new ArrayList<>();
    private ArrayList<Coin> coinlerList = new ArrayList<>();
    private String path="https://www.binance.com/api/v3/ticker/bookTicker";
    //-----------------------------------------------------------------------------
    TextView adText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ana_sayfa);
        mAuth = FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();
        bottomNavigationView = (BottomNavigationView)findViewById(R.id.main_activity_bottomView);
        recyclerView=(RecyclerView)findViewById(R.id.list_recycler);
        imageView=(ImageView)findViewById(R.id.imageViewAnaSayfa);
        adText=(TextView)findViewById(R.id.adTextView);
        mFirestore=FirebaseFirestore.getInstance();
        DocumentReference docRef = mFirestore.collection("Kullanicilar").document(mUser.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        deger=document.get("photoUrl").toString();
                        Picasso.with(AnaSayfaActivity.this).load(deger).into(imageView);
                    }
                }
            }
        });




        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.bottom_nav_ic_takip:
                        return true;
                    case R.id.bottom_nav_ic_tumu:
                        startActivity(new Intent(AnaSayfaActivity.this,TumActivity.class));
                }
                return false;
            }
        });

        new AnaSayfaActivity.GetServerData().execute();
    }




    class GetServerData extends AsyncTask {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=new ProgressDialog(AnaSayfaActivity.this);
            progressDialog.setMessage("Cripto para bilgileri getiriliyor");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Object doInBackground(Object[] objects) {
//getWebServiceResponseData

            return GetWebServiceResponseData();
        }

        @Override
        protected void onPostExecute(Object o) {

            super.onPostExecute(o);
            if ( progressDialog.isShowing()) {
                progressDialog.dismiss();
                RecyclerViewAtama();
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

    public void RecyclerViewAtama(){
        FirebaseFirestore mFirestore2=FirebaseFirestore.getInstance();
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

                        for (int i=0;i<coinler.size();i++){
                            if (coinler.get(i).getParaAdi().equals(document.get("takip"+s))){
                                Coin coinEkle=new Coin(coinler.get(i).getParaAdi(),coinler.get(i).getaFiyat(),coinler.get(i).getsFiyat());
                                coinlerList.add(coinEkle);
                            }
                        }
                    }
                    coinAdapter=new CoinAdapter(coinlerList,AnaSayfaActivity.this);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(AnaSayfaActivity.this,LinearLayoutManager.VERTICAL,false);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    recyclerView.setAdapter(coinAdapter);
                }
            }
        });
    }



    public void showPopup(View view){
        PopupMenu popup = new PopupMenu(this, view);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.secenekler_menu);
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.secenekler_profil:
                startActivity(new Intent(AnaSayfaActivity.this,ProfilActivity.class));
                return true;
            case R.id.secenekler_cikis: {
                mAuth.signOut();
                startActivity(new Intent(AnaSayfaActivity.this,MainActivity.class));
                Toast.makeText(AnaSayfaActivity.this, "Çıkış yapıldı", Toast.LENGTH_LONG).show();
            }
            return true;
            default:
                return false;
        }
    }
}