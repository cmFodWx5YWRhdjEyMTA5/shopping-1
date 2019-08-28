/*
 * Copyright (C) 2016 Facishare Technology Co., Ltd. All Rights Reserved.
 */
package com.nahuo.live.xiaozhibo.permission;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.view.WindowManager;

import com.nahuo.Dialog.CDialog;
import com.nahuo.live.xiaozhibo.permission.rom.HuaweiUtils;
import com.nahuo.live.xiaozhibo.permission.rom.MeizuUtils;
import com.nahuo.live.xiaozhibo.permission.rom.MiuiUtils;
import com.nahuo.live.xiaozhibo.permission.rom.OppoUtils;
import com.nahuo.live.xiaozhibo.permission.rom.QikuUtils;
import com.nahuo.live.xiaozhibo.permission.rom.RomUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;


/**
 * Description:
 *
 * @author zhaozp
 * @since 2016-10-17
 */

public class FloatWindowManager {
    private static final String TAG = "FloatWindowManager";

    private static volatile FloatWindowManager instance;

    private boolean isWindowDismiss = true;
    private WindowManager windowManager = null;
    private WindowManager.LayoutParams mParams = null;
    private AVCallFloatView floatView = null;
    private CDialog dialog;

    public static FloatWindowManager getInstance() {
        if (instance == null) {
            synchronized (FloatWindowManager.class) {
                if (instance == null) {
                    instance = new FloatWindowManager();
                }
            }
        }
        return instance;
    }

    public void applyOrShowFloatWindow(Context context, GotoPlay gotoPlay) {
        if (checkPermission(context)) {
            if (gotoPlay != null)
                gotoPlay.onGotoPlay();
            // showWindow(context);
        } else {
            applyPermission(context, gotoPlay);
        }
    }

    private boolean checkPermission(Context context) {
        //6.0 版本之后由于 google 增加了对悬浮窗权限的管理，所以方式就统一了
        if (Build.VERSION.SDK_INT < 23) {
            if (RomUtils.checkIsMiuiRom()) {
                return miuiPermissionCheck(context);
            } else if (RomUtils.checkIsMeizuRom()) {
                return meizuPermissionCheck(context);
            } else if (RomUtils.checkIsHuaweiRom()) {
                return huaweiPermissionCheck(context);
            } else if (RomUtils.checkIs360Rom()) {
                return qikuPermissionCheck(context);
            } else if (RomUtils.checkIsOppoRom()) {
                return oppoROMPermissionCheck(context);
            }
        }
        return commonROMPermissionCheck(context);
    }

    private boolean huaweiPermissionCheck(Context context) {
        return HuaweiUtils.checkFloatWindowPermission(context);
    }

    private boolean miuiPermissionCheck(Context context) {
        return MiuiUtils.checkFloatWindowPermission(context);
    }

    private boolean meizuPermissionCheck(Context context) {
        return MeizuUtils.checkFloatWindowPermission(context);
    }

    private boolean qikuPermissionCheck(Context context) {
        return QikuUtils.checkFloatWindowPermission(context);
    }

    private boolean oppoROMPermissionCheck(Context context) {
        return OppoUtils.checkFloatWindowPermission(context);
    }

    private boolean commonROMPermissionCheck(Context context) {
        //最新发现魅族6.0的系统这种方式不好用，天杀的，只有你是奇葩，没办法，单独适配一下
        if (RomUtils.checkIsMeizuRom()) {
            return meizuPermissionCheck(context);
        } else {
            Boolean result = true;
            if (Build.VERSION.SDK_INT >= 23) {
                try {
                    Class clazz = Settings.class;
                    Method canDrawOverlays = clazz.getDeclaredMethod("canDrawOverlays", Context.class);
                    result = (Boolean) canDrawOverlays.invoke(null, context);
                } catch (Exception e) {
                    Log.e(TAG, Log.getStackTraceString(e));
                }
            }
            return result;
        }
    }

    private void applyPermission(Context context, GotoPlay gotoPlay) {
        if (Build.VERSION.SDK_INT < 23) {
            if (RomUtils.checkIsMiuiRom()) {
                miuiROMPermissionApply(context, gotoPlay);
            } else if (RomUtils.checkIsMeizuRom()) {
                meizuROMPermissionApply(context, gotoPlay);
            } else if (RomUtils.checkIsHuaweiRom()) {
                huaweiROMPermissionApply(context, gotoPlay);
            } else if (RomUtils.checkIs360Rom()) {
                ROM360PermissionApply(context, gotoPlay);
            } else if (RomUtils.checkIsOppoRom()) {
                oppoROMPermissionApply(context, gotoPlay);
            }
        } else {
            commonROMPermissionApply(context, gotoPlay);
        }
    }

    private void ROM360PermissionApply(final Context context, final GotoPlay gotoPlay) {
        showConfirmDialog(context, new OnConfirmResult() {
            @Override
            public void confirmResult(boolean confirm) {
                if (confirm) {
                    QikuUtils.applyPermission(context);
                } else {
                    canCelGotoPlay(gotoPlay);
                    Log.e(TAG, "ROM:360, user manually refuse OVERLAY_PERMISSION");
                }
            }
        });
    }

    void canCelGotoPlay(GotoPlay gotoPlay) {
        if (gotoPlay != null)
            gotoPlay.onCancel();
    }

    private void huaweiROMPermissionApply(final Context context,final GotoPlay gotoPlay) {
        showConfirmDialog(context, new OnConfirmResult() {
            @Override
            public void confirmResult(boolean confirm) {
                if (confirm) {
                    HuaweiUtils.applyPermission(context);
                } else {
                    canCelGotoPlay(gotoPlay);
                    Log.e(TAG, "ROM:huawei, user manually refuse OVERLAY_PERMISSION");
                }
            }
        });
    }

    private void meizuROMPermissionApply(final Context context,final GotoPlay gotoPlay) {
        showConfirmDialog(context, new OnConfirmResult() {
            @Override
            public void confirmResult(boolean confirm) {
                if (confirm) {
                    MeizuUtils.applyPermission(context);
                } else {
                    canCelGotoPlay(gotoPlay);
                    Log.e(TAG, "ROM:meizu, user manually refuse OVERLAY_PERMISSION");
                }
            }
        });
    }

    private void miuiROMPermissionApply(final Context context, final GotoPlay gotoPlay) {
        showConfirmDialog(context, new OnConfirmResult() {
            @Override
            public void confirmResult(boolean confirm) {
                if (confirm) {
                    MiuiUtils.applyMiuiPermission(context);
                } else {
                    canCelGotoPlay(gotoPlay);
                    Log.e(TAG, "ROM:miui, user manually refuse OVERLAY_PERMISSION");
                }
            }
        });
    }

    private void oppoROMPermissionApply(final Context context,final GotoPlay gotoPlay) {
        showConfirmDialog(context, new OnConfirmResult() {
            @Override
            public void confirmResult(boolean confirm) {
                if (confirm) {
                    OppoUtils.applyOppoPermission(context);
                } else {
                    canCelGotoPlay(gotoPlay);
                    Log.e(TAG, "ROM:miui, user manually refuse OVERLAY_PERMISSION");
                }
            }
        });
    }

    /**
     * 通用 rom 权限申请
     */
    private void commonROMPermissionApply(final Context context,final GotoPlay gotoPlay) {
        //这里也一样，魅族系统需要单独适配
        if (RomUtils.checkIsMeizuRom()) {
            meizuROMPermissionApply(context, gotoPlay);
        } else {
            if (Build.VERSION.SDK_INT >= 23) {
                showConfirmDialog(context, new OnConfirmResult() {
                    @Override
                    public void confirmResult(boolean confirm) {
                        if (confirm) {
                            try {
                                commonROMPermissionApplyInternal(context);
                            } catch (Exception e) {
                                Log.e(TAG, Log.getStackTraceString(e));
                            }
                        } else {
                            canCelGotoPlay(gotoPlay);
                            Log.d(TAG, "user manually refuse OVERLAY_PERMISSION");
                            //需要做统计效果
                        }
                    }
                });
            }
        }
    }

    public static void commonROMPermissionApplyInternal(Context context) throws NoSuchFieldException, IllegalAccessException {
        Class clazz = Settings.class;
        Field field = clazz.getDeclaredField("ACTION_MANAGE_OVERLAY_PERMISSION");

        Intent intent = new Intent(field.get(null).toString());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setData(Uri.parse("package:" + context.getPackageName()));
        context.startActivity(intent);
    }

    private void showConfirmDialog(Context context, OnConfirmResult result) {
        showConfirmDialog(context, "是否开启小窗口播放", result);
    }

    private void showConfirmDialog(Context context, String message, final OnConfirmResult result) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
//        dialog= CommDialog.getInstance((Activity) context);
//        dialog.setDialogType(CommDialog.DialogType.D_EXIT).setContent(message).setLeftStr("取消").setRightStr("去开启").setPositive(new CommDialog.PopDialogListener() {
//            @Override
//            public void onPopDialogButtonClick(int ok_cancel, CommDialog.DialogType type) {
//                if (ok_cancel==BUTTON_POSITIVIE){
//                    result.confirmResult(true);
//                }else {
//                    result.confirmResult(false);
//                }
//            }
//        }).showDialog();
        dialog = new CDialog((Activity) context);
        dialog.setHasTittle(true).setTitle("").setMessage(message).setPositive("去开启", new CDialog.PopDialogListener() {
            @Override
            public void onPopDialogButtonClick(int which) {
                if (CDialog.BUTTON_POSITIVIE == which) {
                    result.confirmResult(true);
                }
            }
        }).setNegative("取消", new CDialog.PopDialogListener() {
            @Override
            public void onPopDialogButtonClick(int which) {
                if (CDialog.BUTTON_NEGATIVE == which) {
                    result.confirmResult(false);
                }
            }
        }).show();

//        dialog = new AlertDialog.Builder(context).setCancelable(true).setTitle("")
//                .setMessage(message)
//                .setPositiveButton("现在去开启",
//                        new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                result.confirmResult(true);
//                                dialog.dismiss();
//                            }
//                        }).setNegativeButton("暂不开启",
//                        new DialogInterface.OnClickListener() {
//
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                result.confirmResult(false);
//                                dialog.dismiss();
//                            }
//                        }).create();
//        dialog.show();
    }

    private interface OnConfirmResult {
        void confirmResult(boolean confirm);
    }

    public interface GotoPlay {
        void onGotoPlay();

        void onCancel();
    }

    private void showWindow(Context context) {

//        if (!isWindowDismiss) {
//            Log.e(TAG, "view is already added here");
//            return;
//        }
//
//        isWindowDismiss = false;
//        if (windowManager == null) {
//            windowManager = (WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
//        }
//
//        Point size = new Point();
//        windowManager.getDefaultDisplay().getSize(size);
//        int screenWidth = size.x;
//        int screenHeight = size.y;
//
//        mParams = new WindowManager.LayoutParams();
//        mParams.packageName = context.getPackageName();
//        mParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
//        mParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
//        mParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
//                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR
//                | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
//        int mType;
//
//        if (Build.VERSION.SDK_INT >= VersionUtls.getOCodes()) {
//            mType = VersionUtls.getTYPE_APPLICATION_OVERLAY();
//        } else {
//            mType = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
//        }
//        mParams.type = mType;
//        mParams.format = PixelFormat.RGBA_8888;
//        mParams.gravity = Gravity.LEFT | Gravity.TOP;
//        mParams.x = screenWidth - dp2px(context, 100);
//        mParams.y = screenHeight - dp2px(context, 171);
//
//
////        ImageView imageView = new ImageView(mContext);
////        imageView.setImageResource(R.drawable.app_icon);
//        floatView = new AVCallFloatView(context);
//        floatView.setParams(mParams);
//        floatView.setIsShowing(true);
//        windowManager.addView(floatView, mParams);
    }

    public void dismissWindow() {
        if (isWindowDismiss) {
            Log.e(TAG, "window can not be dismiss cause it has not been added");
            return;
        }

        isWindowDismiss = true;
        floatView.setIsShowing(false);
        if (windowManager != null && floatView != null) {
            windowManager.removeViewImmediate(floatView);
        }
    }

    private int dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}
