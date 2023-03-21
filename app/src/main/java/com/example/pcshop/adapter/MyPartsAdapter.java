package com.example.pcshop.adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pcshop.R;
import com.example.pcshop.ShowDetailsOfItem;
import com.example.pcshop.listener.ICartLoadListener;
import com.example.pcshop.listener.IRecyclerViewClickListener;
import com.example.pcshop.model.CartModel;
import com.example.pcshop.model.PartsModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MyPartsAdapter extends RecyclerView.Adapter<MyPartsAdapter.MyGamesViewHolder> {
    private Context context;
    private List<PartsModel> partsModelList;
    private ICartLoadListener iCartLoadListener;



    public MyPartsAdapter(Context context, List<PartsModel> partsModelList, ICartLoadListener iCartLoadListener) {
        this.context = context;
        this.partsModelList = partsModelList;
        this.iCartLoadListener = iCartLoadListener;
    }

    public MyPartsAdapter(Context context, ArrayList<PartsModel> gamesArrayList) {

    }

    @NonNull
    @Override
    public MyGamesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyGamesViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_parts_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyGamesViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Glide.with(context)
                .load(partsModelList.get(position).getImage())
                .into(holder.imageView);
        holder.imageView.setTag(partsModelList.get(position).getImage().toString());
        holder.txtPrice.setText(new StringBuilder("$").append(partsModelList.get(position).getPrice()));
        holder.txtName.setText(new StringBuilder().append(partsModelList.get(position).getName()));


        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ShowDetailsOfItem.class);
                intent.putExtra("key", partsModelList.get(position).getKey());
                Log.d("KEY", partsModelList.get(position).getKey());
                context.startActivity(intent);
            }
        });
        holder.cartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addToCart(partsModelList.get(position));
            }
        });


    }

    private void addToCart(PartsModel partsModel) {
        DatabaseReference userCart = FirebaseDatabase
                .getInstance()
                .getReference("Cart")
                .child("UNIQUE_USER_ID");
        Log.d("key", partsModel.getKey());
        userCart.child(partsModel.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    CartModel cartModel = snapshot.getValue(CartModel.class);
                    cartModel.setQuantity(cartModel.getQuantity()+1);
                    Map<String,Object> updateData = new HashMap<>();
                    updateData.put("quantity", cartModel.getQuantity());
                    updateData.put("totalPrice", cartModel.getQuantity()*Float.parseFloat(cartModel.getPrice()));
                            userCart.child(partsModel.getKey())
                                    .updateChildren(updateData)
                                    .addOnSuccessListener(unused -> {
                                       iCartLoadListener.onCartLoadFailed("Dodano u košaricu");
                                    })
                            .addOnFailureListener(e -> iCartLoadListener.onCartLoadFailed("Greška"));
                }
                else{
                    CartModel cartModel = new CartModel();
                    Log.d("Key", partsModel.getKey());
                    cartModel.setName(partsModel.getName());
                    Log.d("name", partsModel.getName());
                    cartModel.setImage(partsModel.getImage());
                    cartModel.setKey(partsModel.getKey());
                    cartModel.setPrice(partsModel.getPrice());
                    cartModel.setQuantity(1);
                    cartModel.setTotalPrice(Float.parseFloat(partsModel.getPrice()));

                    userCart.child(partsModel.getKey())
                            .setValue(cartModel)
                            .addOnSuccessListener(unused -> {
                                iCartLoadListener.onCartLoadFailed("Dodano u košaricu");
                            })
                            .addOnFailureListener(e -> iCartLoadListener.onCartLoadFailed(e.getMessage()));

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                iCartLoadListener.onCartLoadFailed(error.getMessage());
            }
        });

    }


    @Override
    public int getItemCount() {
        return partsModelList.size();
    }

    public static class MyGamesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.imageView)
        public
        ImageView imageView;
        @BindView(R.id.txtName)
        public
        TextView txtName;
        @BindView(R.id.txtPrice)
        public
        TextView txtPrice;
        @BindView(R.id.cartButton)
        public
        ImageView cartButton ;

        public CardView cardView;

        IRecyclerViewClickListener listener;

        public void setListener(IRecyclerViewClickListener listener) {
            this.listener = listener;
        }

        private Unbinder unbinder;
        public MyGamesViewHolder(@NonNull View itemView) {
            super(itemView);
            unbinder = ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);
            cardView = itemView.findViewById(R.id.main_container);

        }


        @Override
        public void onClick(View view) {
            listener.onRecyclerClick(view,getAdapterPosition());
        }
    }
}
