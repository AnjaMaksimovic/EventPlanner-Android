package com.example.eventplanner.activities.events;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.eventplanner.R;
import com.example.eventplanner.databinding.ActivityEventTypeCreationBinding;

public class EventTypeCreationActivity extends AppCompatActivity {

    ActivityEventTypeCreationBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEventTypeCreationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Button btnSubmit = findViewById(R.id.submit_btn);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //FragmentTransition.to(EventType.newInstance(), EventTypeCreationActivity.this, false, R.id.downView);
            }
        });

    }

}