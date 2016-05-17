package com.lihang.accounting.activity.base;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.widget.Toast;

import com.lihang.accounting.R;

import java.lang.reflect.Field;

/**
 * Created by LiHang on 2016/2/25.
 */
public class BaseActivity extends Activity {
    private ProgressDialog progressDialog;

    protected void showProgressDialog(int titleResId, int msgResId) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(titleResId);
        progressDialog.setMessage(getString(msgResId));
        progressDialog.show();
    }

    protected void dismissProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    //封装的toast方法，方便Toast提示
    protected void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    protected void showMessage(int resId) {
        Toast.makeText(this, getString(resId), Toast.LENGTH_LONG).show();
    }

    //启动Activity方法，方便打开新的Activity
    protected void openActivity(Class<?> cla) {
        Intent intent = new Intent();
        intent.setClass(this, cla);
        startActivity(intent);
    }

    //获取layoutinflater的方法，方便获取layoutinflater对象
    protected LayoutInflater getInflater() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        return layoutInflater;
    }

    //通过反射设置dialog是否点击按钮后进行关闭
    protected void setAlertDialogIsClose(DialogInterface dialog, boolean isClose) {
        try {
            //通过反射或取dialog的mShowing参数
            Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
            field.setAccessible(true);
            //设置对应参数，使其关闭或者开启
            field.set(dialog, isClose);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //通过调用创建AlertDialog方法创建一个简易的AlertDialog，并使其显示出来
    protected AlertDialog showAlertDialog(int titleResId, String message, DialogInterface.OnClickListener clickListener) {
        String title = getResources().getString(titleResId);
        return showAlertDialog(title, message, clickListener);
    }

    //创建AlertDialog的方法，用来创建一个简易的AlertDialog
    protected AlertDialog showAlertDialog(String title, String message, DialogInterface.OnClickListener clickListener) {

        return new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setNegativeButton(R.string.button_text_no, null)
                .setPositiveButton(R.string.button_text_yes, clickListener)
                .show();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.back_enter_anim, R.anim.back_exit_anim);
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.enter_anim, R.anim.exit_anim);
    }
}
