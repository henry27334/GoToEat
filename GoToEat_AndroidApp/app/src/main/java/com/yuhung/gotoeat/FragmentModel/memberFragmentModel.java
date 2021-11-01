package com.yuhung.gotoeat.FragmentModel;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.yuhung.gotoeat.ProductProcessing.buyingCart;
import com.yuhung.gotoeat.R;
import com.yuhung.gotoeat.UsedComponent.PutData;
import com.yuhung.gotoeat.MemberOperation.memberUpdate;

import org.json.JSONArray;
import org.json.JSONObject;


public class memberFragmentModel extends Fragment {

    private View view;
    private Toolbar tbMember;
    private ImageView imgMShop;
    private TextView tvAccount, tvName, tvPhone, tvAddress, tvEmail;
    private Button btnUpdatePW;
    private TableRow trName, trPhone, trAddress, trEmail;

    private String username, password, dataBasePic,
            phoneStr ,nameStr, addressStr, emailStr;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_memberpage, container, false);
        setHasOptionsMenu(true);
        bindingID();
        setData();

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        tbMember.setTitle("會員專區");
        activity.setSupportActionBar(tbMember);

        Glide.with(getContext()).load(dataBasePic).into(imgMShop);
        tvAccount.setText(username);
        tvName.setText(nameStr);
        tvPhone.setText(phoneStr);
        tvAddress.setText(addressStr);
        tvEmail.setText(emailStr);

        trName.setClickable(true);
        trName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buildAlertDialog("名稱", nameStr,"guest_name");
            }
        });

        trPhone.setClickable(true);
        trPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buildAlertDialog("手機", phoneStr,"guest_phone");
            }
        });

        trAddress.setClickable(true);
        trAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buildAlertDialog("地址", addressStr,"guest_address");
            }
        });

        trEmail.setClickable(true);
        trEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buildAlertDialog("Email", emailStr,"guest_mail");
            }
        });

        btnUpdatePW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), memberUpdate.class);
                getActivity().startActivity(intent);

            }
        });

        return view;
    }//end of create

    private void bindingID(){
        imgMShop = view.findViewById(R.id.imgMShop);
        tbMember = view.findViewById(R.id.tbMember);
        tvAccount = view.findViewById(R.id.tvAccount);
        tvName = view.findViewById(R.id.tvName);
        tvPhone = view.findViewById(R.id.tvPhone);
        tvAddress = view.findViewById(R.id.tvAddress);
        tvEmail = view.findViewById(R.id.tvEmail);
        btnUpdatePW = view.findViewById(R.id.btnUpdatePW);

        trName = view.findViewById(R.id.trName);
        trPhone = view.findViewById(R.id.trPhone);
        trAddress = view.findViewById(R.id.trAddress);
        trEmail = view.findViewById(R.id.trEmail);

    }//end of bindingID

    private void setData(){

        username = getActivity().getSharedPreferences("userData", getActivity().MODE_PRIVATE)
                .getString("userAccount","");

        password =  getActivity().getSharedPreferences("userData", getActivity().MODE_PRIVATE)
                .getString("userPassword","");

        dataBasePic = getActivity().getSharedPreferences("userData", getActivity().MODE_PRIVATE)
                .getString("dataBasePic","");

        String[] field = new String[1];
        field[0] = "guest_account";
        //Creating array for data
        String[] data = new String[1];
        data[0] = username;
        PutData putData = new PutData("http://"+getText(R.string.IPv4)+"/"+getText(R.string.SQLConnection) +"/App_Operation/RequireMemberInfo.php", "POST", field, data);
        if (putData.startPut()) {
            if (putData.onComplete()) {
                String result = putData.getResult();

                try {
                    JSONArray ja = new JSONArray(result);
                    JSONObject jo = ja.getJSONObject(0);

                    nameStr = jo.getString("guest_name");
                    phoneStr = jo.getString("guest_phone");
                    addressStr = jo.getString("guest_address");
                    emailStr = jo.getString("guest_mail");

                }catch(Exception e){
                    Log.e("會員資訊", result);
                }

            }
        }
    }

    private void updateData(String userField,String userData){

        String[] field = new String[3];
        field[0] = "field";
        field[1] = "data";
        field[2] = "guest_account";
        String[] data = new String[3];
        data[0] = userField;
        data[1] = userData;
        data[2] = username;
        PutData putData = new PutData("http://"+getText(R.string.IPv4)+"/"+getText(R.string.SQLConnection) +"/App_Operation/UpdateMemberInfo.php", "POST", field, data);
        if (putData.startPut()) {
            if (putData.onComplete()) {
                String result = putData.getResult();
                Log.i("MemberInfo", result);
                Log.i("MemberInfo", "update " + userField + " is completed.");
            }
        }
    }//end of updataData

    private void buildAlertDialog(String changeField,String originalData, String field){

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        View v = getLayoutInflater().inflate(R.layout.style_updatememberinfo,null);
        alertDialog.setView(v);
        TextView tvMTitle = v.findViewById(R.id.tvMTitle);
        TextView tvTagInfo = v.findViewById(R.id.tvTagInfo);
        TextView tvNewInfo = v.findViewById(R.id.tvNewInfo);
        EditText edOriginal = v.findViewById(R.id.edtOrginal);
        EditText edNewData = v.findViewById(R.id.edtNewData);
        Button btnExit = v.findViewById(R.id.btnExit);
        Button btnSend = v.findViewById(R.id.btnSend);
        edOriginal.setTextSize(20);
        edNewData.setTextSize(20);
        btnExit.setTextSize(20);
        btnSend.setTextSize(20);

        tvMTitle.setText("修改"+changeField);
        tvTagInfo.setText(" "+changeField);
        tvNewInfo.setText(" 新的"+changeField);
        edOriginal.setText(originalData);
        edOriginal.setKeyListener(null);

        AlertDialog dialog = alertDialog.create();
        dialog.show();

        btnExit.setOnClickListener((v2 -> {
            dialog.dismiss();
        }));

        btnSend.setOnClickListener((v3 -> {
            AlertDialog.Builder twoDialog = new AlertDialog.Builder(getActivity());
            AlertDialog dialogs = twoDialog.create();
            twoDialog.setTitle("確定修改資料?");
            twoDialog.setNegativeButton("取消",(dialog1, which) -> { });
            twoDialog.setPositiveButton("確定",((dialog1, which) -> {

                String NewData =  edNewData.getText().toString();

                if(!NewData.equals("")) {
                    updateData(field, edNewData.getText().toString());
                    Intent intent = getActivity().getIntent();
                    getActivity().finish();
                    intent.putExtra("selectedPage",4);
                    startActivity(intent);
                    getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

                }else{
                    Toast.makeText(getActivity(),"請輸入新的資料!",Toast.LENGTH_SHORT).show();
                }

                dialogs.dismiss();
                dialog.dismiss();

            }));
            twoDialog.show();
        }));

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.actionbar_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.orderCart:
                Intent intent = new Intent(getContext(), buyingCart.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

}//