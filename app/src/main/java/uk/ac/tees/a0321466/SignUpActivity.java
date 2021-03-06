package uk.ac.tees.a0321466;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    /* register layout variables */
    EditText et_name, et_email, et_pass;
    ProgressBar progressBar_register;
    String firstName,email,password;
    LinearLayout registerPageLayout;

    /* login layout variables */
    EditText et_email_login, et_pass_login;
    ProgressBar progressBar_login;
    String email_login,password_login;
    LinearLayout loginPageLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        /* initilize firebase authentication*/
        fAuth =FirebaseAuth.getInstance();
        fStore =FirebaseFirestore.getInstance();

        /* register layout viwe components get ids */
        et_name = findViewById(R.id.et_firstname1);
        et_email = findViewById(R.id.et_email);
        et_pass = findViewById(R.id.et_password);
        progressBar_register = findViewById(R.id.progress_bar_register);
        registerPageLayout = findViewById(R.id.register_layout);




        /* login layout viwe components get ids */
        et_email_login = findViewById(R.id.et_email_login);
        et_pass_login = findViewById(R.id.et_password_login);
        progressBar_login = findViewById(R.id.progress_bar_login);
        loginPageLayout = findViewById(R.id.login_layout);



        /* register button press listener

         */
        findViewById(R.id.btn_signUp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar_register.setVisibility(progressBar_register.VISIBLE);
                /* get data from edit text field */
                firstName= et_name.getText().toString().trim();
                email = et_email.getText().toString().trim();
                password = et_pass.getText().toString().trim();

               /* input edit text validation */
                if (firstName.isEmpty()) {
                    et_name.setError("Enter Name ..");
                    progressBar_register.setVisibility(progressBar_register.GONE);
                    return;
                }
                if (email.isEmpty() || !email.contains("@"))  {
                    et_email.setError("Enter Valid Email..");
                    progressBar_register.setVisibility(progressBar_register.GONE);
                    return;
                }
                if (password.isEmpty() || password.length() < 6) {
                    et_pass.setError("Enter password ..");
                    progressBar_register.setVisibility(progressBar_register.GONE);
                    return;
                }

                /* create account using email and password */
                fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            et_name.setText("");
                            et_email.setText("");
                            et_pass.setText("");
                            splashMsg("Successfully User Account Created..");

                            storeUserData(firstName,email);
                            /* redirect to home page when user account is created successfully

                             */
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();

                        } else {
                            splashMsg("Error to create user account");
                        }
                        progressBar_register.setVisibility(progressBar_register.GONE);
                    }
                });
            }
        });

        findViewById(R.id.btn_openLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginPageLayout.setVisibility(loginPageLayout.VISIBLE);
                registerPageLayout.setVisibility(registerPageLayout.GONE);
            }
        });

        findViewById(R.id.btn_openRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginPageLayout.setVisibility(loginPageLayout.GONE); //invisible
                registerPageLayout.setVisibility(registerPageLayout.VISIBLE); //visible
            }
        });


        /* user account login  event listener button */
      findViewById(R.id.btn_signIn).setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              progressBar_login.setVisibility(progressBar_login.VISIBLE);
              /* get data from edit text field */
              email_login = et_email_login.getText().toString().trim();
              password_login = et_pass_login.getText().toString().trim();
              /* input edit text validation */
              if (email_login.isEmpty() || !email_login.contains("@")) {
                  et_email_login.setError("Enter Valid Email");
                  progressBar_login.setVisibility(progressBar_register.GONE);
                  return;
              }
              if (password_login.isEmpty() || password_login.length() < 6) {
                  et_pass_login.setError("Enter Valid Password");
                  progressBar_login.setVisibility(progressBar_register.GONE);
                  return;
              }
              //start login process using firebase function ..
              fAuth.signInWithEmailAndPassword(email_login,password_login).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                  @Override
                  public void onComplete(@NonNull Task<AuthResult> task) {
                      if(task.isSuccessful()){
                          //reset edit text fields..
                          et_email_login.setText("");
                          et_pass_login.setText("");
                          splashMsg("Successfully login ..");
                          /* redirect to home page when user account is created successfully

                           */
                          startActivity(new Intent(getApplicationContext(), MainActivity.class));
                          finish();

                      }
                      else{
                         splashMsg("wrong username or password");
                      }
                      progressBar_login.setVisibility(progressBar_login.GONE);
                  }
              });

          }
      });



        /* check user is login or not  basically check when app start*/
        if(fAuth.getCurrentUser() !=null){
            //it means user already login so redirect directly to main activity
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }
        else{
            //open register page //
            loginPageLayout.setVisibility(loginPageLayout.GONE); //invisible
            registerPageLayout.setVisibility(registerPageLayout.VISIBLE); //visible
        }
    }



    /* store data to firebase store */

    /* store data to firebase firestore */
    private void storeUserData(String fName, String fEmail){
       String userId = fAuth.getCurrentUser().getUid();
        DocumentReference db = fStore.collection("users")
                .document(userId);
        Map<String, Object> user = new HashMap<>();
        user.put("firstName", fName);
        user.put("lastName", "");
        user.put("email", fEmail);
        user.put("DOB", "");
        user.put("mobile", "");

        db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getApplicationContext(), "Successfully data store on the firebase", Toast.LENGTH_SHORT).show();
                        boolean isSuccess = true;
                        // Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    boolean isSuccess =false;
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                        // Log.w(TAG, "Error adding document", e);
                    }
                });

    }




            public void splashMsg(String msg) {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            }
}
