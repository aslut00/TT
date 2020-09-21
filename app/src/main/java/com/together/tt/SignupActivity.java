package com.together.tt;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SignupActivity extends AppCompatActivity {

    EditText email;
    EditText pw;
    EditText name;
    EditText gender;
    EditText birthday;

    Button signup_requestBtn;

    String data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        signup_requestBtn = findViewById(R.id.signup_request);
        email = findViewById(R.id.email_edit);
        pw = findViewById(R.id.password_edit);
        name = findViewById(R.id.name_edit);
        gender = findViewById(R.id.gender_edit);
        birthday = findViewById(R.id.birthday_edit);

        signup_requestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data = "signup"+email.getText().toString()+"-"+pw.getText().toString()+"-"+name.getText().toString()+"-"+gender.getText().toString()+"-"+birthday.getText().toString();
                Network network = new Network();
                network.connect();

                // 리스너 추가
                Intent intent = new Intent(SignupActivity.this,MainActivity.class);
                startActivity(intent);
                finish();

            }
        });

    }

}
