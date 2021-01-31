package com.koders.facileyieldanalyzer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    TabLayout tabLayout;
    Button btn;
    EditText name, username, password, email;
    TextView forgotPassword;
    boolean isLogin = true;
    FirebaseAuth auth;
    ImageView progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
    }

    private void init() {
        tabLayout = findViewById(R.id.tabLayout);
        btn = findViewById(R.id.btn);
        name = findViewById(R.id.nameText);
        username = findViewById(R.id.userNameText);
        password = findViewById(R.id.passwordText);
        email = findViewById(R.id.emailText);
        forgotPassword = findViewById(R.id.forgotPassword);
        auth = FirebaseAuth.getInstance();
        progress = findViewById(R.id.progress);

        showLogin();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    showLogin();
                } else if (tab.getPosition() == 1) {
                    showSignUp();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                if (isLogin) {
                    if (!TextUtils.isEmpty(email.getText().toString().trim()) & !TextUtils.isEmpty(password.getText().toString().trim())) {
                        progress.setVisibility(View.VISIBLE);
                        auth.signInWithEmailAndPassword(email.getText().toString().trim(), password.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progress.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                    finish();
                                } else {
                                    Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                    } else {
                        Toast.makeText(LoginActivity.this, "Fields must not be empty", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    if (!TextUtils.isEmpty(email.getText().toString().trim()) & !TextUtils.isEmpty(password.getText().toString().trim())
                            & !TextUtils.isEmpty(username.getText().toString().trim()) & !TextUtils.isEmpty(name.getText().toString().trim())) {
                        progress.setVisibility(View.VISIBLE);

                        auth.createUserWithEmailAndPassword(email.getText().toString().trim(), password.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progress.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    Toast.makeText(LoginActivity.this, "SignUp Successful", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                    finish();
                                } else {
                                    Toast.makeText(LoginActivity.this, "SignUp Failed", Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                    } else {
                        Toast.makeText(LoginActivity.this, "Fields must not be empty", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, ForgetPasswordActivity.class));
            }
        });
    }

    private void showLogin() {
        email.setVisibility(View.VISIBLE);
        password.setVisibility(View.VISIBLE);
        forgotPassword.setVisibility(View.VISIBLE);
        btn.setText("Login In");

        name.setVisibility(View.GONE);
        username.setVisibility(View.GONE);

        isLogin = true;
    }

    private void showSignUp() {
        username.setVisibility(View.VISIBLE);
        name.setVisibility(View.VISIBLE);
        email.setVisibility(View.VISIBLE);
        password.setVisibility(View.VISIBLE);
        btn.setText("Sign Up");

        forgotPassword.setVisibility(View.GONE);

        isLogin = false;
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}