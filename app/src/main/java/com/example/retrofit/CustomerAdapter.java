package com.example.retrofit;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.ViewHolder> {
    private ArrayList<Customer> customers;

    CustomerAdapter(ArrayList<Customer> list){
        customers = list;
    }
     static class ViewHolder extends RecyclerView.ViewHolder{
        TextView textViewName;
        TextView textViewEmail;
        TextView textViewId;
        ViewHolder(@NonNull View itemView){
            super(itemView);

            textViewName = itemView.findViewById(R.id.customerNameTextView);
            textViewEmail = itemView.findViewById(R.id.customerEmailTextView);
            textViewId = itemView.findViewById(R.id.idCustomerTextView);
        }
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout, parent ,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itemView.setTag(customers.get(position));

        holder.textViewName.setText(customers.get(position).getName());
        holder.textViewEmail.setText(customers.get(position).getEmail());
        holder.textViewId.setText(customers.get(position).getAuto_increment_id());
    }

    @Override
    public int getItemCount() {
        return customers.size();
    }
}
