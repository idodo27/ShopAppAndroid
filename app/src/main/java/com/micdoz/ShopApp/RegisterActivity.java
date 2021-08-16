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
import com.micdoz.ShopApp.baseObjects.User;

public class RegisterActivity extends AppCompatActivity {
    private String passHint = "Passwords need to at least 8 characters\nAnd include at least" +
            "1 number, 1 uppercase letter, 1 lowercase letter and 1 special character";
    private String detailsError = "One or more details are missing";
    Intent toLogin;
    EditText firstName, lastName, email, password;
    FirebaseUser fUser;
    FirebaseDatabase db;
    FirebaseAuth fAuth;
    DatabaseReference dbRef;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        firstName = (EditText) findViewById(R.id.firstName);
        lastName = (EditText) findViewById(R.id.lastName);
        email = (EditText) findViewById(R.id.EmailTextRegister);
        password = (EditText) findViewById(R.id.passwordTextRegister);
        fAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        dbRef = db.getReference("path_to_firebase_database");
    }

    public void passwordHint(View view) {
        Toast.makeText(this, passHint, Toast.LENGTH_LONG).show();
    }

    public void registerMoveLogin(View view) {
        if (firstName.getText().toString().length() <= 0 ||
                lastName.getText().toString().length() <= 0 ||
                !(email.getText().toString().contains("@") &&
                        email.getText().toString().contains(".com") ||
                        email.getText().toString().contains(".org") ||
                        email.getText().toString().contains(".co.il")) ||
                password.getText().toString().length() <= 7) {

            Toast.makeText(this, detailsError, Toast.LENGTH_SHORT).show();
            return;
        } else {
            fAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                fUser = fAuth.getCurrentUser();
                                if (fUser != null) {
                                    createUser();
                                }
                                toLogin = new Intent(RegisterActivity.this, LoginActivity.class);
                                toLogin.putExtra("user_id", fUser.getUid());
                                Toast.makeText(RegisterActivity.this, "User Created.", Toast.LENGTH_SHORT).show();
                                startActivity(toLogin);
                            } else {
                                Toast.makeText(RegisterActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
            ;
        }


    }

    public void createUser() {
        User user = new User(firstName.getText().toString(), lastName.getText().toString());
        dbRef.child("users").child(fUser.getUid()).setValue(user);

    }

    public void clearFields(View view) {
        firstName.setText("");
        lastName.setText("");
        email.setText("");
        password.setText("");
    }

}
