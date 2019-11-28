package com.foodgeene.home.hometwo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.foodgeene.R;
import com.foodgeene.home.brandlist.brandmodel.Bannerlist;
import com.foodgeene.home.brandlist.brandmodel.Brandlist;

import java.util.List;

public class FlipperAdapter extends BaseAdapter {

    List<Bannerlist> list;
    Context context;


    public FlipperAdapter(Context context, List<Bannerlist> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Bannerlist hero = list.get(i);
        LayoutInflater inflater = LayoutInflater.from(context);
         view = inflater.inflate(R.layout.brand_row, null);
        ImageView imageView =  view.findViewById(R.id.mBrandImage);

        Glide.with(context).load(hero.getImage()).into(imageView);
        return view;
    }
}
