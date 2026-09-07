package example.Practice4.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import example.Practice4.service.EnrollService;

@RestController@RequestMapping("/api/enroll")
public class EnrollController {
    @Autowired private EnrollService enrollService;
}
