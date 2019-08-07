package cn.orgtec.file.service;

import cn.orgtec.file.entity.FileEntity;

import java.util.List;
import java.util.Optional;

/**
 * 文件服务接口
 *
 * @author Yibo Zhang
 * @date 2019/08/07
 */
public interface FileService {

    /**
     * 保存文件
     *
     * @param file 文件
     * @return FileEntity
     */
    FileEntity saveFile(FileEntity file);

    /**
     * 根据 id 删除文件
     *
     * @param id 文件 id
     */
    void removeFile(String id);

    /**
     * 删除存储库管理的所有实体
     */
    void deleteAll();

    /**
     * 根据id获取文件
     *
     * @param id 文件id
     * @return FileEntity
     */
    Optional<FileEntity> getFileById(String id);

    /**
     * 分页查询，按上传时间降序
     *
     * @param pageIndex 页码
     * @param pageSize  每页显示的数量
     * @return List<File>
     */
    List<FileEntity> listFilesByPage(int pageIndex, int pageSize);
}
