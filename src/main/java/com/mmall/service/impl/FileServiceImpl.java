package com.mmall.service.impl;

import com.google.common.collect.Lists;
import com.mmall.service.IFileService;
import com.mmall.util.FTPUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by tino on 10/14/18.
 */

@Service("iFileService")
public class FileServiceImpl implements IFileService {

    private Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    public String upload(MultipartFile file, String path) {
        // extension
        // if file name is 'abd.jpg', fileExtensionName is 'jpg'
        String fileName = file.getOriginalFilename();
        String fileExtensionName = fileName.substring(fileName.lastIndexOf(".") + 1);
        // avoid uploading a file with same name
        String uploadFileName = UUID.randomUUID().toString() + "." + fileExtensionName;
        logger.info("Begin to upload file, file name:{}, uploading path:{}, new file name:{}", fileName, path, uploadFileName);

        // create directory under this path
        File fileDir = new File(path);
        if(!fileDir.exists()) {
            // file is not writable even you have permission of tomcat
            // file can be modified after being published
            fileDir.setWritable(true);
            fileDir.mkdirs();// can create multiple file directories
        }

        // create target file in the directory uder this path
        File targetFile = new File(path, uploadFileName);

        try {
            // upload successfully
            file.transferTo(targetFile);
            //upload files to ftp
            FTPUtil.upLoadFile(Lists.newArrayList(targetFile));
            // delete files in upload
            targetFile.delete();
        } catch (IOException e) {
            logger.error("Cannot upload files", e);
            return null;
        }
        return targetFile.getName();
    }

}
