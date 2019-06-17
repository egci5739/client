package com.dyw.client.service;

import com.dyw.client.entity.PassRecordEntity;

import java.util.ArrayList;
import java.util.List;

public class PageSelectionService {
    private List<PassRecordEntity> bigList = new ArrayList<PassRecordEntity>(); //大集合，从外界获取
    private List<PassRecordEntity> smallList = new ArrayList<PassRecordEntity>(); //小集合，返回给调用它的类
    private static int currentPageIndex = 1;        //当前页码

    //更改每页显示条数
    public void setPerPageNumber(int perPageNumber) {
        this.perPageNumber = perPageNumber;
    }

    private int perPageNumber = 5;        //每页显示条数
    private int pageCount;           //总页数
    private int recordCount;           //总记录条数

    /*
     *不带参数的构造函数
     * */
    public PageSelectionService() {
    }

    //传入指定页码的构造函数，参看第几页。
    public PageSelectionService(int currentPageIndex) {
        PageSelectionService.currentPageIndex = currentPageIndex;
    }

    //首页
    public List<PassRecordEntity> firstPage(List<PassRecordEntity> passInfoEntityList) {
        currentPageIndex = 1;
        if (passInfoEntityList.size() % perPageNumber == 0) {
            pageCount = (int) Math.ceil(passInfoEntityList.size() / perPageNumber);
        } else {
            pageCount = (int) Math.ceil(passInfoEntityList.size() / perPageNumber) + 1;

        }
        bigList = passInfoEntityList;
        return select();
    }

    //下一页
    public List<PassRecordEntity> nextPage(List<PassRecordEntity> passInfoEntityList) {
        if (currentPageIndex < pageCount) {
            currentPageIndex++;
            bigList = passInfoEntityList;
        }
        return select();
    }

    //上一页
    public List<PassRecordEntity> previousPage(List<PassRecordEntity> passInfoEntityList) {
        if (currentPageIndex > 1) {
            currentPageIndex--;
            bigList = passInfoEntityList;
        }
        return select();
    }

    //此方法供以上方法调用，根据当前页，筛选记录
    public List<PassRecordEntity> select() {
        smallList.clear();
        recordCount = bigList.size();
        for (int i = (currentPageIndex - 1) * perPageNumber; i < currentPageIndex * perPageNumber && i < recordCount; i++) {
            smallList.add(bigList.get(i));
        }
        return smallList;
    }
}
