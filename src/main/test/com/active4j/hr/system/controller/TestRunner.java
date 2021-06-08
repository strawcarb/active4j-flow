package com.active4j.hr.system.controller;


import com.active4j.hr.Active4jhrApplication;
import com.active4j.hr.system.util.PgpUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;

@SpringBootTest(classes= Active4jhrApplication.class)
public class TestRunner {
    @Autowired
    private Environment environment;

    /**
     * 文件签名，并生成签名问文件，文件路径，与原签名一直
     * 建议：签名后的源文件建议重新生成，保留源文件
     */
    @Test
    public void  testAddSigunature() {

        String pgpPwd = environment.getProperty("pgp.pwd");
        String pgpPublicKeyPath = environment.getProperty("pgp.public.key.path");
        String pgpSourceFilePath= environment.getProperty("pgp.source.file.path")+"/"+"message.zip";
        String pgpPrivateKeyPath = environment.getProperty("pgp.private.key.path");

        // 文件签名
        this.addSigunature(pgpSourceFilePath,pgpPrivateKeyPath,pgpPwd);
        // 文件签名校验
        this.verifySigunature(pgpSourceFilePath,pgpPublicKeyPath);
    }

    private  void addSigunature(String pgpSourceFilePath,String privateKeyPath,String pgpPwd){
        byte[] sign= PgpUtils.signatureCreate(pgpSourceFilePath, privateKeyPath,
                pgpSourceFilePath+"." + "asc", pgpPwd);
        System.out.println("签名校验结果："+ new String(sign));
    }

    private  void verifySigunature(String pgpSourceFilePath,String pgpPublicKeyPath){
        boolean flag = PgpUtils.verifySignature(pgpSourceFilePath, pgpPublicKeyPath, pgpSourceFilePath+"." + "asc");
        System.out.println(flag);
    }

    /**
     * 文件内容加密
     */
    /*public  void encryptFile(String pgpPublicKeyPath)  throws  Exception{
        PgpUtils pgpUtils = PgpUtils.getInstance();
        PGPPublicKey pgpPublicKey = pgpUtils.readPublicKey(new FileInputStream("/Users/bijia/temp/self_gen/public-key.txt"));
        OutputStream os = new FileOutputStream(new File("/Users/bijia/temp/20191014encrypt_file.txt"));
        pgpUtils.encryptFile(os,"/Users/bijia/temp/origin_file.txt",pgpPublicKey,false,false);

    }*/

    /**
     * 文件内容解密
     */
   /* private void decryptFile()  throws  Exception{
        PgpUtils pgpUtils = PgpUtils.getInstance();
        pgpUtils.decryptFile(new FileInputStream(new File("/Users/bijia/temp/20191014encrypt_file.txt")),
                new FileOutputStream(new File("/Users/bijia/temp/20191014decrypt_file.txt")),
                new FileInputStream(new File("/Users/bijia/temp/self_gen/private-key.txt")),
                "12345678".toCharArray());
    }*/
}
