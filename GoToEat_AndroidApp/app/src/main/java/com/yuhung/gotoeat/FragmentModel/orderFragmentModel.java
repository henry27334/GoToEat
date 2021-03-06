package com.yuhung.gotoeat.FragmentModel;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yuhung.gotoeat.ProductProcessing.buyingCart;
import com.yuhung.gotoeat.ProductProcessing.orderInfo;
import com.yuhung.gotoeat.R;
import com.yuhung.gotoeat.UsedComponent.PutData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;

public class orderFragmentModel extends Fragment {

    private LinearLayout linearOrder;
    private View view;
    private Toolbar tbOrder;
    private RecyclerView orderRv;

    private String dataBaseName, username, result;
    private String[] orderID, orderDate, orderDataFormate;
    private String[] countDrink, totalPrice;
    private orderRecycleView orderAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_orderpage, container, false);
        setHasOptionsMenu(true);
        BindingID();
        setData();
        checkState();

        tbOrder.setTitle("歷史訂單");
        tbOrder.inflateMenu(R.menu.actionbar_order);
        tbOrder.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.orderCart) {
                    Intent intent = new Intent(getContext(), buyingCart.class);
                    startActivity(intent);
                }
                return true;
            }
        });

        orderRv.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL, false));
        orderRv.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        orderAdapter = new orderRecycleView(orderID, orderDataFormate, countDrink, totalPrice);
        orderRv.setAdapter(orderAdapter);

        return view;
    }

    private void BindingID(){
        linearOrder = view.findViewById(R.id.linearOrder);
        tbOrder = view.findViewById(R.id.tbOrder);
        orderRv = view.findViewById(R.id.RVOrder);
    }

    private void setData(){

        username = getActivity().getSharedPreferences("userData", MODE_PRIVATE)
                .getString("userAccount",null);
        dataBaseName = getActivity().getSharedPreferences("userData", MODE_PRIVATE)
                .getString("selectedShop",null);

        if(username == null) {
            Toast.makeText(getActivity(), "目前沒有登入！", Toast.LENGTH_SHORT).show();
        }else{
            String[] field = new String[2];
            field[0] = "database";
            field[1] = "guest_account";
            String[] data = new String[2];
            data[0] = dataBaseName;
            data[1] = username;
            PutData putData = new PutData("http://"+getText(R.string.IPv4)+ "/" + getText(R.string.SQLConnection)+ "/OrderDisplay/DisplayAllOrder.php", "POST", field, data);
            putData.startPut();
            if(putData.onComplete()){
                result = putData.getData();
                try {
                    JSONArray ja = new JSONArray(result);
                    JSONObject jo;
                    orderID = new String[ja.length()];
                    orderDate = new String[ja.length()];
                    countDrink = new String[ja.length()];
                    totalPrice = new String[ja.length()];
                    for (int i = 0; i < ja.length(); i++) {
                        jo = ja.getJSONObject(i);
                        orderID[i] = jo.getString("order_id");
                        orderDate[i] = jo.getString("order_date");
                        countDrink[i] = jo.getString("drinkQuantity");
                        totalPrice[i] = jo.getString("totalPrice");
                    }
                } catch (Exception ex) {
                    Log.e("基礎飲料設定", ex.getMessage());
                }


                if(!result.equals("Error: No history order.")){

                    orderDataFormate = new String[orderDate.length];
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                    for(int i=0;i<orderDate.length;i++){
                        try {
                            Date dateParse = sdf.parse(orderDate[i]);
                            String dateStringParse = sdf.format(dateParse);
                            orderDataFormate[i] = dateStringParse;
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }

                }//if
            }//onComplete

        }//else
    }//setData

    private void checkState(){

        LinearLayout.LayoutParams linearlayout_parent_params = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        linearlayout_parent_params.setMargins(0, 10, 0, 10);
        TextView tvInfo = new TextView(getActivity());

        if(result.equals("Error: No history order.")){

            orderRv.setVisibility(View.GONE);
            tvInfo.setText("\n\n\n\n目前沒有歷史訂單，快去選購吧！");
            tvInfo.setTextSize(20);
            tvInfo.setGravity(Gravity.CENTER);
            linearOrder.addView(tvInfo,linearlayout_parent_params);

        }
    }//end of checkState

    class orderRecycleView extends RecyclerView.Adapter<orderRecycleView.ViewHolder> {

        private String[] orderID, orderDate, countDrink, totalPrice;

        public orderRecycleView(String[] orderID, String[] orderDate, String[] countDrink, String[] totalPrice) {
            this.orderID = orderID;
            this.orderDate = orderDate;
            this.totalPrice = totalPrice;
            this.countDrink = countDrink;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            private ImageView imageView;
            private TextView tvPrice, tvDateStatus;
            private Button btnInfo;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.imageView);
                tvDateStatus = itemView.findViewById(R.id.tvDateStatus);
                tvPrice = itemView.findViewById(R.id.tvOrderPrice);
                btnInfo = itemView.findViewById(R.id.btnInfo);

            }
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View viewChild = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.style_orderdisplay, parent, false);
            return new ViewHolder(viewChild);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            holder.itemView.setTag(position);

            holder.tvDateStatus.setText("○" + orderDate[position] + "○  已完成訂單");
            holder.tvPrice.setText("○NT$" + totalPrice[position] + "○");
            //holder.tvPrice.setText("○" + countDrink[position] + "份○" + "  NT$" + totalPrice[position]);

            holder.btnInfo.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), orderInfo.class);
                        intent.putExtra("totalOrderID", orderID[position]);
                        startActivity(intent);
                        Log.e("totalOrderID", orderID[position]);
                }
            });

        }

        @Override
        public int getItemCount() {
            return orderID.length;
        }
    }


}//end of class


