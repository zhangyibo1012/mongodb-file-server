package cn.orgtec.file.repository;

import cn.orgtec.file.entity.FileEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * FileRepository 仓库
 *
 * @author Yibo Zhang
 * @date 2019/08/07
 */
public interface FileRepository extends MongoRepository<FileEntity, String> {
}
