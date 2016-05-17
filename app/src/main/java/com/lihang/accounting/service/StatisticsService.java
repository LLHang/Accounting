package com.lihang.accounting.service;

import android.content.Context;
import android.os.Environment;

import com.lihang.accounting.R;
import com.lihang.accounting.entitys.Payout;
import com.lihang.accounting.entitys.Statistics;
import com.lihang.accounting.service.base.BaseService;
import com.lihang.accounting.utils.DateUtil;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import jxl.Workbook;
import jxl.write.*;
import jxl.write.Number;

/**
 * Created by HARRY on 2016/3/14.
 */
public class StatisticsService extends BaseService {
    public static final String SDCARD_PATH = Environment.getExternalStorageDirectory().getPath() + "/Accounting/Export/";
    private PayoutService payoutService;
    private UserService userService;
    private AccountBookService accountBookService;
    private List<String> keyList;

    public StatisticsService(Context context) {
        super(context);
        payoutService = new PayoutService(context);
        userService = new UserService(context);
        accountBookService = new AccountBookService(context);
    }

    /*private List<Statistics> getStatisticsList(String condition) {
        //按支付人的ID排序取出消费记录
        List<Payout> payoutList = payoutService.getPayoutOrderByPayoutUserId(condition);
        //获取计算方式的数组
        String[] payoutTypeArray = context.getResources().getStringArray(R.array.PayoutType);
        List<Statistics> statisticsesList = new ArrayList<>();
        if (payoutList != null) {
            //遍历消费记录列表
            for (int i = 0; i < payoutList.size(); i++) {
                //取出一条消费记录
                Payout payout = payoutList.get(i);
                //将消费人Id转换为真实名称
                String[] payoutUserName = userService.getUserNameByUserId(payout.getPayoutUserId()).split(",");
                String[] payoutUserId = payout.getPayoutUserId().split(",");
                //取出当前消费记录的方式
                String payoutType = payout.getPayoutType();
                //存放计算后的消费金额
                BigDecimal cost;
                //判断本次消费记录的消费类型，如果是均分
                if (payoutType.equals(payoutTypeArray[0])) {
                    //得到消费人数
                    int payoutTotal = payoutUserName.length;
                    //得到计算后的平均消费金额
                    cost = payout.getAmount().divide(
                            new BigDecimal(payoutTotal),
                            2,
                            BigDecimal.ROUND_HALF_EVEN);
                } else {
                    //是借贷或是个人消费
                    //直接去除消费金额
                    cost = payout.getAmount();
                }
                //遍历这条消费记录所有的消费人的数组
                for (int j = 0; j < payoutUserId.length; j++) {
                    //如果是借贷则跳过第一个索引，因为第一个人是借贷人自己
                    if (payoutType.equals(payoutTypeArray[1]) && j == 0) {
                        continue;
                    }

                    //声明一个统计类
                    Statistics statistics = new Statistics();
                    //将统计类的付款人设置为消费人数组的第一个人
                    statistics.payerUserId = payoutUserName[0];
                    //设置消费人
                    statistics.consumerUserId = payoutUserId[j];
                    //设置消费类型
                    statistics.payoutType = payoutType;
                    //设置算好的消费金额
                    statistics.cost = cost;
                    statisticsesList.add(statistics);
                }
            }
        }
        return statisticsesList;
    }

    public List<Statistics> getPayoutUserId(String condition) {
        //得到拆分好的统计信息
        List<Statistics> list = getStatisticsList(condition);
        //存放按付款人分类的临时统计信息
        List<Statistics> listTemp = new ArrayList<>();
        //存放统计好的汇总
        List<Statistics> totalList = new ArrayList<>();
        String result = "";
        //遍历拆分好的统计信息
        for (int i = 0; i < list.size(); i++) {
            //得到拆分好的一条信息
            Statistics statistics = list.get(i);
            result += statistics.payerUserId + "#"
                    + statistics.consumerUserId + "#"
                    + statistics.cost + "\r\n";
            //保存当前付款人的ID
            String currentPayerUserId = statistics.payerUserId;
            //把当前信息按付款人分类的临时数据
            Statistics statisticsTemp = new Statistics();
            statisticsTemp.payerUserId = statistics.payerUserId;
            statisticsTemp.consumerUserId = statistics.consumerUserId;
            statisticsTemp.cost = statistics.cost;
            statisticsTemp.payoutType = statistics.payoutType;
            listTemp.add(statisticsTemp);


            //计算下一行的索引
            int nextIndex;
            //如果下一行索引小于统计信息索引，+1
            if ((i + 1) < list.size()) {
                nextIndex = i + 1;
            } else {
                //证明已经到尾了，索引值为当前行
                nextIndex = i;
            }


    /**
     * 如果当前付款人与下一位付款人不同，证明分类统计已经到尾，
     * 或者已经循环到统计数组最后一位，就开始进入统计
     *//*
            if (!currentPayerUserId.equals(list.get(nextIndex).payerUserId) || nextIndex == i) {
                //进行当前分类统计数组的统计
                for (int j = 0; j < listTemp.size(); j++) {
                    Statistics statisticsTotal = listTemp.get(j);
                    //判断在总统计数组当中是否已经存在给付款人和消费人的信息
                    int index = getPositionByConsumerUserId(totalList,
                            statisticsTotal.payerUserId,
                            statisticsTotal.consumerUserId);
                    //如果已经存在，就在原来的数据上进行累加
                    if (index != -1) {
                        totalList.get(index).cost = totalList.get(index).cost
                                .add(statisticsTotal.cost);
                    } else {
                        //否则就是一条新信息，添加到统计数组当中
                        totalList.add(statisticsTotal);
                    }
                }
                listTemp.clear();
            }
        }
        return totalList;
    }

    private int getPositionByConsumerUserId(List<Statistics> totalList, String payerUserId, String consumerUserId) {
        int index = -1;
        for (int i = 0; i < totalList.size(); i++) {
            if (totalList.get(i).payerUserId.equals(payerUserId) &&
                    totalList.get(i).consumerUserId.equals(consumerUserId)) {
                index = i;
            }
        }
        return index;
    }*/

    /*public String getPayoutUserIdByAccountBookId(int accountBookId) {
        String result = "";
        //得到一个总统计结果的集合
        List<Statistics> totalList = getPayoutUserId(" and accountBookId = " + accountBookId);
        for (int i = 0; i < totalList.size(); i++) {
            Statistics statistics = totalList.get(i);
            if ("个人".equals(statistics.payoutType)) {
                result += statistics.payerUserId + "个人消费"
                        + statistics.cost.toString() + "元\r\n";
            } else if ("均分".equals(statistics.payoutType)) {
                if (statistics.payerUserId.equals(statistics.consumerUserId)) {
                    result += statistics.payerUserId + "个人消费"
                            + statistics.cost.toString() + "元\r\n";
                } else {
                    result += statistics.consumerUserId + "应支付给"
                            + statistics.payerUserId
                            + statistics.cost.toString() + "元\r\n";
                }
            }
        }
        return result;
    }*/
    public List<Statistics> getStatisticses(int accountBookId) {
        List<Payout> payouts = payoutService.getPayoutListByAccountBookId(accountBookId);
        HashMap<String, String> hashUsers = userService.getUserToHashMap();
        String[] payoutType = context.getResources().getStringArray(R.array.PayoutType);
        List<Statistics> statisticses = new ArrayList<>();
        Payout payout;
        for (int i = 0; i < payouts.size(); i++) {
            payout = payouts.get(i);
            if (payout.getPayoutType().equals(payoutType[0])) {
                String[] avgUsers = payout.getPayoutUserId().split(",");
                for (int a = 0; a < avgUsers.length; a++) {
                    Statistics statistics = new Statistics();
                    statistics.payerUserId = hashUsers.get(avgUsers[0]);
                    statistics.consumerUserId = hashUsers.get(avgUsers[a]);
                    statistics.cost = payout.getAmount().divide(
                            new BigDecimal(avgUsers.length),
                            2,
                            BigDecimal.ROUND_HALF_EVEN);
                    statisticses.add(statistics);
                }
            } else if (payout.getPayoutType().equals(payoutType[1])) {
                String[] loadUsers = payout.getPayoutUserId().split(",");
                for (int a = 1; a < loadUsers.length; a++) {
                    Statistics statistics = new Statistics();
                    statistics.payerUserId = hashUsers.get(loadUsers[0]);
                    statistics.consumerUserId = hashUsers.get(loadUsers[a]);
                    statistics.cost = payout.getAmount().divide(
                            new BigDecimal(loadUsers.length - 1),
                            2,
                            BigDecimal.ROUND_HALF_EVEN);
                    statisticses.add(statistics);
                }
            } else {
                String[] paersonalUser = payout.getPayoutUserId().split(",");
                Statistics statistics = new Statistics();
                statistics.payerUserId = hashUsers.get(paersonalUser[0]);
                statistics.consumerUserId = hashUsers.get(paersonalUser[0]);
                statistics.cost = payout.getAmount();
                statisticses.add(statistics);
            }
        }
        return statisticses;
    }

    public HashMap<String, BigDecimal> parseStatistics(List<Statistics> statisticses) {
        HashMap<String, BigDecimal> parsedStatisticses = new HashMap<>();
        keyList = new ArrayList<>();
        String key = "";
        for (int i = 0; i < statisticses.size(); i++) {
            Statistics statistics = statisticses.get(i);
            if (statistics.payerUserId.equals(statistics.consumerUserId)) {
                key = statistics.payerUserId + "2";
            } else {
                key = statistics.consumerUserId + "2" + statistics.payerUserId;
            }
            if (parsedStatisticses.containsKey(key)) {
                parsedStatisticses.put(key, parsedStatisticses.get(key).add(statistics.cost));
            } else {
                parsedStatisticses.put(key, statistics.cost);
                keyList.add(key);
            }
        }
        return parsedStatisticses;
    }

    public String getPayoutUserIdByAccountBookId(int accountBookId) {
        HashMap<String, BigDecimal> parsedStatisticses = parseStatistics(getStatisticses(accountBookId));

        String result = "";
        String key = "";
        for (int i = 0; i < keyList.size(); i++) {
            key = keyList.get(i);
            String[] keyInfo = key.split("2");
            if (keyInfo.length == 1) {
                result += keyInfo[0] + "个人消费" + parsedStatisticses.get(key).toString() + "元\r\n";
            } else if (keyInfo.length > 1) {
                result += keyInfo[0] + "应付给" + keyInfo[1] + parsedStatisticses.get(key).toString() + "元\r\n";
            }
        }
        return result;
    }

    public String exportStatistics(int accountBookId) throws Exception {
        String result = "";
        //判断是否有外存储设备
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String accountBookName = accountBookService.getAccountBookNameById(accountBookId);
            String fileName = accountBookName + DateUtil.date2string(new Date(), DateUtil.YYYY_MM_DD) + ".xls";
            System.out.println(SDCARD_PATH + fileName);
            File fileDir = new File(SDCARD_PATH);
            if (!fileDir.exists()) {
                fileDir.mkdirs();
            }
            File file = new File(SDCARD_PATH + fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            //声明一个可写的表对象
            WritableWorkbook workBookData;
            //创建工作薄,需要告诉它往哪个文件里边创建
            workBookData = Workbook.createWorkbook(file);
            //创建工作表,相当于Excel的Sheet1、Sheet2，注意索引从0开始
            WritableSheet wsAccountBook = workBookData.createSheet(accountBookName, 0);

            String[] titles = {"编号", "姓名", "消费信息", "金额"};
            //声明一个标签
            Label label;
            //添加标题行
            for (int i = 0; i < titles.length; i++) {
                //参数1列，2行，3内容
                label = new Label(i, 0, titles[i]);
                //将标签填入一个单元格
                wsAccountBook.addCell(label);
            }
            //添加行
            HashMap<String, BigDecimal> totaList = parseStatistics(getStatisticses(accountBookId));
            for (int i = 0; i < keyList.size(); i++) {
                //添加编号列，参数：1列，2行，3内容
                Number idCell = new Number(0, i + 1, i + 1);
                wsAccountBook.addCell(idCell);
                String[] names = keyList.get(i).split("2");
                Label nameLabel = new Label(1, i + 1, names[0]);
                wsAccountBook.addCell(nameLabel);
                Label infoLabel = null;
                if (names.length == 1) {
                    infoLabel = new Label(2, i + 1, "个人消费");
                } else if (names.length > 1) {
                    infoLabel = new Label(2, i + 1, "付给" + names[1]);
                }
                wsAccountBook.addCell(infoLabel);

                // 格式化金额类型显示，#。##表示格式化为小数点后两位
                NumberFormat moneyFormat = new NumberFormat("#,##");
                WritableCellFormat wcf = new WritableCellFormat(moneyFormat);

                Number moneyLabel = new Number(3, i + 1, totaList.get(keyList.get(i)).doubleValue(), wcf);
                wsAccountBook.addCell(moneyLabel);

            }
            //写入SD卡
            workBookData.write();
            workBookData.close();
            result = "数据已经导出！文件位置：" + SDCARD_PATH + fileName;
        } else {
            result = "未检测到SD卡，数据导出失败";
        }
        return result;
    }
}
