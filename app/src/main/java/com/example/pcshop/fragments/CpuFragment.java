package com.example.pcshop.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pcshop.R;
import com.example.pcshop.ShowDetailsOfItem;
import com.example.pcshop.listener.IRecyclerViewClickListener;
import com.example.pcshop.model.PartsModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class CpuFragment extends Fragment {

    private RecyclerView recyclerView;

    private DatabaseReference gameList;

public CpuFragment(){

}

    @Override
    public void onStart() {
        super.onStart();
        List<PartsModel> partsModels = new ArrayList<>();
        FirebaseRecyclerOptions<PartsModel> options = new FirebaseRecyclerOptions.Builder<PartsModel>().setQuery(gameList, PartsModel.class).build();
        FirebaseRecyclerAdapter<PartsModel,MyGamesViewHolder> adapter = new FirebaseRecyclerAdapter<PartsModel, MyGamesViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyGamesViewHolder holder, @SuppressLint("RecyclerView") int position, @NonNull PartsModel model) {

                DatabaseReference getTypeRef = getRef(position);

                getTypeRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            PartsModel partsModel = snapshot.getValue(PartsModel.class);
                            partsModel.setKey(snapshot.getKey());
                            partsModels.add(partsModel);

                            Glide.with(getContext())
                                    .load(partsModels.get(position).getImage())
                                    .into(holder.imageView);
                            holder.imageView.setTag(partsModels.get(position).getImage().toString());
                            holder.txtPrice.setText(new StringBuilder("$").append(partsModels.get(position).getPrice()));
                            holder.txtName.setText(new StringBuilder().append(partsModels.get(position).getName()));

                            holder.cardView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(getContext(), ShowDetailsOfItem.class);
                                    intent.putExtra("key", partsModels.get(position).getKey());
                                    Log.d("KEY", partsModels.get(position).getKey());
                                    getContext().startActivity(intent);
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }


            @NonNull
            @Override
            public MyGamesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_parts_item,parent,false);
                MyGamesViewHolder holder = new MyGamesViewHolder(view);
                return holder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cpu, container, false);
        gameList = FirebaseDatabase.getInstance().getReference("Parts").child("CPU");
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_games);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        return view;
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