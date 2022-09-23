package white.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import white.common.R;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/common")
public class CommonController {
    @Value("${white.uploadPath}")
    private String uploadPath;
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file){
        File dir = new File(uploadPath);
        if (!dir.exists()){
           dir.mkdir();
        }
        UUID name = UUID.randomUUID();
        String[] strings = file.getOriginalFilename().split("\\.");
        try {
            file.transferTo(new File(uploadPath + name+"."+strings[1]));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return R.success(file.getOriginalFilename());
    }
    @GetMapping("/download")
    public void download(HttpServletResponse response,String name){
        try {
            FileInputStream inputStream = new FileInputStream(new File(uploadPath+name));
            ServletOutputStream outputStream = response.getOutputStream();
            response.setContentType("img/jpeg");
            byte[] bytes = new byte[1024];
            int len = 0;
            while ((len =inputStream.read(bytes)) != -1){
                outputStream.write(bytes,0,len);
                outputStream.flush();
            }
            outputStream.close();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
