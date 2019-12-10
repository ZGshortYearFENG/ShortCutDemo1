# ShortCutDemo1
Android ShortCut Demo

## 适配

Android O以下 Intent发广播创建快捷方式 duplicate 字段限制重复创建

Android O ShortCutManager创建快捷方式 添加防重复的方法

## 当前适配情况：（仅手上测试机）

### oneplus：很棒

### vivo：

问题：BLShortCut方法参数title，需要与app_name不一样且开启权限，并且创建成功有系统Toast

### huawei：

问题：需要开启权限，跟oneplus一样

### xiaomi:

问题：第一次点击快捷方式创建顶部出现该权限被禁止提示，之后不会提醒，桌面快捷方式的权限默认禁止，调为询问后，点击弹出权限弹窗

### 具体解决：

模仿微信小程序右上角添加到桌面功能，什么手机都弹出一个已尝试添加到桌面的提示

# ShortCut使用

## 静态ShortCut
静态快捷方式最适合在用户与应用程序互动的整个生命周期中使用一致结构链接到内容的应用程序。由于大多数启动器一次只能显示四个快捷方式，因此静态快捷方式对于常见活动很有用。例如，如果用户希望以特定方式查看其日历或电子邮件，则使用静态快捷方式可确保他们执行例行任务的经验是一致的。

### 1/ 添加intent-filter
```
 <activity android:name=".MainActivity">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />

                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>

        </activity>
```
### 2/ 添加meta-data

```
 <activity android:name=".MainActivity">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />

                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
            <meta-data android:name="android.app.shortcuts"
                android:resource="@xml/shortcuts" />
        </activity>
```

### 3/ 创建shortcuts.xml
```
<?xml version="1.0" encoding="utf-8"?>
<shortcuts xmlns:android="http://schemas.android.com/apk/res/android">
    <shortcut
        android:shortcutId="compose"
        android:enabled="true"
        android:icon="@mipmap/ic_launcher_round"
        android:shortcutShortLabel="@string/compose_shortcut_short_label1"
        android:shortcutLongLabel="@string/compose_shortcut_long_label1"
        android:shortcutDisabledMessage="@string/compose_disabled_message1">
        <intent
            android:action="android.intent.action.VIEW"
            android:targetPackage="fengzj.sample.shortcutdemo"
            android:targetClass="fengzj.sample.shortcutdemo.MainActivity" />
        <!-- If your shortcut is associated with multiple intents, include them
             here. The last intent in the list determines what the user sees when
             they launch this shortcut. -->
        <categories android:name="android.shortcut.conversation" />
    </shortcut>
    <!-- Specify more shortcuts here. -->
</shortcuts>
```
在这个新的资源文件中，添加一个shortcuts根元素，其中包含一个shortcut元素列表。每个shortcut元素都包含有关静态快捷方式的信息，包括其图标，其描述标签以及它在应用程序中启动intent


## 动态ShortCut

动态快捷键提供了指向应用程序中特定于上下文的特定操作的链接。动态快捷方式的不错选择包括导航至特定位置以及从用户的最后保存点加载游戏。
```
ShortcutManager shortcutManager = getSystemService(ShortcutManager.class);

ShortcutInfo shortcut = new ShortcutInfo.Builder(context, "id1")
    .setShortLabel("Website")
    .setLongLabel("Open the website")
    .setIcon(Icon.createWithResource(context, R.drawable.icon_website))
    .setIntent(new Intent(Intent.ACTION_VIEW,
                   Uri.parse("https://www.mysite.example.com/")))
    .build();

shortcutManager.setDynamicShortcuts(Arrays.asList(shortcut));
```

