package cn.orgtec.file.controller.rest;

import cn.orgtec.file.entity.FileEntity;
import cn.orgtec.file.service.FileService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;

/**
 *  文件图片服务控制器
 *
 * @author Yibo Zhang
 * @date 2019/08/07
 */
@RestController
@AllArgsConstructor
public class FileController {

    private final FileService fileService;

    /**
     * 分页查询文件
     *
     * @param pageIndex  页码
     * @param pageSize   每页显示的数量
     * @return           List<FileEntity>
     */
    @GetMapping(value = "files/{pageIndex}/{pageSize}")
    public List<FileEntity> listFilesByPage(@PathVariable(value = "pageIndex") int pageIndex, @PathVariable(value = "pageSize") int pageSize) {
        return fileService.listFilesByPage(pageIndex, pageSize);
    }

    /**
     * 获取文件片信息
     *
     * @param id
     * @return
     * @throws UnsupportedEncodingException
     */
    @GetMapping("files/{id}")
    public ResponseEntity<Object> serveFile(@PathVariable(value = "id") String id) throws UnsupportedEncodingException {

        Optional<FileEntity> file = fileService.getFileById(id);

        if (file.isPresent()) {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; fileName=" + new String(file.get().getName().getBytes("utf-8"), "ISO-8859-1"))
                    .header(HttpHeaders.CONTENT_TYPE, "application/octet-stream")
                    .header(HttpHeaders.CONTENT_LENGTH, file.get().getSize() + "").header("Connection", "close")
                    .body(file.get().getContent().getData());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File was not fount");
        }
    }

    /**
     * 根据 id 在线显示文件
     * @param id  文件 id
     * @return    FileEntity
     */
    @GetMapping("/view/{id}")
    public ResponseEntity<Object> serveFileOnline(@PathVariable(value = "id") String id) {

        Optional<FileEntity> file = fileService.getFileById(id);

        return file.<ResponseEntity<Object>>map(fileEntity -> ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "fileName=\"" + fileEntity.getName() + "\"")
                .header(HttpHeaders.CONTENT_TYPE, fileEntity.getContentType())
                .header(HttpHeaders.CONTENT_LENGTH, fileEntity.getSize() + "").header("Connection", "close")
                .body(fileEntity.getContent().getData())).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("File was not fount"));
    }
}
