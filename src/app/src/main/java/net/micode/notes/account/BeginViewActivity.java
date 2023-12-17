package net.micode.notes.account;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.telecom.Call;
import android.view.View;
import android.widget.Button;

import net.micode.notes.R;
import net.micode.notes.ui.NotesListActivity;

public class BeginViewActivity extends Activity {
    Button loginButton;//登录注册按钮
    Button registerButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.begin_view);

        /**
         * Insert an introduction when user firstly use this application
         */
        loginButton=(Button)findViewById(R.id.login_press);
        registerButton=(Button)findViewById(R.id.register_press);
        loginButton.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {
                                              //转到登录
                                              Intent intent = new Intent(BeginViewActivity.this, LoginActivity.class);
                                              intent.setAction("android.intent.action.CALLr");
                                              startActivity(intent);
                                              finish();
                                          }
                                      }
        );
        registerButton.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View v) {
                                               //转到注册
                                               Intent intent = new Intent(BeginViewActivity.this, RegisterActivity.class);
                                               intent.setAction("android.intent.action.CALLr");
                                               startActivity(intent);
                                               finish();
                                           }
                                       }
        );
    }
}
