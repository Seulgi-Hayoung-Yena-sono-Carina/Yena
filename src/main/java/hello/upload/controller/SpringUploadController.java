package hello.upload.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Slf4j
@Controller
@RequestMapping("/spring")
public class SpringUploadController {

    @Value("${file.dir}")
    private String fileDir;

    @GetMapping("/upload")
    public String newFile() {
        return "upload-form";
    }

    @PostMapping("/upload")
    public String saveFile(@RequestParam String itemName,
                           @RequestParam MultipartFile file, HttpServletRequest
                                   request) throws IOException {
        log.info("request={}", request);
        log.info("itemName={}", itemName);
        log.info("multipartFile={}", file);
        if (!file.isEmpty()) { //파일이 비어있지 않으면
            //fileDir 경로에 업로드된 파일의 원본 파일 이름을 이어붙여 전체 파일 경로를 생성
            String fullPath = fileDir + file.getOriginalFilename(); //.getOriginalFilename(): 업로드 파일 명
            log.info("파일 저장 fullPath={}", fullPath);
            //업로드된 파일을 지정된 경로에 저장
            file.transferTo(new File(fullPath)); //.transferTo(): 파일 저장
        }
        return "upload-form"; //원래 폼 페이지로 돌아감
    }
}
