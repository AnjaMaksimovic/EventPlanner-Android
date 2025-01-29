package com.example.eventplanner;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.eventplanner.activities.HomeActivity;
import com.example.eventplanner.activities.RegistrationActivity;
import com.example.eventplanner.databinding.FragmentLoginBinding;
import com.example.eventplanner.fragments.FragmentTransition;
import com.example.eventplanner.fragments.HomePageFragment;
import com.example.eventplanner.fragments.SettingsFragment;
import com.example.eventplanner.fragments.registration.OrganizerRegistration;

import com.example.eventplanner.model.User;
import com.example.eventplanner.model.enums.Role;
import com.example.eventplanner.settings.PreferencesManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginFragment extends Fragment {

    private NavController navController;
    private FragmentLoginBinding binding;
    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inicijalizacija NavController-a
        navController = Navigation.findNavController(requireActivity(), R.id.fragment_nav_content_main);

        // Provera da li postoji prijavljeni korisnik kada se otvori fragment za prijavu
        if (PreferencesManager.isLoggedIn(getContext())) {
            Log.i("logovan", "postoji logovan" + PreferencesManager.isLoggedIn(getContext()));
            // Ako postoji, obrišite trenutno prijavljenog korisnika
            PreferencesManager.clearLoggedUser(getContext());
            Log.i("logovan", "postoji logovan" + PreferencesManager.isLoggedIn(getContext()));
        }

        binding = FragmentLoginBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireActivity(), RegistrationActivity.class);
                startActivity(intent);
            }
        });

        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.username.getText().toString().trim();
                String password = binding.password.getText().toString().trim();
                Log.d("Login_msg", "Login credentials: " + email + ", " + password);
                loginInFirebase(email, password, view);
            }
        });

        return view;
    }

    private void adjustMenuVisibility(Role userRole) {
        NavigationView navigationView = requireActivity().findViewById(R.id.nav_view);
        Menu menu = navigationView.getMenu();
        MenuItem registrationRequestItem = menu.findItem(R.id.pupv_requests);
        MenuItem categoriesItem = menu.findItem(R.id.nav_categories);
        MenuItem subcategoriesItem = menu.findItem(R.id.nav_subcategories);
        MenuItem packageRes = menu.findItem(R.id.nav_package_res);

        if (userRole == Role.admin) {
            registrationRequestItem.setVisible(true);
            categoriesItem.setVisible(true);
            subcategoriesItem.setVisible(true);
            packageRes.setVisible(false);
        } else if (userRole == Role.owner){
            registrationRequestItem.setVisible(false);
            categoriesItem.setVisible(false);
            subcategoriesItem.setVisible(false);
            packageRes.setVisible(false);
        } else if (userRole == Role.employee){
            registrationRequestItem.setVisible(false);
            categoriesItem.setVisible(false);
            subcategoriesItem.setVisible(false);
            packageRes.setVisible(false);
        } else if (userRole == Role.organizator){
            registrationRequestItem.setVisible(false);
            categoriesItem.setVisible(false);
            subcategoriesItem.setVisible(false);
            packageRes.setVisible(true);
        } else {
            registrationRequestItem.setVisible(false);
            categoriesItem.setVisible(false);
            subcategoriesItem.setVisible(false);
            packageRes.setVisible(false);
        }
    }

    void loginInFirebase(String email, String password, View view) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        Log.d("Login_msg", "Login started: " + email + ", " + password);
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    Log.d("Login_msg", "Login completed");
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                        if (firebaseUser.isEmailVerified()) {
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            db.collection("users")
                                    .whereEqualTo("email", firebaseUser.getEmail())
                                    .get()
                                    .addOnSuccessListener(documentSnapshot -> {
                                        Log.d("Login_msg", "Found user");
                                        for(DocumentSnapshot document : documentSnapshot.getDocuments()){
                                            User user = document.toObject(User.class);

                                            if(user.isBlocked()){
                                                Toast.makeText(LoginFragment.this.getContext(), "Your account is blocked.", Toast.LENGTH_SHORT).show();
                                                return;
                                            } else {

                                                PreferencesManager.setLoggedUser(getContext(), user.getEmail(), user.getRole().toString(), user.getId());
                                                Toast.makeText(LoginFragment.this.getContext(), "Login successful", Toast.LENGTH_SHORT).show();

                                                // Prilagodite vidljivost stavke menija na osnovu korisničke uloge nakon prijave korisnika
                                                adjustMenuVisibility(user.getRole());

                                                navController.navigate(R.id.nav_home_page);
                                                return;
                                            }
                                        }
                                    })
                                    .addOnFailureListener(e -> {
                                        Log.e("LogInFragment", "Error getting user document", e);
                                        Toast.makeText(requireContext(), "Error getting user document", Toast.LENGTH_SHORT).show();
                                    });
                        } else {
                            Toast.makeText(LoginFragment.this.getContext(), "Email not verified", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(LoginFragment.this.getContext(), task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        );
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireActivity(), RegistrationActivity.class);
                startActivity(intent);
            }
        });
        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.username.getText().toString().trim();
                String password = binding.password.getText().toString().trim();
                Log.d("Login_msg", "Login credentials: " + email + ", " + password);
                loginInFirebase(email, password, view);
            }
        });
    }

    private void closeFragment(){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

        if(fragmentManager.getBackStackEntryCount() > 0){
            fragmentManager.popBackStack(); return;
        }
        fragmentManager.beginTransaction().remove(this).commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}