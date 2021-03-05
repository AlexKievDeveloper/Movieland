package com.hlushkov.movieland.service.util;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;
@Slf4j
public class TimeoutedThreadPool extends ThreadPoolExecutor {
    /**
     * Thread pool for threads which control worker threads.
     */
    private Executor supervisorThreadPool;
    /**
     * Time in milliseconds after which interrupt worker thread
     */
    private int delay;

    /**
     * {@inheritDoc}
     *
     * @param timeout- timeout in milliseconds
     */
    public TimeoutedThreadPool(int timeout,
                               int corePoolSize,
                               int maximumPoolSize,
                               long keepAliveTime,
                               TimeUnit unit,
                               BlockingQueue workQueue) {
        this(timeout, corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, Executors.defaultThreadFactory());
    }

    /**
     * {@inheritDoc}
     *
     * @param timeout - timeout in milliseconds
     */
    public TimeoutedThreadPool(int timeout,
                               int corePoolSize,
                               int maximumPoolSize,
                               long keepAliveTime,
                               TimeUnit unit,
                               BlockingQueue workQueue,
                               ThreadFactory threadFactory) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
        init(timeout);
    }

    /**
     * Do some initialization work
     *
     * @param timeout - timeout in milliseconds
     */
    private void init(int timeout) {
        this.delay = timeout;

        supervisorThreadPool = Executors.newCachedThreadPool(r -> {
            Thread t = Executors.defaultThreadFactory().newThread(r);
            t.setName("supervisor-" + t.getName());
            return t;
        });

    }

    /**
     * Attach supervisor thread to worker thread here
     * {@inheritDoc}
     */
    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        supervisorThreadPool.execute(new SupervisorThread(t));
    }

    /**
     * Thread which attaches to worker thread and interrupt it after given timeout expires
     */
    private class SupervisorThread implements Runnable {
        private Thread target;

        public SupervisorThread(Thread target) {
            this.target = target;
        }

        @Override
        public void run() {
            try {
                target.join(delay); // do attach here
            } catch (InterruptedException e) {
                log.info("Interrupted exception was throw!", e);
                target.interrupt(); // do interrupt here if supervisor interrupted
            }
            if (target.isAlive()) target.interrupt(); // do interrupt here if worker thread is still alive
        }
    }
}