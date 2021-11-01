package com.yuhung.gotoeat.BannerComponent;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.youth.banner.adapter.BannerAdapter;
import com.yuhung.gotoeat.R;

import java.util.List;

public class ImageTitleAdapter extends BannerAdapter<DataBean, ImageTitleHolder> {

    public ImageTitleAdapter(List<DataBean> mDatas) {
        super(mDatas);
    }

    @Override
    public ImageTitleHolder onCreateHolder(ViewGroup parent, int viewType) {
        return new ImageTitleHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.banner_image_title, parent, false));
    }

    @Override
    public void onBindView(ImageTitleHolder holder, DataBean data, int position, int size) {

//        holder.imageView.setImageResource(data.imageRes);
        Glide.with(holder.itemView.getContext()).load(data.imageUrl).into(holder.imageView);
        holder.title.setText(data.title);
    }

}
