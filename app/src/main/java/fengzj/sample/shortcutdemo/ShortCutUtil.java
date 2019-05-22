package fengzj.sample.shortcutdemo;


import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.Intent.ShortcutIconResource;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * @author rxread
 *
 */
public class ShortCutUtil {

    /**
     * 检查快捷方式是否存在 <br/>
     * <font color=red>注意：</font> 有些手机无法判断是否已经创建过快捷方式<br/>
     * 因此，在创建快捷方式时，请添加<br/>
     * shortcutIntent.putExtra("duplicate", false);// 不允许重复创建<br/>
     * 最好使用{@link #isShortCutExist(Context, String, Intent)}
     * 进行判断，因为可能有些应用生成的快捷方式名称是一样的的<br/>
     * 	此处需要在AndroidManifest.xml中配置相关的桌面权限信息<br/>
     * 错误信息已捕获<br/>
     */
    public static boolean isShortCutExist(Context context, String title) {
        boolean result = false;
        try {
            final ContentResolver cr = context.getContentResolver();
            StringBuilder uriStr = new StringBuilder();
            String authority = LauncherUtil.getAuthorityFromPermissionDefault(context);
            if(authority==null||authority.trim().equals("")){
                authority = LauncherUtil.getAuthorityFromPermission(context,LauncherUtil.getCurrentLauncherPackageName(context)+".permission.READ_SETTINGS");
            }
            uriStr.append("content://");
            if (TextUtils.isEmpty(authority)) {
                int sdkInt = android.os.Build.VERSION.SDK_INT;
                if (sdkInt < 8) { // Android 2.1.x(API 7)以及以下的
                    uriStr.append("com.android.launcher.settings");
                } else if (sdkInt < 19) {// Android 4.4以下
                    uriStr.append("com.android.launcher2.settings");
                } else {// 4.4以及以上
                    uriStr.append("com.android.launcher3.settings");
                }
            } else {
                uriStr.append(authority);
            }
            uriStr.append("/favorites?notify=true");
            Uri uri = Uri.parse(uriStr.toString());
            Cursor c = cr.query(uri, new String[] { "title" },
                    "title=? ",
                    new String[] { title }, null);
            if (c != null && c.getCount() > 0) {
                result = true;
            }
            if (c != null && !c.isClosed()) {
                c.close();
            }
        } catch (Exception e) {
            Log.d("fzj", e.getMessage());
            e.printStackTrace();
            result=false;
        }
        return result;
    }

    /**
     * 不一定所有的手机都有效，因为国内大部分手机的桌面不是系统原生的<br/>
     * 更多请参考{@link #isShortCutExist(Context, String)}<br/>
     * 桌面有两种，系统桌面(ROM自带)与第三方桌面，一般只考虑系统自带<br/>
     * 第三方桌面如果没有实现系统响应的方法是无法判断的，比如GO桌面<br/>
     * 	此处需要在AndroidManifest.xml中配置相关的桌面权限信息<br/>
     * 错误信息已捕获<br/>
     */
    public static boolean isShortCutExist(Context context, String title, Intent intent) {
        boolean result = false;
        try{
            final ContentResolver cr = context.getContentResolver();
            StringBuilder uriStr = new StringBuilder();
            String authority = LauncherUtil.getAuthorityFromPermissionDefault(context);
            if(authority==null||authority.trim().equals("")){
                authority = LauncherUtil.getAuthorityFromPermission(context,LauncherUtil.getCurrentLauncherPackageName(context)+".permission.READ_SETTINGS");
            }
            uriStr.append("content://");
            if (TextUtils.isEmpty(authority)) {
                int sdkInt = android.os.Build.VERSION.SDK_INT;
                if (sdkInt < 8) { // Android 2.1.x(API 7)以及以下的
                    uriStr.append("com.android.launcher.settings");
                } else if (sdkInt < 19) {// Android 4.4以下
                    uriStr.append("com.android.launcher2.settings");
                } else {// 4.4以及以上
                    uriStr.append("com.android.launcher3.settings");
                }
            } else {
                uriStr.append(authority);
            }
            uriStr.append("/favorites?notify=true");
            Uri uri = Uri.parse(uriStr.toString());
            Cursor c = cr.query(uri, new String[] { "title", "intent" },
                    "title=?  and intent=?",
                    new String[] { title, intent.toUri(0) }, null);
            if (c != null && c.getCount() > 0) {
                result = true;
            }
            if (c != null && !c.isClosed()) {
                c.close();
            }
        }catch(Exception ex){
            result=false;
            ex.printStackTrace();
        }
        return result;
    }

}
