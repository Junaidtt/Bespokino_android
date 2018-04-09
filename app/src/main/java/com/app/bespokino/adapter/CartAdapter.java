package com.app.bespokino.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.bespokino.R;
import com.app.bespokino.activity.CartActivity;
import com.app.bespokino.activity.DetailActivity;
import com.app.bespokino.helper.AppConfig;
import com.app.bespokino.model.CartProduct;
import com.app.bespokino.model.ItemModel;
import com.app.bespokino.model.Product;
import com.app.bespokino.model.Shirt;
import com.app.bespokino.task.DeleteCartItemTask;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bespokino on 8/2/2017 AD.
 */

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {

    Context mContext;
    List<CartProduct>productList = new ArrayList<>();
    List<Product>pList= new ArrayList<>();
    List<ItemModel>selectedItems=new ArrayList<>();
    DeleteCartItemTask deleteCartItemTask;



    public CartAdapter(Context mContext, List<CartProduct> productList) {
        this.mContext = mContext;
        this.productList = productList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_rv_item,parent,false);
        MyViewHolder viewHolder=new MyViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final int count = position+1;
        int c;
        final CartProduct product = productList.get(position);

        int cid = product.getCustomerID();
        int ordrno = product.getOrderNo();
        int papNo = product.getPaperNo();
        String trackid = product.getTrackingID();


        holder.nameText.setText(product.getShirtCount());
        holder.priceText.setText(String.valueOf("Price: $"+String.format("%.2f",product.getShirtPrice())));
        Glide.with(mContext).load("http://www.bespokino.com/images/fabric/"+product.getFabricImg()).into(holder.fabricImage);



        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                final  AlertDialog.Builder  builder = new AlertDialog.Builder(mContext);
                builder.setCancelable(true);
                builder.setTitle("Alert");
                builder.setMessage("Are you sure..?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // new FetchFabricImageTask().execute();

                        new DeleteCartItemTask().execute(String.valueOf(product.getCustomerID()),String.valueOf(product.getOrderNo()),String.valueOf(product.getPaperNo()),product.getTrackingID());
                        productList.remove(position);
                        notifyDataSetChanged();
                        notifyItemRemoved(position);
                        AppConfig.mCartItemCount = AppConfig.mCartItemCount - 1;
                        CartActivity.setupBadge();



                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        dialogInterface.dismiss();
                    }
                });
                builder.show();





            }
        });

                holder.detailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(mContext,DetailActivity.class);

                intent.putExtra("customerID",product.getCustomerID());
                intent.putExtra("orderNo",product.getOrderNo());
                intent.putExtra("paperNo",product.getPaperNo());
                intent.putExtra("trackingID",product.getTrackingID());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);


            }
        });




    }

    @Override
    public int getItemCount() {

        return productList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView fabricImage;

        TextView priceText,nameText;

        Button detailButton;
        ImageView deleteButton;

        public MyViewHolder(View itemView) {
            super(itemView);

            nameText = itemView.findViewById(R.id.tvName);
            fabricImage = itemView.findViewById(R.id.imageView);
            priceText = itemView.findViewById(R.id.tvPrice);
            deleteButton = itemView.findViewById(R.id.btDelete);
            detailButton = itemView.findViewById(R.id.btDetails);

            mContext = itemView.getContext();


        }
    }



}
