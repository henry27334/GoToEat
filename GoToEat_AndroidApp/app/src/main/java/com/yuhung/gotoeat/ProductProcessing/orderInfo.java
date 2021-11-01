package com.yuhung.gotoeat.ProductProcessing;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.yuhung.gotoeat.R;
import com.yuhung.gotoeat.UsedComponent.PutData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class orderInfo extends AppCompatActivity {

    private Toolbar tbOrderInfo;
    private RecyclerView recycleViewOrder;
    private orderInfoAdapter orderInfoAdapter;

    private String dataBaseName, username, totalOrderID;
    private String[] detailID, tableUnit;;
    private ArrayList<String> arrSingleDrink;
    private HashMap<String,ArrayList<String>> singleDrinkMap;
    private HashMap<String, HashMap<String, String>> multiChoiceMap;
    public String strStar, strRecommend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_info);

        Intent intent = getIntent();
        totalOrderID = intent.getStringExtra("totalOrderID");

        BindingID();
        setData();
        setSingleUnit();

        tbOrderInfo.setTitle("訂單詳情");
        setSupportActionBar(tbOrderInfo);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        recycleViewOrder.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false));
        recycleViewOrder.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        orderInfoAdapter = new orderInfoAdapter();
        recycleViewOrder.setAdapter(orderInfoAdapter);

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish(); // close this activity as oppose to navigating up
        return false;
    }

    private void BindingID(){
        tbOrderInfo = findViewById(R.id.tbOrderInfo);
        recycleViewOrder = findViewById(R.id.RVForOInfo);
    }

    private void setData(){

        username = getSharedPreferences("userData", MODE_PRIVATE)
                .getString("userAccount",null);

        dataBaseName = getSharedPreferences("userData", MODE_PRIVATE)
                .getString("selectedShop",null);

        String[] field, data;
        String result;
        PutData putData;

        field = new String[2];
        field[0] = "database";
        field[1] = "order_id";
        data = new String[2];
        data[0] = dataBaseName;
        data[1] = totalOrderID;
        putData = new PutData("http://"+getText(R.string.IPv4)+ "/" + getText(R.string.SQLConnection)+ "/OrderDisplay/GetSingleDetailID.php", "POST", field, data);
        putData.startPut();
        if(putData.onComplete()){
            result = putData.getData();
            Log.i("總訂單的個別飲料編號", result);
            try {
                JSONArray ja = new JSONArray(result);
                JSONObject jo;
                detailID = new String[ja.length()];
                for (int i = 0; i < ja.length(); i++) {
                    jo = ja.getJSONObject(i);
                    detailID[i] = jo.getString("detail_id");
                }
            } catch (Exception ex) {
                Log.e("個別訂單編號", ex.getMessage());
            }

            singleDrinkMap = new HashMap<>();
            multiChoiceMap = new HashMap<>();

            for(int i=0;i<detailID.length;i++){
                field = new String[3];
                field[0] = "database";
                field[1] = "guest_account";
                field[2] = "detail_id";
                data = new String[3];
                data[0] = dataBaseName;
                data[1] = username;
                data[2] = detailID[i];
                putData = new PutData("http://"+getText(R.string.IPv4)+ "/" + getText(R.string.SQLConnection)+ "/OrderDisplay/GetDrinkInfoForOrder.php", "POST", field, data);
                putData.startPut();
                if(putData.onComplete()) {
                    result = putData.getData();
                    Log.i("個別飲料搜尋", result);
                    try {
                        JSONArray ja = new JSONArray(result);
                        JSONObject jo = ja.getJSONObject(0);

                        arrSingleDrink = new ArrayList<>();
                        String drinkTitle = jo.getString("product_name");
                        String drinkPic = "http://"+getText(R.string.IPv4)+"/images/" + dataBaseName + "/" + jo.getString("product_pic");
                        String drinkQuan = jo.getString("detail_quantity");
                        String drinkSize = jo.getString("product_size");
                        String drinkIce = jo.getString("single1_name");
                        String drinkSugar = jo.getString("single2_name");
                        String drinkCoffeebean = jo.getString("singleplus3_name");
                        String drinkPrice = jo.getString("detail_price");
                        String drinkAddOther = jo.getString("add_other");
                        String comment =  jo.getString("comment");
                        String Star =  jo.getString("comment_score");
                        String recommend = jo.getString("recommend");

                        arrSingleDrink.add(drinkTitle);
                        arrSingleDrink.add(drinkPic);
                        arrSingleDrink.add(drinkQuan);
                        arrSingleDrink.add(drinkSize);
                        arrSingleDrink.add(drinkIce);
                        arrSingleDrink.add(drinkSugar);
                        arrSingleDrink.add(drinkCoffeebean);
                        arrSingleDrink.add(drinkPrice);
                        arrSingleDrink.add(drinkAddOther);
                        arrSingleDrink.add(comment);
                        arrSingleDrink.add(Star);
                        arrSingleDrink.add(recommend);
                        singleDrinkMap.put(detailID[i], arrSingleDrink);

                        HashMap<String, String> temp = new HashMap<>();
                        for (int s = 0; s < ja.length(); s++) {
                            jo = ja.getJSONObject(s);
                            String plusName = jo.getString("name");
                            String plusQuan = jo.getString("plus_quantity");
                            temp.put(plusName,plusQuan);
                            multiChoiceMap.put(detailID[i], temp);
                        }

                    } catch (Exception ex) {
                        Log.e("個別飲料資料", ex.getMessage());
                    }

                }
            }


        }//end complete

    }

    private void setSingleUnit(){

        String[] field = new String[1];
        field[0] = "database";
        String[] data = new String[1];
        data[0] = dataBaseName;
        PutData putData = new PutData("http://"+getText(R.string.IPv4)+"/"+getText(R.string.SQLConnection)+ "/OrderQuerying/GetTableUnit.php", "POST", field, data);
        putData.startPut();
        if (putData.onComplete()) {
            String result = putData.getData();
            try {
                JSONArray ja = new JSONArray(result);
                JSONObject jo;

                tableUnit = new String[ja.length()];
                for (int s = 0; s < ja.length(); s++) {
                    jo = ja.getJSONObject(s);
                    tableUnit[s] = jo.getString("Unit");

                }

            } catch (Exception ex) {
                Log.e("配料單位", ex.getMessage());
            }
        }//end of onComplete

    }//end of setSingleUnit

    private class orderInfoAdapter extends RecyclerView.Adapter<orderInfoAdapter.ViewHolder>{

        class ViewHolder extends RecyclerView.ViewHolder{

            private ImageView imageView;
            private TextView tvTitle, tvQuan,tvIce, tvSugar, tvCoffeebeans, tvPlus, tvSize,tvPrice;
            private TextView tvStar, tvComment;
            private RatingBar ratingBarShow;
            private Button btnComment;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                imageView = itemView.findViewById(R.id.imgODPic);
                tvSize = itemView.findViewById(R.id.tvODSize);
                tvTitle = itemView.findViewById(R.id.tvODrink);
                tvQuan = itemView.findViewById(R.id.tvODQuan);
                tvIce = itemView.findViewById(R.id.tvOIce);
                tvSugar = itemView.findViewById(R.id.tvOSugar);
                tvCoffeebeans = itemView.findViewById(R.id.tvOCoffeanbeans);
                tvPlus = itemView.findViewById(R.id.tvOPlus);
                tvStar = itemView.findViewById(R.id.tvOStar);
                tvComment = itemView.findViewById(R.id.tvOComment);
                tvPrice = itemView.findViewById(R.id.tvODPrice);
                ratingBarShow = itemView.findViewById(R.id.ratingBarSDC);
                btnComment =  itemView.findViewById(R.id.btnComment);

                this.setIsRecyclable(false);
            }
        }

        @Override
        public int getItemCount() {
            return detailID.length;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.style_orderinfo,parent,false);

            return new ViewHolder(view);
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            ArrayList<String> ChoicTemp = singleDrinkMap.get(detailID[position]);

            String strPlus = "\n";
            HashMap<String, String> Mtemp = multiChoiceMap.get(detailID[position]);
            Set<String> keysets = multiChoiceMap.get(detailID[position]).keySet();
            for (String key : keysets) {
                if(!key.equals("null")) {
                    strPlus += "  " +key + " " + Mtemp.get(key) + "份" + "\n";
                }else{
                    holder.tvPlus.setVisibility(View.GONE);
                    break;
                }
            }

            holder.tvPlus.setText(R.string.OrderPlus);
            holder.tvPlus.setClickable(true);
            String finalStrPlus = strPlus;
            holder.tvPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(orderInfo.this);
                    View v = getLayoutInflater().inflate(R.layout.style_plusinfo,null);
                    alertDialog.setView(v);
                    TextView tvInfo = v.findViewById(R.id.tvLStar);
                    Button btnExit = v.findViewById(R.id.btnSend);
                    tvInfo.setText(finalStrPlus);
                    AlertDialog dialog = alertDialog.create();
                    dialog.show();
                    btnExit.setOnClickListener((v1 -> {
                        dialog.dismiss();
                    }));
                }
            });

            Glide.with(holder.itemView.getContext()).load(ChoicTemp.get(1)).into(holder.imageView);
            holder.tvTitle.setText(ChoicTemp.get(0));
            holder.tvSize.setText("容量: " + ChoicTemp.get(3));
            holder.tvQuan.setText("數量: " + ChoicTemp.get(2));

            if(!ChoicTemp.get(4).equals("null")){
                holder.tvIce.setText(tableUnit[0]+": " + ChoicTemp.get(4));
            }else if(ChoicTemp.get(5).equals("null")){
                holder.tvIce.setVisibility(View.GONE);
            }else{
                holder.tvIce.setText(tableUnit[0]+ ": 熱飲");
            }

            if(!ChoicTemp.get(5).equals("null")){
                holder.tvSugar.setText(tableUnit[1]+": " + ChoicTemp.get(5));
            }else{
                holder.tvSugar.setVisibility(View.GONE);
            }

            if(!ChoicTemp.get(6).equals("null")){
                holder.tvCoffeebeans.setText(tableUnit[2]+": "+ ChoicTemp.get(6));
            }else{
                holder.tvCoffeebeans.setVisibility(View.GONE);
            }

            holder.tvPrice.setText("價格: NT$" + ChoicTemp.get(7));

            if(!ChoicTemp.get(9).equals("null")){
                holder.tvComment.setText("評論:\n" + ChoicTemp.get(9));
            }
            else{
                holder.ratingBarShow.setVisibility(View.GONE);
                holder.tvComment.setText("尚未給予評價！");
            }

            if(!ChoicTemp.get(11).equals("null")){
                holder.tvStar.setText("評價: ");
                Float tempRank = Float.parseFloat(ChoicTemp.get(10))/2;
                holder.ratingBarShow.setRating(tempRank);
                holder.btnComment.setVisibility(View.INVISIBLE);
            }else{
                holder.ratingBarShow.setVisibility(View.INVISIBLE);
            }

            holder.btnComment.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(orderInfo.this);
                    View s = getLayoutInflater().inflate(R.layout.style_drinkcomment,null);
                    alertDialog.setView(s);
//                    EditText edtStar = s.findViewById(R.id.edtOrginal);
                    RatingBar ratingBar = s.findViewById(R.id.ratingBarSDC);
                    EditText edtComment = s.findViewById(R.id.edtComment);
                    CheckBox cbxRecommend = s.findViewById(R.id.cbxRecommend);
                    Button btnSend = s.findViewById(R.id.btnSend);
                    Button btnCancel = s.findViewById(R.id.btnExit);
                    AlertDialog dialog = alertDialog.create();
                    dialog.show();

                    cbxRecommend.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View view) {
                            if(((CompoundButton) view).isChecked()){
                                strRecommend = "1";
                            } else {
                                strRecommend = "2";
                            }
                        }
                    });

                    ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                        @Override
                        public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                            strStar = String.valueOf(rating*2);
                        }
                    });

                    btnSend.setOnClickListener((v1 -> {
                        String commentStr = edtComment.getText().toString();

                        if(!commentStr.equals("")){
                            String IDStr = detailID[position];
                            String[] field = new String[6];
                            field[0] = "database";
                            field[1] = "detail_id";
                            field[2] = "guest_account";
                            field[3] = "comment";
                            field[4] = "comment_score";
                            field[5] = "recommend";
                            String[] data = new String[6];
                            data[0] = dataBaseName;
                            data[1] = IDStr;
                            data[2] = username;
                            data[3] = commentStr;
                            data[4] = strStar;
                            data[5] = strRecommend;
                            PutData putData = new PutData("http://"+getText(R.string.IPv4)+ "/" + getText(R.string.SQLConnection)+ "/OrderDisplay/InsertComment.php", "POST", field, data);
                            putData.startPut();
                            if(putData.onComplete()) {
                                String result = putData.getData();
                                Log.i("輸入評價", result);
                            }

                            dialog.dismiss();
                            Intent intent = getIntent();
                            finish();
                            startActivity(intent);

                        }else{
                            Toast.makeText(getBaseContext(),"請輸入評分或評價！",Toast.LENGTH_SHORT).show();
                        }

                    }));
                    btnCancel.setOnClickListener((v1 -> {
                        dialog.dismiss();
                    }));
                }
            });



        }
    }//end of adapter
}//end of class