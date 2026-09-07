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

@RestController@RequestMapping("/api/enroll")
public class EnrollController {
    @Autowired private EnrollService enrollService;

    @PostMapping("") // { "studentId" : 1 , "courseId" : 1 , "status" :"수강신청" }
    public boolean 수강등록( @RequestBody EnrollDto enrollDto ){
        return enrollService.수강등록( enrollDto );
    }

    @GetMapping("/detail")
    public EnrollDto 수강조회( 
        @RequestParam( name = "enrollId") Integer enrollId){
        return enrollService.수강조회( enrollId );
    }

}
