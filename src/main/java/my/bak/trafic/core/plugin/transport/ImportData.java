package my.bak.trafic.core.plugin.transport;

import my.bak.trafic.core.plugin.exception.WrongParametersException;
import my.bak.trafic.core.plugin.loader.PluginInfo;

import java.util.function.Consumer;


public interface ImportData {

    /**
     * Import data from current plugin to database
     *
     * @param info
     * @param params
     * @param hanler
     * @throws WrongParametersException
     */
    void importData(PluginInfo info, String params, Consumer<Boolean> hanler) throws WrongParametersException;


    /**
     * Stop import
     */
    void stopImport();
}
