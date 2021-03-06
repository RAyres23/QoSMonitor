/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.arrowhead.core.qos.monitor.database.provider;

import eu.arrowhead.core.qos.monitor.database.MonitorLog;
import eu.arrowhead.core.qos.monitor.database.codec.MonitorLogCodec;
import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;

/**
 * A provider of {@code MonitorLogCodec} instances.
 *
 * @author 1120681@isep.ipp.pt - Renato Ayres
 */
public class MonitorLogCodecProvider implements CodecProvider {

    @Override
    public <T> Codec<T> get(Class<T> clazz, CodecRegistry registry) {
        if (clazz.equals(MonitorLog.class)) {
            return (Codec<T>) new MonitorLogCodec();
        }
        return null;
    }

}
