# ShortCutDemo1
Android ShortCut Demo

适配

Android O以下 Intent发广播创建快捷方式 duplicate 字段限制重复创建

Android O ShortCutManager创建快捷方式 添加防重复的方法

当前适配情况：（仅手上测试机）

oneplus：很棒

vivo：

问题：BLShortCut方法参数title，需要与app_name不一样且开启权限，并且创建成功有系统Toast

huawei：

问题：需要开启权限，跟oneplus一样

xiaomi:

问题：第一次点击快捷方式创建顶部出现该权限被禁止提示，之后不会提醒，桌面快捷方式的权限默认禁止，调为询问后，点击弹出权限弹窗

具体解决：

模仿微信小程序右上角添加到桌面功能，什么手机都弹出一个已尝试添加到桌面的提示
