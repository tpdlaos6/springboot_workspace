package org.zerock.mreview.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.net.URLEncoder;

@Data
@AllArgsConstructor
public class UploadResultDTO {

    private String fileName;
    private String uuid;
    private String folderPath;

    //원본
    public String getImageURL() {
        try {
            return URLEncoder.encode(folderPath+"/"+uuid+"_"+fileName,"UTF-8"); // URLEncoder.encode() : 인코딩하는 함수
            // 인코딩하는 이유는 나중에 썸네일 미리보기를 만들건데, 썸네일을 클릭하면
            //원본파일을 크게 보여준다던가 하는 방식. 이때 인코딩해서 받아와서 써먹을라고.
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    //썸네일
    public String getThumbnailURL(){
        try {
            return URLEncoder.encode(folderPath+"/s_"+uuid+"_"+fileName,"UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
