package com.micdoz.ShopApp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

/**
 * This class is used to login an existing user, or to navigate to the registration page.
 */

public class LoginActivity extends AppCompatActivity {

    /**
     * class Variables
     */

    Intent toRegister;
    Intent toShopping;
    EditText email, password;
    FirebaseAuth fAuth;
    FirebaseUser fUser;
    FirebaseDatabase db;
    DatabaseReference dbRef;
    private String detailsError= "One or more details are incorrect";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email = (EditText) findViewById(R.id.EmailText);
        password = (EditText) findViewById(R.id.passwordText);
        fAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        /*
            change to your database name.
         */
        dbRef = db.getReference("shopapp-e8021-default-rtdb");

    }

    /**
     * method to move to register page.
     * @param view
     */

    public void moveRegister(View view) {
        toRegister = new Intent(this, RegisterActivity.class);
        startActivity(toRegister);
    }
    /**
     * method to move to shopping page.
     * @param view
     */

    public void moveShoppingPage(View view){
        if(!(email.getText().toString().contains("@") &&
                email.getText().toString().contains(".com") ||
                email.getText().toString().contains(".org") ||
                email.getText().toString().contains(".co.il")) ||
                password.getText().toString().length() <= 7){
            Toast.makeText(this, detailsError, Toast.LENGTH_SHORT).show();
            return;
        }else{

            fAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                fUser = fAuth.getCurrentUser();
                                Toast.makeText(LoginActivity.this, "login successfully", Toast.LENGTH_SHORT).show();
                                toShopping = new Intent(LoginActivity.this, ShoppingActivity.class);
                                if(getIntent() == null){
                                    toShopping.putExtra("user_id", Objects.requireNonNull(Objects.requireNonNull(task.getResult()).getUser()).getUid());
                                }
                                else{
                                    toShopping.putExtra("user_id", getIntent().getStringExtra("user_id"));
                                }
                                email.setText("");
                                password.setText("");
                                startActivity(toShopping);
                            } else {
                                Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }
    }

}
