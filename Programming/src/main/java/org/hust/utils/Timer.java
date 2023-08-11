package org.hust.utils;

import java.util.concurrent.TimeUnit;

/**
 * Timer is a class used to count time in minute.
 */
public class Timer implements Runnable {
    private int count = 0;
    private boolean isRunning;

    /**
     * Start the timer so that the timer start tickling for each minute pass.
     */
    @Override
    public void run() {
        isRunning = true;
        while (isRunning) {
            try {
                TimeUnit.MINUTES.sleep(1);
                count++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Stop the timer.
     */
    public void stop() {
        isRunning = false;
    }

    /**
     * Reset the count currently in the timer
     */
    public void reset() {
        count = 0;
    }

    /**
     * Return the count of the timer.
     *
     * @return minutes passed after the timer run
     */
    public int getCount() {
        return this.count;
    }
}
