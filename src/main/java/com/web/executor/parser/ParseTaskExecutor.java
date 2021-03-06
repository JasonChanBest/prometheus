package com.web.executor.parser;

import com.web.executor.TaskExecutor;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author jayson   2015-07-12 17:26
 * @since v1.0
 */
@Component("ParseTaskExecutor")
public class ParseTaskExecutor implements TaskExecutor<ParseTask> {
    private ExecutorService pool = Executors.newCachedThreadPool();

    @Override
    public void execute(ParseTask task) {
        pool.execute(()->{
            task.execute();
        });
    }
}
