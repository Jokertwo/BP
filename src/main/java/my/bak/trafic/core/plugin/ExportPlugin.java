package my.bak.trafic.core.plugin;

import my.bak.trafic.core.plugin.transport.ExportDataBuilder;


public interface ExportPlugin extends Plugin {

    @Override
    default PluginFlag getPluginFlag() {
        return PluginFlag.EXPORT;
    }


    /**
     * Export data from database
     *
     * @param data
     */
    void exportData(ExportDataBuilder data) throws Exception;
}
