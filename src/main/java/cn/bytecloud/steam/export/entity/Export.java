package cn.bytecloud.steam.export.entity;

import cn.bytecloud.steam.base.entity.BaseEntity;
import cn.bytecloud.steam.constant.ModelConstant;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import static cn.bytecloud.steam.constant.ModelConstant.*;

/**
 * 智能导出
 */
@Data
@Document(collection = T_EXPORT)
public class Export extends BaseEntity {

    //名字
    @Field(EXPORT_NAME)
    private String name;

    //压缩文件地址
    @Field(EXPORT_PATH)
    private String path;

    //下载链接
    @Field(EXPORT_DOWNLOAD_URL)
    private String downloadUrl;

    //状态
    @Field(EXPORT_STATUS)
    private ExportStatus status;
}
