package com.app.bespokino.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app.bespokino.R;
import com.app.bespokino.model.FabricModel;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by bespokino on 10/27/2017 AD.
 */

public class ThumbAdapter extends RecyclerView.Adapter<ThumbAdapter.MyViewHolder> {



    List<FabricModel> fabricModelList;
    Context context;

    public ThumbAdapter(List<FabricModel> fabricModelList, Context context) {
        this.fabricModelList = fabricModelList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View photoView = inflater.inflate(R.layout.fabric, parent, false);
        ThumbAdapter.MyViewHolder viewHolder = new ThumbAdapter.MyViewHolder(photoView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {


        FabricModel fabricModel = fabricModelList.get(position);

        Glide.with(context)
                .load(fabricModel.getFabric_img())
                .centerCrop()
                .fitCenter()
                .into(holder.mPhotoImageView);


    }

    @Override
    public int getItemCount() {
        return fabricModelList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView mPhotoImageView;

        public MyViewHolder(View itemView) {

            super(itemView);
            mPhotoImageView = (ImageView) itemView.findViewById(R.id.iv_photo);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            int position = getAdapterPosition();
            if(position != RecyclerView.NO_POSITION) {
               // SpacePhoto spacePhoto = mSpacePhotos[position];
                  /*  Intent intent = new Intent(mContext, SpacePhotoActivity.class);
                    intent.putExtra(SpacePhotoActivity.EXTRA_SPACE_PHOTO, spacePhoto);
                    startActivity(intent);*/
            }
        }
    }
}
