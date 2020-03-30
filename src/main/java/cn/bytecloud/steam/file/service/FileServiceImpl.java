package cn.bytecloud.steam.file.service;

import cn.bytecloud.steam.exception.ByteException;
import cn.bytecloud.steam.exception.ErrorCode;
import cn.bytecloud.steam.util.FileUtil;
import cn.bytecloud.steam.util.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.List;


@Slf4j
@Service
public class FileServiceImpl implements FileService {

    @Override
    public String saveFileEntity(HttpServletRequest request, Integer fileType) throws FileUploadException,
            IOException,
            ByteException {
        if (fileType == 5 || request.getContentLengthLong() <= getMaxFileSize(fileType)) {
            FileItemFactory factory = new DiskFileItemFactory();
            ServletFileUpload servletFileUpload = new ServletFileUpload(factory);
            List<FileItem> files = servletFileUpload.parseRequest(request);
            FileItem file = null;
            for (FileItem file1 : files) {
                if (file1.getFieldName().equals("file")) {
                    file = file1;
                }
            }

            assert file != null;
            InputStream in = file.getInputStream();
            String fileName = file.getName();
            if (fileName.contains("\\")) {
                fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
            }
            String dataPath = "/" + UUIDUtil.getUUID() + "/" + fileName;

            String path = FileUtil.getProjectPath() + dataPath;

            log.info("文件保存路径++" + path);
            File saveFile = new File(path);
            if (saveFile.exists() && saveFile.isFile()) {
                saveFile.delete();
                saveFile = new File(path);
            }

            // 判断路径是否存在,如果不存在就创建文件路径
            if (!saveFile.getParentFile().exists()) {
                final boolean mkdirs = saveFile.getParentFile().mkdirs();
            }

            // 将上传文件保存到一个目标文件当中
            OutputStream out = new FileOutputStream(saveFile);

            byte[] bytes = new byte[1024];
            int num;
            while ((num = in.read(bytes)) != -1) {
                out.write(bytes, 0, num);
            }
            in.close();
            out.close();
            return "/uploads" + dataPath;
        } else {
            throw new ByteException(ErrorCode.FILE_SIZE_ERROR);
        }
    }

    public long getMaxFileSize(Integer fileType) throws ByteException {
        switch (fileType) {
            case 1://ppt
            case 2://doc
            case 3://video
                return 521 * 1024 * 1024;
            case 4://图片
                return 10 * 1024 * 1024;
            default:
                throw new ByteException(ErrorCode.FILE_SIZE_ERROR);
        }
    }

    @Override
    public boolean deleteFile(String path) throws ByteException {
        File file = new File(FileUtil.getdeletePath(path));
        boolean exists = file.exists();
        boolean flag = file.isFile();
        if (flag && exists) {
            return file.delete();
        } else {
            throw new ByteException(ErrorCode.FAILURE);
        }

    }

    @Override
    public String upload(HttpServletRequest request, Integer fileType) throws FileUploadException, ByteException,
            IOException {
        return saveFileEntity(request, fileType);
    }

}
