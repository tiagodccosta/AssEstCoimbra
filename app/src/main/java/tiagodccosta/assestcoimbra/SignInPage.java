package tiagodccosta.assestcoimbra;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignInPage extends AppCompatActivity {

    private Button btnSignIn;

    private FirebaseAuth firebaseAuth;

    private EditText emailField;
    private EditText passField;

    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_page);

        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);

        emailField = (EditText) findViewById(R.id.nameField);
        passField = (EditText) findViewById(R.id.passField);

        btnSignIn = (Button) findViewById(R.id.btnSignIn);
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateEmail(emailField.getText().toString())) {
                    emailField.setError("Email inválido!");
                    emailField.requestFocus();
                } else if (!validatePassword(passField.getText().toString())) {
                    passField.setError("Password inválida!\n" +
                            "-Mínimo 9 letras");
                    passField.requestFocus();
                } else {
                    signInUser();
                }
            }
        });

    }

    protected void signInUser() {
        String email = emailField.getText().toString().trim();
        String password = passField.getText().toString().trim();

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(SignInPage.this, "Sessão iniciada com sucesso!", Toast.LENGTH_SHORT).show();
                            finish();
                            if(Objects.equals(emailField.getText().toString(), "admin@adminest.com") && Objects.equals(passField.getText().toString(), "admincoimbra2018")) {
                                startActivity(new Intent(SignInPage.this, AdminActivity.class));
                            } else {
                                startActivity(new Intent(SignInPage.this, MainActivity.class));
                            }
                        } else {
                            Toast.makeText(SignInPage.this, "Algo deu errado! Revê os teus dados!", Toast.LENGTH_SHORT).show();
                        }
                    }
        });
    }

    protected boolean validateEmail(String email) {
        String emailPattern = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`" +
                "{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]" +
                "|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*" +
                "[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]" +
                "?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:" +
                "(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-" +
                "\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";

        Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }

    protected Boolean validatePassword(String password) {
        if (!(password.isEmpty()) && password.length() > 8) {
            return true;
        } else {
            return false;
        }
    }



}
