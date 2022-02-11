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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private EditText editTextEmailAddressLogin;
    private EditText editTextPasswordLogin;

    private String error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Objects.requireNonNull(getSupportActionBar()).hide();
        editTextEmailAddressLogin = findViewById(R.id.editTextEmailAddressLogin);
        editTextPasswordLogin = findViewById(R.id.editTextPasswordLogin);
        mAuth = FirebaseAuth.getInstance();
    }

    public void onClickNotRegistered(View view) {
        Intent intent = new Intent(this,RegisterActivity.class);
        startActivity(intent);
    }

    public void onClickLogin(View view) {
        String email = editTextEmailAddressLogin.getText().toString().trim();
        String password = editTextPasswordLogin.getText().toString().trim();

        if(email.isEmpty() || password.isEmpty()){
            Toast toast = Toast.makeText(LoginActivity.this, "Enter valid email and password", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Log.i("test","SignIn with with e-mail: success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
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
        Toast toast = Toast.makeText(LoginActivity.this, "" + error, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0,0);
        toast.show();
    }
}
