package com.yuhung.gotoeat.ProductProcessing;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
import com.yuhung.gotoeat.shopHomePage;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class buyingCart extends AppCompatActivity {

    private LinearLayout linearBC, linearFinish;
    private RecyclerView RVForOrder;
    private TextView tvTotal;
    private Button btnComplete;
    private Toolbar tbBuyingCart;

    private String[] customizedID, productName, productPic, productSize,
                     single1name, single2name, single3name, addOther, detailQuan, detailPrice;
    private String[] tableUnit;
    private String[] sizeChoice, single1ID, single2ID, single3ID, addGroup;
    private String dataBaseName, username, result, currentOrderID;

    private int totalPrice = 0;
    public HashMap<String, String> multiChoiceMap;
    public HashMap<Integer, String> multiChoiceMapForUpdate;
    public HashMap<String, HashMap<String, String>> drinkMultiPlus;
    public HashMap<String, HashMap<Integer, String>> drinkMultiPlusForUpdate;

    private buyingCartRecycleView buyingCartRV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buying_cart);
        BindingID();
        setData();
        checkState();

        ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Log.e("Result", "Got the Result");
                            Intent intent = getIntent();
                            finish();
                            startActivity(intent);
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        }
                    }
                });

        tbBuyingCart.setTitle("我的購物車");
        setSupportActionBar(tbBuyingCart);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        tvTotal.setText("  總金額為 NT$" + totalPrice);
        tvTotal.setTextSize(20);
        btnComplete.setText("確認完成訂單");
        btnComplete.setTextSize(20);

        RVForOrder.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false));
        RVForOrder.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        buyingCartRV = new buyingCartRecycleView(productName,productPic,productSize,
                                                 single1name,single2name,detailPrice);
        RVForOrder.setAdapter(buyingCartRV);

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(buyingCart.this);
        buyingCartRV.setOnItemClickListener(new buyingCartRecycleView.OnItemClickListener(){
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onItemClick(View view, int position) {

                setSingleUnit();

                View v = getLayoutInflater().inflate(R.layout.style_drinkingcart,null);
                alertDialog.setView(v);
                ImageView imgView = v.findViewById(R.id.imgPic);
                TextView tvDName = v.findViewById(R.id.tvLStar);
                TextView tvDSize = v.findViewById(R.id.tvDSize);
                TextView tvDIce = v.findViewById(R.id.tvDIce);
                TextView tvDSugar = v.findViewById(R.id.tvDSugar);
                TextView tvDPlus = v.findViewById(R.id.tvDPlus);
                TextView tvDSPrice = v.findViewById(R.id.tvDSPrice);
                TextView tvDCoffeeBean = v.findViewById(R.id.tvDCoffeeBean);
                Button btnUpdate = v.findViewById(R.id.btnUpdate);
                Button btDelete = v.findViewById(R.id.btnSend);

                btnUpdate.setTextSize(20);
                btDelete.setTextSize(20);

                Glide.with(buyingCart.this).load(productPic[position]).into(imgView);
                tvDName.setText(productName[position]);
                tvDSize.setText(" 尺寸: " + productSize[position] + " " + detailQuan[position] + "份");

                String single1n = !single1name[position].equals("null")? single1name[position] : "";
                String single2n = !single2name[position].equals("null")? single2name[position] : "";
                String single3n = !single3name[position].equals("null")? single3name[position] : "";

                if(!single1n.equals("")){
                    tvDIce.setText(" " +tableUnit[0]+": " + single1n);
                }else{
                    tvDIce.setVisibility(View.GONE);
                }

                if(!single2n.equals("")){
                    tvDSugar.setText(" " +tableUnit[1]+": " + single2n);
                }else{
                    tvDSugar.setVisibility(View.GONE);
                }

                if(!single3n.equals("")){
                    tvDCoffeeBean.setText(" " + tableUnit[2] + ": " + single3n);
                }else{
                    tvDCoffeeBean.setVisibility(View.GONE);
                }

                String strPlus = "";
                HashMap<String, String> Mtemp = drinkMultiPlus.getOrDefault(customizedID[position], null);
                if(Mtemp != null) {
                    Set<String> keysets = Mtemp.keySet();
                    for (String key : keysets) {
                        strPlus += " " + key + " " + Mtemp.get(key) + "份" + "\n";

                    }
                    tvDPlus.setText(" "+ tableUnit[3] + ": \n"+strPlus);
                }

                tvDSPrice.setText(" 價格: $" + detailPrice[position]);

                AlertDialog dialog = alertDialog.create();
                dialog.show();

                btnUpdate.setOnClickListener((v2 -> {
                    dialog.dismiss();

//                    Set<Integer> keysets = drinkMultiPlusForUpdate.get(customizedID[position]).keySet();
//                    for(Integer key : keysets){
//                        Log.e("S Keysets", key + "/" + drinkMultiPlusForUpdate.get(customizedID[position]).get(key));
//                    }

                    //資料回填 將各自選項填回去供修改訂單
                    HashMap<String, String> hashMapForUpdate = new HashMap<>();
                    hashMapForUpdate.put("customizedID", customizedID[position]); //刪除訂單用 刪除後新增
                    hashMapForUpdate.put("5", sizeChoice[position]); //原先產品的容量
                    hashMapForUpdate.put("ProductQuantity", detailQuan[position]); //原先產品的數量
                    hashMapForUpdate.put("preAddOther", addOther[position]);
                    hashMapForUpdate.put("1", single1ID[position]);
                    hashMapForUpdate.put("2", single2ID[position]);
                    hashMapForUpdate.put("3", single3ID[position]);

                    //回傳物件給頁面
                    Intent intent = new Intent(buyingCart.this, productDetail.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("productName", productName[position]); //productName
                    bundle.putSerializable("choiceSet",hashMapForUpdate); //所有先前填好的單選

                    Log.e("customized", customizedID[position]);
                    Log.e("Maps", drinkMultiPlusForUpdate.toString());

                    bundle.putSerializable("multiPlusSet", drinkMultiPlusForUpdate.getOrDefault(customizedID[position],null));
                    //產品多選 買家不一定有多選or產品不可多選

                    intent.putExtras(bundle);
                    someActivityResultLauncher.launch(intent);

                }));

                btDelete.setOnClickListener((v3 -> {
                    AlertDialog.Builder twoDialog = new AlertDialog.Builder(buyingCart.this);
                    AlertDialog dialogs = twoDialog.create();
                    twoDialog.setTitle("確定刪除飲品?");
                    twoDialog.setNegativeButton("取消",(dialog1, which) -> { });
                    twoDialog.setPositiveButton("確定",((dialog1, which) -> {
                        dialogs.dismiss();
                        dialog.dismiss();

                        //刪除所選飲料
                        String[] field = new String[3];
                        field[0] = "database";
                        field[1] = "customized_id";
                        field[2] = "order_id";
                        String[] data = new String[3];
                        data[0] = dataBaseName;
                        data[1] = customizedID[position];
                        data[2] = currentOrderID;
                        PutData putData = new PutData("http://"+getText(R.string.IPv4)+"/"+getText(R.string.SQLConnection)+ "/OrderQuerying/DeleteOrder.php", "POST", field, data);
                        putData.startPut();
                        if(putData.onComplete()){
                            result = putData.getData();
                            Log.i("刪除訂單", result);
                            Intent intent = getIntent();
                            finish();
                            startActivity(intent);
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

                        }//end complete

                    }));
                    twoDialog.show();
                }));

            }
        });

        findViewById(R.id.btnBuyIt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //更改訂單狀態為成功(1)
                String[] field = new String[2];
                field[0] = "database";
                field[1] = "guest_account";
                String[] data = new String[2];
                data[0] = dataBaseName;
                data[1] = username;
                PutData putData = new PutData("http://"+getText(R.string.IPv4)+"/"+getText(R.string.SQLConnection)+ "/OrderQuerying/UpdateOrderStatus.php", "POST", field, data);
                putData.startPut();
                if(putData.onComplete()){
                    result = putData.getData();
                    Log.i("完成訂單", result);
                    Toast.makeText(buyingCart.this, "購買完成！將轉跳至訂單頁面。", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(buyingCart.this, shopHomePage.class);
                    intent.putExtra("selectedPage", 3);
                    finish();
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }//end complete

            }
        });

//        HashMap<String, String> temp = drinkMultiPlus.get("110");
//        Set<Map.Entry<String, String>> entriess = temp.entrySet();
//        for (Map.Entry<String, String> entry: entriess) {
//             Log.i("Tags", "drinkMultiPlus:" + entry.getKey() + "/" + entry.getValue());
//        }

//        for(int i=0;i<customizedID.length;i++){
//            Log.e("customized ID ",customizedID[i]+"");
//        }
//
//        Set<String> keysets = drinkMultiPlus.keySet();
//        for(String key : keysets){
//            Log.e("M Keysets", key);
//
//        }

    }//end of onCreate

    private void BindingID(){
        linearBC = findViewById(R.id.linearBC);
        linearFinish = findViewById(R.id.linearFinish);
        RVForOrder = findViewById(R.id.RVOrder);
        tvTotal = findViewById(R.id.tvTPrice);
        btnComplete = findViewById(R.id.btnBuyIt);
        tbBuyingCart = findViewById(R.id.tbBuyingCart);
    }

    private void setData(){

        username = getSharedPreferences("userData", MODE_PRIVATE)
                .getString("userAccount",null);

        drinkMultiPlus = new HashMap<>();
        drinkMultiPlusForUpdate = new HashMap<>();

        if(username != null) {

            dataBaseName = getSharedPreferences("userData", MODE_PRIVATE)
                    .getString("selectedShop",null);

            //取得總訂單的所有飲料
            String[] field = new String[2];
            field[0] = "database";
            field[1] = "guest_account";
            String[] data = new String[2];
            data[0] = dataBaseName;
            data[1] = username;
            PutData putData = new PutData("http://"+getText(R.string.IPv4)+"/"+getText(R.string.SQLConnection)+ "/OrderQuerying/GetOrderCar.php", "POST", field, data);
            putData.startPut();
            if(putData.onComplete()){
                result = putData.getData();
                Log.i("總訂單的結果", result);
                try {
                    JSONArray ja = new JSONArray(result);
                    JSONObject jo;

                    customizedID = new String[ja.length()];
                    productName = new String[ja.length()];
                    productPic = new String[ja.length()];
                    productSize = new String[ja.length()];
                    single1name = new String[ja.length()];
                    single2name = new String[ja.length()];
                    single3name = new String[ja.length()];
                    addOther = new String[ja.length()];
                    detailQuan = new String[ja.length()];
                    detailPrice = new String[ja.length()];

                    sizeChoice = new String[ja.length()];
                    single1ID = new String[ja.length()];
                    single2ID = new String[ja.length()];
                    single3ID = new String[ja.length()];
                    addGroup = new String[ja.length()];

                    for (int i = 0; i < ja.length(); i++) {
                        jo = ja.getJSONObject(i);
                        currentOrderID = jo.getString("order_id");
                        customizedID[i] = jo.getString("customized_id");
                        productName[i] = jo.getString("product_name");
                        productPic[i] = "http://"+getText(R.string.IPv4)+"/images/" + dataBaseName + "/" + jo.getString("product_pic");
                        productSize[i] = jo.getString("product_size");
                        single1name[i] = jo.getString("single1name");
                        single2name[i] = jo.getString("single2name");
                        single3name[i] = jo.getString("single3name");
                        addOther[i] = jo.getString("add_other");
                        detailQuan[i] = jo.getString("detail_quantity");
                        detailPrice[i] = jo.getString("detail_price");

                        sizeChoice[i] = jo.getString("productsize_id");
                        single1ID[i] = jo.getString("sp1_number");
                        single2ID[i] = jo.getString("sp2_number");
                        single3ID[i] = jo.getString("sp3_number");
                        addGroup[i] = jo.getString("addgroup");

                        int temp = Integer.parseInt(detailPrice[i]);
                        totalPrice += temp;
                    }

                } catch (Exception ex) {
                    Log.e("總訂單飲料", result);
                    Log.e("總訂單飲料", ex.getMessage());
                }
            }//end complete

            if(!result.equals("No product selected.")){

                //每杯飲料個別的配料(複數)
                for(int i=0; i<customizedID.length;i++) {
                    field = new String[2];
                    field[0] = "database";
                    field[1] = "customized_id";
                    data = new String[2];
                    data[0] = dataBaseName;
                    data[1] = customizedID[i];
                    putData = new PutData("http://"+getText(R.string.IPv4)+"/"+getText(R.string.SQLConnection)+ "/OrderQuerying/GetProductMultiplus.php", "POST", field, data);
                    putData.startPut();
                    if (putData.onComplete()) {
                        result = putData.getData();
                        try {
                            JSONArray ja = new JSONArray(result);
                            JSONObject jo;

                            multiChoiceMap = new HashMap<>();
                            multiChoiceMapForUpdate = new HashMap<>(); //用於資料回填
                            String[] multiplusName = new String[ja.length()];
                            int[] multiplusNumber = new int[ja.length()];
                            String[] multiplusQuan = new String[ja.length()];

                            for (int s = 0; s < ja.length(); s++) {
                                jo = ja.getJSONObject(s);
                                multiplusName[s] = jo.getString("multi_name");
                                multiplusNumber[s] = jo.getInt("multi_number")-1; //資料庫近來為1開始 index為0開始
                                multiplusQuan[s] = jo.getString("plus_quantity");
                                multiChoiceMap.put(multiplusName[s], multiplusQuan[s]);
                                multiChoiceMapForUpdate.put(multiplusNumber[s], multiplusQuan[s]);

                            }

                            drinkMultiPlus.put(customizedID[i], multiChoiceMap);
                            drinkMultiPlusForUpdate.put(customizedID[i], multiChoiceMapForUpdate); //資料回填

                        } catch (Exception ex) {
                            Log.e("飲料個別多選", customizedID[i] + "/" + ex.getMessage());
                        }
                    }
                }//end of for
            }//end of if

        }else{
            Toast.makeText(this, "目前沒有登入！", Toast.LENGTH_SHORT).show();
        }//end of else

    }//end of setData

    private void setSingleUnit(){

        String[] field = new String[1];
        field[0] = "database";
        String[] data = new String[1];
        data[0] = dataBaseName;
        PutData putData = new PutData("http://"+getText(R.string.IPv4)+"/"+getText(R.string.SQLConnection)+ "/OrderQuerying/GetTableUnit.php", "POST", field, data);
        putData.startPut();
        if (putData.onComplete()) {
            result = putData.getData();
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

    private void setOrderUpdate(int position){

        String[] field = new String[2];
        field[0] = "database";
        field[1] = "customized_id";
        String[] data = new String[2];
        data[0] = dataBaseName;
        data[1] = customizedID[position];
        PutData putData = new PutData("http://"+getText(R.string.IPv4)+"/"+getText(R.string.SQLConnection)+ "/OrderQuerying/UpdateProductInfo.php", "POST", field, data);
        putData.startPut();
        if (putData.onComplete()) {
            result = putData.getData();
            try {
                JSONArray ja = new JSONArray(result);
                JSONObject jo = ja.getJSONObject(0);

//                private String single1ID, single2ID, single3ID, addOtherBack, addGroup;
//                private String[] mpNumber, plusQuantity;
//                single1ID = jo.getString("Unit");
//                single2ID = jo.getString("Unit");
//                single3ID = jo.getString("Unit");
//                addGroup = jo.getString("Unit");

                tableUnit = new String[ja.length()];
                for (int s = 0; s < ja.length(); s++) {
                    jo = ja.getJSONObject(s);
                    tableUnit[s] = jo.getString("Unit");

                }

            } catch (Exception ex) {
                Log.e("配料單位", ex.getMessage());
            }
        }//end of onComplete
    }

    private void checkState(){

        LinearLayout.LayoutParams linearlayout_parent_params = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        linearlayout_parent_params.setMargins(0, 10, 0, 10);
        TextView tvInfo = new TextView(this);

        if(username == null) {

            linearFinish.setVisibility(View.INVISIBLE);
            RVForOrder.setVisibility(View.GONE);
            tvInfo.setText("\n\n\n\n尚未登入會員！");
            tvInfo.setTextSize(30);
            tvInfo.setGravity(Gravity.CENTER);
            linearBC.addView(tvInfo,linearlayout_parent_params);

        }else if(result.equals("No product selected.")){

            linearFinish.setVisibility(View.INVISIBLE);
            RVForOrder.setVisibility(View.GONE);
            tvInfo.setText("\n\n\n\n目前沒有訂購任何飲料，快去選購吧！");
            tvInfo.setTextSize(20);
            tvInfo.setGravity(Gravity.CENTER);
            linearBC.addView(tvInfo,linearlayout_parent_params);

        }
    }//end of checkState

    @Override
    public boolean onSupportNavigateUp() {
        finish(); // close this activity as oppose to navigating up
        return false;
    }

}//end of class

class buyingCartRecycleView extends RecyclerView.Adapter<buyingCartRecycleView.ViewHolder>{

    private String[] productName, productPic, productSize,
                     single1Name, single2Name, detailPrice;

    public buyingCartRecycleView(String[] productName,
                                 String[] productPic,
                                 String[] productSize,
                                 String[] single1Name,
                                 String[] single2Name,
                                 String[] detailPrice){

        this.productName = productName;
        this.productPic = productPic;
        this.productSize = productSize;
        this.single1Name = single1Name;
        this.single2Name = single2Name;
        this.detailPrice = detailPrice;
        this.detailPrice = detailPrice;

    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView imageView;
        private TextView tvCount,tvName,tvSChoice, tvDPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            tvCount = itemView.findViewById(R.id.tvCount);
            tvName = itemView.findViewById(R.id.tvDrinkName);
            tvSChoice = itemView.findViewById(R.id.tvDrinkSingle);
            tvDPrice = itemView.findViewById(R.id.tvDPrice);

        }

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewChild = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.style_totalorder,parent,false);

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
        Glide.with(holder.itemView.getContext()).load(productPic[position]).into(holder.imageView);
        holder.tvCount.setText(String.valueOf(position+1));
        holder.tvName.setText(productName[position]);

        String single1n = !single1Name[position].equals("null")? single1Name[position] : "";
        String single2n = !single2Name[position].equals("null")? single2Name[position] : "";

        holder.tvSChoice.setText(productSize[position]+ " " + single1n + single2n);
        holder.tvDPrice.setText( "$" + detailPrice[position]);

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