package my.bak.trafic.core;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import javafx.application.Platform;
import my.bak.trafic.logger.InjectLogger;
import org.apache.logging.log4j.core.Logger;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;


@Singleton
public final class ThreadPoolImpl implements ThreadPool {

    @InjectLogger
    private static Logger logger;

    private ExecutorService commonExecutor;
    private ExecutorService pluginExecutor;
    private Executor javafxExecutor = Platform::runLater;


    @Inject
    public ThreadPoolImpl() {
        commonExecutor = ForkJoinPool.commonPool();
        pluginExecutor = ForkJoinPool.commonPool();
        javafxExecutor = Platform::runLater;
    }


    /**
     * Shutdown all used executors
     */
    public void shutDown() {
        commonExecutor.shutdown();
        pluginExecutor.shutdown();

        try {
            commonExecutor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            logger.fatal("Can not shutdown common executor", e);
            commonExecutor.shutdownNow();
        }
        try {
            pluginExecutor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            logger.fatal("Can not shutdown common executor", e);
            pluginExecutor.shutdownNow();
        }
    }


    @Override
    public Executor getFXExecutor() {
        return javafxExecutor;
    }


    @Override
    public ExecutorService getCommonExecutor() {
        return commonExecutor;
    }


    @Override
    public void execute(Runnable run) {
        commonExecutor.execute(run);
    }


    @Override
    public Executor getPluginExecutor() {
        return pluginExecutor;
    }
}
