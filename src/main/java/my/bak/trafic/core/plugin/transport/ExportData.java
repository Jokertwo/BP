package my.bak.trafic.core.plugin.transport;

import my.bak.trafic.core.plugin.loader.PluginInfo;
import my.bak.trafic.database.query.column.Column;

import javax.persistence.Tuple;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;
import java.util.function.Consumer;


public interface ExportData {
    /**
     * Export data throw plugin
     *
     * @param info    plugin metadata
     * @param params  plugin parameters
     * @param select  select query
     * @param columns columns for parser
     * @throws Exception
     */
    void exportData(PluginInfo info,
                    String params,
                    CriteriaQuery<Tuple> select,
                    List<Column> columns,
                    Consumer<Boolean> handler)
            throws Exception;


    void stop();
}
