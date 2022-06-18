package com.example.proje;

import android.content.Context;
import android.media.Image;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.google.firestore.v1.DocumentDelete;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NewCoinAdapter extends RecyclerView.Adapter<NewCoinAdapter.CoinHolder> {
    ArrayList<Coin> coinArrayList;
    Context context;
    ArrayList<Coin> takipList;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseFirestore mFirestore;
    private DocumentReference docRef;
    private DocumentReference docRef2;
    int s = 0;

    public NewCoinAdapter(Context context, ArrayList<Coin> coinArrayList, ArrayList<Coin> coinLikeList) {
        this.context = context;
        this.coinArrayList = coinArrayList;
        this.takipList = coinLikeList;
    }

    @NonNull
    @Override
    public CoinHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.coinler, parent, false);
        return new CoinHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CoinHolder holder, int position) {
        Coin selectedCoin = coinArrayList.get(position);
        holder.imageDisLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.imageLike.setVisibility(ImageView.VISIBLE);
                mAuth = FirebaseAuth.getInstance();
                mUser = mAuth.getCurrentUser();
                mFirestore = FirebaseFirestore.getInstance();
                docRef = mFirestore.collection("Kullanicilar").document(mUser.getUid());
                s = 0;
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                for (int a = 0; a < document.getData().size() - 4; a++) {
                                    s++;
                                    if (document.get("takip" + s) == null) {
                                        mFirestore.collection("Kullanicilar").document(mUser.getUid())
                                                .update("takip" + s, holder.coinName.getText().toString());
                                        break;
                                    } else {
                                    }
                                }
                                String s1 = holder.coinFiyat.getText().toString();
                                double double1 = Double.parseDouble(s1);
                                Coin coinEkle = new Coin(holder.coinName.getText().toString(), double1,1d);
                                takipList.add(coinEkle);
                            }
                        }
                    }
                });
            }
        });

        holder.imageLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.imageLike.setVisibility(ImageView.INVISIBLE);
                mAuth = FirebaseAuth.getInstance();
                mUser = mAuth.getCurrentUser();
                mFirestore = FirebaseFirestore.getInstance();
                docRef = mFirestore.collection("Kullanicilar").document(mUser.getUid());
                s = 0;
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {

                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                String s1 = holder.coinFiyat.getText().toString();
                                double double1 = Double.parseDouble(s1);
                                docRef2 = mFirestore.collection("Kullanicilar").document(mUser.getUid());

                                for (int a = 0; a < document.getData().size() - 5; a++) {
                                    s++;
                                    while (document.get("takip" + s) == null) {
                                        s++;
                                    }
                                    if (document.get("takip" + s).equals(holder.coinName.getText().toString())) {
                                        docRef2.update("takip" + s, FieldValue.delete());

                                    }
                                }
                            }

                        }
                    }
                });
            }
        });

        holder.setData(selectedCoin);
    }



    @Override
    public int getItemCount() {
        return coinArrayList.size();
    }

    class CoinHolder extends RecyclerView.ViewHolder {
        TextView coinName;
        TextView coinFiyat;
        ImageView imageLike;
        ImageView imageDisLike;
        public CoinHolder(@NonNull View itemView) {
            super(itemView);
            coinName = (TextView) itemView.findViewById(R.id.adTextView);
            coinFiyat = (TextView) itemView.findViewById(R.id.fiyatTextView);
            imageLike = (ImageView) itemView.findViewById(R.id.image_like);
            imageDisLike = (ImageView) itemView.findViewById(R.id.image_dislikes);
        }
        public void setData(Coin selectedCoin) {
            this.coinName.setText(selectedCoin.getParaAdi());
            this.coinFiyat.setText(String.valueOf(selectedCoin.getaFiyat()));
            this.imageLike.setVisibility(ImageView.INVISIBLE);
            this.imageDisLike.setVisibility(ImageView.VISIBLE);
            for (int c=0;c<takipList.size();c++){
                if (takipList.get(c).getParaAdi().equals(selectedCoin.getParaAdi()))
                {
                    this.imageLike.setVisibility(ImageView.VISIBLE);

                }
                else{
                    this.imageDisLike.setVisibility(ImageView.VISIBLE);
                }
            }

        }
    }
}
