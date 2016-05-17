package com.lihang.accounting.service;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.lihang.accounting.database.dao.AccountBookDAO;
import com.lihang.accounting.entitys.AccountBook;
import com.lihang.accounting.service.base.BaseService;

import java.util.List;

/**
 * Created by LiHang on 2016/3/1.
 */
public class AccountBookService extends BaseService {
    private AccountBookDAO accountBookDAO;
    private PayoutService payoutService;

    public AccountBookService(Context context) {
        super(context);
        accountBookDAO = new AccountBookDAO(context);
        payoutService = new PayoutService(context);
    }

    public boolean hideAccountBookByAccountBookId(AccountBook accountBook) {
        boolean result = false;
        boolean result2 = false;
        accountBookDAO.getDatabase().beginTransaction();
        try {
            result = accountBookDAO.updateAccountBook(" and accountBookId = " + accountBook.getAccountBookId(), accountBook);
            result2 = payoutService.hidePayoutByAccountBookId(accountBook.getAccountBookId());
            System.out.println(result + "," + result2);
            if (result && result2) {
                accountBookDAO.setTransactionSuccessful();
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }finally {
            accountBookDAO.endTransaction();
        }
    }

    public boolean setIsDefault(int accountBookId) throws Exception {
        String condition = "isDefault = 1";
        ContentValues contentValues = new ContentValues();
        contentValues.put("isDefault", 0);
        boolean result = accountBookDAO.updateAccountBook(condition, contentValues);
        condition = "accountBOokId = " + accountBookId;
        contentValues.clear();
        contentValues.put("isDefault", 1);
        boolean result2 = accountBookDAO.updateAccountBook(condition, contentValues);

        if (result && result2) {
            Log.i("提示", "设置默认成功！" + result + "&" + result2);
            return true;
        } else {
            Log.i("提示", "设置默认失败！" + result + "&" + result2);
            return false;
        }
    }

    public boolean insertAccountBook(AccountBook accountBook) {
        return insertOrUpdate(accountBook, null);
    }

    public boolean updateAccountBook(AccountBook accountBook) {
        String condition = " and accountBookId=" + accountBook.getAccountBookId();
        return insertOrUpdate(accountBook, condition);
    }

    public boolean insertOrUpdate(AccountBook accountBook, String condition) {
        //开始事务
        Log.i("提示", "开始事务！");
        accountBookDAO.beginTransaction();
        try {

            boolean result = false;
            if (condition == null) {
                Log.i("提示", "执行插入！");
                accountBook.setAccountBookId((int) accountBookDAO.insertAccountBookIdReNewId(accountBook));
                if (accountBook.getAccountBookId() > 0) {
                    result = true;
                } else {
                    result = false;
                }
            } else {
                Log.i("提示", "执行更新！");
                result = accountBookDAO.updateAccountBook(condition, accountBook);
            }
            boolean result2 = true;
            if (accountBook.getIsDefault() == 1 && result) {
                Log.i("提示", "开始设置为默认！" + accountBook.getAccountBookId());
                result2 = setIsDefault(accountBook.getAccountBookId());
            }
            if (result && result2) {
                Log.i("提示", "事务成功！");
                accountBookDAO.setTransactionSuccessful();
                return true;
            } else {
                Log.i("提示", "返回false！");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("提示", "跳到catch！");
            return false;
        } finally {
            Log.i("提示", "事务结束！");
            accountBookDAO.endTransaction();
        }
    }

    public List<AccountBook> getAccountBooks(String condition) {
        return accountBookDAO.getAccountBooks(condition);
    }

    public AccountBook getAccountBookByAccountBookId(int accountBookId) {
        List<AccountBook> list = accountBookDAO.getAccountBooks(" and accountBookid = " + accountBookId);
        if (list != null && list.size() == 1) {
            return list.get(0);
        } else {
            return null;
        }
    }

    public List<AccountBook> getNotHideAccountBook() {
        return accountBookDAO.getAccountBooks(" and state=1");
    }


    public boolean isExistAccountBookByAccountBookName(String accountBookName, Integer accountBookId) {
        String condition = "and accountBookname = '" + accountBookName + "'";
        if (accountBookId != null) {
            condition += "and accountBookId <>" + accountBookId;
        }

        List<AccountBook> list = accountBookDAO.getAccountBooks(condition);
        if (list != null && list.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    //根据用户ID隐藏该用户
    public boolean hideAccountBookByAccountBookId(int accountBookId) {
        String condition = "accountBookid = " + accountBookId;
        ContentValues contentValues = new ContentValues();
        contentValues.put("state", 0);
        return accountBookDAO.updateAccountBook(condition, contentValues);
    }

    /**
     * 获取默认账本
     *
     * @return
     */
    public AccountBook getDefaultAccountBook() {
        return getAccountBooks(" and isDefault = 1").get(0);
    }

    public String getAccountBookNameById(Integer accountBookId) {
        return accountBookDAO.getAccountBooks(" and accountBookId = " + accountBookId).get(0).getAccountBookName();
    }

}
