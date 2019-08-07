package cn.orgtec.file.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * 文件/图片实体类
 *
 * @author Yibo Zhang
 * @date 2019/08/07
 */
@Data
@Document
@EqualsAndHashCode
public class FileEntity {

    /**
     * 唯一标识
     */
    @Id
    private String id;

    /**
     * 文件名称
     */
    private String name;

    /**
     * 文件类型
     */
    private String contentType;

    /**
     * 文件大小
     */
    private long size;

    /**
     * 上传日期
     */
    private LocalDateTime uploadDate;

    /***
     *  md5 摘要算法  用来检测这些资源文件的完整性
     */
    private String md5;

    /**
     * 文件二进制内容
     */
    private Binary content;

    /**
     * path
     */
    private String path;

    /**
     * 构造方法初始化上传时间
     *
     * @param name        文件名称
     * @param contentType 文件类型
     * @param size        文件大小
     * @param content     文件二进制内容
     */
    public FileEntity(String name, String contentType, long size, Binary content) {
        this.name = name;
        this.contentType = contentType;
        this.size = size;
        this.uploadDate = LocalDateTime.now();
        this.content = content;
    }

}
