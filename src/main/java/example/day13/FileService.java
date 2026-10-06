package example.day13;

import java.io.File;
import java.io.FileInputStream;
import java.net.URLEncoder;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

@Service 
public class FileService {
    // src폴더: 실행전(서버에 업로드전) 폴더로 개발자가 코드 작성하는 폴더
    // build폴더: 실행후(서버에 업로드된) 폴더로 개발자가 작성한 코드 실행(컴파일)한 결과물 폴더
    // * 일반사용자들은 업로드할 경우 개발자폴더(src) 가 아닌 서버폴더(build)에 업로드 해야한다.
    // * 추후에 AWS(클라우드) 경우 에는 클라우드 IP

    // [1] 업로드 경로 설정
    // 1. 현재 프로젝트의 최상위 폴더찾기 
    private String baseDir = System.getProperty("user.dir");
    // 2. 최상위폴더 이후로 build 폴더로 업로드할 경로 지정 
    private String uploadPath = baseDir+"/build/resources/main/static/upload/";

    // [2] 업로드 함수
    public String fildUpload( MultipartFile multipartFile ){
        // 1. 업로드할 파일의 MultipartFile 인터페이스 가져오기 
        // 2. 만약에 업로드 파일이 없으면 취소
        if( multipartFile == null || multipartFile.isEmpty() ){ return null; }
        // 3. 만약에 업로드 폴더가 없으면 폴더 생성 , File객체란? 자바가 운영체제의 파일 조작 클래스
        File dir = new File( uploadPath ); // 설정한 경로 File 객체에 대입한다. 
        if( !dir.exists() ){ dir.mkdir(); } // 설정한 경로에 폴더가 없으면 폴더 생성 
        // 4. 업로드할 파일명이 중복 방지 --> 1] UUID 2] 업로드날짜/시간 3] PK 등등 식별 추가한다.
        // 왜? 유재석/강호동이 서로 다른 파일의 같은 파일명으로 짱구.jpg 업로드 한 경우에 다른 파일 취급하기 위해서
        // 예] 짱_구.jpg --->  uuid _ 짱-구.jpg
        // _역할은 uuid 와 실제파일명과 구분용도 , 파일명에 _언더바 존재하면 안된다. 
        // replaceAll( "기존문자" , "새로운문자" ) , 문자열내 기존문자들을 새로운문자로 치환/교환 함수 
        String fileName = UUID.randomUUID().toString()+"_"+multipartFile.getOriginalFilename()
                                                                        .replaceAll("_", "-");
        // 5. 업로드 , multipartFile.transferTo( 업로드할file경로 ); , 예외처리발생 
        try{
            multipartFile.transferTo( new File( uploadPath+fileName ) );
            return fileName;
        }catch(Exception e ){ System.out.println(e);}
        return null;
    }   

    // [3] 다운로드 함수
    // C드라이브파일 --FileInput--> JAVA --ServletOut--> 브라우저
    public void fileDownload( String fileName , HttpServletResponse response ){
        // 1. 다운로드할 파일명 과 HTTP응답객체 받는다.
        // 2. 다운로드할 파일명과 업로드 경로 조합 
        String downloadPath = uploadPath + fileName; // 업로드경로 + 파일명;
        // 3. 만약에 해당 경로에 파일이 없으면 취소
        File file = new File( downloadPath ); if( !file.exists() ){ return; }
        // 4. 있으면 파일 읽어오기 , FileInputStream , 예외발생 
        try{
            long fileSize = file.length(); // 파일명 (바이트) 용량확인
            byte[] bytes = new byte[ (int)fileSize ]; // 파일 용량만큼 바이트 배열 생성 
            FileInputStream fin = new FileInputStream( downloadPath ); // 파일입력객체 생성
            fin.read( bytes ); // 파일입력객체가  입럭온 바이트들을 바이트배열에 저장 
            fin.close(); // 스트림(이동)간 안전하게 스트림 직접 닫기 
        // 6. 다운로드 형식 지정 : 브라우저 마다 상이 
        // 실제 파일명으로 찾기 , UUID_짱구.jpg ---> 짱구.jpg
        // .split("기준문자"); , 문자열내 특정 기준문자로 분해
        String realFileName = fileName.split("_")[1]; // _언더바 기준으로 쪼개서 2번째 인덱스 값 가져오기
        // HTTP 헤더에 다운로드 형식 지정 , 한글이 지원 안된다. URLEncoder.encode
        response.setHeader( "Content-Disposition" , 
                            "attactment;filename="+URLEncoder.encode( realFileName , "UTF-8"));
        // 5. 서버로 가져온 파일(바이트들)을 HTTP 응답하기 , 현재 다운로드 요청한 서블릿의 출력스트림 가져오기 
            ServletOutputStream fout = response.getOutputStream();
            fout.write( bytes ); // 서블릿출력스트림 객체로 앞전에 읽어온 파읿바이트배열 내보내기  
            fout.close();
        }catch( Exception e ){ System.out.println( e );}
    }
    // [4] 파일 삭제 함수
    public boolean fileDelete( String fileName ){
        // 1. 삭제할 파일명 과 경로 조합 
        String deleteFilePath = uploadPath+fileName;
        // 2. 만약에 경로에 파일이 존재하면 파일 삭제 
        File file = new File( deleteFilePath );
        if( file.exists() ){
            file.delete(); // 해당 경로에 파일 삭제 함수 
            return true;
        }else{ return false; }
    }
} // service end 
