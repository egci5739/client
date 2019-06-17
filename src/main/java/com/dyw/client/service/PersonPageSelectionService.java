package com.dyw.client.service;

import com.dyw.client.entity.protection.FaceInfoEntity;

import java.util.ArrayList;
import java.util.List;

public class PersonPageSelectionService {
    private List<FaceInfoEntity> bigList = new ArrayList<FaceInfoEntity>(); //大集合，从外界获取
    private List<FaceInfoEntity> smallList = new ArrayList<FaceInfoEntity>(); //小集合，返回给调用它的类
    private static int currentPageIndex = 1;        //当前页码

    //更改每页显示条数
    public void setPerPageNumber(int perPageNumber) {
        this.perPageNumber = perPageNumber;
    }

    private int perPageNumber = 50;        //每页显示条数
    private int pageCount;           //总页数
    private int recordCount;           //总记录条数

    /*
     *不带参数的构造函数
     * */
    public PersonPageSelectionService() {
    }

    //传入指定页码的构造函数，参看第几页。
    public PersonPageSelectionService(int currentPageIndex) {
        PersonPageSelectionService.currentPageIndex = currentPageIndex;
    }

    //首页
    public List<FaceInfoEntity> firstPage(List<FaceInfoEntity> faceInfoEntityList) {
        currentPageIndex = 1;
        if (faceInfoEntityList.size() % perPageNumber == 0) {
            pageCount = (int) Math.ceil(faceInfoEntityList.size() / perPageNumber);
        } else {
            pageCount = (int) Math.ceil(faceInfoEntityList.size() / perPageNumber) + 1;

        }
        bigList = faceInfoEntityList;
        return select();
    }

    //下一页
    public List<FaceInfoEntity> nextPage(List<FaceInfoEntity> faceInfoEntityList) {
        if (currentPageIndex < pageCount) {
            currentPageIndex++;
            bigList = faceInfoEntityList;
        }
        return select();
    }

    //上一页
    public List<FaceInfoEntity> previousPage(List<FaceInfoEntity> faceInfoEntityList) {
        if (currentPageIndex > 1) {
            currentPageIndex--;
            bigList = faceInfoEntityList;
        }
        return select();
    }

    //此方法供以上方法调用，根据当前页，筛选记录
    public List<FaceInfoEntity> select() {
        smallList.clear();
        recordCount = bigList.size();
        for (int i = (currentPageIndex - 1) * perPageNumber; i < currentPageIndex * perPageNumber && i < recordCount; i++) {
            smallList.add(bigList.get(i));
        }
        return smallList;
    }
}
