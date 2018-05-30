package view.edit.input.gaad_db;

import android.content.Intent;
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
    public void onGetItem(String id) {
        sendInfo(id);
//        Log.v("MIMO_SAHA:: ", "PP: " + id);
    }

    private void sendInfo(String id) {
        Intent intent = getIntent();
        intent.putExtra("key", id);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void sendData(int id) {
        Intent sendIntent = new Intent();
        // Set the action to be performed i.e 'Send Data'
        sendIntent.setAction(Intent.ACTION_SEND);
        // Add the text to the intent
        sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
        // Set the type of data i.e 'text/plain'
        sendIntent.setType("text/plain");
        // Launches the activity; Open 'Text editor' if you set it as default app to handle Text
        startActivity(sendIntent);
    }

    private class PullData extends AsyncTask<Void, Void, Void> {

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
