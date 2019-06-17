package com.dyw.client.service;

import com.dyw.client.controller.Egci;
import com.dyw.client.entity.PassRecordEntity;
import com.dyw.client.entity.StaffEntity;
import com.dyw.client.tool.Tool;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ExportExcelService {
    private Logger logger = LoggerFactory.getLogger(ExportExcelService.class);

    public void export(int number) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("YYYYMMDDhhmmss");
            String now = dateFormat.format(new Date());
            //导出文件路径
            String basePath = System.getProperty("user.dir") + "\\staffInfo\\";
            //文件名
            String exportFileName = "人员信息_" + now + ".xlsx";
            String[] cellTitle = {"姓名", "性别", "出生年月", "年龄", "证件类型", "证件ID", "家庭地址", "电话号码", "组织机构", "头像url"};
            //需要导出的数据
            List<StaffEntity> dataList = Egci.session.selectList("mapping.staffMapper.getStaffByNumber", number);
            // 声明一个工作薄
            XSSFWorkbook workBook = null;
            workBook = new XSSFWorkbook();
            // 生成一个表格
            XSSFSheet sheet = workBook.createSheet();
            workBook.setSheetName(0, "人员信息");
            // 创建表格标题行 第一行
            XSSFRow titleRow = sheet.createRow(0);
            for (int i = 0; i < cellTitle.length; i++) {
                titleRow.createCell(i).setCellValue(cellTitle[i]);
            }
            //插入需导出的数据
            int i = 0;
            for (StaffEntity staffEntity : dataList) {
                if (savePhoto(staffEntity.getStaffImage(), staffEntity.getStaffName() + "_" + staffEntity.getStaffCardNumber())) {
                    XSSFRow row = sheet.createRow(i + 1);
                    row.createCell(0).setCellValue(staffEntity.getStaffName());
                    row.createCell(1).setCellValue(staffEntity.getStaffGender());
                    row.createCell(2).setCellValue(staffEntity.getStaffBirthday());
                    row.createCell(3).setCellValue(20);
                    row.createCell(4).setCellValue("身份证");
                    row.createCell(5).setCellValue(staffEntity.getStaffCardNumber());
                    row.createCell(6).setCellValue("广州");
                    row.createCell(7).setCellValue("13500000000");
                    row.createCell(8).setCellValue("组织");
                    row.createCell(9).setCellValue(System.getProperty("user.dir") + "\\staffInfo\\" + staffEntity.getStaffName() + "_" + staffEntity.getStaffCardNumber() + ".jpg ");
                    i++;
                }
            }
            File file = new File(basePath + exportFileName);
            //文件输出流
            FileOutputStream outStream = null;
            outStream = new FileOutputStream(file);
            workBook.write(outStream);
            outStream.flush();
            outStream.close();
            Tool.showMessage("导出文件成功！文件导出路径：--" + basePath + exportFileName, "提示", 0);
        } catch (Exception e) {
            Tool.showMessage("导出文件失败", "提示", 1);
            logger.error("导出文件失败", e);
        }
    }

    /*
     * 图片存入本地
     * */
    private static Boolean savePhoto(byte[] bytes, String fileName) {
        try {
            OutputStream os = new FileOutputStream(System.getProperty("user.dir") + "\\staffInfo\\" + fileName + ".jpg");
            os.write(bytes, 0, bytes.length);
            os.flush();
            os.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /*
     * 导出通行信息
     * */
    public void exportPassInfo(List<PassRecordEntity> passInfoHistoryList) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("YYYYMMDDhhmmss");
            String now = dateFormat.format(new Date());
            //导出文件路径
            String basePath = System.getProperty("user.dir") + "\\passInfo\\";
            //文件名
            String exportFileName = "历史通行信息" + now + ".xlsx";
            String[] cellTitle = {"时间", "姓名", "卡号", "事件", "分值", "设备"};
            // 声明一个工作薄
            XSSFWorkbook workBook = null;
            workBook = new XSSFWorkbook();
            // 生成一个表格
            XSSFSheet sheet = workBook.createSheet();
            workBook.setSheetName(0, "历史通行信息");
            // 创建表格标题行 第一行
            XSSFRow titleRow = sheet.createRow(0);
            for (int i = 0; i < cellTitle.length; i++) {
                titleRow.createCell(i).setCellValue(cellTitle[i]);
            }
            //插入需导出的数据
            int i = 0;
            for (PassRecordEntity passInfoEntity : passInfoHistoryList) {
                XSSFRow row = sheet.createRow(i + 1);
                row.createCell(0).setCellValue(passInfoEntity.getPassRecordPassTime().toString());
                row.createCell(1).setCellValue(passInfoEntity.getPassRecordName());
                row.createCell(2).setCellValue(passInfoEntity.getPassRecordCardNumber());
                row.createCell(3).setCellValue(passInfoEntity.getPassRecordEventTypeId());
                row.createCell(4).setCellValue(passInfoEntity.getPassRecordSimilarity());
                row.createCell(5).setCellValue(passInfoEntity.getPassRecordEquipmentName());
                i++;
            }
            File file = new File(basePath + exportFileName);
            //文件输出流
            FileOutputStream outStream = null;
            outStream = new FileOutputStream(file);
            workBook.write(outStream);
            outStream.flush();
            outStream.close();
            Tool.showMessage("导出文件成功！文件导出路径：--" + basePath + exportFileName, "提示", 1);
        } catch (Exception e) {
            Tool.showMessage("导出文件失败", "提示", 0);
            logger.error("导出文件失败", e);
        }
    }
}

