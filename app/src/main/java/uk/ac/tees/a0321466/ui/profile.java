package uk.ac.tees.a0321466.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hbb20.CountryCodePicker;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.squareup.picasso.Picasso;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;
import uk.ac.tees.a0321466.R;
import uk.ac.tees.a0321466.SignUpActivity;
import uk.ac.tees.a0321466.javaClass.MySharedPref22;

import static android.app.Activity.RESULT_OK;
import static com.github.dhaval2404.imagepicker.ImagePicker.*;
import static uk.ac.tees.a0321466.javaClass.MySharedPref22.IMAGE;


public class profile extends Fragment implements DatePickerDialog.OnDateSetListener {
    EditText et_firstName, et_lastName, et_mobile;
    String firstName, lastName,_email,_dateOfBirth,mobile;
    TextView tv_dob, et_email;
    CircleImageView userImage;
    CountryCodePicker countryCodePicker;
    DatePickerDialog datePickerDialog;
    String imageFile1 = "";
    FirebaseAuth fAuth;
    FirebaseFirestore fStore; // store firebase data
    StorageReference storageReference;
    String userId = "";
    MySharedPref22 mySharedPref22;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
       storageReference = FirebaseStorage.getInstance().getReference();
        userImage = view.findViewById(R.id.img_roundImage);
        et_firstName = view.findViewById(R.id.edit_firstname);
        et_lastName = view.findViewById(R.id.edit_lastname);
        et_email = view.findViewById(R.id.edit_email);
        tv_dob = view.findViewById(R.id.edit_date_birth);
        et_mobile = view.findViewById(R.id.edit_mobile);
        countryCodePicker = view.findViewById(R.id.ccp_profile_info);
        mySharedPref22 = MySharedPref22.getInstance(getActivity());
        startDatePicker();
        initListeners();

        /* save button press */
        view.findViewById(R.id.btn_save12).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                firstName = et_firstName.getText().toString().trim();
                lastName = et_lastName.getText().toString().trim();
                _email = et_email.getText().toString().trim();
                _dateOfBirth = tv_dob.getText().toString().trim();
                mobile = et_mobile.getText().toString().trim();
             //   Toast.makeText(getActivity(),"profile ..",Toast.LENGTH_LONG).show();
                confirmSave();


            }
        });


        tv_dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.show(getFragmentManager(), "Datepickerdialog");
            }
        });

       /* read the cloud store image using storage reference
       do some modifications here in the name of profile name because i want to store images for every user
         */
        String imagelink = "users/"+ fAuth.getCurrentUser().getUid() + "/profile.jpg";
        StorageReference fileRef= storageReference.child(imagelink);
        fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(userImage);
            }
        });
     ///////////////////////////////////////////////////////////////////////////
        
        readFireBaseFireStore(); //read firebase store data and update to edit text fields
        return view;
    }


    private void startDatePicker() {
        Calendar now = Calendar.getInstance();
        datePickerDialog = DatePickerDialog.newInstance(
                profile.this,
                now.get(Calendar.YEAR), // Initial year selection
                now.get(Calendar.MONTH), // Initial month selection
                now.get(Calendar.DAY_OF_MONTH) // Inital day selection
        );
    }


    private void initListeners() {
        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Companion.with(profile.this)
                        .cropSquare()
                        .compress(300)
                        .maxResultSize(200, 200).start();
            }
        });

    }



/* store data to firebase firestore */
    private void storeUserData(){
        userId = fAuth.getCurrentUser().getUid();
        DocumentReference db = fStore.collection("users")
                .document(userId);
        Map<String, Object> user = new HashMap<>();
        user.put("firstName", firstName);
        user.put("lastName", lastName);
        //user.put("email", _email);  //email should not change
        user.put("DOB", _dateOfBirth);
        user.put("mobile", mobile);


        db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getActivity(), "Successfully data store on the firebase", Toast.LENGTH_SHORT).show();
                        boolean isSuccess = true;
                      // Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    boolean isSuccess =false;
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
                        // Log.w(TAG, "Error adding document", e);
                    }
                });

    }






    /* read firebase data */

    public void readFireBaseFireStore() {
        //userTypeBtn // means which button is pressed
        String userId= fAuth.getCurrentUser().getUid();
        DocumentReference db = fStore.collection("users")
                .document(userId);

        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String first_name = document.getString("firstName");
                                String last_name = document.getString("lastName");
                                String email = document.getString("email");
                                String dob = document.getString("DOB");
                                String _mobile = document.getString("mobile");
                                et_firstName.setText(first_name);
                                et_lastName.setText(last_name);
                                et_email.setText(email);
                                tv_dob.setText(dob);
                                et_mobile.setText(_mobile);

//                                String imagePath = mySharedPref22.getValue(IMAGE);
//
//
//                                if (imagePath != null && !imagePath.isEmpty()) {
//                                    userImage.setImageURI(Uri.fromFile(new File(imagePath)));
//                                }

                            }
                        }else {
                            Toast.makeText(getActivity(), "error. .. ", Toast.LENGTH_SHORT).show();
                        }
                        // Log.w(TAG, "Error getting documents.", task.getException());

                    }
                });
    }





    /* ask for confirmation to Save */
    private void confirmSave() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Are you sure you want to update user details?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                       storeUserData(); //write data to firebase

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            Uri fileUri = data.getData();
            userImage.setImageURI(fileUri);
            imageFile1 = Companion.getFilePath(data);
            mySharedPref22.saveValue(imageFile1, IMAGE);
            uploadImageTofirebaseStore(fileUri);
            //            mySharedPref22.saveName((firstName + " " + lastName));
        } else if (resultCode == RESULT_ERROR) {
            Toast.makeText(getActivity(), Companion.getError(data), Toast.LENGTH_SHORT).show();
        }

    }

    private void uploadImageTofirebaseStore(Uri imageUri) {
        String imagelink = "users/"+ fAuth.getCurrentUser().getUid() + "/profile.jpg";
       StorageReference fileRef= storageReference.child(imagelink);
       fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
    @Override
    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
        Toast.makeText(getActivity(),"Image uploaded successfuly",Toast.LENGTH_SHORT).show();
    }
}).addOnFailureListener(new OnFailureListener() {
    @Override
    public void onFailure(@NonNull Exception e) {
        Toast.makeText(getActivity(),"Error to upload Image",Toast.LENGTH_SHORT).show();
    }
});

    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = dayOfMonth + "/" +(monthOfYear + 1) + "/" + year;
        tv_dob.setText(date);
    }
}