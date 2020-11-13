package my.bak.trafic.core.plugin.loader;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import javafx.util.Pair;
import my.bak.trafic.core.plugin.ExportPlugin;
import my.bak.trafic.core.plugin.ImportPlugin;
import my.bak.trafic.core.plugin.Plugin;
import my.bak.trafic.core.plugin.PluginFlag;
import my.bak.trafic.core.plugin.exception.NotSupportedPlugin;
import my.bak.trafic.logger.InjectLogger;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.jar.Attributes;
import java.util.jar.JarInputStream;
import java.util.jar.Manifest;
import java.util.stream.Collectors;


@Singleton
public class PluginLoaderFactory implements PluginProvider {
    @InjectLogger
    private static Logger logger;
    private static final String PLUGIN_FOLDER = "./plugins";
    public static final String PLUGIN_IDENTIFIER = "Plugin-Class";
    private static final FilenameFilter PLUGIN_FILTER = (file, name) -> name.contains(".jar");
    private static List<PluginInfo> plugins;
    private File pluginFolder;


    @Inject
    public PluginLoaderFactory() {
        plugins = new ArrayList<>();
        pluginFolder = new File(PLUGIN_FOLDER);
        checkPluginFolder();
        loadExternalPlugins();
    }


    /**
     * Check if plugin folder exist
     */
    private void checkPluginFolder() {
        if (!pluginFolder.exists()) {
            logger.info("Create folder for plugins");
            pluginFolder.mkdir();
        }
    }


    public List<PluginInfo> getAvailableExportPlugins() {
        return filterPlugins(PluginFlag.EXPORT);
    }


    @Override
    public List<PluginInfo> getAvailableImportPlugins() {
        return filterPlugins(PluginFlag.IMPORT);
    }


    private List<PluginInfo> filterPlugins(PluginFlag flag) {
        return plugins.stream().filter(plugin -> plugin.getFlag() == flag).collect(Collectors.toList());
    }


    private void loadExternalPlugins() {
        File[] pluginsFile = pluginFolder.listFiles(PLUGIN_FILTER);
        plugins = Arrays.stream(pluginsFile)
                .map(this::loadPlugin)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(item -> new PluginInfo(item.getValue(), item.getKey()))
                .collect(Collectors.toList());
    }


    @Override
    public Optional<ImportPlugin> loadImportPlugin(PluginInfo pluginInfo) {
        ImportPlugin plugin = null;
        Optional<Pair<File, ImportPlugin>> res = loadPlugin(new File(pluginInfo.getPluginPath()));
        if (res.isPresent()) {
            plugin = res.get().getValue();
        }
        return Optional.ofNullable(plugin);
    }


    @Override
    public Optional<ExportPlugin> loadExportPlugin(PluginInfo pluginInfo) {
        ExportPlugin plugin = null;
        Optional<Pair<File, ExportPlugin>> res = loadPlugin(new File(pluginInfo.getPluginPath()));
        if (res.isPresent()) {
            plugin = res.get().getValue();
        }
        return Optional.ofNullable(plugin);
    }


    private <PluginType extends Plugin> Optional<Pair<File, PluginType>> loadPlugin(File pluginFile) {
        try (InputStream input = new FileInputStream(pluginFile)) {
            ClassLoader loader = URLClassLoader.newInstance(new URL[]{pluginFile.toURI().toURL()});
            JarInputStream jis = new JarInputStream(input);
            Manifest mf = jis.getManifest();
            Attributes attributes = mf.getMainAttributes();
            String pluginClassName = attributes.getValue(PLUGIN_IDENTIFIER);
            Class<?> clazz = Class.forName(pluginClassName, true, loader);
            @SuppressWarnings("unchecked")
            PluginType plugin = (PluginType) clazz.newInstance();
            logger.debug("Loading plugin: {}", plugin.getName());
            jis.close();
            return Optional.of(new Pair<>(pluginFile, plugin));
        } catch (Exception e) {
            logger.fatal("", e);
            return Optional.empty();
        }
    }


    @Override
    public synchronized void savePlugin(File plugin) throws IOException, NotSupportedPlugin {
        String path = pluginFolder.getAbsolutePath() + "/" + plugin.getName();
        if (loadPlugin(plugin).isPresent()) {
            logger.info("Copy plugin from path: {} to {}", plugin.getAbsolutePath(), path);
            Files.copy(plugin.toPath(), new File(path).toPath());
            logger.info("Reinicialize plugins");
            plugins.clear();
            loadExternalPlugins();
        } else {
            throw new NotSupportedPlugin("Not supported plugin");
        }
    }


    @Override
    public synchronized void deletePlugin(PluginInfo pluginInfo) throws IOException {
        logger.info("Removing plugin {}, and file {}", pluginInfo.getName(),
                pluginInfo.getPluginPath());
        System.gc();
        Files.delete(new File(pluginInfo.getPluginPath()).toPath());
        plugins.remove(pluginInfo);

    }

}
