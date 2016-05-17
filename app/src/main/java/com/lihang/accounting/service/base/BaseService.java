package com.lihang.accounting.service.base;

import android.content.Context;

/**
 * Created by LiHang on 2016/3/1.
 */
public class BaseService {
    protected Context context;

    protected BaseService(Context context) {
        this.context = context;
    }
}
