package example.Practice4.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import example.Practice4.model.dto.EnrollDto;
import example.Practice4.service.EnrollService;

@RestController
@RequestMapping("/api/enroll")
public class EnrollController {

    @Autowired
    private EnrollService enrollService;

    // 등록기능3: 수강(Enroll) 정보 등록
    @PostMapping
    public boolean enrollAdd(@RequestBody EnrollDto enrollDto) {
        return enrollService.enrollAdd(enrollDto);
    }

    // 조회기능: 수강번호(enrollId)를 조회하면 학생명과 과정명을 포함한 수강정보를 조회 (RequestParam)
    @GetMapping
    public EnrollDto enrollFindParam(@RequestParam(name = "enrollId") Long enrollId) {
        return enrollService.enrollFind(enrollId);
    }
}
