package view.edit.input.gaad_db;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.List;

/**
 * Created by hp on 5/29/2018.
 */

public class UserHistory extends AppCompatActivity implements EmployeeAdapter.OnItemClick {

    private RecyclerView recyclerView;
    private EmployeeAdapter employeeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_user_history);

        recyclerView = findViewById(R.id.recycler_view);

        databaseHelper = new UserDB(this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

    }

    @Override
    protected void onResume() {
        super.onResume();
        new PullData().execute();
    }

    private UserDB databaseHelper;
    List<UserInfoModel> employeeModels;

    @Override
    public void onGetItem(int id) {
        Log.v("MIMO_SAHA:: ", "PP: " + id);
    }

    public class PullData extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            employeeModels = databaseHelper.getAllUsers();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            employeeAdapter = new EmployeeAdapter(UserHistory.this,
                    employeeModels, UserHistory.this);
            recyclerView.setAdapter(employeeAdapter);
            super.onPostExecute(aVoid);
        }
    }

}
