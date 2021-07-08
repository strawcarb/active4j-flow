package com.active4j.hr.activiti.util;

import com.active4j.hr.system.util.PgpUtils;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class AddSigunature {

    @Autowired
    private Environment environment;


    public void  sigunature(MultipartFile file) {

        String pgpPwd = environment.getProperty("pgp.pwd");
        String pgpPublicKeyPath = environment.getProperty("pgp.public.key.path");
        String pgpSourceFilePath= environment.getProperty("pgp.source.file.path")+"/"+file;
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

}
