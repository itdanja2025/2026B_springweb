package example.day13_;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Service
public class FileService {

    // [*] 업로드 경로
    // 방법1) 프로젝트내 src : 개발자가 코드를 작성하는 폴더
    // 방법2) 프로젝트내 build : 서버 실행 했을때 컴파일된 코드 즉] 서버(24시간) 의 갖는 실행 된 폴더
    // 1. 현재 프로젝트의 최상위 디렉토리(폴더) 경로 찾기
    private String baseDir = System.getProperty("user.dir");
    // 2. 방법2 처럼 개발자폴더가 아닌 실행된 서버의 폴더로 업로드 경로 지정하기 , *개발환경* 에 따라 달라진다.
    private String uploadPath = baseDir + "/build/resources/main/static/upload/";

    // [1] 파일 업로드
    public String fileUpload(MultipartFile multipartFile) {
        if (multipartFile == null || multipartFile.isEmpty()) {
            return null;
        }

        // 폴더 없으면 생성
        File dir = new File(uploadPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // UUID + 파일명 생성
        String fileName = UUID.randomUUID().toString() + "_" + multipartFile.getOriginalFilename().replaceAll("_", "-");

        try {
            multipartFile.transferTo(new File(uploadPath + fileName));
            return fileName;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    // [2] 파일 다운로드
    public void fileDownload(String fileName, HttpServletResponse response) {
        File file = new File(uploadPath + fileName);
        if (!file.exists()) {
            return;
        }

        try {
            // 원본 파일명 추출 및 한글 인코딩
            String originalName = fileName.substring(fileName.indexOf("_") + 1);
            String encodedName = UriUtils.encode(originalName, StandardCharsets.UTF_8);

            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + encodedName + "\"");

            FileInputStream in = new FileInputStream(file);
            OutputStream out = response.getOutputStream();

            in.transferTo(out); // 바이트 스트리밍 전송

            in.close();
            out.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    // [3] 파일 삭제
    public boolean fileDelete(String fileName) {
        File file = new File(uploadPath + fileName);
        if (file.exists()) {
            return file.delete();
        }
        return false;
    }
}