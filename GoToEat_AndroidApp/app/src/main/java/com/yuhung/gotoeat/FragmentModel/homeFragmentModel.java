package com.yuhung.gotoeat.FragmentModel;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.youth.banner.Banner;
import com.youth.banner.config.BannerConfig;
import com.youth.banner.config.IndicatorConfig;
import com.youth.banner.indicator.CircleIndicator;
import com.youth.banner.indicator.RoundLinesIndicator;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.util.BannerUtils;
import com.yuhung.gotoeat.BannerComponent.DataBean;
import com.yuhung.gotoeat.BannerComponent.ImageTitleAdapter;
import com.yuhung.gotoeat.MainActivity;
import com.yuhung.gotoeat.ProductProcessing.buyingCart;
import com.yuhung.gotoeat.ProductProcessing.productDetail;
import com.yuhung.gotoeat.R;
import com.yuhung.gotoeat.UsedComponent.PutData;

import org.json.JSONArray;
import org.json.JSONObject;

public class homeFragmentModel extends Fragment {

    private LinearLayout linearHome;
    private RelativeLayout bannerLayout;
    private View view;
    private TextView tvDrinkRank;
    private Banner banner;
    private RoundLinesIndicator indicator;
    private Toolbar tbHome;
    private RecyclerView homeRV;

    private String[] rankProductName, rankProductPic,rankProductInfo;
    public static String[] adTitle, adPhoto, adURL;
    public int[] rankReward = { R.drawable.topone, R.drawable.toptwo, R.drawable.topthree, R.drawable.topfour, R.drawable.topfive};
    private String dataBaseName, adResult, result;
    private homeRecycleView homeRVAdapter;
    private SharedPreferences sharedPref;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_homepage, container, false);
        setHasOptionsMenu(true);
        bindingID();

        try {
            setData();
            setBanner();
            checkState();

            Intent intent = getActivity().getIntent();
            String strShopChinese = intent.getStringExtra("shopChinese");

            AppCompatActivity activity = (AppCompatActivity) getActivity();
            tbHome.setTitle(strShopChinese);
            activity.setSupportActionBar(tbHome);

            homeRV.setNestedScrollingEnabled(false);
            homeRV.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL, false));
            homeRV.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

            homeRVAdapter = new homeRecycleView(rankProductName, rankProductPic, rankProductInfo, rankReward);
            homeRV.setAdapter(homeRVAdapter);

            homeRVAdapter.setOnItemClickListener(new homeRecycleView.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Intent intent = new Intent(getActivity(), productDetail.class);
                    intent.putExtra("productName", rankProductName[position]);
                    startActivity(intent);
                }
            });


//            if(rankProductName.length != 0) {
//                homeRVAdapter = new homeRecycleView(rankProductName, rankProductPic, rankProductInfo, rankReward);
//                homeRV.setAdapter(homeRVAdapter);
//
//                homeRVAdapter.setOnItemClickListener(new homeRecycleView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(View view, int position) {
//                        Intent intent = new Intent(getActivity(), productDetail.class);
//                        intent.putExtra("productName", rankProductName[position]);
//                        startActivity(intent);
//                    }
//                });
//
//            }


        }catch(Exception ex){

            Log.e("homePage", ex.getMessage());
            Toast.makeText(getActivity(), "伺服器維修中，請稍後再嘗試。", Toast.LENGTH_SHORT).show();

            Handler handler = new Handler();
            handler.postDelayed(runTimerStop, 500);

        }

        return view;
    }// on create

    public void bindingID(){
        bannerLayout = view.findViewById(R.id.bannerLayout);
        banner = view.findViewById(R.id.banner);
        indicator = (RoundLinesIndicator) view.findViewById(R.id.indicator);
        tbHome = view.findViewById(R.id.tbHome);
        homeRV = view.findViewById(R.id.homeRV);
        linearHome = view.findViewById(R.id.linearhome);
        tvDrinkRank = view.findViewById(R.id.tvDrinkRank);
    }

    public void setData(){

        dataBaseName = getActivity().getSharedPreferences("userData", getActivity().MODE_PRIVATE)
                .getString("selectedShop",null);

        String[] field, data;
        PutData putData;

        field = new String[1];
        field[0] = "database";
        data = new String[1];
        data[0] = dataBaseName;
        putData = new PutData("http://"+getText(R.string.IPv4)+"/"+getText(R.string.SQLConnection)+"/RequireBase/RequireSellingRank.php", "POST", field, data);
        putData.startPut();
        if(putData.onComplete()){
            result = putData.getData();
            try {
                JSONArray ja = new JSONArray(result);
                JSONObject jo;

                rankProductName = new String[ja.length()];
                rankProductPic = new String[ja.length()];
                rankProductInfo = new String[ja.length()];

                for (int i = 0; i < ja.length(); i++) {
                    jo = ja.getJSONObject(i);
                    rankProductName[i] = jo.getString("product_name");
                    rankProductPic[i] = "http://"+getText(R.string.IPv4)+"/images/" + dataBaseName + "/" + jo.getString("product_pic");
                    rankProductInfo[i] = jo.getString("product_introduction");
                }
            } catch (Exception ex) {
                Log.e("商品排行榜",result);
                Log.e("商品排行榜", ex.getMessage());

            }
        }

    }//end of set Data

    public void setADData(){

        String[] field, data;
        PutData putData;

        field = new String[1];
        field[0] = "database";
        data = new String[1];
        data[0] = dataBaseName;
        putData = new PutData("http://"+ getText(R.string.IPv4)+"/"+ getText(R.string.SQLConnection)+"/RequireBase/RequireAdvertise.php", "POST", field, data);
        putData.startPut();
        if(putData.onComplete()){
            adResult = putData.getData();
            try {
                JSONArray ja = new JSONArray(adResult);
                JSONObject jo;

                adTitle = new String[ja.length()];
                adPhoto = new String[ja.length()];
                adURL = new String[ja.length()];

                for (int i = 0; i < ja.length(); i++) {
                    jo = ja.getJSONObject(i);
                    adTitle[i] = jo.getString("title");
                    adPhoto[i] = "http://"+ getText(R.string.IPv4) + "/images/" + dataBaseName + "/AdvertisePhoto/"  + jo.getString("photo");
                    adURL[i] = jo.getString("URL");
                }
            } catch (Exception ex) {
                Log.e("廣告讀取",adResult);
                Log.e("廣告讀取", ex.getMessage());
            }
        }

    }

    public void setBanner(){

        setADData();

        if(!adResult.equals("Error: No advertise to show.")) {
            banner.setAdapter(new ImageTitleAdapter(DataBean.getADData(adTitle, adPhoto)));
            banner.setIndicator(new CircleIndicator(getContext()));
            banner.setIndicatorGravity(IndicatorConfig.Direction.RIGHT);
            banner.setIndicatorMargins(new IndicatorConfig.Margins(0, 0,
                    BannerConfig.INDICATOR_MARGIN, (int) BannerUtils.dp2px(12)));
            banner.setOnBannerListener(new OnBannerListener() {
                @Override
                public void OnBannerClick(Object data, int position) {
                    if(!adURL[position].equals("null")){
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(adURL[position]));
                        startActivity(browserIntent);
                        Log.i("ad", adURL[position]);
                    }
                }//end of onClick
            });
        }else{
            bannerLayout.setVisibility(View.GONE);
        }

    }//end of setBanner

    private void checkState(){

        LinearLayout.LayoutParams linearlayout_parent_params = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        linearlayout_parent_params.setMargins(0, 10, 0, 10);
        TextView tvInfo = new TextView(getActivity());

        if(result.equals("Error: No history selling rank.")){

            tvDrinkRank.setVisibility(View.INVISIBLE);
            homeRV.setVisibility(View.GONE);
            tvInfo.setText("\n\n\n\n歡迎光臨，盡情採購!");
            tvInfo.setTextSize(20);
            tvInfo.setGravity(Gravity.CENTER);
            linearHome.addView(tvInfo,linearlayout_parent_params);

        }
    }//end of checkState

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.actionbar_home, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.backToMain:

                Intent intent = new Intent();
                intent.setClass(getContext(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
                getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private Runnable runTimerStop = new Runnable() {
        @Override
        public void run()
        {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            getActivity().finish();
            getActivity().overridePendingTransition(0, android.R.anim.fade_out);
            startActivity(intent);

        }
    };

}//end of class

class homeRecycleView extends RecyclerView.Adapter<homeRecycleView.ViewHolder>{

    private String[] productName, productPic, productInfo;
    private int[] rankReward;

    public homeRecycleView(String[] productName, String[] productPic, String[] productInfo, int[] rankReward){
        this.productName = productName;
        this.productPic = productPic;
        this.productInfo = productInfo;
        this.rankReward = rankReward;

    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView imgDrink, imgRank;
        private TextView tvDrinkName,tvDrinkInfo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgDrink = itemView.findViewById(R.id.imgSRdrink);
            imgRank = itemView.findViewById(R.id.imgRank);
            tvDrinkName = itemView.findViewById(R.id.tvRDrink);
            tvDrinkInfo = itemView.findViewById(R.id.tvRDinfo);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewChild = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.style_sellingrank,parent,false);

        viewChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(v, (int) v.getTag());
                }
            }
        });

        return new ViewHolder(viewChild);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.itemView.setTag(position);

        Glide.with(holder.itemView.getContext()).load(productPic[position]).into(holder.imgDrink);

        try {
            holder.imgRank.setImageResource(rankReward[position]);
        }catch (Exception e){

        }
        holder.tvDrinkName.setText(productName[position]);
        holder.tvDrinkInfo.setText(productInfo[position]);

    }

    @Override
    public int getItemCount() {
        return productName.length;
    }

    private OnItemClickListener mOnItemClickListener;

    public static interface OnItemClickListener {
        //接口方法里面的参数，可以传递任意你想回调的数据，不止View 和 Item 的位置position
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

}