/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.arrowhead.core.qos.monitor.database.codec;

import eu.arrowhead.core.qos.monitor.database.MongoDBNames;
import eu.arrowhead.core.qos.monitor.database.MonitorLog;
import java.util.HashMap;
import java.util.Map;
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
public class MonitorLogCodec implements CollectibleCodec<MonitorLog> {

    @Override
    public MonitorLog generateIdIfAbsentFromDocument(MonitorLog log) {
        return log;
    }

    @Override
    public boolean documentHasId(MonitorLog log) {
        return true;
    }

    @Override
    public BsonValue getDocumentId(MonitorLog log) {
        return new BsonObjectId(log.getId());
    }

    @Override
    public void encode(BsonWriter writer, MonitorLog log, EncoderContext encoderContext) {
        writer.writeStartDocument();

        writer.writeObjectId(MongoDBNames.DOCUMENT_ID, log.getId());

        writer.writeString(MongoDBNames.MONITOR_TYPE, log.getType());

        writer.writeDateTime(MongoDBNames.TIMESTAMP, log.getTimestamp());

        Set<Map.Entry<String, Double>> params = log.getParameters().entrySet();

        for (Map.Entry<String, Double> param : params) {
            writer.writeDouble(param.getKey(), param.getValue());
        }

        writer.writeEndDocument();
    }

    @Override
    public Class<MonitorLog> getEncoderClass() {
        return MonitorLog.class;
    }

    @Override
    public MonitorLog decode(BsonReader reader, DecoderContext decoderContext) {
        reader.readStartDocument();

        ObjectId id = reader.readObjectId(MongoDBNames.DOCUMENT_ID);

        String type = reader.readString(MongoDBNames.MONITOR_TYPE);

        Long timestamp = reader.readDateTime(MongoDBNames.TIMESTAMP);

        Map<String, Double> parameters = new HashMap<>();

        while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
            String fieldName = reader.readName();
            Double value = reader.readDouble();
            parameters.put(fieldName, value);
        }

        reader.readEndDocument();

        return new MonitorLog(id, type, timestamp, parameters);
    }

}
