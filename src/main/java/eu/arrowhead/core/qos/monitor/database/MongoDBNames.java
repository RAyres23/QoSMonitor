package eu.arrowhead.core.qos.monitor.database;

/**
 * Repository of names and keys for standard access by the
 * {@link MongoDatabaseManager} class.
 *
 * @author 1120681@isep.ipp.pt - Renato Ayres
 */
public class MongoDBNames {

    /**
     * Provider key
     */
    public static final String PROVIDER = "provider";

    /**
     * Consumer key
     */
    public static final String CONSUMER = "consumer";

    /**
     * Monitor type key
     */
    public static final String MONITOR_TYPE = "type";

    /**
     * Rules table name
     */
    public static final String RULES_TABLE = "Rule";

    /**
     * Document id member name
     */
    public static final String DOCUMENT_ID = "_id";

    /**
     * Provider system group key
     */
    public static final String PROVIDER_SYSTEM_GROUP = "providerSystemGroup";

    /**
     * Provider system name key
     */
    public static final String PROVIDER_SYSTEM_NAME = "providerSystemName";

    /**
     * Consumer system group key
     */
    public static final String CONSUMER_SYSTEM_GROUP = "consumerSystemGroup";

    /**
     * Consumer system name key
     */
    public static final String CONSUMER_SYSTEM_NAME = "consumerSystemName";

    /**
     * Timestamp key
     */
    public static final String TIMESTAMP = "timestamp";

    /**
     * Soft real time key
     */
    public static final String SOFTREALTIME = "softRealTime";
}
