package fengzj.sample.shortcutdemo;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Build;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;

import java.util.Arrays;
import java.util.List;

public class BLShortCut {

    private static final String ACTION_INSTALL_SHORTCUT = "com.android.launcher.action.INSTALL_SHORTCUT";
    private static final String ACTION_UNINSTALL_SHORTCUT = "com.android.launcher.action.UNINSTALL_SHORTCUT";

    /**
     * 删除快捷方式 不一定能成功
     * 
     * @param context
     * @param cls
     * @param shortCutName
     */
    public static void delShortcut(Context context, Class<?> cls, String shortCutName) {

        Intent intent = new Intent(ACTION_UNINSTALL_SHORTCUT);
        final Intent launchIntent = new Intent(context, cls);
        launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        intent.putExtra("duplicate", false); // 为true是循环删除快捷方式
        intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, shortCutName); // 快捷方式的标题
        intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, launchIntent); // 快捷方式的运行程序主入口
        context.sendBroadcast(intent);

    }

    /**
     * 创建应用入口的快捷方式 不一定能成功
     */
    public static boolean createAppShortcut(Context context, Class<?> cls, String shortCutName, int shortIconId) {
        return createAppShortcut(context, cls.getName(), shortCutName, shortIconId);
    }

    /**
     * 创建应用入口的快捷方式 不一定能成功
     */
    public static boolean createAppShortcut(Context context, String clsName, String shortCutName, int shortIconId) {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            Intent intent = new Intent(ACTION_INSTALL_SHORTCUT);
            final Intent launchIntent = new Intent().setComponent(new ComponentName(context, clsName));
            launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            Parcelable icon = Intent.ShortcutIconResource.fromContext(context.getApplicationContext(), shortIconId);
            intent.putExtra("duplicate", false); // 不允许重复创建
            intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, shortCutName); // 快捷方式的标题
            intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon); // 快捷方式的图片
            intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, launchIntent); // 快捷方式的运行程序主入口
            context.sendBroadcast(intent);
            return true;
        } else {
            ShortcutManager shortcutManager = context.getSystemService(ShortcutManager.class);
            if (shortcutManager.isRequestPinShortcutSupported()) {

                Intent intent = new Intent().setComponent(new ComponentName(context, clsName));
                intent.setAction(Intent.ACTION_MAIN);
                ShortcutInfo pinShortcutInfo = new ShortcutInfo.Builder(context, "pinned-shortcut")
                        .setIcon(Icon.createWithResource(context, shortIconId)).setIntent(intent)
                        .setShortLabel(shortCutName).build();
                Intent pinnedShortcutCallbackIntent = shortcutManager.createShortcutResultIntent(pinShortcutInfo);
                // Get notified when a shortcut is pinned successfully//
                PendingIntent successCallback = PendingIntent.getBroadcast(context, 0, pinnedShortcutCallbackIntent, 0);
                return shortcutManager.requestPinShortcut(pinShortcutInfo, successCallback.getIntentSender());
            }
            return false;
        }

    }

    public static boolean createAppShortcut(Context context, Class<?> cls, String shortCutName, Bitmap shortIconId) {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            Intent intent = new Intent(ACTION_INSTALL_SHORTCUT);
            final Intent launchIntent = new Intent(context, cls);
            launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            intent.putExtra("duplicate", false); // 不允许重复创建
            intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, shortCutName); // 快捷方式的标题
            intent.putExtra(Intent.EXTRA_SHORTCUT_ICON, shortIconId); // 快捷方式的图片
            intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, launchIntent); // 快捷方式的运行程序主入口
            context.sendBroadcast(intent);
            return true;
        } else {
            ShortcutManager shortcutManager = context.getSystemService(ShortcutManager.class);
            if (shortcutManager.isRequestPinShortcutSupported()) {
                Intent intent = new Intent(context, cls);
                intent.setAction(Intent.ACTION_MAIN);
                ShortcutInfo pinShortcutInfo = new ShortcutInfo.Builder(context, "pinned-shortcut")
                        .setIcon(Icon.createWithBitmap(shortIconId)).setIntent(intent).setShortLabel(shortCutName)
                        .build();
                Intent pinnedShortcutCallbackIntent = shortcutManager.createShortcutResultIntent(pinShortcutInfo);
                // Get notified when a shortcut is pinned successfully//
                PendingIntent successCallback = PendingIntent.getBroadcast(context, 0, pinnedShortcutCallbackIntent, 0);
                return shortcutManager.requestPinShortcut(pinShortcutInfo, successCallback.getIntentSender());
            }
            return false;
        }

    }

    /**
     * AndroidO ShortCutManager判断快捷方式是否重复添加
     * @param context
     * @param mPinShortcutId
     * @return
     */
    public static boolean isShortCutExistWithShortCutManager(Context context, String mPinShortcutId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ShortcutManager shortcutManager = context.getSystemService(ShortcutManager.class);
            List<ShortcutInfo> infos = shortcutManager.getPinnedShortcuts();
            for (int i = 0; i < infos.size(); i++) {
                ShortcutInfo info = infos.get(i);
                if (info.getId().equals(mPinShortcutId)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void setActivityEnable(Context context, String clsName, boolean enable) {
        try {
            ComponentName componentName = new ComponentName(context, clsName);
            PackageManager packageManager = context.getPackageManager();
            int oldState = packageManager.getComponentEnabledSetting(componentName);
            int state = enable ? PackageManager.COMPONENT_ENABLED_STATE_ENABLED
                    : PackageManager.COMPONENT_ENABLED_STATE_DISABLED;
            if (oldState != state) {
                packageManager.setComponentEnabledSetting(componentName, state, PackageManager.DONT_KILL_APP);
            }
            PackageManager pm = context.getPackageManager();
            pm.getLaunchIntentForPackage(context.getPackageName());
        } catch (Exception e) {
//            BLLog.e(e);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N_MR1)
    public static void createDynamicShortCuts(Context context){
        ShortcutManager shortcutManager = context.getSystemService(ShortcutManager.class);

        ShortcutInfo shortcut = new ShortcutInfo.Builder(context, "id1")
                .setShortLabel("Website")
                .setLongLabel("Open the website")
                .setIcon(Icon.createWithResource(context, R.drawable.ic_launcher_background))
                .setIntent(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.mysite.example.com/")))
                .build();

        shortcutManager.setDynamicShortcuts(Arrays.asList(shortcut));
    }

}
