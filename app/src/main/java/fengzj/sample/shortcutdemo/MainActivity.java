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

                if(ContextCompat.checkSelfPermission(MainActivity.this, "net.oneplus.launcher.permission.READ_SETTINGS")
                        != PackageManager.PERMISSION_GRANTED) {

                    AndPermission.with(MainActivity.this)
                            .runtime()
                            .permission(new String[] {"net.oneplus.launcher.permission.READ_SETTINGS", "net.oneplus.launcher.permission.WRITE_SETTINGS"})
                            .onGranted(new Action<List<String>>() {
                                @Override
                                public void onAction(List<String> data) {
                                    Log.d("fzj", "if 权限申请好了");
                                    if (ShortCutUtil.isShortCutExist(MainActivity.this, "ShortCutDemo快捷方式")) {
                                        Log.d("fzj", "ShortCut重复创建");
                                        return;
                                    }
                                    if (BLShortCut.createAppShortcut(MainActivity.this, MainActivity.class.getName(), "ShortCutDemo快捷方式", 1)) {
                                        Toast.makeText(MainActivity.this, "创建快捷方式成功", Toast.LENGTH_SHORT).show();
                                        Log.d("fzj", "ShortCut创建成功");
                                    } else {
                                        Toast.makeText(MainActivity.this, "Failure", Toast.LENGTH_SHORT).show();
                                        Log.d("fzj", "ShortCut创建失败");
                                    }
                                }
                            })
                            .onDenied(new Action<List<String>>() {
                                @Override
                                public void onAction(List<String> data) {
                                    Log.d("fzj", "权限没申请下来");
                                }
                            })
                            .start();
                } else {
                    Log.d("fzj", "else 权限申请好了");
                    if (ShortCutUtil.isShortCutExist(MainActivity.this, "ShortCutDemo快捷方式")) {
                        Log.d("fzj", "ShortCut重复创建");
                        return;
                    }
                    if (BLShortCut.createAppShortcut(MainActivity.this, MainActivity.class.getName(), "ShortCutDemo快捷方式", 1)) {
                        Toast.makeText(MainActivity.this, "创建快捷方式成功", Toast.LENGTH_SHORT).show();
                        Log.d("fzj", "ShortCut创建成功");
                    } else {
                        Toast.makeText(MainActivity.this, "Failure", Toast.LENGTH_SHORT).show();
                        Log.d("fzj", "ShortCut创建失败");
                    }
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
