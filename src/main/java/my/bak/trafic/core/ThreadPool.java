package my.bak.trafic.core;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;


public interface ThreadPool {

    /**
     * Return executor for update gui
     *
     * @return
     */
    Executor getFXExecutor();


    /**
     * Return common executor
     *
     * @return
     */
    ExecutorService getCommonExecutor();


    /**
     * Return executor for plugin
     *
     * @return
     */
    Executor getPluginExecutor();


    /**
     * Shutdown all used executors
     */
    void shutDown();


    /**
     * Execute in common executor
     */
    void execute(Runnable run);
}
