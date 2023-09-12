package com.king.flixa.Adapter;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.king.flixa.Model.SliderModel;
import com.king.flixa.ExpPlayer.PlayerActivity;
import com.king.flixa.R;
import com.king.flixa.webview.WebActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class SliderAdapter extends PagerAdapter {

    private List<SliderModel> sliderModelList;

    public SliderAdapter(List<SliderModel> sliderModelList) {
        this.sliderModelList = sliderModelList;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.banner_slider,container,false);
        ImageView bannerImage = view.findViewById(R.id.bannnerImage);
        TextView bannerTitle = view.findViewById(R.id.bannerTitle);

//        bannerImage.setImageResource(sliderModelList.get(position).getBanner());
        Glide.with(container.getContext()).load(sliderModelList.get(position).getImage()).
                apply(new RequestOptions().placeholder(R.mipmap.ic_launcher))
                .transform(new RoundedCorners(28))
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(bannerImage);
        bannerTitle.setText(sliderModelList.get(position).getTitle());

        container.addView(view,0);

        bannerImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Title = sliderModelList.get(position).getTitle();
                String Description = sliderModelList.get(position).getDescription();
                String Image = sliderModelList.get(position).getImage();
                String streamUrl = sliderModelList.get(position).getLiveUrl();
                String type = sliderModelList.get(position).getType();
                String referer = sliderModelList.get(position).getReferer();
                String origin = sliderModelList.get(position).getOrigin();
                String cookie = sliderModelList.get(position).getCookie();
                String useragent = sliderModelList.get(position).getUseragent();
                String license= sliderModelList.get(position).getLicense();

                if (type.equals("web")){
                    Intent intent;
                    intent = new Intent(view.getContext(), WebActivity.class);
                    intent.putExtra("webUrl", streamUrl);
                    view.getContext().startActivity(intent);
                }
                else if (type.equals("ads")){
                    Intent intent;
                    Uri uri = Uri.parse(streamUrl);
                    intent = new Intent(Intent.ACTION_VIEW,uri);
                    view.getContext().startActivity(intent);
                }
                else{
                    Intent intent = new Intent(view.getContext(), PlayerActivity.class);
                    intent.putExtra("stream-url", streamUrl);
                    intent.putExtra("license",license);
                    intent.putExtra("title",Title);
                    intent.putExtra("user-agent", useragent);
                    intent.putExtra("origin",origin);
                    intent.putExtra("referer", referer);
                    intent.putExtra("cookie",cookie);
                    view.getContext().startActivity(intent);
                }
            }
        });
        return view;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return sliderModelList.size();
    }
}
