package hello.upload.controller;
import hello.upload.domain.UploadFile;
import hello.upload.domain.Item;
import hello.upload.domain.ItemRepository;
import hello.upload.file.FileStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
//하나의 첨부파일을 다운로드 업로드하고, 여러 이미지 파일을 한번에 업로드할 수 있는 기능
public class ItemController {
    private final ItemRepository itemRepository;
    private final FileStore fileStore;

    @GetMapping("/items/new")
    public String newItem(@ModelAttribute ItemForm form){
        return "item-form";
    }

    @PostMapping("/items/new")
    public String saveItem(@ModelAttribute ItemForm form, RedirectAttributes redirectAttributes) throws IOException {
        //사용자가 업로드한 하나의 첨부 파일을 서버에 저장하고 UploadFile이라는 파일 정보 객체 반환
        UploadFile attachFile = fileStore.storeFile(form.getAttachFile());

        //사용자가 업로드한 이미지 파일들을 서버에 저장하고 UploadFile 리스트를 반환
        List<UploadFile> storeImageFiles =
                fileStore.storeFiles(form.getImageFiles());

        //데이터베이스에 저장
        Item item = new Item();
        item.setItemName(form.getItemName());
        item.setAttachFile(attachFile);
        item.setImageFiles(storeImageFiles);
        itemRepository.save(item);

        //redirect시 itemID 파라미터를 전달
        redirectAttributes.addAttribute("itemId", item.getId());
        //상세 페이지로 redirect
        return "redirect:/items/{itemId}";
    }

    @GetMapping("/items/{id}")
    public String items(@PathVariable Long id, Model model) {
        Item item = itemRepository.findById(id);
        model.addAttribute("item", item);
        return "item-view";
    }

    //이미지는 아래와 같은 다른 컨트롤러를 작성해줘야 함
    @ResponseBody
    @GetMapping("/images/{filename}")
    public Resource downloadImage(@PathVariable String filename) throws
            MalformedURLException {
        //"file:/C:/Users/.../uuid값.png"를 반환
        return new UrlResource("file:" + fileStore.getFullPath(filename));
    }

    //첨부파일 다운로드하기
    //ResponseEntity<Resource>: 응답 본문에 Resource, 응답 헤더에는 파일 이름 등을 담음
    @GetMapping("/attach/{itemId}")
    public ResponseEntity<Resource> downloadAttach(@PathVariable Long itemId)
            throws MalformedURLException {
        //itemId로 Item 객체 조회
        Item item = itemRepository.findById(itemId);

        //파일 이름 정보 추출 ( 서버에 저장된 실제 파일명과 사용자가 업로드한 원래 파일명)
        String storeFileName = item.getAttachFile().getStoreFileName();
        String uploadFileName = item.getAttachFile().getUploadFileName();
        //저장된 파일을 URL 기반으로 로드
        /**
         * fileStore.getFullPath(): 저장된 파일의 전체 경로를 반환
         * UrlResource(...): 해당 경로의 파일을 읽을 수 있는 Spring Resource 객체로 변환
         * */
        UrlResource resource = new UrlResource("file:" +
                fileStore.getFullPath(storeFileName));
        log.info("uploadFileName={}", uploadFileName);
        //파일 이름을 UTF-8로 인코딩(브라우저에서 한글 파일명 등 특수 문자가 깨지지 않도록 인코딩)
        String encodedUploadFileName = UriUtils.encode(uploadFileName,
                StandardCharsets.UTF_8);
        //Content-Disposition 헤더 설정: 브라우저가 이 응답을 다운로드로 인식하도록 설정
        //attachment;filename="이력서.pdf" 형태로 헤더 설정
        String contentDisposition = "attachment; filename=\"" +
                encodedUploadFileName + "\"";
        //파일을 응답으로 반환
        return ResponseEntity.ok() //200 응답
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition) //헤더에 Content-Disposition 설정(브라우저에서 "다운로드 창" 띄움)
                .body(resource); //응답 본문에 데이터를 포함(resource)
    }
}
