package com.hit.game012.net.server;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GeneratorScheduler {
    ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
    DailyBoardGenerator generator;
    ZoneId currentZone = ZoneId.systemDefault();
    ZonedDateTime zonedNextTarget;

    public GeneratorScheduler(DailyBoardGenerator generator, int targetHour, int targetMin, int targetSec) {
        this.generator = generator;
        ZonedDateTime zonedNow = ZonedDateTime.of(LocalDateTime.now(), currentZone);
        zonedNextTarget = zonedNow.withHour(0).withMinute(0).withSecond(0);
        startExecution();

    }
    public void startExecution()
    {
        Runnable taskWrapper = new Runnable(){

            @Override
            public void run()
            {
                generator.run();
                startExecution();
            }

        };
        long delay = computeNextDelay();
        executorService.schedule(taskWrapper, delay, TimeUnit.SECONDS);
    }

    private long computeNextDelay()
    {
        ZonedDateTime zonedNow = ZonedDateTime.of(LocalDateTime.now(), currentZone);
        Duration duration = Duration.between(zonedNow, zonedNextTarget);

        if(duration.getSeconds() <= 0){
            zonedNextTarget = zonedNextTarget.plusDays(1);
            duration = Duration.between(zonedNow, zonedNextTarget);
        }
        return duration.getSeconds();
    }

    public void stop()
    {
        executorService.shutdown();
        try {
            executorService.awaitTermination(1, TimeUnit.DAYS);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
}
