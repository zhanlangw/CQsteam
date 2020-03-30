package cn.bytecloud.steam.file.service;

import cn.bytecloud.steam.exception.ByteException;
import org.apache.commons.fileupload.FileUploadException;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


public interface FileService {

    public String saveFileEntity(HttpServletRequest request, Integer maxSize) throws IOException,
            FileUploadException, ByteException;

    public boolean deleteFile(String path) throws ByteException;

    String upload(HttpServletRequest request, Integer fileType) throws FileUploadException, ByteException, IOException;
}
