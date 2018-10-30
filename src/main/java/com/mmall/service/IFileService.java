package com.mmall.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * Created by tino on 10/14/18.
 */
public interface IFileService {

    String upload(MultipartFile file, String path);
}
