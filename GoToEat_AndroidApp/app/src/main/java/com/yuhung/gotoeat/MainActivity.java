package com.yuhung.gotoeat;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yuhung.gotoeat.UsedComponent.RunThread;

import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private String[] shopChinese, shopDataBase, shopPhoto;
    private boolean resultFlag = false;
    private SharedPreferences sharedPref;

    private LinearLayout linearShop;
    private Toolbar tbGoToEat;
    private RecyclerView shopRV;
    private shopSelectionAdapter shopSelectionAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setData();
        bindingID();

        tbGoToEat.setTitle("購toEat");
        tbGoToEat.setSubtitle("Let 's go to eat !");

        if(resultFlag) {
            shopRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            shopRV.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
            shopSelectionAdapter = new shopSelectionAdapter(shopChinese, shopPhoto);
            shopRV.setAdapter(shopSelectionAdapter);

            shopSelectionAdapter.setOnItemClickListener(new shopSelectionAdapter.OnItemClickListener(){
                @Override
                public void onItemClick(View view, int position) {

                    sharedPref = getSharedPreferences("userData",MODE_PRIVATE);
                    sharedPref.edit()
                            .putString("selectedShop", shopDataBase[position])
                            .putString("dataBasePic", shopPhoto[position])
                            .commit();

                    Intent intent = new Intent(getApplicationContext(), shopHomePage.class);
                    intent.putExtra("shopChinese", shopChinese[position]);
                    startActivity(intent);
                    finish();

                }
            });

        }else{

            LinearLayout.LayoutParams linearlayout_parent_params = new LinearLayout.LayoutParams
                    (LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            linearlayout_parent_params.setMargins(0, 10, 0, 10);
            TextView tvInfo = new TextView(this);

            shopRV.setVisibility(View.GONE);
            tvInfo.setText("\n\n\n\n尚未有店家登錄。");
            tvInfo.setTextSize(25);
            tvInfo.setGravity(Gravity.CENTER);
            linearShop.addView(tvInfo,linearlayout_parent_params);

        }


    }//end of onCreate

    public void bindingID(){
        linearShop = findViewById(R.id.linearShop);
        tbGoToEat = findViewById(R.id.tbGoToEat);
        shopRV = findViewById(R.id.shopRV);
    }

    public void setData(){

        try {
            RunThread threadDB = new RunThread();
            threadDB.setApiUrl("http://" + getText(R.string.IPv4) + "/"+getText(R.string.SQLConnection) +"/DataBaseSelection/RequireDatabase.php");
            threadDB.start();
            threadDB.join();

            String result = threadDB.getResult();
            Log.i("tags", "Get the result: " + result);

            JSONArray ja = new JSONArray(result);
            JSONObject jo = null;

            shopChinese = new String[ja.length()];
            shopDataBase = new String[ja.length()];
            shopPhoto = new String[ja.length()];

            for (int i = 0; i < ja.length(); i++) {
                jo = ja.getJSONObject(i);
                shopChinese[i] = jo.getString("shop_chinese");
                shopDataBase[i] = jo.getString("shop_name");
                shopPhoto[i] = "http://"+getText(R.string.IPv4)+"/images/ShopPhoto/"+ jo.getString("photo");

            }

            resultFlag = true;

        } catch (Exception ex) {
            Log.e("選取資料庫", ex.getMessage());

        }

    }//end of setData


}//end of class

class shopSelectionAdapter extends RecyclerView.Adapter<shopSelectionAdapter.ViewHolder>{

    private String[] shopChinese, shopImage;

    public shopSelectionAdapter(String[] shopChinese, String[] shopImage){
        this.shopChinese = shopChinese;
        this.shopImage = shopImage;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView imageView;
        private TextView tvShopName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imgShop);
            tvShopName = itemView.findViewById(R.id.tvShopName);

        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewChild = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.style_shopdisplay,parent,false);

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
        Glide.with(holder.itemView.getContext()).load(shopImage[position]).into(holder.imageView);
        holder.tvShopName.setText(shopChinese[position]);

    }

    @Override
    public int getItemCount() {
        return shopChinese.length;
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