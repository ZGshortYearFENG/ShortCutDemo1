package fengzj.sample.shortcutdemo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(BLShortCut.isShortCutExistWithShortCutManager(MainActivity.this, "pinned-shortcut")){
                    Toast.makeText(MainActivity.this, "app快捷方式重复创建", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(BLShortCut.createAppShortcut(MainActivity.this, SecondActivity.class, getResources().getString(R.string.app_name), R.mipmap.ic_launcher)){

                    // 自动添加弹窗 跟 下面Toast同时发出

                    Toast.makeText(MainActivity.this, "快捷方式创建成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "尝试创建快捷方式，可能失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 多Launcher方式
                BLShortCut.setActivityEnable(MainActivity.this, SecondActivity.class.getName(), true);
            }
        });
    }
}
