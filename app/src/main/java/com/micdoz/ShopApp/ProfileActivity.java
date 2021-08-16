package com.micdoz.ShopApp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.micdoz.ShopApp.baseObjects.Cart;
import com.micdoz.ShopApp.baseObjects.Product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

/**
 * This class is used to display the user's settings, update his settings and delete his account.
 */
public class ProfileActivity extends AppCompatActivity {

    /**
     * Class variables
     */

    FirebaseAuth fAuth;
    FirebaseUser fUser;
    FirebaseDatabase db;
    DatabaseReference dbRef;
    TextView fName, lName, email, password;
    EditText eFName, eLName, eEmail, ePassword;
    String userId;
    Intent toShopping;
    Button mDisplay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        fAuth = FirebaseAuth.getInstance();
        fUser = fAuth.getCurrentUser();
        db = FirebaseDatabase.getInstance();
        dbRef = db.getReference("path_to_firebase_database");
        fName = (TextView) findViewById(R.id.firstNameProf);
        lName = (TextView) findViewById(R.id.lastNameProf);
        email = (TextView) findViewById(R.id.EmailTextProf);
        password = (TextView) findViewById(R.id.passwordTextProf);
        eFName = (EditText) findViewById(R.id.firstNameEdit);
        eLName = (EditText) findViewById(R.id.lastNameEdit);
        eEmail = (EditText) findViewById(R.id.EmailEditProf);
        ePassword = (EditText) findViewById(R.id.passwordEditProf);
        mDisplay = (Button) findViewById(R.id.displayButton);
        if(getIntent().getStringExtra("user_id") == null){
            userId = fUser.getUid();
        }
        else{
            userId = getIntent().getStringExtra("user_id");
        }

        dbRef.child("users").child(userId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(ProfileActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
                else {
                    HashMap h = (HashMap) Objects.requireNonNull(task.getResult()).getValue();
                    fName.setText("" + h.get("fName"));
                    lName.setText("" + h.get("lName"));
                    email.setText("" + fUser.getEmail());
                    password.setText("*********");
                    fName.setTypeface(null, Typeface.BOLD);
                    lName.setTypeface(null, Typeface.BOLD);
                    email.setTypeface(null, Typeface.BOLD);
                }
            }
        });

    }

    /**
     * this method is used to display and hide relevant fields.
     * @param view
     */

    public void showUpdateMode(View view) {
        fName.setVisibility(View.INVISIBLE);
        lName.setVisibility(View.INVISIBLE);
        email.setVisibility(View.INVISIBLE);
        password.setVisibility(View.INVISIBLE);
        eFName.setVisibility(View.VISIBLE);
        eLName.setVisibility(View.VISIBLE);
        eEmail.setVisibility(View.VISIBLE);
        ePassword.setVisibility(View.VISIBLE);
        mDisplay.setVisibility(View.VISIBLE);
    }

    /**
     * This method is used to apply the changes that the user made to the firebase db.
     * @param view
     */

    public void applyChanges(View view) {

        if(eFName.getText().toString().length() <= 0 || eLName.getText().toString().length() <= 0){
            return;
        }
        else{
            if(eFName.getText().toString().length() > 2){
                dbRef.child("users").child(userId).child("fName").setValue(eFName.getText().toString());
                fName.setText(eFName.getText().toString());
            }
            if(eLName.getText().toString().length() > 2){
                dbRef.child("users").child(userId).child("lName").setValue(eLName.getText().toString());
                lName.setText(eLName.getText().toString());
            }
            if(eEmail.getText().toString().length() > 2){
                fUser.updateEmail(eEmail.getText().toString());
                email.setText(eEmail.getText().toString());
            }
            if (ePassword.getText().toString().length() > 7){
                fUser.updatePassword(ePassword.getText().toString());
            }
            eFName.setVisibility(View.INVISIBLE);
            eLName.setVisibility(View.INVISIBLE);
            eEmail.setVisibility(View.INVISIBLE);
            ePassword.setVisibility(View.INVISIBLE);
            fName.setVisibility(View.VISIBLE);
            lName.setVisibility(View.VISIBLE);
            email.setVisibility(View.VISIBLE);
            password.setVisibility(View.VISIBLE);
        }



    }

    /**
     * This method is used to move to the shopping page.
     * @param view
     */

    public void moveToShopping(View view) {
        toShopping = new Intent(ProfileActivity.this, ShoppingActivity.class);
        ArrayList<Product> arr = getIntent().getParcelableArrayListExtra("products");
        Cart cart = getIntent().getParcelableExtra("cart");
        toShopping.putParcelableArrayListExtra("products", arr);
        toShopping.putExtra("cart", cart);
        startActivity(toShopping);
        finish();
    }

    /**
     * This method is used to delete the current user that is connected to the application.
     * @param view
     */

    public void deleteUser(View view){
        fUser.delete();
        Intent toLogin = new Intent(ProfileActivity.this, LoginActivity.class);
        startActivity(toLogin);
    }

    public void showDisplayMode(View view) {
        eFName.setVisibility(View.INVISIBLE);
        eLName.setVisibility(View.INVISIBLE);
        eEmail.setVisibility(View.INVISIBLE);
        ePassword.setVisibility(View.INVISIBLE);
        fName.setVisibility(View.VISIBLE);
        lName.setVisibility(View.VISIBLE);
        email.setVisibility(View.VISIBLE);
        password.setVisibility(View.VISIBLE);
    }
}
