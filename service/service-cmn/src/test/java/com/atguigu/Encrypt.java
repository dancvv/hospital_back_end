package com.atguigu;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.StandardPBEBigDecimalEncryptor;
import org.jasypt.util.text.BasicTextEncryptor;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class Encrypt {
    @Test
    public void String(){
        BasicTextEncryptor encryptor = new BasicTextEncryptor();
        encryptor.setPassword("weiwanglearnjava");
        System.out.println("origin sql password");
        String var1 = encryptor.encrypt("123456");
        System.out.println(var1);
        System.out.println("nacos addr");
        System.out.println(encryptor.encrypt("210.30.97.238:8849"));
        System.out.println("server add");
        System.out.println(encryptor.encrypt("210.30.97.238"));
        System.out.println("accessId");
        System.out.println(encryptor.encrypt("LTAI5tMCenC3SnWwNvi1Pxhz"));
        System.out.println("sercret");
        System.out.println(encryptor.encrypt("kJfg2bg09jQlLFqpS588e7DD2ehSx0"));

    }
}
