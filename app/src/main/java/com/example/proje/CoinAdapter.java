package com.example.proje;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class CoinAdapter extends RecyclerView.Adapter<CoinAdapter.ViewHolder>{


    ArrayList<Coin> takipList;
    Context context;


    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseFirestore mFirestore;
    private DocumentReference docRef;

    public CoinAdapter(ArrayList<Coin> takipList, Context context) {
        this.takipList = takipList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.anasayfa_coinler, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Coin selectedCoin = takipList.get(position);
        holder.coinName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AyrintiActivity.class);
                intent.putExtra("coinName",selectedCoin.getParaAdi());
                intent.putExtra("coinAsk",selectedCoin.getaFiyat());
                intent.putExtra("coinBid",selectedCoin.getsFiyat());
                System.out.println(selectedCoin.getParaAdi()+"------"+selectedCoin.getaFiyat()+"-----------"+selectedCoin.getsFiyat());
                v.getContext().startActivity(intent);
            }
        });
        holder.setData(selectedCoin);
    }

    @Override
    public int getItemCount() {
        return takipList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView coinName;
        TextView coinFiyat;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            coinName = (TextView) itemView.findViewById(R.id.adTextView);
            coinFiyat = (TextView) itemView.findViewById(R.id.fiyatTextView);



        }
        public void setData(Coin selectedCoin){
            this.coinName.setText(selectedCoin.getParaAdi());
            this.coinFiyat.setText(String.valueOf(selectedCoin.getaFiyat()));
        }
    }
}
