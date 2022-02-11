package techmarket.uno.movie45;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText editTextEmailAddressRegister;
    private EditText editTextPasswordRegister;
    private TextView textViewAlreadyRegistered;
    private String error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Objects.requireNonNull(getSupportActionBar()).hide();
        mAuth = FirebaseAuth.getInstance();
        editTextEmailAddressRegister = findViewById(R.id.editTextEmailAddressRegister);
        editTextPasswordRegister = findViewById(R.id.editTextPasswordRegister);
        textViewAlreadyRegistered = findViewById(R.id.textViewAlreadyRegistered);
        textViewAlreadyRegistered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    public void onClickAlreadyRegistered(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void onClickRegister(View view) {
        String email = editTextEmailAddressRegister.getText().toString().trim();
        String password = editTextPasswordRegister.getText().toString().trim();
        if(email.isEmpty() || password.isEmpty()){
            Toast.makeText(RegisterActivity.this, "Enter valid email and password", Toast.LENGTH_SHORT).show();
            return;
        }
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                          if (task.isSuccessful()){
                              Log.i("test","Create User with e-mail: success");
                              FirebaseUser user = mAuth.getCurrentUser();
                              Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
                              startActivity(intent);
                          }else {
                              Log.i("test", "" + task.getException());
                              if (task.getException()
                                      .toString()
                                      .equals("com.google.firebase.auth.FirebaseAuthInvalidCredentialsException: The email address is badly formatted.")) {
                                  error = "The email address is badly formatted.";
                                  errorMessage();
                              } else if (task.getException()
                                      .toString()
                                      .equals("com.google.firebase.auth.FirebaseAuthWeakPasswordException: The given password is invalid. [ Password should be at least 6 characters ]")) {
                                  error = "Password should be at least 6 characters.";
                                  errorMessage();
                              }
                          }
                    }
                });

    }
    void errorMessage(){
        Toast toast = Toast.makeText(RegisterActivity.this, "" + error, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0,0);
        toast.show();
    }
}