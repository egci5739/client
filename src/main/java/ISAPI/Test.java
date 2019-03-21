package ISAPI;

public class Test {
    /*
     * 测试登陆
     * */
    public static void main(String[] args) {
        HttpsClientUtil.bHttpsEnabled = true;
        HttpsClientUtil.httpsClientInit("192.168.3.102", 443, "admin", "ceshi123456");
        System.out.println(HttpsClientUtil.httpsGet("https://192.168.3.102:443/ISAPI/Security/userCheck"));
    }
}
