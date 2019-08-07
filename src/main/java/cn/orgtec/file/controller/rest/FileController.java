package cn.orgtec.file.controller.rest;

import cn.hutool.crypto.SecureUtil;
import cn.orgtec.file.entity.FileEntity;
import cn.orgtec.file.service.FileService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;

/**
 *  文件图片服务控制器
 *
 *   RequiredArgsConstructor
 *   会生成一个包含常量（final），和标识了@NotNull的变量 的构造方法
 * @author Yibo Zhang
 * @date 2019/08/07
 */
@RestController
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @Value("${server.address}")
    private String serverAddress;

    @Value("${server.port}")
    private String serverPort;

    /**
     * 上传接口
     *
     * @param file  文件
     * @return      ResponseEntity<String>
     */
    @PostMapping(value = "/upload")
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file) {
        FileEntity returnFile;
        try {
            FileEntity entity = new FileEntity(file.getOriginalFilename(), file.getContentType(), file.getSize(),
                    new Binary(file.getBytes()));
            entity.setMd5(SecureUtil.md5(file.getInputStream()));
            returnFile = fileService.saveFile(entity);
            String path = "//" + serverAddress + ":" + serverPort + "/view/" + returnFile.getId();
            return ResponseEntity.status(HttpStatus.OK).body(path);

        } catch (IOException  ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }

    }

    /**
     * 根据 id 删除文件
     *
     * @param id 文件 id
     * @return
     */
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deleteFile(@PathVariable(value = "id") String id) {

        try {
            fileService.removeFile(id);
            return ResponseEntity.status(HttpStatus.OK).body("DELETE Success!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    /**
     * 删除全部文件
     *
     */
    @DeleteMapping(value = "/deleteAll")
    public ResponseEntity<String> deleteAll() {

        try {
            fileService.deleteAll();
            return ResponseEntity.status(HttpStatus.OK).body("DELETE Success!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    /**
     * 分页查询文件
     *
     * @param pageIndex  页码
     * @param pageSize   每页显示的数量
     * @return           List<FileEntity>
     */
    @GetMapping(value = "/files/{pageIndex}/{pageSize}")
    public List<FileEntity> listFilesByPage(@PathVariable(value = "pageIndex") int pageIndex, @PathVariable(value = "pageSize") int pageSize) {
        return fileService.listFilesByPage(pageIndex, pageSize);
    }

    /**
     * 获取文件片信息
     *
     * @param id  文件id
     * @return    ResponseEntity<Object>
     */
    @GetMapping(value = "/files/{id}")
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
