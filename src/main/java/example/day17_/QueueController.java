package example.day17_;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/queue")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class QueueController {

    private final QueueService queueService;

    @PostMapping("/enter")
    public Map<String, Object> enter() {
        return queueService.enterQueue();
    }

    @GetMapping("/status/{taskId}")
    public Map<String, Object> getStatus(@PathVariable(name = "taskId") String taskId) {
        return queueService.getQueueStatus(taskId);
    }
}