package com.yuhung.gotoeat.MemberOperation;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.yuhung.gotoeat.R;
import com.yuhung.gotoeat.UsedComponent.PutData;

public class memberSignUp extends AppCompatActivity {

    TextInputEditText textInputEditTextFullname, textInputEditTextUsername, textInputEditTextPassword, textInputEditTextEmail, textInputEditTextAddress,textInputEditTextPhone;
    Button buttonSignUp;
    TextView textViewLogin;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memersignuup);

        //getSupportActionBar().hide(); //隱藏標題欄位

        BindingID();
        textViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), memberLogin.class);
                startActivity(intent);
                finish();
            }
        });

        buttonSignUp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                final String username, password, fullname, address, email, phone;

                username = String.valueOf(textInputEditTextUsername.getText());
                password = String.valueOf(textInputEditTextPassword.getText());
                fullname = String.valueOf(textInputEditTextFullname.getText());
                address = String.valueOf(textInputEditTextAddress.getText());
                email = String.valueOf(textInputEditTextEmail.getText());
                phone = String.valueOf(textInputEditTextPhone.getText());

                if(!username.equals("") && !password.equals("") && !fullname.equals("") && !address.equals("") && !email.equals("")) {
                    progressBar.setVisibility(View.VISIBLE);
                    //Start ProgressBar first (Set visibility VISIBLE)
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            //Starting Write and Read data with URL
                            //Creating array for parameters
                            String[] field = new String[6];
                            field[0] = "guest_account";
                            field[1] = "guest_password";
                            field[2] = "guest_name";
                            field[3] = "guest_phone";
                            field[4] = "guest_mail";
                            field[5] = "guest_address";
                            //Creating array for data
                            String[] data = new String[6];
                            data[0] = username;
                            data[1] = password;
                            data[2] = fullname;
                            data[3] = phone;
                            data[4] = email;
                            data[5] = address;
                            PutData putData = new PutData("http://"+getText(R.string.IPv4)+"/"+getText(R.string.SQLConnection) + "/App_Operation/signup.php", "POST", field, data);
                            if (putData.startPut()) {
                                if (putData.onComplete()) {
                                    progressBar.setVisibility(View.GONE);
                                    String result = putData.getResult();
                                    if(result.equals("註冊成功，將轉跳至登入畫面!")){
                                        Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplicationContext(), memberLogin.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else {
                                        Toast.makeText(getApplicationContext(), result,Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                            //End Write and Read data with URL
                        }
                    });
                }
                else {
                    Toast.makeText(getApplicationContext(), "All fields required", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void BindingID(){
        textInputEditTextFullname = findViewById(R.id.fullname);
        textInputEditTextUsername = findViewById(R.id.username);
        textInputEditTextPassword = findViewById(R.id.password);
        textInputEditTextAddress = findViewById(R.id.Address);
        textInputEditTextEmail = findViewById(R.id.Email);
        textInputEditTextPhone = findViewById(R.id.Phone);
        buttonSignUp= findViewById(R.id.buttonSignUp);
        textViewLogin = findViewById(R.id.loginText);
        progressBar = findViewById(R.id.progress);
    }
}