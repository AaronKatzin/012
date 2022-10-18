package com.hit.game012.net.server;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Class to schedule next daily board generation.
 * Using ScheduledExecutorService class.
 */
public class GeneratorScheduler {
    ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
    DailyBoardGenerator generator;
    ZoneId currentZone = ZoneId.systemDefault();
    ZonedDateTime zonedNextTarget;

    public GeneratorScheduler(DailyBoardGenerator generator, int targetHour, int targetMin, int targetSec) {
        this.generator = generator;
        ZonedDateTime zonedNow = ZonedDateTime.of(LocalDateTime.now(), currentZone);
        // Target next generate to 00:00:00 Local Time
        zonedNextTarget = zonedNow.withHour(targetHour).withMinute(targetMin).withSecond(targetSec);
        startExecution();
    }

    /**
     * Schedule the next execution, after run we call the function again to schedule next execution.
     */
    public void startExecution() {
        Runnable taskWrapper = new Runnable() {
            @Override
            public void run() {
                generator.run();
                // After each run, we schedule the next execution again
                startExecution();
            }
        };
        long delay = computeNextDelay();
        executorService.schedule(taskWrapper, delay, TimeUnit.SECONDS);
    }

    private long computeNextDelay() {
        ZonedDateTime zonedNow = ZonedDateTime.of(LocalDateTime.now(), currentZone);
        Duration duration = Duration.between(zonedNow, zonedNextTarget);

        if (duration.getSeconds() <= 0) {
            // Negative duration means we are after the target time, we will increase it in 1 day.
            zonedNextTarget = zonedNextTarget.plusDays(1);
            duration = Duration.between(zonedNow, zonedNextTarget);
        }
        return duration.getSeconds();
    }

    public void stop() {
        executorService.shutdown();
        try {
            executorService.awaitTermination(1, TimeUnit.DAYS);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
}
