/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.arrowhead.qos.monitor.database.provider;

import eu.arrowhead.qos.monitor.database.MonitorLog;
import eu.arrowhead.qos.monitor.database.codec.MonitorLogCodec;
import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;

/**
 *
 * @author Renato Ayres
 */
public class MonitorLogCodecProvider implements CodecProvider {

    /**
     *
     * @param <T>
     * @param clazz
     * @param registry
     * @return
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> Codec<T> get(Class<T> clazz, CodecRegistry registry) {
        if (clazz.equals(MonitorLog.class)) {
            return (Codec<T>) new MonitorLogCodec();
        }
        return null;
    }

}
