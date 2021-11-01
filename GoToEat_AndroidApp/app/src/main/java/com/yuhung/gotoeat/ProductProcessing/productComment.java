package com.yuhung.gotoeat.ProductProcessing;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.yuhung.gotoeat.R;
import com.yuhung.gotoeat.UsedComponent.PutData;

import org.json.JSONArray;
import org.json.JSONObject;

public class productComment extends AppCompatActivity {

    private LinearLayout LinearComment;
    private Toolbar tbDrinckComment;
    private TextView tvProductName;
    private RecyclerView recyclerView;

    public String productName, dataBaseName;
    public String[] detailID, guestAccount, commentInfo, commentScore, tableUnit;;
    public commentRecycleView commentRV;

    private String productSize, single1Name, single2Name, singlePlus3Name, detailPrice, detailQuantity;
    private String[] plusName, plusQuantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink_comment);
        BindingID();

        Intent intent = getIntent();
        productName = intent.getStringExtra("productName");

        setData();

        tbDrinckComment.setTitle("商品評價");
        setSupportActionBar(tbDrinckComment);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        tvProductName.setText(productName);

        if(commentInfo != null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
            commentRV = new commentRecycleView(detailID, guestAccount, commentInfo, commentScore);
            recyclerView.setAdapter(commentRV);
        }else{
            recyclerView.setVisibility(View.GONE);
            TextView tvInfo = new TextView(this);
            tvInfo.setText("\n目前尚無評論！");
            tvInfo.setTextSize(25);
            tvInfo.setGravity(Gravity.CENTER);
            LinearComment.addView(tvInfo);
        }



    }

    public void BindingID(){
        LinearComment = findViewById(R.id.LinearComment);
        tbDrinckComment = findViewById(R.id.tbDComment);
        recyclerView = findViewById(R.id.RVComment);
        tvProductName = findViewById(R.id.tvDCName);
    }

    public void setData(){

        dataBaseName = getSharedPreferences("userData", MODE_PRIVATE)
                .getString("selectedShop",null);

        String[] field, data;
        String result;
        PutData putData;

        field = new String[2];
        field[0] = "database";
        field[1] = "product_Name";
        data = new String[2];
        data[0] = dataBaseName;
        data[1] = productName;
        putData = new PutData("http://"+getText(R.string.IPv4)+ "/" + getText(R.string.SQLConnection)+ "/RequireBase/GetProductRecommend.php", "POST", field, data);
        putData.startPut();

        if(putData.onComplete()) {
            result = putData.getData();
            Log.i("Comment",result);

            try {
                JSONArray ja = new JSONArray(result);
                JSONObject jo;
                detailID = new String[ja.length()];
                guestAccount = new String[ja.length()];
                commentInfo = new String[ja.length()];
                commentScore = new String[ja.length()];

                for (int i = 0; i < ja.length(); i++) {
                    jo = ja.getJSONObject(i);
                    detailID[i] = jo.getString("detail_id");
                    guestAccount[i] = jo.getString("guest_account");
                    commentInfo[i] = jo.getString("comment");
                    commentScore[i] = jo.getString("comment_score");
                    Log.e("All info",detailID[i] + "/" + guestAccount[i] + "/" + commentInfo[i] +"/" + commentScore[i]);
                }

            } catch (Exception ex) {
                Log.e("飲料推薦評論", ex.getMessage());
                commentInfo = null;
            }

        }
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

    @Override
    public boolean onSupportNavigateUp() {
        finish(); // close this activity as oppose to navigating up
        return false;
    }

    class commentRecycleView extends RecyclerView.Adapter<commentRecycleView.ViewHolder>{

        private String[] detailID, guestAccount, commentInfo, commentScore;

        public commentRecycleView(String[] detailID, String[] guestAccount, String[] commentInfo, String[] commentScore){
            this.detailID = detailID;
            this.guestAccount = guestAccount;
            this.commentInfo = commentInfo;
            this.commentScore = commentScore;
        }

        class ViewHolder extends RecyclerView.ViewHolder{

            private TextView tvAccount, tvComment, tvStar;
            private RatingBar ratingBarSDC;
            private Button btnCInfo;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                tvAccount = itemView.findViewById(R.id.tvCAccount);
                tvComment = itemView.findViewById(R.id.tvCComment);
                tvStar = itemView.findViewById(R.id.tvStar);
                btnCInfo = itemView.findViewById(R.id.btnCInfo);
                ratingBarSDC = itemView.findViewById(R.id.ratingBarSDC);

            }
        }

        @Override
        public int getItemCount() {
            return commentInfo.length;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.style_showdrinkcomment,parent,false);

            return new ViewHolder(view);
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            holder.tvAccount.setText(guestAccount[position]);
            holder.tvComment.setText(commentInfo[position]);

            holder.tvStar.setText("評價");
            Float tempRate = Float.parseFloat(commentScore[position])/2;
            holder.ratingBarSDC.setRating(tempRate);

            holder.btnCInfo.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setInfoData(position);
                    setSingleUnit();

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(productComment.this);
                    View v = getLayoutInflater().inflate(R.layout.style_plusinfo,null);
                    alertDialog.setView(v);
                    TextView tvInfo = v.findViewById(R.id.tvLStar);
                    Button btnExit = v.findViewById(R.id.btnSend);

                    String strSize = "份量: " +productSize + "\n";
                    String strSingle1 = single1Name != "null"?  tableUnit[0] + ": "+ single1Name + "\n":"";
                    String strSingle2 = single2Name != "null"?  tableUnit[1]+ ": " + single2Name + "\n" :"";
                    String strSingle3 = singlePlus3Name != "null"?  tableUnit[2]+ ": " + singlePlus3Name + "\n":"";

                    String mPlus = "";
                    if(!plusName[0].equals("null")) {
                        mPlus = tableUnit[3]+ ": \n";
                        for (int i = 0; i < plusName.length; i++) {
                            mPlus += plusName[i] + " " + plusQuantity[i] + "份 \n";
                        }
                    }

                    int singlePrice = Integer.parseInt(detailPrice) / Integer.parseInt(detailQuantity);
                    String strSinglePrice = "\n價錢: $" + singlePrice;

                    String strInfo = "\n" + strSize + strSingle1 + strSingle2 + strSingle3 + mPlus + strSinglePrice;

                    tvInfo.setText(strInfo);
                    AlertDialog dialog = alertDialog.create();
                    dialog.show();
                    btnExit.setOnClickListener((v1 -> {
                        dialog.dismiss();
                    }));

                }
            });

        }
    }//end of adapter

    public void setInfoData(int position){

        String[] field, data;
        String result;
        PutData putData;

        field = new String[2];
        field[0] = "database";
        field[1] = "detail_id";
        data = new String[2];
        data[0] = dataBaseName;
        data[1] = detailID[position];
        putData = new PutData("http://"+getText(R.string.IPv4)+ "/" + getText(R.string.SQLConnection)+ "/RequireBase/GetProductRecommendInfo.php", "POST", field, data);
        putData.startPut();

        if(putData.onComplete()) {
            result = putData.getData();
            Log.i("Comment Info",result);

            try {
                JSONArray ja = new JSONArray(result);
                JSONObject jo = ja.getJSONObject(0);

                productSize = jo.getString("product_size");
                single1Name = jo.getString("single1_name");
                single2Name = jo.getString("single2_name");
                singlePlus3Name = jo.getString("singleplus3_name");
                detailPrice = jo.getString("detail_price");
                detailQuantity = jo.getString("detail_quantity");

                plusName = new String[ja.length()];
                plusQuantity = new String[ja.length()];
                for (int i = 0; i < ja.length(); i++) {
                    jo = ja.getJSONObject(i);
                    plusName[i] = jo.getString("name");
                    plusQuantity[i] = jo.getString("plus_quantity");
                }

            } catch (Exception ex) {
                Log.e("飲料推薦的配料選項", ex.getMessage());
            }

        }
    }
}