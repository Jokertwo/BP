package my.bak.trafic.core.plugin.loader;

import my.bak.trafic.core.plugin.ExportPlugin;
import my.bak.trafic.core.plugin.ImportPlugin;
import my.bak.trafic.core.plugin.exception.NotSupportedPlugin;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;


public interface PluginProvider {

    /**
     * Return currently available import plugins
     *
     * @return
     */
    List<PluginInfo> getAvailableImportPlugins();


    /**
     * Return currently available export plugins
     *
     * @return
     */
    List<PluginInfo> getAvailableExportPlugins();


    /**
     * Save new plugin
     *
     * @param plugin
     */
    void savePlugin(File plugin) throws IOException, NotSupportedPlugin;


    /**
     * Load import plugin
     *
     * @param pluginInfo
     * @return
     */
    Optional<ImportPlugin> loadImportPlugin(PluginInfo pluginInfo);


    /**
     * Load export plugin
     *
     * @param pluginInfo
     * @return
     */
    Optional<ExportPlugin> loadExportPlugin(PluginInfo pluginInfo);


    /**
     * Delete current plugin
     *
     * @param hashcode
     */
    void deletePlugin(PluginInfo plugin) throws IOException;
}
