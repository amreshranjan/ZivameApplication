package com.example.amreshranjan.zivame.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.amreshranjan.zivame.R;


/**
 * Created by amreshranjan on 08/04/18.
 */

public class ViewPagerAdapter extends PagerAdapter {

    private Context mContext;
    private int[] imageIds;

    public ViewPagerAdapter(Context context, int[] imageIds){
        this.mContext = context;
        this.imageIds = imageIds;
    }

    @Override
    public int getCount() {
        return imageIds.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == (LinearLayout)object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        View itemView = LayoutInflater.from(mContext).inflate(R.layout.layout_pager_view, container, false);
        ImageView imageView = (ImageView) itemView.findViewById(R.id.image_view_pager);

        imageView.setImageResource(imageIds[position]);
        ((ViewPager) container).addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}
