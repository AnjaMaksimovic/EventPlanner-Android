package com.example.eventplanner.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.eventplanner.R;
import com.example.eventplanner.fragments.FragmentTransition;
import com.example.eventplanner.fragments.registration.OrganizerRegistration;
import com.example.eventplanner.fragments.registration.OwnerRegistration;

public class RegistrationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registration);
        //ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
          //  Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            //v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            //return insets;
        //});

        Button btnRegisterOrganizer = findViewById(R.id.btnODRegister);
        btnRegisterOrganizer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransition.to(OrganizerRegistration.newInstance(), RegistrationActivity.this, false, R.id.downView);
            }
        });

        Button btnRegisterOwner = findViewById(R.id.btnPUPVRegister);
        btnRegisterOwner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransition.to(OwnerRegistration.newInstance(), RegistrationActivity.this, false, R.id.downView);
            }
        });

    }
}