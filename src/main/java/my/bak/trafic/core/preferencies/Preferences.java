package my.bak.trafic.core.preferencies;

public interface Preferences {

    String PROP_FILE = "app.properties";


    /**
     * Save actual version of properties
     */
    void save();


    /**
     * Return password
     *
     * @return
     */
    String getPassword();


    /**
     * Return db address
     *
     * @return
     */
    String getDBAddress();


    /**
     * Return user name
     *
     * @return
     */
    String getUserName();


    /**
     * Return true if app should start connected to db
     *
     * @return
     */
    boolean getStartConnected();


    /**
     * Return last path opened file vie filechooser
     *
     * @return
     */
    String getLastOpenedFile();


    /**
     * Set last path of opened file
     *
     * @param path
     */
    void setLastOpenedFile(String path);


    /**
     * Set new password to db
     *
     * @param password
     */
    void setPassword(String password);


    /**
     * Save new user name for db
     *
     * @param userName
     */
    void setUserName(String userName);


    /**
     * Set new address to db
     *
     * @param address
     */
    void setDBAdress(String address);


    /**
     * Set if app should start connected to db
     *
     * @param connected
     */
    void setStartConnected(boolean connected);

}
