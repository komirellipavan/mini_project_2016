package com.example.pavan.cardoctor;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

public class ClientSignUp extends AppCompatActivity {
    private EditText name;
    private EditText emailId;
    private EditText password;
    private EditText rPassword;
    private Button b;
    private String gender;

    //TextWatcher
    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3)
        {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            enableSubmitIfReady();

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_sign_up);
        b = (Button) findViewById(R.id.next);
        b.setEnabled(false);
        //radioGroup
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        gender = "male";
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected
                if(checkedId == R.id.male) {
                        gender = "male";
                } else if(checkedId == R.id.female){
                        gender = "female";

                }
            }

        });


            name = (EditText) findViewById(R.id.name);
        emailId = (EditText) findViewById(R.id.emailId);
        password = (EditText) findViewById(R.id.password);
        rPassword = (EditText) findViewById(R.id.rPassword);

        //set listeners
        name.addTextChangedListener(textWatcher);
        emailId.addTextChangedListener(textWatcher);
        password.addTextChangedListener(textWatcher);
        rPassword.addTextChangedListener(textWatcher);

    }

    public void back(View view) {
        Intent intent = new Intent(this, ClientLogin.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

    }

    public void onBackPressed() {
        Intent i = new Intent(this, ClientLogin.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);

    }

    public void next(View view) {

        Intent intent = new Intent(this, ClientSignUpAddress.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

        SharedPreferences sharedpreferences = getSharedPreferences("signuptemp", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("name",name.getText().toString());
        editor.putString("password",password.getText().toString());
        editor.putString("gender",gender);
        editor.putString("emailId",emailId.getText().toString());

        editor.apply();

        startActivity(intent);
    }

    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public void enableSubmitIfReady() {
        b = (Button) findViewById(R.id.next);


        if (name.getText().toString().length() < 5 ||
                isValidEmail( emailId.getText().toString())== false||
                password.getText().toString().length()  < 4 ||
                rPassword.getText().toString().length() < 4 ||
                password.getText().toString().equals(rPassword.getText().toString())== false) {
            b.setEnabled(false);


        }
        if (name.getText().toString().length() >= 5 &&
                isValidEmail( emailId.getText().toString()) &&
                password.getText().toString().length()  >=4 &&
                rPassword.getText().toString().length() >=4 &&
                password.getText().toString().equals(rPassword.getText().toString())) {
            b.setEnabled(true);


        } else {
            if (password.getText().toString().length() == 0)
                password.setError("Password is required!(at least 4 char)");
            if (rPassword.getText().toString().length() == 0)
                rPassword.setError("Retype Password!");
            if (name.getText().toString().length() == 0)
                name.setError("Name is required!");
            if (emailId.getText().toString().length() == 0)
                emailId.setError("emailId is required!");
        }

    }


}
