package example.day07;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController@RequestMapping("/day07/todo")
public class TodoController {
    @Autowired
    private TodoRespository todoRespository;

    @GetMapping("")
    public Map<String,Object> method3(){
        
        return todoRespository
            .myquery3("안녕1", "안녕2");

    }

}
