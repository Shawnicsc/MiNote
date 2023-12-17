package net.micode.notes.account;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import net.micode.notes.R;
import net.micode.notes.ui.NotesListActivity;

public class LoginActivity extends Activity {
    EditText userAccount;//账号密码
    EditText userPassword;
    Button loginButton;//登录按钮
    Button loginReturn;//返回上一级
    String user_account;
    String user_password;
    boolean isSuccess=false;//判断登录是否成功
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("运行到LoginActivity了");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_edit);
        userAccount=(EditText) findViewById(R.id.user_account_login);
        userPassword=(EditText) findViewById(R.id.user_password_login);
        loginButton=(Button)findViewById(R.id.login_account);
        loginReturn=(Button)findViewById(R.id.login_ret);
        loginButton.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {
                                              //获取输入密码 账号
                                              user_account=userAccount.getText().toString();
                                              user_password=userPassword.getText().toString();
                                              new Thread(new Runnable() {
                                                  @Override
                                                  public void run() {
                                                      System.out.println("运行到run了");
                                                      isSuccess=DBConnection.login(user_account,user_password);
                                                      if (isSuccess){
                                                          System.out.println("登录成功");
                                                          Intent intent = new Intent(LoginActivity.this, NotesListActivity.class);
                                                          intent.putExtra("user_account",user_account);
                                                          intent.putExtra("user_password",user_password);//将账号密码传递到NotesListActivity中
                                                          intent.setAction("android.intent.action.CALLr");
                                                          startActivity(intent);
                                                          finish();
                                                      }
                                                      else {
                                                          System.out.println("登录失败");
                                                          userAccount.setText("");//清空
                                                          userPassword.setText("");
                                                      }
                                                  }
                                              }).start();

                                          }
                                      }
        );
        loginReturn=(Button)findViewById(R.id.login_ret);
        loginReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,BeginViewActivity.class);
                intent.setAction("android.intent.action.MAIN");
                startActivity(intent);
                finish();
            }
        });
    }
}
