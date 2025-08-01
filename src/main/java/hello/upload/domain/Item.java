package hello.upload.domain;

import lombok.Data;

import java.util.List;
@Data
public class Item {
    private Long id;
    private String itemName;
    private UploadFile attachFile;
    private List<UploadFile> imageFiles; //이미지 다중 업로드
}