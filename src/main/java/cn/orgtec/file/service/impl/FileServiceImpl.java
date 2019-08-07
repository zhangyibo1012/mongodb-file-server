package cn.orgtec.file.service.impl;

import cn.orgtec.file.entity.FileEntity;
import cn.orgtec.file.repository.FileRepository;
import cn.orgtec.file.service.FileService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * 文件服务接口实现类
 *
 * @author Yibo Zhang
 * @date 2019/08/07
 */
@Service
@AllArgsConstructor
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;

    @Override
    public FileEntity saveFile(FileEntity file) {
        return fileRepository.save(file);
    }

    @Override
    public void removeFile(String id) {
        fileRepository.deleteById(id);
    }

    @Override
    public void deleteAll() {
        fileRepository.deleteAll();
    }

    @Override
    public Optional<FileEntity> getFileById(String id) {
        return fileRepository.findById(id);
    }

    @Override
    public List<FileEntity> listFilesByPage(int pageIndex, int pageSize) {
        Sort sort = new Sort(Sort.Direction.DESC, "uploadDate");
        Pageable pageable = PageRequest.of(pageIndex, pageSize, sort);
        return fileRepository.findAll(pageable).getContent();
    }
}
