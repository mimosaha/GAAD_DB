package view.edit.input.gaad_db;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private UserDB userDB;

    private EditText editText1, editText2, editText3, editText4;
    private Button done, open;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText1 = findViewById(R.id.editText1);
        editText2 = findViewById(R.id.editText2);
        editText3 = findViewById(R.id.editText3);
        editText4 = findViewById(R.id.editText4);

        done = findViewById(R.id.ok);
        open = findViewById(R.id.open);

        done.setOnClickListener(this);
        open.setOnClickListener(this);

        userDB = new UserDB(this);

    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.ok) {
            saveInDB();
        } else if (view.getId() == R.id.open) {
            Intent intent = new Intent(this, UserHistory.class);
            startActivity(intent);
        }
    }

    private void saveInDB() {
        String name = editText1.getText().toString();
        String address = editText2.getText().toString();
        String des = editText3.getText().toString();
        String contact = editText4.getText().toString();

        UserInfoModel userInfoModel = new UserInfoModel().setUserName(name)
                .setUserAddress(address).setUserDesignation(des).setUserContactNumber(contact);
        long userId = userDB.insertAll(userInfoModel);
        Log.v("MIMO_SAHA", "ID: " + userId);
    }
}
