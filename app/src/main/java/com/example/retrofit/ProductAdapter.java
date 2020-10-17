package com.example.retrofit;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private ArrayList<Product> products;
    private OnItemClickListener mListener;

    public interface  OnItemClickListener{
        void onDeleteClick(int position);
        void onUpdateClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }
    ProductAdapter(ArrayList<Product> list){
        products = list;
    }
    static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView descriptionTextView;
        public TextView categoryTextView;
        public TextView priceTextView;
        public TextView nameTextView;
        public TextView idTextView;
        public Button updateButton;
        public Button deleteButton;

        ViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            categoryTextView = itemView.findViewById(R.id.categoryTextView);
            priceTextView = itemView.findViewById(R.id.productPriceTextView);
            nameTextView = itemView.findViewById(R.id.productNameTextView);
            idTextView = itemView.findViewById(R.id.idProductTextView);
            updateButton = itemView.findViewById(R.id.buttonUpdate);
            deleteButton = itemView.findViewById(R.id.buttonDelete);

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onDeleteClick(position);
                        }
                    }
                }
            });
            updateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onUpdateClick(position);
                        }
                    }
                }
            });
        }
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_product, parent ,false);
        return new ProductAdapter.ViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itemView.setTag(products.get(position));

        holder.descriptionTextView.setText(products.get(position).getDescription());
        holder.categoryTextView.setText(products.get(position).getCategory());
        holder.priceTextView.setText(""+products.get(position).getPrice());
        holder.nameTextView.setText(products.get(position).getName());
        holder.idTextView.setText(products.get(position).getId());
    }

    @Override
    public int getItemCount() {
        return products.size();
    }


}
