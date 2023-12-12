package net.micode.notes.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.os.Bundle;
import android.widget.Toast;
import net.micode.notes.R;
import net.micode.notes.model.WorkingNote;

//这是一个检验密码的Activity，之后会添加其他。
public class PassWordEditActivity extends Activity {
    //用来输入密码和一个按钮
    EditText passWord;
    Button set_password;
    //储存要验证的便签的密码
    String mPassword;
    //要验证的便签
    WorkingNote mWorkingNote=null;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        intent = getIntent();
        if (savedInstanceState == null && !initActivityState()) {
            finish();
            return;
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        //不知道作用
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE
                        | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        //
        boolean User_boolean = mWorkingNote.hasPassword();//获取用户是否设置了密码
        if(!User_boolean) //User_boolean = false时，（没有设置密码），直接跳转到便签主界面
        {
            Intent intent=new Intent(PassWordEditActivity.this,NoteEditActivity.class);
            intent.setAction("android.intent.action.VIEW");
            intent.putExtra(Intent.EXTRA_UID, mWorkingNote.getNoteId());
            startActivity(intent);
            PassWordEditActivity.this.finish();
        }
        passWord=(EditText) findViewById(R.id.password);
        set_password=(Button)findViewById(R.id.set_password);
        set_password.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            //获取输入密码
                                            String password=passWord.getText().toString();
                                            if(password.equals(mPassword)){
                                                Intent intent=new Intent(PassWordEditActivity.this,NoteEditActivity.class);
                                                intent.setAction("android.intent.action.VIEW");
                                                intent.putExtra(Intent.EXTRA_UID, mWorkingNote.getNoteId());
                                                startActivity(intent);
                                                finish();

                                            }
                                            else{
                                                Toast.makeText(PassWordEditActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
                                                passWord.setText("");//把密码框内输入过的错误密码清空
                                            }
                                        }


                                        }
        );

    }
    private boolean initActivityState() {
        mWorkingNote = null;
        long noteId = intent.getLongExtra("noteId",-1);
        if (noteId!=-1) {
            //得到要验证的便签
            mWorkingNote = WorkingNote.load(PassWordEditActivity.this, noteId);
            //获取该便签密码
            mPassword=mWorkingNote.getmPassword();
            return true;
        }
        return false;
    }
}
