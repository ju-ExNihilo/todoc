package com.cleanup.julien.todoc.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.cleanup.julien.todoc.R;
import com.cleanup.julien.todoc.factory.ViewModelFactory;
import com.cleanup.julien.todoc.injection.Injection;
import com.cleanup.julien.todoc.model.Project;
import com.cleanup.julien.todoc.model.Task;
import com.cleanup.julien.todoc.model.TaskWithProject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>Home activity of the application which is displayed when the user opens the app.</p>
 * <p>Displays the list of tasks.</p>
 *
 * @author Gaëtan HERFRAY
 */
public class MainActivity extends AppCompatActivity implements com.cleanup.julien.todoc.ui.TasksAdapter.DeleteTaskListener {

    /**
     * View Model
     */
    private MySaveTaskViewModel mySaveTaskViewModel;
    /**
     * List of all projects available in the application
     */
    private List<Project> allProjects = new ArrayList<>();

    /**
     * Dialog to create a new task
     */
    @Nullable
    public AlertDialog dialog = null;

    /**
     * EditText that allows user to set the name of a task
     */
    @Nullable
    private EditText dialogEditText = null;

    /**
     * Spinner that allows the user to associate a project to a task
     */
    @Nullable
    private Spinner dialogSpinner = null;

    /**
     * The RecyclerView which displays the list of tasks
     */
    // Suppress warning is safe because variable is initialized in onCreate
    @SuppressWarnings("NullableProblems")
    @NonNull
    private RecyclerView listTasks;

    /**
     * The TextView displaying the empty state
     */
    // Suppress warning is safe because variable is initialized in onCreate
    @SuppressWarnings("NullableProblems")
    @NonNull
    private TextView lblNoTasks;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.configureViewModel();
        this.getAllMyProject();
        this.getAllMyTask();
        listTasks = findViewById(R.id.list_tasks);
        lblNoTasks = findViewById(R.id.lbl_no_task);
        listTasks.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        findViewById(R.id.fab_add_task).setOnClickListener(view -> showAddTaskDialog());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.filter_alphabetical:
                MySaveTaskViewModel.SORT_METHOD = SortMethod.ALPHABETICAL;
                getAllMyTask();
                break;
            case R.id.filter_alphabetical_inverted:
                MySaveTaskViewModel.SORT_METHOD = SortMethod.ALPHABETICAL_INVERTED;
                getAllMyTask();
                break;
            case R.id.filter_oldest_first:
                MySaveTaskViewModel.SORT_METHOD = SortMethod.OLD_FIRST;
                getAllMyTask();
                break;
            case R.id.filter_recent_first:
                MySaveTaskViewModel.SORT_METHOD = SortMethod.RECENT_FIRST;
                getAllMyTask();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void onDeleteTask(TaskWithProject task) {
        mySaveTaskViewModel.deleteTask(task.getIdTask());
        getAllMyTask();
    }

    /** Configuring ViewModel **/
    private void configureViewModel(){
        ViewModelFactory mViewModelFactory = Injection.provideDaoViewModelFactory(this);
        this.mySaveTaskViewModel = new ViewModelProvider(this, mViewModelFactory).get(MySaveTaskViewModel.class);
        this.mySaveTaskViewModel.init(MySaveTaskViewModel.SORT_METHOD);
    }

    /**
     * Called when the user clicks on the positive button of the Create Task Dialog.
     *
     * @param dialogInterface the current displayed dialog
     */
    private void onPositiveButtonClick(DialogInterface dialogInterface) {
        // If dialog is open
        if (dialogEditText != null && dialogSpinner != null) {
            // Get the name of the task
            String taskName = dialogEditText.getText().toString();

            // Get the selected project to be associated to the task
            Project taskProject = null;
            if (dialogSpinner.getSelectedItem() instanceof Project) {
                taskProject = (Project) dialogSpinner.getSelectedItem();
            }

            // If a name has not been set
            if (taskName.trim().isEmpty()) {
                dialogEditText.setError(getString(R.string.empty_task_name));
            }
            // If both project and name of the task have been set
            else if (taskProject != null) {
                Task task = new Task(
                        taskProject.getId(),
                        taskName,
                        new Date().getTime()
                );

                addTask(task);

                dialogInterface.dismiss();
            }
            // If name has been set, but project has not been set (this should never occur)
            else{
                dialogInterface.dismiss();
            }
        }
        // If dialog is already closed
        else {
            dialogInterface.dismiss();
        }
    }

    /**
     * Shows the Dialog for adding a Task
     */
    private void showAddTaskDialog() {
        final AlertDialog dialog = getAddTaskDialog();
        dialog.show();
        dialogEditText = dialog.findViewById(R.id.txt_task_name);
        dialogSpinner = dialog.findViewById(R.id.project_spinner);
        populateDialogSpinner();
    }

    /**
     * Adds the given task to the list of created tasks.
     *
     * @param task the task to be added to the list
     */
    private void addTask(@NonNull Task task) {
        mySaveTaskViewModel.insertTask(task);
        getAllMyTask();
}

    /**
     * Updates the list of tasks in the UI
     */
    private void updateTasks(List<TaskWithProject> tasks) {
        if (tasks.size() == 0) {
            lblNoTasks.setVisibility(View.VISIBLE);
            listTasks.setVisibility(View.GONE);
        } else {
            lblNoTasks.setVisibility(View.GONE);
            listTasks.setVisibility(View.VISIBLE);
            listTasks.setAdapter(new TasksAdapter(tasks,this));
        }
    }

    /**
     * Returns the dialog allowing the user to create a new task.
     *
     * @return the dialog allowing the user to create a new task
     */
    @NonNull
    private AlertDialog getAddTaskDialog() {
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this, R.style.Dialog);

        alertBuilder.setTitle(R.string.add_task);
        alertBuilder.setView(R.layout.dialog_add_task);
        alertBuilder.setPositiveButton(R.string.add, null);
        alertBuilder.setOnDismissListener(dialogInterface -> {
            dialogEditText = null;
            dialogSpinner = null;
            dialog = null;
        });

        dialog = alertBuilder.create();

        // This instead of listener to positive button in order to avoid automatic dismiss
        dialog.setOnShowListener(dialogInterface -> {

            Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            button.setOnClickListener(view -> onPositiveButtonClick(dialog));
        });

        return dialog;
    }
    /** Get project from data base **/
    public void getAllMyProject(){
        this.mySaveTaskViewModel.getAllProject().observe(this, this::allMyProject);
    }
    public void allMyProject(List<Project> projects){
        this.allProjects = projects;
    }
    /*******************************/

    /** Get task from data base **/
    public void getAllMyTask(){
        switch (MySaveTaskViewModel.SORT_METHOD) {
            case ALPHABETICAL:
                this.mySaveTaskViewModel.getTaskByAZ().observe(this, this::updateTasks);
                break;
            case ALPHABETICAL_INVERTED:
                this.mySaveTaskViewModel.getTaskByZA().observe(this, this::updateTasks);
                break;
            case RECENT_FIRST:
                this.mySaveTaskViewModel.getTaskByNewer().observe(this, this::updateTasks);
                break;
            case OLD_FIRST:
                this.mySaveTaskViewModel.getTaskByOlder().observe(this, this::updateTasks);
                break;
            case NONE:
                this.mySaveTaskViewModel.getAllTask().observe(this, this::updateTasks);
                break;
        }
    }

    /*******************************/

    /**
     * Sets the data of the Spinner with projects to associate to a new task
     */
    private void populateDialogSpinner() {
        final ArrayAdapter<Project> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, allProjects);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        if (dialogSpinner != null) {
            dialogSpinner.setAdapter(adapter);
        }
    }

    /**
     * List of all possible sort methods for task
     */
    public enum SortMethod {
        /**
         * Sort alphabetical by name
         */
        ALPHABETICAL,
        /**
         * Inverted sort alphabetical by name
         */
        ALPHABETICAL_INVERTED,
        /**
         * Lastly created first
         */
        RECENT_FIRST,
        /**
         * First created first
         */
        OLD_FIRST,
        /**
         * No sort
         */
        NONE
    }

}
