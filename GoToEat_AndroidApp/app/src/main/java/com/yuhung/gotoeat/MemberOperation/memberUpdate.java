package com.yuhung.gotoeat.MemberOperation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.yuhung.gotoeat.R;
import com.yuhung.gotoeat.UsedComponent.PutData;


public class memberUpdate extends AppCompatActivity {

    private Toolbar tbMemberPW;
    private EditText edtOldPw, edtNewPw, edtNewPwA;
    private Button btnSubmit;

    private SharedPreferences pref;
    private String username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memberupdate);
        bindingID();

        tbMemberPW.setTitle("修改密碼");
        setSupportActionBar(tbMemberPW);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        username = getSharedPreferences("userData", MODE_PRIVATE)
                .getString("userAccount", "");
        password = getSharedPreferences("userData", MODE_PRIVATE)
                .getString("userPassword", "");

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String inputOldPw = edtOldPw.getText().toString();
                String inputNewPw = edtNewPw.getText().toString();
                String inputNewPwA = edtNewPwA.getText().toString();

                if (!inputOldPw.equals(password)) {

                    Toast.makeText(memberUpdate.this, "目前密碼錯誤，請重新輸入。", Toast.LENGTH_SHORT).show();

                } else {

                    if (!inputNewPw.equals(inputNewPwA)) {

                        Toast.makeText(memberUpdate.this, "新密碼輸入錯誤，請重新輸入。", Toast.LENGTH_SHORT).show();

                    } else if (inputNewPw.equals("") && inputNewPwA.equals("")) {

                        Toast.makeText(memberUpdate.this, "輸入欄位不可為空白。", Toast.LENGTH_SHORT).show();

                    } else {

                        String result = updataData(inputNewPw);
                        Toast.makeText(memberUpdate.this, result, Toast.LENGTH_SHORT).show();

                        pref = getSharedPreferences("userData", MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.remove("userAccount");
                        editor.remove("userPassword");
                        editor.commit();

                        Intent intent = new Intent(memberUpdate.this, memberLogin.class);
                        finish();
                        startActivity(intent);

                    }
                }

            }//onClick
        });

    }

    private void bindingID() {
        tbMemberPW = findViewById(R.id.tbMemberPW);
        edtOldPw = findViewById(R.id.edtOldPW);
        edtNewPw = findViewById(R.id.edtNewPW);
        edtNewPwA = findViewById(R.id.edtNewPWA);
        btnSubmit = findViewById(R.id.btnSubmit);

    }

    private String updataData(String newPassword) {

        String[] field, data;
        String result = "Error Update";
        PutData putData;

        field = new String[2];
        field[0] = "guest_account";
        field[1] = "new_passwrod";
        data = new String[2];
        data[0] = username;
        data[1] = newPassword;
        putData = new PutData("http://" + getText(R.string.IPv4) + "/" + getText(R.string.SQLConnection) + "/App_Operation/UpdateMemberPassword.php", "POST", field, data);
        putData.startPut();
        if (putData.onComplete()) {
            result = putData.getData();

        }

        return result;
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish(); // close this activity as oppose to navigating up
        return false;
    }
}