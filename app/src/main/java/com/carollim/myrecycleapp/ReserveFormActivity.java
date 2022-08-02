package com.carollim.myrecycleapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ReserveFormActivity extends AppCompatActivity {

    TextInputLayout addr, date, time;
    Spinner cat;
    String  addrS, catS, dateS, timeS, uid;
    Button buttonSubmitForm;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve_form);

        addr = findViewById(R.id.formAddress);
        cat = findViewById(R.id.spinnerCategory);
        date = findViewById(R.id.formDate);
        time = findViewById(R.id.formTime);
        buttonSubmitForm = findViewById(R.id.buttonSubmitForm);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        addrS = extras.getString("address");

        addr.getEditText().setText(addrS);

        Spinner spinner = (Spinner) findViewById(R.id.spinnerCategory);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.category_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        fAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = fAuth.getCurrentUser();
        uid = firebaseUser.getUid();

        buttonSubmitForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateAddr()) {
                    return;
                } else if (!validateCat()) {
                    return;
                } else if (!validateDate()) {
                    return;
                } else if (!validateTime()) {
                    return;
                } else {
                    addrS = addr.getEditText().getText().toString();
                    catS = cat.getSelectedItem().toString();
                    dateS = date.getEditText().getText().toString();
                    timeS = time.getEditText().getText().toString();

                    Reservation reservation = new Reservation(addrS, catS, dateS, timeS, "Submitted", uid, "-");
                    FirebaseDatabase database = FirebaseDatabase.getInstance(getResources().getString(R.string.db));
                    DatabaseReference refReserve = database.getReference("reservation");

                    refReserve.push().setValue(reservation).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {

                               Toast.makeText(ReserveFormActivity.this, "Submitted successfully.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ReserveFormActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();

                            } else {
                                try {
                                    throw task.getException();
                                } catch (Exception e) {
                                    Toast.makeText(ReserveFormActivity.this, "Submit Error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    });


                }
            }

            private Boolean validateAddr() {
                String val = addr.getEditText().getText().toString();

                if (val.isEmpty()) {
                    addr.setError("Field cannot be empty");
                    addr.requestFocus();
                    return false;
                } else {
                    addr.setError(null);
                    return true;
                }
            }

            private Boolean validateCat() {
                String val = cat.getSelectedItem().toString();

                if (val.equals("Select Category")) {
                    Toast.makeText(ReserveFormActivity.this, "Please choose a category", Toast.LENGTH_LONG).show();
                    cat.requestFocus();
                    return false;
                } else {
                    return true;
                }
            }

            private Boolean validateDate() {
                String val = date.getEditText().getText().toString();
                String ptr = "^([0-2][0-9]|(3)[0-1])(\\/)(((0)[0-9])|((1)[0-2]))(\\/)\\d{4}$";
                if (val.isEmpty()) {
                    date.setError("Field cannot be empty");
                    date.requestFocus();
                    return false;
                } else if (!val.matches(ptr)) {
                    date.setError("Date format must be dd/mm/yyyy");
                    date.requestFocus();
                    return false;
                } else {
                    date.setError(null);
                    return true;
                }
            }

            private Boolean validateTime() {
                String val = time.getEditText().getText().toString();
                String ptr = "^(0?[1-9]|1[0-2]):([0-5]\\d)\\s?((?:A|P)\\.?M\\.?)$";

                if (val.isEmpty()) {
                    time.setError("Field cannot be empty");
                    time.requestFocus();
                    return false;
                } else if (!val.matches(ptr)) {
                    time.setError("Time format must be hh:mm AM or PM");
                    time.requestFocus();
                    return false;
                } else {
                    time.setError(null);
                    return true;
                }
            }
        });
    }
}