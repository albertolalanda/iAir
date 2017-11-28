package com.example.bruno.iair.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.example.bruno.iair.R;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class SettingsActivity extends AppCompatActivity {

    String username;
    TextView userTextView;
    EditText userEditText;
    ViewSwitcher switcher;
    Button saveUsernameBtn;
    Button cancelUsernameBtn;
    LinearLayout saveBtnCancelBtnLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        username = DashBoardActivity.username;

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        userEditText = findViewById(R.id.editTextUserID);
        userTextView = findViewById(R.id.textViewUserID);
        saveUsernameBtn = findViewById(R.id.btnSaveUsername);
        cancelUsernameBtn = findViewById(R.id.btnCancelUsername);
        saveBtnCancelBtnLayout = findViewById(R.id.SaveCancelBtnsLayout);

        saveBtnCancelBtnLayout.setVisibility(View.GONE);

        switcher = findViewById(R.id.viewSwitcher);

        userTextView.setText(username);
        userEditText.setText(username);

        userTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switcher.showNext();
                saveBtnCancelBtnLayout.setVisibility(View.VISIBLE);
            }
        });

        saveUsernameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = userEditText.getText().toString();
                DashBoardActivity.username = username;
                Toast.makeText(SettingsActivity.this, "User changed ans saved!", Toast.LENGTH_SHORT).show();
                SharedPreferences sharedPrefs = getSharedPreferences("username", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putString("user", username);
                editor.commit();
                setResult(RESULT_OK);
                finish();
            }
        });

        cancelUsernameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SettingsActivity.this, "Changes discarded!", Toast.LENGTH_SHORT).show();
                switcher.setDisplayedChild(0);
                saveBtnCancelBtnLayout.setVisibility(View.GONE);

            }
        });







    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.btnSearch);
        MenuItem btnBack = menu.findItem(R.id.btnBack);
        MenuItem search = menu.findItem(R.id.btnSearch);
        MenuItem settings = menu.findItem(R.id.btnSettings);
        settings.setVisible(FALSE);
        search.setVisible(FALSE);
        btnBack.setVisible(TRUE);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.btnCity:
                Intent appInfo = new Intent(SettingsActivity.this, CityListActivity.class);
                startActivity(appInfo);
                break;
            case R.id.btnBack:
                setResult(RESULT_OK);
                finish();
                break;
            default:

        }

        return true;
    }

}