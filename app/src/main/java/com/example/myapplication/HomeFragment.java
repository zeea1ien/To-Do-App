package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Controllers.ToDoListActivity;
import com.example.myapplication.Model.TodoItem;
import com.example.myapplication.Toolbox.DataBaseManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment  {
//home fragment representing the home screen of my app, this is where all my tasks will be listed
//manager for the database
    DataBaseManager manager;
    //UI elements on the home screen
    ImageButton calendarButton;
    ListView listView;
    CustomAdapter customAdapter;
    EditText addTask;
    int currentBiggerId = 0;
    String selectedDate;

    Button addTaskButton;
//ViewModel for managing the UI-related data stuff
    private FontSizeViewModel viewModel;
//progress bar
    ProgressBar progressBar;
// code is unused, but not deleted incase i want to come back and use this.
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        manager = new DataBaseManager(getContext());
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        setProgressBar();
        calendarButton = (ImageButton)view.findViewById(R.id.calendarButton);
        addTask = (EditText) view.findViewById(R.id.editTextAddTask);
        listView = (ListView) view.findViewById(R.id.listTodo);
        addTaskButton = (Button)view.findViewById(R.id.addTaskButton);
        viewModel = new ViewModelProvider(requireActivity()).get(FontSizeViewModel.class);

//set up button listeners
        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTaskEvent(v);
            }
        });
        calendarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCalendar();
            }
        });
//data load from database
        try {
            printDB(true, true);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        return view;
    }

//opens the date picker so you can add a new task and select a date
    private void openCalendar() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getActivity(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                        Toast.makeText(getActivity(), "Selected Date: " + selectedDate, Toast.LENGTH_SHORT).show();
                    }
                },
                year, month, day);

        datePickerDialog.show();
    }
// adds task from the UI, validates input before adding it to the database
    public void addTaskEvent(View v) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                String taskTitle =  addTask.getText().toString();

                if(taskTitle.isEmpty()) {
                    showAlert("Enter Title", "Please enter Task Title", R.drawable.close);
                    return;
                }

                else if(selectedDate == null || selectedDate.isEmpty()) {
                    showAlert("Select Date", "Please Select Task Date", R.drawable.close);
                    return;
                }


                manager.addTask( false,taskTitle, selectedDate);
                try {
                    printDB(true, true);
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        runnable.run();
    }
//updates the list view from the database
    public void printDB(boolean updateFromServer, boolean updateFromDb) throws ExecutionException, InterruptedException {
        if (updateFromDb) {
            customAdapter = new CustomAdapter(manager.readFromDB(updateFromServer));
            listView.setAdapter(customAdapter);
        }
        customAdapter.notifyDataSetChanged();
        currentBiggerId = manager.getCurrentBiggestId();
        addTask.setText("");
        selectedDate = null;

    }

// displaying tasks in list view
    // created space between the code, so i can read it better to spot my errors
    public class CustomAdapter extends BaseAdapter {
        ArrayList<TodoItem> todoList;

        CustomAdapter(ArrayList<TodoItem> todoList) {
            this.todoList = todoList;
        }

        @Override
        public int getCount() {
            return todoList.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            view = getLayoutInflater().inflate(R.layout.task_todo_item, null);

            final TextView title = view.findViewById(R.id.todoName);
            final TextView date = view.findViewById(R.id.todoDate);
            Log.d("VAL", "getView: ");

            viewModel.getFontSize().observe(getViewLifecycleOwner(), new Observer<Integer>() {
                @Override
                public void onChanged(Integer size) {
                    title.setTextSize(size);
                    date.setTextSize(size);
                }
            });



            title.setText(todoList.get(i).getTaskName());
            date.setText(todoList.get(i).getTaskDate());
            CheckBox button = view.findViewById(R.id.checkButton);
            ImageButton deleteTask = view.findViewById(R.id.deleteTask);
            button.setChecked(todoList.get(i).isStatus());


            deleteTask.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    manager.removeTask(todoList.get(i).getId());
                    setProgressBar();
                    try {
                        printDB(true, true);
                    } catch (ExecutionException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });

            int[][] states = new int[][]{
                    new int[]{-android.R.attr.state_checked}, //disabled
                    new int[]{android.R.attr.state_checked} //enabled
            };
            @SuppressLint("ResourceType") int[] colorBlue = new int[]{Color.parseColor(getString(R.color.colorBlue)),
                    Color.parseColor(getString(R.color.colorBlue))};


            RelativeLayout layout = view.findViewById(R.id.itemBackground);

            final int taskId = todoList.get(i).getId();
            final String taskDate = todoList.get(i).getTaskDate();
            final String taskName = todoList.get(i).getTaskName();
            layout.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.list_item_background_blue));
            button.setButtonTintList(new ColorStateList(states, colorBlue));
            button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    itemChecked(b, taskId, taskName, taskDate);
                    setProgressBar();

                }
            });

            return view;
        }

        void setProgressBar() {
            int completedTasks = manager.getCompletedTasksCount();
            int totalTasks = manager.getTotalTasksCount();
            float completionPercentage = ((float) completedTasks / totalTasks) * 100;
            int value = (int) completionPercentage;
            progressBar.setProgress(value);
        }

        void itemChecked(final boolean taskStatus, final int taskId, final String taskName, final String taskDate) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    manager.updateTask(taskStatus, taskId, taskName, taskDate);
                }
            };
            runnable.run();
        }

    }
    public void showAlert(String title, String message, int icon) {
        new AlertDialog.Builder(getActivity()).setIcon(icon).setTitle(title).setMessage(message).setNeutralButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).show();
    }
// update my progress bar based on task completion status
    void setProgressBar() {
        int completedTasks = manager.getCompletedTasksCount();
        int totalTasks = manager.getTotalTasksCount();
        float completionPercentage = ((float) completedTasks / totalTasks) * 100;
        int value = (int) completionPercentage;
        progressBar.setProgress(value);
    }
}