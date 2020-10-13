package com.dyw.client.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baidu.aip.bodyanalysis.AipBodyAnalysis;
import com.baidu.aip.face.AipFace;
import com.dyw.client.entity.FaceInfoEntity;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Decoder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;

public class BodySegService {
    private final Logger logger = LoggerFactory.getLogger(BodySegService.class);
    private final ImageResizerService imageResizerService;
    private final AipBodyAnalysis client;
    private final HashMap<String, String> options;

    private final AipFace aipFace;
    private final HashMap<String, String> faceOptions;

    public BodySegService() {
        /*
         * 初始化人体分割
         * */
        imageResizerService = new ImageResizerService();
        client = new AipBodyAnalysis("22748184", "7ePFsFTVkBNiS4i4WKU9GkO7", "bZYvDYH7TNAcbruFHWTi4pe0BuzrP4kb");
        options = new HashMap<>();
        options.put("type", "foreground");
        /*
         * 初始化人脸信息获取
         * */
        aipFace = new AipFace("22725438", "5Pio5914PReYDpx35Q3Cx02D", "4fLOjpx1FMswRaAecXjdvG5KC5FFXXZp");
        faceOptions = new HashMap<>();
        faceOptions.put("face_field", "landmark");
    }

    /*
     * 抠图+换背景
     * */
    public byte[] seg(byte[] picFile) throws Exception {
        org.json.JSONObject res = client.bodySeg(picFile, options);//人脸抠图
        //Base64ToImage(changePNGBackgroudColorByBase64(res.get("foreground").toString(), Color.blue), "H:\\dyw\\live\\abcd.png");
        FaceInfoEntity faceInfoEntity = getFaceInfo(picFile);//获取人脸位置
        /*
         * 分情况返回抠图效果
         * */
        if (faceInfoEntity.getFaceWidth() / faceInfoEntity.getImageWidth() > 0.5) {//人脸宽度与照片宽度比值大于0.5
            logger.info("one");
            return imageResizerService.cut(0, 0, (int) faceInfoEntity.getImageWidth(), (int) faceInfoEntity.getImageHeight(), changePNGBackgroudColorByBase64(res.get("foreground").toString(), Color.red));
        } else if (faceInfoEntity.getFaceHeight() / faceInfoEntity.getImageHeight() > 0.5) {//人脸高度与照片高度比值大于0.5
            logger.info("two");
            return imageResizerService.cut(0, 0, (int) faceInfoEntity.getImageWidth(), (int) faceInfoEntity.getImageHeight(), changePNGBackgroudColorByBase64(res.get("foreground").toString(), Color.red));
        } else if (faceInfoEntity.getNoseY() / faceInfoEntity.getImageHeight() < 0.25) {//人脸太靠近上方
            logger.info("three");
            return imageResizerService.cut(deal(faceInfoEntity.getNoseX(), faceInfoEntity.getFaceWidth()), 0, (int) (faceInfoEntity.getFaceWidth() * 2), (int) (faceInfoEntity.getFaceHeight() * 2.7), changePNGBackgroudColorByBase64(res.get("foreground").toString(), Color.red));
        } else {
            logger.info("four");
            return imageResizerService.cut(deal(faceInfoEntity.getNoseX(), faceInfoEntity.getFaceWidth()), deal(deal(faceInfoEntity.getNoseY(), faceInfoEntity.getFaceHeight()), 30), (int) (faceInfoEntity.getFaceWidth() * 2), (int) (faceInfoEntity.getFaceHeight() * 2.7), changePNGBackgroudColorByBase64(res.get("foreground").toString(), Color.red));//先换背景再截图
        }
    }

    /*
     * 用来计算人像框的大小
     * */
    private int deal(double x, double y) {
        if (x > y)
            return (int) Math.ceil(x - y);
        else
            return (int) Math.ceil(y - x);
    }

    /*
     * 获取人脸信息
     * */
    private FaceInfoEntity getFaceInfo(byte[] picFile) throws Exception {
        FaceInfoEntity faceInfoEntity = new FaceInfoEntity();
        org.json.JSONObject res = aipFace.detect(byte2Base64StringFun(picFile), "BASE64", faceOptions);
        //logger.info("人脸全部信息" + res.toString(2));
        //获取照片尺寸
        InputStream input = BaseToInputStream(byte2Base64StringFun(picFile));
        BufferedImage image = ImageIO.read(input);
        faceInfoEntity.setImageWidth(image.getWidth());
        faceInfoEntity.setImageHeight(image.getHeight());

        String result = res.getString("result");
        JSONArray face_list = JSON.parseObject(result).getJSONArray("face_list");
        //获取鼻子中心位置
        JSONArray landmark = face_list.getJSONObject(0).getJSONArray("landmark");
        faceInfoEntity.setNoseX(Double.parseDouble(landmark.getJSONObject(2).get("x").toString()));
        faceInfoEntity.setNoseY(Double.parseDouble(landmark.getJSONObject(2).get("y").toString()));
        //获取人脸宽度和高度
        JSONObject location = JSON.parseObject(face_list.getJSONObject(0).get("location").toString());
        faceInfoEntity.setFaceWidth(location.getDouble("width"));
        faceInfoEntity.setFaceHeight(location.getDouble("height"));
        logger.info(faceInfoEntity.toString());
        return faceInfoEntity;
    }

    /*
     * byte转base64字符串
     * */
    public String byte2Base64StringFun(byte[] b) {
        return Base64.encodeBase64String(b);
    }

    /**
     * base64字符串转换成图片
     *
     * @param imgStr      base64字符串
     * @param imgFilePath 图片存放路径
     * @return
     * @author ZHANGJL
     * @dateTime 2018-02-23 14:42:17
     */
    public static boolean Base64ToImage(String imgStr, String imgFilePath) { // 对字节数组字符串进行Base64解码并生成图片
        if (isEmpty(imgStr)) // 图像数据为空
            return false;
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            // Base64解码
            byte[] b = decoder.decodeBuffer(imgStr);
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {// 调整异常数据
                    b[i] += 256;
                }
            }
            OutputStream out = new FileOutputStream(imgFilePath);
            out.write(b);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 验证字符串是否为空
     *
     * @param input
     * @return
     */
    private static boolean isEmpty(String input) {
        return input == null || input.equals("");
    }

    /**
     * @param sourceImage    原始图片 最好是PNG透明的
     * @param backgroudColor 背景色
     * @return java.lang.String
     * @Description 给PNG图片增加背景色 返回base64
     * @Author 小帅丶
     **/
    public static byte[] changePNGBackgroudColorByBase64(String sourceImage, Color backgroudColor) {
        try {
            String base64 = "";
            BufferedImage result = changePNGBackgroudColor(sourceImage, backgroudColor);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(result, "jpg", baos);
            baos.flush();
            byte[] imageInByte = baos.toByteArray();
            baos.close();
//            final Base64.Encoder encoder = Base64.getEncoder();
//            base64 = encoder.encodeToString(imageInByte);
//            return base64;
            return imageInByte;
        } catch (Exception e) {
            System.out.println("有问题了" + e.getMessage());
            return null;
        }
    }

    /**
     * @param sourceImage    原始图片 最好是PNG透明的
     * @param backgroudColor 背景色
     * @return BufferedImage
     * @Description 给PNG图片增加背景色 返回BufferedImage
     * @Author 小帅丶
     **/
    public static BufferedImage changePNGBackgroudColor(String sourceImage, Color backgroudColor) throws IOException {
        try {
            InputStream input = BaseToInputStream(sourceImage);
            BufferedImage image = ImageIO.read(input);

            BufferedImage result = new BufferedImage(
                    image.getWidth(),
                    image.getHeight(),
                    BufferedImage.TYPE_INT_RGB);

            Graphics2D graphic = result.createGraphics();
            graphic.drawImage(image, 0, 0, backgroudColor, null);
            graphic.dispose();
            return result;
        } catch (IOException e) {
            System.out.println("有问题了" + e.getMessage());
            return null;
        }
    }

    private static InputStream BaseToInputStream(String base64string) {
        ByteArrayInputStream stream = null;
        try {
            BASE64Decoder decoder = new BASE64Decoder();
            byte[] bytes1 = decoder.decodeBuffer(base64string);
            stream = new ByteArrayInputStream(bytes1);
        } catch (Exception e) {
            // TODO: handle exception
        }
        return stream;
    }

}
