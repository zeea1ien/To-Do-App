package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;

import com.google.firebase.auth.FirebaseAuth;
//fragment for handling changes within the app e.g font size
public class SettingFragment extends Fragment {

    private FontSizeViewModel viewModel;
//ViewModel for handling UI-related data like the font size
    public static SettingFragment newInstance(String param1, String param2) {
        SettingFragment fragment = new SettingFragment();
        return fragment;
    }
//unused code may use at later date if not delete
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_setting, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(FontSizeViewModel.class);
        SeekBar seekBar = view.findViewById(R.id.fontSizeSlider);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                viewModel.setFontSize(progress);
            }
//setup SeekBar for font size Adjustment
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
//optionally handle touch even start
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
            //optionally handle touch event end
        });
        // firebase authentication instance
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
//set up logout button
        Button logoutButton = view.findViewById(R.id.buttonLogout);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                Intent intent = new Intent(getActivity(), Login.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
//signout from firebase and navigate to the login screen


        return view;


    }
}