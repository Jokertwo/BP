package my.bak.trafic.core.plugin;

import my.bak.trafic.core.plugin.transport.ImportDataBuilder;


public interface ImportPlugin extends Plugin {

    @Override
    default PluginFlag getPluginFlag() {
        return PluginFlag.IMPORT;
    }


    /**
     * import data from plugin to database
     */
    ImportDataBuilder importData();


    /**
     * Return true while import not done
     *
     * @return
     */
    boolean hasNext();

}
