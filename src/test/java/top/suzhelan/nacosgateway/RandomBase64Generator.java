package top.suzhelan.nacosgateway;

import java.security.SecureRandom;
import java.util.Base64;


public class RandomBase64Generator {

    public static String generateRandomBase64() {
        // 创建安全随机数生成器
        SecureRandom secureRandom = new SecureRandom();

        // 创建32字节的随机数据数组
        byte[] randomBytes = new byte[32];
        secureRandom.nextBytes(randomBytes);

        // 使用Base64编码并返回字符串
        return Base64.getEncoder().encodeToString(randomBytes);
    }

    public static void main(String[] args) {
        // 生成并打印随机Base64字符串
        System.out.println(generateRandomBase64());
    }
}
