package com.example.pavan.cardoctor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AssistantLogin extends AppCompatActivity {

    private EditText emailId;
    private EditText password;
    private Button b;
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
        setContentView(R.layout.activity_assistant_login);

        b = (Button) findViewById(R.id.login);
        b.setEnabled(false);

        emailId = (EditText) findViewById(R.id.emailId);
        password = (EditText) findViewById(R.id.password);

        emailId.addTextChangedListener(textWatcher);
        password.addTextChangedListener(textWatcher);
    }
    public void cancel(View view){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();// destroys current activity
    }
    public void onBackPressed(){
        Intent i = new Intent(this,MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        finish();

    }
    public void login(View view){

    }
    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
    public void enableSubmitIfReady() {
        b = (Button) findViewById(R.id.login);


        if (isValidEmail( emailId.getText().toString())== false||
                password.getText().toString().length()  < 4){

            b.setEnabled(false);
        }
        if (isValidEmail( emailId.getText().toString()) &&
                password.getText().toString().length()  >=4) {
            b.setEnabled(true);
        } else {
            if (password.getText().toString().length() == 0)
                password.setError("Password is required!(at least 4 char)");

            if (emailId.getText().toString().length() == 0)
                emailId.setError("emailId is required!");
        }

    }

}
