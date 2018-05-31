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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginPage extends AppCompatActivity {

    private Button btnGoToSignIn;
    private Button btnRegister;

    private FirebaseAuth firebaseAuth;

    private EditText emailField;
    private EditText passField;
    private EditText passFieldConf;
    private EditText Name;
    private EditText Curso;
    private EditText Ano;

    String name, curso, ano;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        firebaseAuth = FirebaseAuth.getInstance();

         if(firebaseAuth.getCurrentUser() != null && !(Objects.equals(firebaseAuth.getCurrentUser().getUid(),"OgMKvtx7pcet4Z4HLQzJqFlxchA3"))) {
         startActivity(new Intent(LoginPage.this, MainActivity.class));
         finish();
         } else if (firebaseAuth.getCurrentUser() != null && (Objects.equals(firebaseAuth.getCurrentUser().getUid(),"OgMKvtx7pcet4Z4HLQzJqFlxchA3"))){
         startActivity(new Intent(LoginPage.this, AdminActivity.class));
             finish();
         }



        emailField = (EditText) findViewById(R.id.emailField);
        passField = (EditText) findViewById(R.id.passField);
        passFieldConf = (EditText) findViewById(R.id.passFieldConf);
        Name = (EditText) findViewById(R.id.nameField);
        Curso = (EditText) findViewById(R.id.cursoField);
        Ano = (EditText) findViewById(R.id.anoField);

        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateName()) {
                    Name.setError("Nome inválido!");
                } else if (!validateCurso()) {
                    Curso.setError("Deve inserir o curso que frequenta!");
                } else if (!validateEmail(emailField.getText().toString())) {
                    emailField.setError("Email inválido!");
                    emailField.requestFocus();
                } else if (!validatePassword(passField.getText().toString())) {
                    passField.setError("Password Inválida!\n" +
                            "-Mínimo 9 letras");
                    passField.requestFocus();
                } else if(!validateAno()) {
                    Ano.setError("Deve inserir o seu ano de escolaridade!");
                } else {
                    registerUser();
                }
            }
        });

        btnGoToSignIn = (Button) findViewById(R.id.btnGoToSignIn);
        btnGoToSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginPage.this, SignInPage.class);
                startActivity(intent);
            }
        });

    }

    protected void registerUser() {

        String email = emailField.getText().toString().trim();
        String password = passField.getText().toString().trim();

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            addUsersToDatabase();
                            startActivity(new Intent(LoginPage.this, MainActivity.class));
                            Toast.makeText(LoginPage.this, "Foi registado com sucesso!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(LoginPage.this, "Já tens uma conta! Tenta iniciar sessão.", Toast.LENGTH_SHORT).show();
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

    protected Boolean validatePassword(String password){
        if(!(password.isEmpty()) && password.length() > 8) {
            return true;
        } else {
            return false;
        }
    }

    protected Boolean validateName(){

        name = Name.getText().toString();

        if(!(name.isEmpty()) && name.length() > 2) {
            return true;
        } else {
            return false;
        }
    }

    protected Boolean validateCurso(){

        curso = Curso.getText().toString();

        if(!(curso.isEmpty())) {
            return true;
        } else {
            return false;
        }
    }

    protected Boolean validateAno(){

        ano = Ano.getText().toString();

        if(!(ano.isEmpty())) {
            return true;
        } else {
            return false;
        }
    }


    protected Boolean validatePasswords(String pass1, String pass2) {
        if(Objects.equals(pass1, pass2)) {
            return true;
        } else {
            return false;
        }
    }

    private void addUsersToDatabase() {

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = firebaseDatabase.getReference(firebaseAuth.getUid());
        String id = myRef.push().getKey();
        Users userProfile = new Users(name, curso, ano, id);
        myRef.setValue(userProfile);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(name).build();

        user.updateProfile(profileUpdates);
    }

}
