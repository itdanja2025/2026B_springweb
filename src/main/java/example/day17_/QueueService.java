package example.day17_;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class QueueService {

    private final ThreadPoolTaskExecutor queueExecutor;
    private final AtomicInteger seq = new AtomicInteger(1);
    
    // 2개 스레드가 동시에 작업을 완료해 remove할 때 순회 에러가 나지 않는 Thread-safe 큐
    private final ConcurrentLinkedQueue<String> waitingList = new ConcurrentLinkedQueue<>();
    private final Map<String, Map<String, Object>> taskStore = new ConcurrentHashMap<>();

    public Map<String, Object> enterQueue() {
        final String taskId = UUID.randomUUID().toString();
        int queueNumber = seq.getAndIncrement();

        Map<String, Object> task = new ConcurrentHashMap<>();
        task.put("taskId", taskId);
        task.put("queueNumber", queueNumber);
        task.put("status", "WAITING");

        taskStore.put(taskId, task);
        waitingList.add(taskId);

        // 스레드풀의 2개 스레드가 작업을 가져가 동시 처리
        queueExecutor.submit(new Runnable() {
            @Override
            public void run() {
                processTask(taskId);
            }
        });

        return task;
    }

    private void processTask(String taskId) {
        Map<String, Object> task = taskStore.get(taskId);
        if (task == null) return;

        task.put("status", "PROCESSING");
        try {
            Thread.sleep(10000); // 10초 대기 작업 (최대 2개 스레드가 각각 실행)
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            task.put("status", "COMPLETED");
            waitingList.remove(taskId);
        }
    }

    public Map<String, Object> getQueueStatus(String taskId) {
        Map<String, Object> task = taskStore.get(taskId);
        Map<String, Object> res = new HashMap<>();

        if (task == null) {
            res.put("error", "Task not found");
            return res;
        }

        res.put("taskId", taskId);
        res.put("queueNumber", task.get("queueNumber"));
        res.put("status", task.get("status"));

        int aheadCount = 0;
        if ("WAITING".equals(task.get("status"))) {
            for (String id : waitingList) {
                if (id.equals(taskId)) break;
                
                // 내 앞에 있으면서 아직 WAITING 상태인 실제 대기자만 카운트
                Map<String, Object> aheadTask = taskStore.get(id);
                if (aheadTask != null && "WAITING".equals(aheadTask.get("status"))) {
                    aheadCount++;
                }
            }
        }
        res.put("aheadCount", aheadCount);

        return res;
    }
}