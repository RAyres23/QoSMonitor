/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.arrowhead.core.qos.monitor.database.codec;

import eu.arrowhead.core.qos.monitor.database.MongoDBNames;
import eu.arrowhead.core.qos.monitor.database.MonitorRule;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.bson.BsonObjectId;
import org.bson.BsonReader;
import org.bson.BsonType;
import org.bson.BsonValue;
import org.bson.BsonWriter;
import org.bson.codecs.CollectibleCodec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.types.ObjectId;

/**
 *
 * @author Renato Ayres
 */
public class MonitorRuleCodec implements CollectibleCodec<MonitorRule> {

    @Override
    public MonitorRule generateIdIfAbsentFromDocument(MonitorRule rule) {
        return rule;
    }

    @Override
    public boolean documentHasId(MonitorRule rule) {
        return true;
    }

    @Override
    public BsonValue getDocumentId(MonitorRule rule) {
        return new BsonObjectId(rule.getId());
    }

    @Override
    public void encode(BsonWriter writer, MonitorRule rule, EncoderContext encoderContext) {
        writer.writeStartDocument();

        writer.writeObjectId(MongoDBNames.DOCUMENT_ID, rule.getId());

        writer.writeString(MongoDBNames.MONITOR_TYPE, rule.getType());

        writer.writeString(MongoDBNames.PROVIDER_SYSTEM_NAME, rule.getProviderSystemName());
        writer.writeString(MongoDBNames.PROVIDER_SYSTEM_GROUP, rule.getProviderSystemGroup());

        writer.writeString(MongoDBNames.CONSUMER_SYSTEM_NAME, rule.getConsumerSystemName());
        writer.writeString(MongoDBNames.CONSUMER_SYSTEM_GROUP, rule.getConsumerSystemGroup());

        writer.writeBoolean(MongoDBNames.SOFTREALTIME, rule.isSoftRealTime());

        Set<Entry<String, String>> params = rule.getParameters().entrySet();

        for (Entry<String, String> param : params) {
            writer.writeString(param.getKey(), param.getValue());
        }

        writer.writeEndDocument();
    }

    @Override
    public Class<MonitorRule> getEncoderClass() {
        return MonitorRule.class;
    }

    @Override
    public MonitorRule decode(BsonReader reader, DecoderContext decoderContext) {
        reader.readStartDocument();

        ObjectId id = reader.readObjectId(MongoDBNames.DOCUMENT_ID);

        String type = reader.readString(MongoDBNames.MONITOR_TYPE);

        String providerSystemName = reader.readString(MongoDBNames.PROVIDER_SYSTEM_NAME);
        String providerSystemGroup = reader.readString(MongoDBNames.PROVIDER_SYSTEM_GROUP);

        String consumerSystemName = reader.readString(MongoDBNames.CONSUMER_SYSTEM_NAME);
        String consumerSystemGroup = reader.readString(MongoDBNames.CONSUMER_SYSTEM_GROUP);

        boolean softRealTime = reader.readBoolean(MongoDBNames.SOFTREALTIME);

        Map<String, String> parameters = new HashMap<>();

        while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
            String fieldName = reader.readName();
            String value = reader.readString();
            parameters.put(fieldName, value);
        }

        reader.readEndDocument();

        return new MonitorRule(id, type,
                providerSystemName, providerSystemGroup,
                consumerSystemName, consumerSystemGroup,
                parameters, softRealTime);
    }
}
