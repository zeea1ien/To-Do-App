package com.example.myapplication.Controllers;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.myapplication.HomeFragment;
import com.example.myapplication.Model.TodoItem;
import com.example.myapplication.R;
import com.example.myapplication.SettingFragment;
import com.example.myapplication.Toolbox.DataBaseManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;
import java.util.concurrent.ExecutionException;

public class ToDoListActivity extends AppCompatActivity   implements BottomNavigationView
        .OnNavigationItemSelectedListener {



    BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);
        bottomNavigationView
                = findViewById(R.id.bottomNavigationView);

        bottomNavigationView
                .setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.home);



    }

   HomeFragment homeFragment = new HomeFragment();
    SettingFragment settingFragment = new SettingFragment();



    @Override
    public boolean
    onNavigationItemSelected(@NonNull MenuItem item)
    {
        int itemId = item.getItemId();
      if(itemId == R.id.home) {
          getSupportFragmentManager()
                  .beginTransaction()
                  .replace(R.id.fragment, homeFragment)
                  .commit();
          return true;
      }

      else if(itemId == R.id.setting) {

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment, settingFragment)
                        .commit();
                return true;


        }
        return false;
    }



}
