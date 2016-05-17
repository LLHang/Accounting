package com.lihang.accounting.view;

import android.app.Dialog;
import android.content.Context;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lihang.accounting.R;

/**
 * Created by LiHang on 2016/3/9.
 */
public class MyDialog extends Dialog implements View.OnClickListener {
    private Context context;
    private EditText mydialog_num_et;
    private Button mydialog_ok_btn, mydialog_num1_btn, mydialog_num2_btn,
            mydialog_num3_btn, mydialog_num4_btn, mydialog_num5_btn,
            mydialog_num6_btn, mydialog_num7_btn, mydialog_num8_btn,
            mydialog_num9_btn, mydialog_num0_btn, mydialog_numpoint_btn,
            mydialog_numback_btn;
    private StringBuffer amount;

    private MydialogDisMiss mydialogDisMiss;

    public interface MydialogDisMiss{
        public void setString(String amount);
    }

    public MyDialog(Context context) {
        super(context, R.style.mydialog);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.mydialog_layout);
        this.context = context;
        initView();
        initListener();
        initData();
    }

    public void setOnMyDialogDisMiss(MydialogDisMiss myDialogDisMiss) {
        this.mydialogDisMiss = myDialogDisMiss;
    }

    public void initView() {
        mydialog_ok_btn = (Button) findViewById(R.id.mydialog_ok_btn);
        mydialog_num_et = (EditText) findViewById(R.id.mydialog_num_et);
        mydialog_num0_btn = (Button) findViewById(R.id.mydialog_num0_btn);
        mydialog_num1_btn = (Button) findViewById(R.id.mydialog_num1_btn);
        mydialog_num2_btn = (Button) findViewById(R.id.mydialog_num2_btn);
        mydialog_num3_btn = (Button) findViewById(R.id.mydialog_num3_btn);
        mydialog_num4_btn = (Button) findViewById(R.id.mydialog_num4_btn);
        mydialog_num5_btn = (Button) findViewById(R.id.mydialog_num5_btn);
        mydialog_num6_btn = (Button) findViewById(R.id.mydialog_num6_btn);
        mydialog_num7_btn = (Button) findViewById(R.id.mydialog_num7_btn);
        mydialog_num8_btn = (Button) findViewById(R.id.mydialog_num8_btn);
        mydialog_num9_btn = (Button) findViewById(R.id.mydialog_num9_btn);
        mydialog_numpoint_btn = (Button) findViewById(R.id.mydialog_numpoint_btn);
        mydialog_numback_btn = (Button) findViewById(R.id.mydialog_numback_btn);
        mydialog_num_et.setInputType(InputType.TYPE_NULL);
    }

    private void initListener() {
        mydialog_ok_btn.setOnClickListener(this);
        mydialog_num0_btn.setOnClickListener(this);
        mydialog_num1_btn.setOnClickListener(this);
        mydialog_num2_btn.setOnClickListener(this);
        mydialog_num3_btn.setOnClickListener(this);
        mydialog_num4_btn.setOnClickListener(this);
        mydialog_num5_btn.setOnClickListener(this);
        mydialog_num6_btn.setOnClickListener(this);
        mydialog_num7_btn.setOnClickListener(this);
        mydialog_num8_btn.setOnClickListener(this);
        mydialog_num9_btn.setOnClickListener(this);
        mydialog_num0_btn.setOnClickListener(this);
        mydialog_numpoint_btn.setOnClickListener(this);
        mydialog_numback_btn.setOnClickListener(this);
    }

    private void initData() {
        amount = new StringBuffer();
        mydialog_num_et.setText(amount);
        mydialog_num_et.setSelection(amount.length());
    }

    @Override
    public void onClick(View v) {
        String num = mydialog_num_et.getText().toString();

        switch (v.getId()) {
            case R.id.mydialog_numpoint_btn:
                if (num.length() == 0) {
                    num += "0.";
                } else if (num.indexOf(".") >= 0) {
                    return;
                } else {
                    num += ".";
                }
                break;
            case R.id.mydialog_num1_btn:
                num += "1";
                break;
            case R.id.mydialog_num2_btn:
                num += "2";
                break;
            case R.id.mydialog_num3_btn:
                num += "3";
                break;
            case R.id.mydialog_num4_btn:
                num += "4";
                break;
            case R.id.mydialog_num5_btn:
                num += "5";
                break;
            case R.id.mydialog_num6_btn:
                num += "6";
                break;
            case R.id.mydialog_num7_btn:
                num += "7";
                break;
            case R.id.mydialog_num8_btn:
                num += "8";
                break;
            case R.id.mydialog_num9_btn:
                num += "9";
                break;
            case R.id.mydialog_num0_btn:
                num += "0";
                break;
            case R.id.mydialog_numback_btn:
                if (num.length() != 0) {
                    num = num.substring(0, num.length() - 1);
                }
                break;
            case R.id.mydialog_ok_btn:
                if (num.indexOf(".") == num.length() - 1 && !num.equals("")) {
                    Toast.makeText(context, "最后一位不能是点", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mydialogDisMiss != null) {
                    mydialogDisMiss.setString(num);
                }
                dismiss();
                break;
            default:
                break;
        }
        if (num.length() >= 10) {
            return;
        }
        mydialog_num_et.setText(num);
        mydialog_num_et.setSelection(num.length());
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }
}
