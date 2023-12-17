package net.micode.notes.account;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import net.micode.notes.R;

public class RegisterActivity extends Activity {

    EditText userAccount;//注册账号密码
    EditText userPassword;//
    EditText userPasswordAsked;
    Button setAccount;//注册确认按钮
    Button registerReturn;//注册返回上一级
    static String user_account="张三";//默认注册的账号密码
    static String user_password="123";
    static String user_password_asked;
    Boolean isSuccess=false;//判断是否注册成功
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("运行到RegisterActivity了");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_edit);
        System.out.println("运行到zhe了");
        userAccount=(EditText) findViewById(R.id.user_account);
        userPassword=(EditText) findViewById(R.id.user_password);
        userPasswordAsked=(EditText)findViewById(R.id.user_password_asked);
        setAccount=(Button)findViewById(R.id.set_account);
        setAccount.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                //获取输入密码 账号
                                                user_account=userAccount.getText().toString();
                                                user_password=userPassword.getText().toString();
                                                user_password_asked=userPasswordAsked.getText().toString();
                                                new Thread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        isSuccess=DBConnection.register(user_account,user_password,user_password_asked);
                                                        if (isSuccess){
                                                            System.out.println("注册成功");
                                                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                                            intent.setAction("android.intent.action.CALLr");
                                                            startActivity(intent);
                                                            finish();
                                                        }
                                                        else {
                                                            System.out.println("注册失败");
                                                            userAccount.setText("");//清空
                                                            userPassword.setText("");
                                                            userPasswordAsked.setText("");
                                                        }
                                                    }
                                                }).start();

                                            }
                                        }
        );
        registerReturn=(Button)findViewById(R.id.set_account_ret);
        registerReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this,BeginViewActivity.class);
                intent.setAction("android.intent.action.MAIN");
                startActivity(intent);
                finish();
            }
        });
    }
}
