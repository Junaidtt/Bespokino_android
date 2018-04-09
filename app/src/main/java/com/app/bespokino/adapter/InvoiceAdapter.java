package com.app.bespokino.adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.bespokino.R;
import com.app.bespokino.model.Invoice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by bespokino on 9/29/2017 AD.
 */

public class InvoiceAdapter extends RecyclerView.Adapter<InvoiceAdapter.MyViewHolder> {


    Context context;
    List<Invoice>invoiceList;

    public InvoiceAdapter(Context context, List<Invoice> invoiceList) {
        this.context = context;
        this.invoiceList = invoiceList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_invoice_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        int count = position + 1;
        Invoice invoice = invoiceList.get(position);

        holder.customeShirtPrice.setText("$ "+String.format("%.2f",invoice.getCustomeMadeShirt()));
        holder.fabricAddUpPrice.setText("$ "+String.format("%.2f",invoice.getFabricUpgrade()));
        holder.stylingAddup.setText("$ "+String.format("%.2f",invoice.getStylingAddup()));
        holder.customeShirtLabel.setText(count+" Custome Made Shirt");
    }

    @Override
    public int getItemCount() {

        return invoiceList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView customeShirtPrice,customeShirtLabel;
        TextView fabricAddUpPrice;
        TextView stylingAddup;

        public MyViewHolder(View itemView) {
            super(itemView);

            customeShirtPrice = itemView.findViewById(R.id.tvCustomeShirtPrice);
            fabricAddUpPrice = itemView.findViewById(R.id.tvFabricUpgrade);
            stylingAddup = itemView.findViewById(R.id.tvAddup);
            customeShirtLabel = itemView.findViewById(R.id.customeShirtLabel);




        }
    }



}
