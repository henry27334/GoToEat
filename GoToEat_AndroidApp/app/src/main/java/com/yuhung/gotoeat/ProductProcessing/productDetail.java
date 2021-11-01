package com.yuhung.gotoeat.ProductProcessing;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.yuhung.gotoeat.MemberOperation.memberLogin;
import com.yuhung.gotoeat.R;
import com.yuhung.gotoeat.UsedComponent.PutData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.security.KeyStore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

//店家置換: 需修改setData/switch的分組(個別配置add_table adapter/countingTotalPrice)
//注意: Size和plus index不同 0/1開始
public class productDetail extends AppCompatActivity {

    private Toolbar tbDD;
    private LinearLayout linearRc;
    private ImageView imgPic;
    private ImageButton btnRecommend;
    private TextView tvName, tvInfo, tvSize, tvTotalPrice, tvAddition;
    private TextView[] tvAddTitle;
    private EditText editText;

    private Button btnAddToBuy;
    public ElegantNumberButton elegantNumber;

    public String dataBaseName;
    public String productName, productID, productInfo, productAdd, productPic, productSort, totalOrderID, customizedID,productSizeChoice;
    public String[] addGroup, addChinese, productSizeID, strNumber;

    public HashMap<String, String[]> addCategoryNameArrList;
    public HashMap<String, int[]> addCategoryPriceList;
    public HashMap<String, String> singleBuyingChoice, multiBuyingForStore;
    public HashMap<Integer, String> multiBuyingChoice;

    public String productQuantity = "1", preProductSize = "1";
    private RecyclerView sizeRecycle;
    private RecyclerView[] recyclerView;

    private HashMap<String, String> choiceSet = new HashMap<>();
    public HashMap<Integer, String> multiPlusSet; //資料回填用 Null的話代表新訂單
    public int totalPrice = 0;

    private String[] tableName = new String[]{"singleplus_1","singleplus_2","singleplus_3","multipleplus"};
    private Boolean[] categoryFlags = new Boolean[]{true,true,true,false};
    public String sizeIndex = String.valueOf(tableName.length+1);

    addChoiceListAdapter sizeListAdapter;
    addChoiceListAdapter[] addChoiceListAdapter;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.style_productdetail);

        Bundle bundle = this.getIntent().getExtras();

        if(bundle != null){

            productName = bundle.getString("productName");
            choiceSet = (HashMap<String, String>)bundle.getSerializable("choiceSet") == null?
                    choiceSet = new HashMap():(HashMap<String, String>)bundle.getSerializable("choiceSet");
            multiPlusSet = (HashMap<Integer, String>)bundle.getSerializable("multiPlusSet") == null ?
                    multiPlusSet = new HashMap<>():(HashMap<Integer, String>)bundle.getSerializable("multiPlusSet") ;

        }

        BindingID();
        setData();
        dynamic_Add();
        setButtonOnClick();

        setSupportActionBar(tbDD);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        getSupportActionBar().setTitle("商品詳情");

        //For SingleMap key
        strNumber = new String[addGroup.length+1];
        for(int i=0; i< addGroup.length;i++){
            strNumber[i] = addGroup[i];
        }
        strNumber[addGroup.length] = String.valueOf(tableName.length+1);;

        tvName.setText(productName);
        tvInfo.setText(productInfo);
        Glide.with(productDetail.this).load(productPic).into(imgPic);

        multiBuyingForStore = new HashMap<>(); //使用index紀錄每筆配料 用於填寫訂單
        multiBuyingChoice = new HashMap<>();

        //判斷是否為新訂單 是的話創新 不是就使用舊數據
        if(!choiceSet.isEmpty()){
            singleBuyingChoice = choiceSet;
            multiBuyingChoice = multiPlusSet; //直接覆蓋設定的multi 可直接套用於RV 不須額外傳遞變數
            singleBuyingChoice.put("productID", productID);
            editText.setText(singleBuyingChoice.getOrDefault("preAddOther",""));
            elegantNumber.setNumber(singleBuyingChoice.get("ProductQuantity"));
            productQuantity = singleBuyingChoice.get("ProductQuantity");

            for(int i=0; i<productSizeID.length; i++){
                if(productSizeID[i].equals(choiceSet.get("5"))){
                    choiceSet.put("5", String.valueOf(i+1));
                    preProductSize = String.valueOf(i+1);
                    break;
                }
            }

        }else{

            singleBuyingChoice = new HashMap<>();
            singleBuyingChoice.put("productID", productID);
            singleBuyingChoice.put("ProductQuantity", "1");
            singleBuyingChoice.put("AddOther", "null");

            for(int i = 0; i< addGroup.length; i++) {
                if(!addGroup[i].equals("4")) { //跳過4 不將複選加入單選
                    singleBuyingChoice.put(String.valueOf(addGroup[i]), "1");
                }
            }
            singleBuyingChoice.put(String.valueOf(tableName.length+1), "1"); //for size

        }//else

        //SizeRecycleView
        tvSize.setText("容量");
        sizeRecycle.setLayoutManager(new RecyclerViewNoBugLinearLayoutManager(this));
        sizeRecycle.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        sizeListAdapter= new addChoiceListAdapter(addCategoryNameArrList, addCategoryPriceList,
                sizeIndex,true, addGroup.length, preProductSize);
        sizeRecycle.setAdapter(sizeListAdapter);

        String tempGroup;
        for(int i=0;i< addGroup.length;i++){
            try {
                int categoryIndex = Integer.parseInt(addGroup[i])-1;

                try {
                    tempGroup = addGroup[i];
                }catch(Exception e){
                    tempGroup = "None";
                }

                tvAddTitle[i].setText(addChinese[i]);
                recyclerView[i].setLayoutManager(new RecyclerViewNoBugLinearLayoutManager(this));
                recyclerView[i].addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
                addChoiceListAdapter[i] = new addChoiceListAdapter(addCategoryNameArrList,addCategoryPriceList, addGroup[i],
                        categoryFlags[categoryIndex], i,  choiceSet.getOrDefault(tempGroup,null));
                recyclerView[i].setAdapter(addChoiceListAdapter[i]);

//                Log.i("Group",tempGroup);
//                Log.i("PreChoice",choiceSet.getOrDefault(tempGroup,null) +"");

            }catch(Exception e){
                Log.e("RecycleView", "No recycleview to show");
                break;
            }
        }
        countingTotalPrice();

    }//end of oncreate

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbar_order, tbDD.getMenu());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.orderCart:
                Intent intent = new Intent(this, buyingCart.class);
                startActivity(intent);
                break;
            default:
                break;
        }
        return true;
    }

    private void BindingID(){
        tbDD = findViewById(R.id.tbDrinkDetail);
        linearRc = findViewById(R.id.linearRc);
        imgPic = findViewById(R.id.imgPic);
        btnRecommend = findViewById(R.id.imageButton);
        tvName = findViewById(R.id.tvLStar);
        tvInfo = findViewById(R.id.tvDSize);
        tvSize = findViewById(R.id.tvSize);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        tvAddition = findViewById(R.id.tvAddition);
        editText = findViewById(R.id.edtComment);
        btnAddToBuy = findViewById(R.id.btnAddToBuy);
        sizeRecycle = findViewById(R.id.sizeRecycle);
        elegantNumber = findViewById(R.id.elegantNum);

    }

    private void setData(){

        dataBaseName = getSharedPreferences("userData", MODE_PRIVATE)
                .getString("selectedShop",null);

        addCategoryNameArrList = new HashMap<>();
        addCategoryPriceList = new HashMap<>();
        String AddTable = "", AddChinese = "";
        String[] productSize = null;
        int[] productPrice = null;

        //取得指定飲料的基本資料
        String[] field = new String[2];
        field[0] = "database";
        field[1] = "product_name";
        String[] data = new String[2];
        data[0] = dataBaseName;
        data[1] = productName;
        PutData putData = new PutData("http://"+getText(R.string.IPv4)+ "/" + getText(R.string.SQLConnection)+ "/RequireBase/RequireProductInfo.php", "POST", field, data);
        putData.startPut();
        if(putData.onComplete()){

            String result = putData.getData();
            Log.i("tags","Result data: " + result);
            try {
                JSONArray ja = new JSONArray(result);
                JSONObject jo = ja.getJSONObject(0);
                productID = jo.getString("product_id");
                productSort = jo.getString("product_sort");
                productPic = "http://"+getText(R.string.IPv4)+"/images/" + dataBaseName + "/" + jo.getString("product_pic");
                productAdd = jo.getString("product_add");
                productInfo = jo.getString("product_introduction");
                AddChinese = jo.getString("plus_id_cn");
                AddTable = jo.getString("plus_id");

                productSize = new String[ja.length()];
                productPrice = new int[ja.length()];
                productSizeID = new String[ja.length()];
                for (int i = 0; i < ja.length(); i++) {
                    jo = ja.getJSONObject(i);
                    productSizeID[i] = jo.getString("productsize_id");
                    productSize[i] = jo.getString("product_size");
                    productPrice[i] = jo.getInt("product_price");
                }

            } catch (Exception ex) {
                Log.e("基礎飲料設定及表單", ex.getMessage());
            }
        }

        addChinese = AddChinese.split(",");
        addGroup = AddTable.split(",");
        String result = "";

        try{
            for(int i = 0; i< addGroup.length; i++){
                //取得配料各分類表
                field = new String[2];
                field[0] = "database";
                field[1] = "add_table";
                data = new String[2];
                data[0] = dataBaseName;
                data[1] = tableName[Integer.parseInt(addGroup[i])-1];
                putData = new PutData("http://"+getText(R.string.IPv4)+"/"+ getText(R.string.SQLConnection)+ "/RequireBase/RequireAddTable.php", "POST", field, data);
                putData.startPut();
                if(putData.onComplete()){

                    result = putData.getData();
                    try {
                        JSONArray ja = new JSONArray(result);
                        JSONObject jo = null;

                        String[] nameArray = new String[ja.length()];
                        int[] priceArray = new int[ja.length()];

                        for (int s = 0; s < ja.length(); s++) {
                            jo = ja.getJSONObject(s);
                            nameArray[s] = jo.getString("name");
                            priceArray[s] = jo.getInt("price") ;
                        }

                        addCategoryNameArrList.put(addGroup[i], nameArray);
                        addCategoryPriceList.put(addGroup[i], priceArray);

                    } catch (Exception ex) {
                        Log.e("基本分類表", ex.getMessage());
                    }
                }
            }

        }catch(Exception ex){
            Log.e("讀取配料表", result);
        }

        addCategoryNameArrList.put(sizeIndex, productSize);
        addCategoryPriceList.put(sizeIndex, productPrice);

    }//end of setData

    private void dynamic_Add(){

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN); //將視窗往上提取
        tvAddition.setText("備註 (非必填)");
        tvAddition.setTextSize(25);
        tvTotalPrice.setText(" 目前價格: NT$0");
        btnAddToBuy.setText("加入購物車");
        btnAddToBuy.setTextSize(15);
        btnAddToBuy.setTextColor(getResources().getColor(R.color.black));

        tvAddTitle = new TextView[addGroup.length];
        recyclerView = new RecyclerView[addGroup.length];
        addChoiceListAdapter = new addChoiceListAdapter[addGroup.length];

        LinearLayout.LayoutParams linearlayout_parent_params = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        linearlayout_parent_params.setMargins(0, 10, 0, 10);

        for(int i=0; i<addGroup.length;i++){
            tvAddTitle[i] = new TextView(this);
            recyclerView[i]  = new RecyclerView(this);
            tvAddTitle[i].setTextSize(25);
            linearRc.addView(tvAddTitle[i],linearlayout_parent_params);
            linearRc.addView(recyclerView[i],linearlayout_parent_params);
        }

    }//end of add

    public void setButtonOnClick(){

        elegantNumber.setOnClickListener(new ElegantNumberButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                productQuantity = elegantNumber.getNumber();
                countingTotalPrice();
                singleBuyingChoice.put("ProductQuantity", productQuantity);
            }
        });

        btnAddToBuy.setOnClickListener(new Button.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {

                String username = getSharedPreferences("userData", MODE_PRIVATE)
                        .getString("userAccount","");

                if(username == ""){
                    Toast.makeText(getBaseContext(),"請先登入會員！",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), memberLogin.class);
                    startActivity(intent);
                    finish();

                }else{
                    int sizeIDIndex = Integer.parseInt(singleBuyingChoice.get(sizeIndex))-1; //productSizeID[temp] 抓出飲料容量的ID
                    String productSize = productSizeID[sizeIDIndex];
                    singleBuyingChoice.put("AddOther", editText.getText().toString());

                    //刪除配料多選為0份的配料 hashmap不可直接判斷刪除(size會出錯) 故使用Iterator
                    Iterator it = multiBuyingChoice.entrySet().iterator();
                    while (it.hasNext()){
                        Map.Entry item = (Map.Entry)it.next();
                        if(item.getValue().equals("0")){ //倘若加入的配料數量為0 (買家取消了)
                            it.remove();
                        }
                    }

                    String[] field;
                    String[] data;
                    PutData putData;

                    if(!choiceSet.isEmpty()) {
                        field = new String[2];
                        field[0] = "database";
                        field[1] = "customized_id";
                        data = new String[2];
                        data[0] = dataBaseName;
                        data[1] = choiceSet.get("customizedID");
                        putData = new PutData("http://"+getText(R.string.IPv4)+"/"+getText(R.string.SQLConnection)+"/OrderQuerying/DeleteOrder.php", "POST", field, data);
                        putData.startPut();
                        if (putData.onComplete()) {
                            String result = putData.getData();
                            Log.i("刪除訂單", result);
                        }
                    }

                    field = new String[2];
                    field[0] = "database";
                    field[1] = "guest_account";
                    data = new String[2];
                    data[0] = dataBaseName;
                    data[1] = username;
                    putData = new PutData("http://"+ getText(R.string.IPv4) + "/" + getText(R.string.SQLConnection) + "/OrderQuerying/OrderCarCheck.php", "POST", field, data);
                    putData.startPut();
                    if(putData.onComplete()){
                        totalOrderID = putData.getData();
                        Log.i("totalOrderID",totalOrderID);
                        try {

                            field = new String[6];
                            field[0] = "database";
                            field[1] = "sp1_number";
                            field[2] = "sp2_number";
                            field[3] = "sp3_number";
                            field[4] = "sp_price";
                            field[5] = "add_other";
                            data = new String[6];
                            data[0] = dataBaseName;
                            data[1] = singleBuyingChoice.getOrDefault("1","NULL"); //singleplus_1
                            data[2] = singleBuyingChoice.getOrDefault("2","NULL"); //sugar
                            data[3] = singleBuyingChoice.getOrDefault("3","NULL"); //A case coffeeplus
                            int sp3_plus = addCategoryPriceList.getOrDefault("3",null) != null?
                                    addCategoryPriceList.get("3")[Integer.parseInt(singleBuyingChoice.get("3"))-1] : 0; //-1是因為資料表數字由1開始
                            data[4] = String.valueOf(sp3_plus);
                            data[5] = singleBuyingChoice.get("AddOther");

                            putData = new PutData("http://"+getText(R.string.IPv4)+"/"+getText(R.string.SQLConnection)+"/OrderQuerying/InsertCustomized.php", "POST", field, data);
                            putData.startPut();
                            if(putData.onComplete()){
                                customizedID = putData.getData();
                                Log.i("CustomizedID", customizedID);
                                //plus 訂單新增

                                if(!multiBuyingChoice.isEmpty()){
                                    field = new String[5];
                                    field[0] = "database";
                                    field[1] = "customized_id";
                                    field[2] = "mp_number";
                                    field[3] = "plus_quantity";
                                    field[4] = "customizedplus_price";
                                    data = new String[5];
                                    data[0] = dataBaseName;
                                    for(int key: multiBuyingChoice.keySet()){
                                        data[1] = customizedID;
                                        data[2] = String.valueOf(key+1);
                                        data[3] = multiBuyingChoice.get(key);
                                        int temp = addCategoryPriceList.get("4")[key];
                                        int quantity = Integer.parseInt(multiBuyingChoice.get(key));
                                        int plusTotatal = temp*quantity;
                                        data[4] = String.valueOf(plusTotatal);

                                        Log.i("所有輸入:" , customizedID + "/" + key + "/"+ multiBuyingChoice.get(key) + "/" + plusTotatal );

                                        putData = new PutData("http://"+getText(R.string.IPv4)+"/"+getText(R.string.SQLConnection)+"/OrderQuerying/InsertCustomizedPlus.php", "POST", field, data);
                                        putData.startPut();
                                        if(putData.onComplete()){
                                            String result = putData.getData();
                                            Log.i("Insert Plus",key +"/"+ plusTotatal + " is completed");
                                            Log.i("配料結果", result);
                                        }
                                    }

                                }


                                field = new String[6];
                                field[0] = "database";
                                field[1] = "order_id";
                                field[2] = "productsize_id";
                                field[3] = "customized_id";
                                field[4] = "detail_quantity";
                                field[5] = "detail_price";
                                data = new String[6];
                                data[0] = dataBaseName;
                                data[1] = totalOrderID;
                                data[2] = productSize;
                                data[3] = customizedID;
                                data[4] = productQuantity;
                                data[5] = String.valueOf(totalPrice);

                                putData = new PutData("http://"+getText(R.string.IPv4)+"/"+getText(R.string.SQLConnection)+"/OrderQuerying/InsertOrderDetail.php", "POST", field, data);
                                putData.startPut();
                                if(putData.onComplete()){
                                    String result = putData.getData();
                                    Log.i("單杯飲料結果", result);
                                }
                            }

                        } catch (Exception ex) {
                            Log.i("訂單處理", ex.getMessage());
                        }
                    }

                }//end of else

                Toast.makeText(getBaseContext(),"已新增至購物車！",Toast.LENGTH_SHORT).show();
                Handler handler = new Handler();
                handler.postDelayed(runTimerStop, 1000);

            }
        });//end of btnAddToBuy

        btnRecommend.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(productDetail.this, productComment.class);
                Log.i("productName", productName);
                intent.putExtra("productName", productName);
                startActivity(intent);
            }
        });

    }//end of set

    public void countingTotalPrice(){

        //容量價格
        totalPrice = 0;
        int sizePriceIndex = Integer.parseInt(singleBuyingChoice.get(sizeIndex))-1; //將容量選項放於5
        int sizePrice = addCategoryPriceList.get(sizeIndex)[sizePriceIndex]; //將容量價格放於最後面

        if(!addGroup[0].equals("null")) { //若讀取無可添加選項 則不計算
            for (int i = 0; i < addGroup.length; i++) {
                try {
                    int priceIndex = Integer.parseInt(singleBuyingChoice.get(addGroup[i])) - 1;
                    int plusPrice = addCategoryPriceList.get(addGroup[i])[priceIndex];
                    totalPrice += plusPrice;
                } catch (NumberFormatException n) {
                    Log.e("SingleChoice Error", n.getMessage());
                }
            }
        }

        //配料選項 只有4才會有選項可算
        if(!multiBuyingChoice.isEmpty()){
            for (int key : multiBuyingChoice.keySet()) {
                int plusPrice = addCategoryPriceList.get("4")[key];
                int plusQuantity = Integer.parseInt(multiBuyingChoice.get(key));
                totalPrice += plusPrice * plusQuantity ;
            }
        }

        totalPrice += sizePrice;
        totalPrice *= Integer.parseInt(productQuantity);
        singleBuyingChoice.put("TotalPrice", String.valueOf(totalPrice));
        tvTotalPrice.setText("目前價格: NT$" + totalPrice);

    }//end of counting

    private class addChoiceListAdapter extends RecyclerView.Adapter<addChoiceListAdapter.ViewHolder>{

        private HashMap<String, String[]> hashMapName;
        private HashMap<String, int[]> hashMapPrice;
        private String nameKey;
        private boolean flag;
        private int forCount;
        private final HashMap<Integer, Boolean> checkMap;

        public addChoiceListAdapter(HashMap<String, String[]> hashMapName, HashMap<String, int[]> hashMapPrice,
                                    String nameKey, boolean flag, int forCount, String choice){
            this.hashMapName = hashMapName;
            this.hashMapPrice = hashMapPrice;
            this.nameKey = nameKey;
            this.flag = flag;
            this.forCount = forCount;

            checkMap = new HashMap<>();

            for (int i = 0; i < this.hashMapName.get(nameKey).length; i++) {
                checkMap.put(i, false);
            }

            try {
                checkMap.put(Integer.parseInt(choice)-1, true); //資料回填 將選擇項目-1 (因讀取為1開始)
            }catch(Exception e){
                Log.e("New order",e.getMessage());
                checkMap.put(0, true);
            }

        }

        public void singlesel(int postion) {
            Set<Map.Entry<Integer, Boolean>> entries = checkMap.entrySet();
            for (Map.Entry<Integer, Boolean> entry : entries) {
                entry.setValue(false);
            }
            checkMap.put(postion, true);
            notifyDataSetChanged();
        }

        class ViewHolder extends RecyclerView.ViewHolder{

            private LinearLayout LL;
            private CheckBox cbxName;
            private TextView tvProduct, tvPrice, tvNumber;
            private ElegantNumberButton elegantNumberButton;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                LL = itemView.findViewById(R.id.LLAll);
                tvProduct = itemView.findViewById(R.id.tvProduct);
                cbxName = itemView.findViewById(R.id.cbxItem);
                tvPrice = itemView.findViewById(R.id.tvPrice);
                tvNumber = itemView.findViewById(R.id.tvNumber);
                elegantNumberButton = itemView.findViewById(R.id.elegantNum);
                this.setIsRecyclable(false);
            }
        }

        @Override
        public int getItemCount() {
            return hashMapName.get(nameKey).length;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.style_addcategory,parent,false);

            return new ViewHolder(view);
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            holder.tvNumber.setVisibility(View.INVISIBLE);

            if(flag == true) {

                holder.tvProduct.setVisibility(View.GONE);
                holder.elegantNumberButton.setVisibility(View.GONE);

                holder.tvPrice.setText("$" + hashMapPrice.get(nameKey)[position]);
                holder.tvNumber.setText(strNumber[forCount]);

                holder.cbxName.setText(hashMapName.get(nameKey)[position]);
                holder.cbxName.setChecked(checkMap.get(position));
                holder.cbxName.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        checkMap.put(position, !checkMap.get(position));
                        notifyDataSetChanged(); //重新整理介面卡
                        singlesel(position); //單選

                        singleBuyingChoice.put(holder.tvNumber.getText().toString(), position+1+"");
                        countingTotalPrice();

                        Set<Map.Entry<String, String>> entries = singleBuyingChoice.entrySet();
                        for (Map.Entry<String, String> entry : entries) {
                            Log.i("單選項目", "目前區域:" + entry.getKey() + "/目前選擇: " + entry.getValue()+ "/目前大小: " +entries.size());
                        }

//                    Set<Map.Entry<Integer, Boolean>> entries = checkMap.entrySet();
//                    for (Map.Entry<Integer, Boolean> entry : entries) {
//                        Log.i("Tags", "All boolean:" + entry.getKey() + "/" + entry.getValue());
//                    }
                    }
                });
            }
            else if(flag == false){

                holder.cbxName.setVisibility(View.GONE);
                holder.tvProduct.setText(hashMapName.get(nameKey)[position]);
                holder.tvPrice.setText("$" + hashMapPrice.get(nameKey)[position]);

                if(multiPlusSet != null) {
                    holder.elegantNumberButton.setNumber(multiPlusSet.getOrDefault(position, "0"));
                }

                holder.elegantNumberButton.setOnClickListener(new ElegantNumberButton.OnClickListener() {
                    @Override
                    public void onClick(View view) {

//                        Toast.makeText(getBaseContext(),"配料總數: " + holder.tvProduct.getText()+"/"+holder.amountView.etAmount.getText(),Toast.LENGTH_SHORT).show();
//                        multiBuyingChoice.put(holder.tvProduct.getText().toString(), holder.elegantNumberButton.getNumber());

                        multiBuyingChoice.put(position, holder.elegantNumberButton.getNumber());
                        countingTotalPrice();
                        Set<Map.Entry<Integer, String>> entries = multiBuyingChoice.entrySet();
                        for (Map.Entry<Integer, String> entry : entries) {
                            Log.i("Plus", "加入的配料:" + entry.getKey() + "/目前數量: " + entry.getValue()+ "/目前大小: " +entries.size());
                        }
                    }//onClick
                });
            }

        }//onBind
    }//end of adapter

    public class RecyclerViewNoBugLinearLayoutManager extends LinearLayoutManager {
        @Override
        public boolean canScrollVertically() {
            return false;
        }

        public RecyclerViewNoBugLinearLayoutManager(Context context) {
            super( context );
        }

        public RecyclerViewNoBugLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
            super( context, orientation, reverseLayout );
        }

        public RecyclerViewNoBugLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super( context, attrs, defStyleAttr, defStyleRes );
        }

        @Override
        public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
            try {
                super.onLayoutChildren( recycler, state );
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }
    }

    private Runnable runTimerStop = new Runnable() {
        @Override
        public void run()
        {
            Intent intent = new Intent(productDetail.this, buyingCart.class);
            setResult(Activity.RESULT_OK, intent);
            finish();
            overridePendingTransition(0, android.R.anim.fade_out);
        }
    };

}//end of class