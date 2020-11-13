package my.bak.trafic.core.plugin.loader;

import lombok.Getter;
import my.bak.trafic.core.plugin.Plugin;
import my.bak.trafic.core.plugin.PluginFlag;

import java.io.File;


public class PluginInfo {

    @Getter
    private String pluginPath;
    @Getter
    private String name;
    @Getter
    private String description;
    @Getter
    private PluginFlag flag;


    public PluginInfo(Plugin plugin, File pluginFile) {
        super();
        this.pluginPath = pluginFile.getAbsolutePath();
        this.name = plugin.getName();
        this.description = plugin.getDescription();
        this.flag = plugin.getPluginFlag();
    }


    @Override
    public String toString() {
        return name;
    }

}
