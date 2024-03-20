package org.zerock.mreview.controller;

import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.zerock.mreview.dto.UploadResultDTO;

import java.io.File;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@Log4j2
public class UploadController {

    @Value("${org.zerock.upload.path}")
    private String uploadPath;

    @PostMapping("/uploadAjax")
    public ResponseEntity<List<UploadResultDTO>> uploadFile(MultipartFile[] uploadFiles){ // 동시에 여러 파일을 업로드하기 위해, []배열로 처리

        List<UploadResultDTO> resultDTOList=new ArrayList<>();

        for(MultipartFile uploadFile: uploadFiles) { // for문을 써서 배열에서 하나씩 꺼내다가 uploadFile에 넣음

            if(uploadFile.getContentType().startsWith("image") == false) { // 확장자 파일은 무시하고, getContentType()만 조사.
                                                                        // "image"로 설정해서 이미지 파일만 업로드 가능
                                                                        // "image"가 false이면 중지하고 그냥 리턴. (이건 서버에서 작동하는 코드)
                log.info("==contentType=== : "+uploadFile.getContentType());
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }

            //옛날 IE나 Edge는 전체 경로가 나오므로 파일명만 구하기 위해 아래 코드들이 필요. 크롬은 노상관.
            String originalName=uploadFile.getOriginalFilename(); // getOriginalFilename()는 path값도 같이 포함되어 있는 메서드
            String fileName=originalName.substring(originalName.lastIndexOf("\\")+1); // substring으로 잘라서 파일의 이름만 찾아옴.
            log.info("====originalName==== : "+originalName);
            log.info("====fileName==== : "+fileName);

            //오늘 날짜 폴더 생성
            String folderPath=makeFolder();

            //uuid
            String uuid= UUID.randomUUID().toString(); // 이메일 인증할때 난수형으로 인증번호 날라오는 방식과 유사. 여기에도 uuid를 활용 가능


            //저장할 파일 이름 중간에 "_"를 이용해서 구분
            String saveName=uploadPath+File.separator+folderPath+File.separator+uuid+"_"+fileName;
            log.info("=====================saveName==============   : " + saveName);
            Path savePath = Paths.get(saveName); // Path객체로 변환
            try{
                uploadFile.transferTo(savePath);  // 파일저장

                //썸네일 생성
                String thumbnailSaveName=uploadPath+File.separator+folderPath+File.separator+"s_"+uuid+"_"+fileName; //썸네일 파일 이름은 중간에 s_로 시작하도록
                File thumbnailFile=new File(thumbnailSaveName);
                Thumbnailator.createThumbnail(savePath.toFile(),thumbnailFile,100,100); // savePath.toFile() => 원본,
                                                                                        // thumbnailFile,100,100=> 썸네일.
                                                                                        // 100,100은 사이즌데... 일반적으로 100,100이렇게 안함. 맨날 정사각형으로 나와서;;;

                resultDTOList.add(new UploadResultDTO(fileName,uuid,folderPath)); // 파일 목록에 추가
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new ResponseEntity<>(resultDTOList, HttpStatus.OK);
    }

    private String makeFolder(){
        String str= LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")); // 현재 날짜(LocalDate.now())를 구해서, "yyyy/MM/dd"형식으로 format함.
        log.info("==str========= : "+str);
        String folderPath = str.replace("/", File.separator); // replace를 이용해 //를 separator로 바꿔라. 여기서 separator는 os별 구분자.
                                                                    //윈도우는 역슬래쉬(\)를 씀
                                                                    // 바꾼 값을 folderPath에 담아서
        log.info("===folderPath ======= : "+folderPath);
        File uploadPathFolder = new File(uploadPath, folderPath); // uploadPath에 folderPath 방식(위에서 설정한대로 2024\03\20 이런 식으로) 으로 폴더 생성
        if (uploadPathFolder.exists() == false) {
            uploadPathFolder.mkdirs(); // mkdir()이 있고, mkdirs()가 있음. 폴더 하나 만들땐 단수형 메서드, 두개이상일땐 복수형 메서드 사용
        }
        return folderPath;
    }

    //image를 찾아서 byte[]로 리턴
    @GetMapping("/display ")
    public ResponseEntity<byte[]> getFile(String fileName) { // 이미지를 읽어서 byte형태의 배열로 보냄
        ResponseEntity<byte[]> result = null;
        try {
            String srcFileName= URLDecoder.decode(fileName,"UTF-8"); //url decode
            File file=new File(uploadPath+File.separator+srcFileName);
            HttpHeaders header=new HttpHeaders();
            header.add("Content-Type", Files.probeContentType(file.toPath())); // probeContentType() : file.toPath 경로에 있는 파일의 type을 구하는 메서드
            result = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file), header, HttpStatus.OK); // copyToByteArray(file) : file을 Byte배열로 변환
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return result;
    }


    // image삭제
    @PostMapping("/removeFile")
    public ResponseEntity<Boolean> removeFile(String fileName){ // 여기서 fileName은 원본 파일
        String srcFileName = null;
        try {
            srcFileName = URLDecoder.decode(fileName,"UTF-8"); // fileName을 디코드 한 다음,
            File file = new File(uploadPath +File.separator+ srcFileName); // 전체 경로를 구하고,
            boolean result = file.delete();                                         //삭제

            File thumbnail = new File(file.getParent(), "s_" + file.getName()); // 썸네일은 원본명 앞에 s_만 붙이면 됨
            result = thumbnail.delete();                                             //썸네일 삭제

            return new ResponseEntity<>(result, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
