package com.s23010467.easy_market;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class itemAdapter extends FirebaseRecyclerAdapter<item_data_model,itemAdapter.myViewHolder> {
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public itemAdapter(@NonNull FirebaseRecyclerOptions<item_data_model> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull item_data_model model) {
        holder.item_name.setText(model.getItem_name());
        holder.per_unit.setText(model.getPer_unit());
        holder.discount.setText(model.getDiscount());
        holder.lowest_price.setText(model.getLowest_price());
        holder.highest_price.setText(model.getHighest_price());
        holder.wholesale_price.setText(model.getWholesale_price());
        holder.retail_price.setText(model.getRetail_price());
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.market_prof_item_card,parent,false);
        return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder{
        TextView item_name;
        TextView per_unit;
        TextView discount;
        TextView lowest_price;
        TextView highest_price;
        TextView wholesale_price;
        TextView retail_price;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            item_name = (TextView)itemView.findViewById(R.id.itemName);
            per_unit =(TextView) itemView.findViewById(R.id.perQuentity);
            discount = (TextView) itemView.findViewById(R.id.itemDiscount);
            lowest_price =(TextView) itemView.findViewById(R.id.itemLowPrice);
            highest_price = (TextView) itemView.findViewById(R.id.itemHighPrice);
            wholesale_price = (TextView) itemView.findViewById(R.id.itemHsalePrice);
            retail_price =(TextView) itemView.findViewById(R.id.itemRetailPrice);
        }
    }
}
