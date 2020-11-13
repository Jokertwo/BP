package my.bak.trafic.core.plugin.exception;

/**
 * Exception which is thrown when user try to load unsupported plugin
 *
 * @author Petr Lastovka
 */
public class NotSupportedPlugin extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = -1847586563987381510L;


    public NotSupportedPlugin(String message) {
        super(message);
    }

}
