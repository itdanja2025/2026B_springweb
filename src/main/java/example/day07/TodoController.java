package example.day07;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController@RequestMapping("/day07/todo")
public class TodoController {
    @Autowired
    private TodoRespository todoRespository;

    

}
