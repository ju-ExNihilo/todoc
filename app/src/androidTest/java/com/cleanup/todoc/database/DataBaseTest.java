package com.cleanup.todoc.database;

import android.content.Context;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;
import com.cleanup.todoc.model.TaskWithProject;
import com.cleanup.todoc.repository.ProjectDataRepository;
import com.cleanup.todoc.repository.TaskDataRepository;
import com.cleanup.todoc.ui.MySaveTaskViewModel;
import com.cleanup.utils.LiveDataTestUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class DataBaseTest {

    // FOR DATA
    private TodocDatabase database;
    private TaskDao taskDao;
    private ProjectDao projectDao;
    private MySaveTaskViewModel mySaveTaskViewModel;
    private ProjectDataRepository projectDataRepository;
    private TaskDataRepository taskDataRepository;
    private Executor executor;

    // DATA SET FOR TEST
    private static long PROJECT_ID1 = 1;
    private static long PROJECT_ID2 = 2;
    private static long PROJECT_ID3 = 3;
    private static String PROJECT_NAME1 = "Projet Tartampion";
    private static String PROJECT_NAME2 = "Projet Lucidia";
    private static String PROJECT_NAME3 = "Projet Circus";
    private static Task NEW_TASK_AVION_PROJECT1 = new Task( 1, "avion", 123);
    private static Task NEW_TASK_BATEAU_PROJECT1 = new Task( 1, "bateau", 124);
    private static Task NEW_TASK_CAMION_PROJECT1 = new Task( 1, "camion", 125);
    private static Task NEW_TASK_BATEAU_PROJECT2 = new Task( 2, "bateau", 124);
    private static Task NEW_TASK_CAMION_PROJECT3 = new Task( 3, "camion", 125);


    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void initDb() {
        Context context = ApplicationProvider.getApplicationContext();
        this.database = Room.inMemoryDatabaseBuilder(context,
                TodocDatabase.class)
                .allowMainThreadQueries()
                .addCallback(TodocDatabase.prepopulateDatabase())
                .build();
        taskDao = database.taskDao();
        projectDao = database.projectDao();
        projectDataRepository = new ProjectDataRepository(projectDao);
        taskDataRepository = new TaskDataRepository(taskDao);
        executor = Executors.newSingleThreadExecutor();
        mySaveTaskViewModel = new MySaveTaskViewModel(taskDataRepository,projectDataRepository,executor);
    }

    @After
    public void closeDb() {
        database.close();
    }

    @Test
    public void getAllProject() throws InterruptedException {

        List<Project> projects = LiveDataTestUtil.getValue(mySaveTaskViewModel.getAllProject());
        /** TEST **/
        assertEquals(PROJECT_NAME1, projects.get(0).getNameProject());
        assertEquals(PROJECT_NAME2, projects.get(1).getNameProject());
        assertEquals(PROJECT_NAME3, projects.get(2).getNameProject());
    }

    @Test
    public void getProject() throws InterruptedException {

        Project project1 = LiveDataTestUtil.getValue(mySaveTaskViewModel.getProject(PROJECT_ID1));
        Project project2 = LiveDataTestUtil.getValue(mySaveTaskViewModel.getProject(PROJECT_ID2));
        Project project3 = LiveDataTestUtil.getValue(mySaveTaskViewModel.getProject(PROJECT_ID3));
        /** TEST **/
        assertEquals(PROJECT_NAME1, project1.getNameProject());
        assertEquals(PROJECT_NAME2, project2.getNameProject());
        assertEquals(PROJECT_NAME3, project3.getNameProject());
    }

    @Test
    public void getTasksWhenNoTaskInserted() throws InterruptedException {

        List<TaskWithProject> items = LiveDataTestUtil.getValue(mySaveTaskViewModel.getAllTask());
        /** TEST **/
        assertTrue(items.isEmpty());
    }

    @Test
    public void insertAndGetTask() throws InterruptedException {
        mySaveTaskViewModel.insertTask(NEW_TASK_AVION_PROJECT1);
        List<TaskWithProject> tasks = LiveDataTestUtil.getValue(mySaveTaskViewModel.getAllTask());
        /** TEST **/
        assertEquals("avion", tasks.get(0).getName());
    }

    @Test
    public void insertAndDeleteTask() throws InterruptedException {

        NEW_TASK_AVION_PROJECT1.setIdTask(1);
        mySaveTaskViewModel.insertTask(NEW_TASK_AVION_PROJECT1);
        mySaveTaskViewModel.deleteTask(1);
        /** TEST **/
        List<TaskWithProject> tasks = LiveDataTestUtil.getValue(mySaveTaskViewModel.getAllTask());
        assertTrue(tasks.isEmpty());
    }

    @Test
    public void test_az_comparator() throws InterruptedException {

        mySaveTaskViewModel.insertTask(NEW_TASK_AVION_PROJECT1);
        mySaveTaskViewModel.insertTask(NEW_TASK_BATEAU_PROJECT2);
        mySaveTaskViewModel.insertTask(NEW_TASK_CAMION_PROJECT3);
        LiveDataTestUtil.getValue(mySaveTaskViewModel.getAllTask());
        List<TaskWithProject> tasks = LiveDataTestUtil.getValue(mySaveTaskViewModel.getTaskByAZ());
        /** TEST **/
        assertEquals(tasks.get(0).getName(), NEW_TASK_AVION_PROJECT1.getName());
        assertEquals(tasks.get(1).getName(), NEW_TASK_BATEAU_PROJECT2.getName());
        assertEquals(tasks.get(2).getName(), NEW_TASK_CAMION_PROJECT3.getName());
    }

    @Test
    public void test_az_comparator_same_project() throws InterruptedException {

        mySaveTaskViewModel.insertTask(NEW_TASK_AVION_PROJECT1);
        mySaveTaskViewModel.insertTask(NEW_TASK_BATEAU_PROJECT1);
        mySaveTaskViewModel.insertTask(NEW_TASK_CAMION_PROJECT1);
        LiveDataTestUtil.getValue(mySaveTaskViewModel.getAllTask());
        List<TaskWithProject> tasks = LiveDataTestUtil.getValue(mySaveTaskViewModel.getTaskByAZ());
        /** TEST **/
        assertEquals(tasks.get(0).getName(), NEW_TASK_AVION_PROJECT1.getName());
        assertEquals(tasks.get(1).getName(), NEW_TASK_BATEAU_PROJECT1.getName());
        assertEquals(tasks.get(2).getName(), NEW_TASK_CAMION_PROJECT1.getName());
    }

    @Test
    public void test_za_comparator() throws InterruptedException {

        mySaveTaskViewModel.insertTask(NEW_TASK_AVION_PROJECT1);
        mySaveTaskViewModel.insertTask(NEW_TASK_BATEAU_PROJECT2);
        mySaveTaskViewModel.insertTask(NEW_TASK_CAMION_PROJECT3);
        LiveDataTestUtil.getValue(mySaveTaskViewModel.getAllTask());
        List<TaskWithProject> tasks = LiveDataTestUtil.getValue(mySaveTaskViewModel.getTaskByZA());
        /** TEST **/
        assertEquals(tasks.get(0).getName(), NEW_TASK_CAMION_PROJECT3.getName());
        assertEquals(tasks.get(1).getName(), NEW_TASK_BATEAU_PROJECT2.getName());
        assertEquals(tasks.get(2).getName(), NEW_TASK_AVION_PROJECT1.getName());
    }

    @Test
    public void test_za_comparator_same_project() throws InterruptedException {

        mySaveTaskViewModel.insertTask(NEW_TASK_AVION_PROJECT1);
        mySaveTaskViewModel.insertTask(NEW_TASK_BATEAU_PROJECT1);
        mySaveTaskViewModel.insertTask(NEW_TASK_CAMION_PROJECT1);
        LiveDataTestUtil.getValue(mySaveTaskViewModel.getAllTask());
        List<TaskWithProject> tasks = LiveDataTestUtil.getValue(mySaveTaskViewModel.getTaskByZA());
        /** TEST **/
        assertEquals(tasks.get(0).getName(), NEW_TASK_CAMION_PROJECT1.getName());
        assertEquals(tasks.get(1).getName(), NEW_TASK_BATEAU_PROJECT1.getName());
        assertEquals(tasks.get(2).getName(), NEW_TASK_AVION_PROJECT1.getName());
    }

    @Test
    public void test_newer_comparator() throws InterruptedException {

        mySaveTaskViewModel.insertTask(NEW_TASK_AVION_PROJECT1);
        mySaveTaskViewModel.insertTask(NEW_TASK_BATEAU_PROJECT2);
        mySaveTaskViewModel.insertTask(NEW_TASK_CAMION_PROJECT3);
        LiveDataTestUtil.getValue(mySaveTaskViewModel.getAllTask());
        List<TaskWithProject> tasks = LiveDataTestUtil.getValue(mySaveTaskViewModel.getTaskByAZ());
        /** TEST **/
        assertEquals(tasks.get(0).getName(), NEW_TASK_AVION_PROJECT1.getName());
        assertEquals(tasks.get(1).getName(), NEW_TASK_BATEAU_PROJECT2.getName());
        assertEquals(tasks.get(2).getName(), NEW_TASK_CAMION_PROJECT3.getName());
    }

    @Test
    public void test_newer_comparator_same_project() throws InterruptedException {

        mySaveTaskViewModel.insertTask(NEW_TASK_AVION_PROJECT1);
        mySaveTaskViewModel.insertTask(NEW_TASK_BATEAU_PROJECT1);
        mySaveTaskViewModel.insertTask(NEW_TASK_CAMION_PROJECT1);
        LiveDataTestUtil.getValue(mySaveTaskViewModel.getAllTask());
        List<TaskWithProject> tasks = LiveDataTestUtil.getValue(mySaveTaskViewModel.getTaskByAZ());
        /** TEST **/
        assertEquals(tasks.get(0).getName(), NEW_TASK_AVION_PROJECT1.getName());
        assertEquals(tasks.get(1).getName(), NEW_TASK_BATEAU_PROJECT1.getName());
        assertEquals(tasks.get(2).getName(), NEW_TASK_CAMION_PROJECT1.getName());
    }

    @Test
    public void test_older_comparator() throws InterruptedException {

        mySaveTaskViewModel.insertTask(NEW_TASK_AVION_PROJECT1);
        mySaveTaskViewModel.insertTask(NEW_TASK_BATEAU_PROJECT2);
        mySaveTaskViewModel.insertTask(NEW_TASK_CAMION_PROJECT3);
        LiveDataTestUtil.getValue(mySaveTaskViewModel.getAllTask());
        List<TaskWithProject> tasks = LiveDataTestUtil.getValue(mySaveTaskViewModel.getTaskByZA());
        /** TEST **/
        assertEquals(tasks.get(0).getName(), NEW_TASK_CAMION_PROJECT3.getName());
        assertEquals(tasks.get(1).getName(), NEW_TASK_BATEAU_PROJECT2.getName());
        assertEquals(tasks.get(2).getName(), NEW_TASK_AVION_PROJECT1.getName());
    }

    @Test
    public void test_older_comparator_same_project() throws InterruptedException {

        mySaveTaskViewModel.insertTask(NEW_TASK_AVION_PROJECT1);
        mySaveTaskViewModel.insertTask(NEW_TASK_BATEAU_PROJECT1);
        mySaveTaskViewModel.insertTask(NEW_TASK_CAMION_PROJECT1);
        LiveDataTestUtil.getValue(mySaveTaskViewModel.getAllTask());
        List<TaskWithProject> tasks = LiveDataTestUtil.getValue(mySaveTaskViewModel.getTaskByZA());
        /** TEST **/
        assertEquals(tasks.get(0).getName(), NEW_TASK_CAMION_PROJECT1.getName());
        assertEquals(tasks.get(1).getName(), NEW_TASK_BATEAU_PROJECT1.getName());
        assertEquals(tasks.get(2).getName(), NEW_TASK_AVION_PROJECT1.getName());
    }
}
