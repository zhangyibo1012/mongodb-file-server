package cn.orgtec.file.controller.ui;

import cn.hutool.core.util.IdUtil;
import cn.hutool.crypto.SecureUtil;
import cn.orgtec.file.entity.FileEntity;
import cn.orgtec.file.service.FileService;
import lombok.AllArgsConstructor;
import org.bson.types.Binary;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *  视图控制器
 *
 * @author Yibo Zhang
 * @date 2019/08/07
 */
@Controller
@AllArgsConstructor
public class FileUiController {

    private final FileService fileService;

    /**
     *  首页
     * @param model  Model
     * @return       index
     */
    @GetMapping(value = "/")
    public String index(Model model) {
        // 展示最新二十条数据
        model.addAttribute("files", fileService.listFilesByPage(0, 20));
        return "index";
    }

    /**
     *  文件上传
     *
     * @param file  文件
     * @param redirectAttributes  重定向隐藏了参数，链接地址上不直接暴露
     * @return    首页
     */
    @PostMapping("/")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        String originalFilename = file.getOriginalFilename();
        String suffixName = originalFilename.substring(originalFilename.lastIndexOf("."));
        try {
            FileEntity entity = new FileEntity(IdUtil.simpleUUID() + suffixName, file.getContentType(), file.getSize(),
                    new Binary(file.getBytes()));
            entity.setMd5(SecureUtil.md5(file.getInputStream()));
            fileService.saveFile(entity);
        } catch (Exception ex) {
            ex.printStackTrace();
            redirectAttributes.addFlashAttribute("message", "Your " + file.getOriginalFilename() + " is wrong!");
            return "redirect:/";
        }

        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded " + file.getOriginalFilename() + "!");

        return "redirect:/";
    }
}
