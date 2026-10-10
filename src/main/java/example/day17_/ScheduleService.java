package example.day17_;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ScheduleService {

    // * 컨트롤러 유무 와 상관없이 특정 시간이 되면 서비스 자동 실행 *
    // 비동기 기반의 구조 , 목적 : 보안기능, 자동화 , 백그라운드처리
    // AppStart 클래스 위에 @EnableScheduling 주입한다.

    // [1] 3초 마다 실행되는 스케줄 설정
    @Scheduled( fixedRate = 3000 ) // 밀리초 , 3초
    public void task1(){
        System.out.println("ScheduleService.task1"); // soutm
    }

    // [2] 5초(변수값) 마다 실행되는 스케줄 설정
    final int time = 5000; // 밀리초(5초)
    @Scheduled( fixedRate = time )
    public void task2(){
        System.out.println("ScheduleService.task2");
    }

    // [3] 시스템의 날짜/시간 기준으로 스케줄링 (크론식 예제 1)
    // 매시각 5초 간격으로 실행 (예: 12:00:00, 12:00:05, 12:00:10 ...)
    @Scheduled( cron = "*/5 * * * * *" )
    public void task3(){
        System.out.println("ScheduleService.task3 - 5초 주기");
    }

    // [4] 시스템의 날짜/시간 기준으로 스케줄링 (크론식 예제 2)
    // 매시각 정각(0초) 기준으로 매 1분마다 실행 (예: 11:13:00, 11:14:00 ...)
    @Scheduled( cron = "0 */1 * * * *" )
    public void task4(){
        System.out.println("ScheduleService.task4 - 1분 주기");
    }
}
/*
    cron 패턴
        1. 형식 : @Scheduled( cron = "초 분 시 일 월 요일" )
        2. 첫번째 초 : 0 ~ 59        두번째 분 : 0 ~ 59
        3. 세번째 시 : 0 ~ 23        네번째 일 : 1 ~ 31
        5. 다섯번째 월 : 1 ~ 12       여섯번째 요일 : 0 ~ 6 (0:일요일 ~ 3:수요일 ~ 6:토요일, 또는 7:일요일)

    예시]
        1) 주말(일/토) 마다 오전 10시 : @Scheduled( cron = "0 0 10 * * 0,6" )
        2) 일요일 마다 오전 9시       : @Scheduled( cron = "0 0 9 * * 0" )
        3) 매월 1일 오전 8시 30분     : @Scheduled( cron = "0 30 8 1 * *" )
        4) 평일(월~금) 오후 6시 정각   : @Scheduled( cron = "0 0 18 * * 1-5" )

    비동기 == 스케줄링 == 백그라운드
        1) HTTP 와 상관없이 자바(서버)내 내부 로직 실행
        2) HTTP Response(응답) 제약 있다. 응답이 필요할 경우 추가 기술 필요
            HTTP( 무상태/비연결 ) vs Socket( 상태/연결유지 ) vs Message
                HTTP : CRUD , 기초 통신 , 사용자가 요청이 있어야만 응답 하는 구조
                Socket : 실시간 양방향 통신 , 사용자가 요청이 없어도 응답 받아야 하는경우 , 예] 채팅 / 실시간데이터 / 푸시알림
*/