package com.dyw.client.controller;

import com.dyw.client.entity.StaffEntity;
import com.dyw.client.service.SessionService;
import org.apache.ibatis.session.SqlSession;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

public class exportExcel {
    public static void main(String[] args) throws IOException {
        SessionService sessionService = new SessionService();
        SqlSession session = sessionService.createSession();
        Scanner in = new Scanner(System.in);
        System.out.println("请输入要导出的人员数量：");
        int a = in.nextInt();//输入一个整数

        SimpleDateFormat dateFormat = new SimpleDateFormat("YYYYMMDDhhmmss");
        String now = dateFormat.format(new Date());
        //导出文件路径
        String basePath = System.getProperty("user.dir") + "\\staffInfo\\";
        //文件名
        String exportFileName = "数据_" + now + ".xlsx";
        String[] cellTitle = {"姓名", "性别", "出生年月", "年龄", "证件类型", "证件ID", "家庭地址", "电话号码", "组织机构", "头像url"};
        //需要导出的数据
        List<StaffEntity> dataList = session.selectList("mapping.staffMapper.getStaffByNumber", a);
        // 声明一个工作薄
        XSSFWorkbook workBook = null;
        workBook = new XSSFWorkbook();
        // 生成一个表格
        XSSFSheet sheet = workBook.createSheet();
        workBook.setSheetName(0, "学生信息");
        // 创建表格标题行 第一行
        XSSFRow titleRow = sheet.createRow(0);
        for (int i = 0; i < cellTitle.length; i++) {
            titleRow.createCell(i).setCellValue(cellTitle[i]);
        }
        //插入需导出的数据
        int i = 0;
        for (StaffEntity staffEntity : dataList) {
            if (savePhoto(staffEntity.getPhoto(), staffEntity.getName() + "_" + staffEntity.getCardNumber())) {
                XSSFRow row = sheet.createRow(i + 1);
                row.createCell(0).setCellValue(staffEntity.getName());
                row.createCell(1).setCellValue(staffEntity.getSex());
                row.createCell(2).setCellValue(staffEntity.getBirthday());
                row.createCell(3).setCellValue(20);
                row.createCell(4).setCellValue("身份证");
                row.createCell(5).setCellValue(staffEntity.getCardNumber());
                row.createCell(6).setCellValue("广州");
                row.createCell(7).setCellValue("13500000000");
                row.createCell(8).setCellValue("组织");
                row.createCell(9).setCellValue("C:\\software\\client\\staffInfo\\" + staffEntity.getName() + "_" + staffEntity.getCardNumber() + ".jpg ");
                i++;
            }
        }
        File file = new File(basePath + exportFileName);
        //文件输出流
        FileOutputStream outStream = new FileOutputStream(file);
        workBook.write(outStream);
        outStream.flush();
        outStream.close();
        System.out.println("导出2007文件成功！文件导出路径：--" + basePath + exportFileName);
    }

    /*
     * 图片存入本地
     * */
    private static Boolean savePhoto(byte[] bytes, String fileName) {
        try {
            OutputStream os = new FileOutputStream("C:\\software\\client\\staffInfo\\" + fileName + ".jpg");
            os.write(bytes, 0, bytes.length);
            os.flush();
            os.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}

