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

    public   void addSigunature(String filePath){
       String pgpPwd = environment.getProperty("pgp.pwd");
        String pgpPublicKeyPath = environment.getProperty("pgp.public.key.path");
        String pgpPrivateKeyPath = environment.getProperty("pgp.private.key.path");
        byte[] sign= PgpUtils.signatureCreate(filePath, pgpPublicKeyPath,
                filePath+"." + "asc", pgpPwd);
//        System.out.println("签名校验结果："+ new String(sign));
    }

    private  void verifySigunature(String pgpSourceFilePath,String pgpPublicKeyPath){
        boolean flag = PgpUtils.verifySignature(pgpSourceFilePath, pgpPublicKeyPath, pgpSourceFilePath+"." + "asc");
        System.out.println(flag);
    }

}
