package sia.tacocloud.config;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Map;

import org.apache.kafka.common.serialization.Serializer;

import sia.tacocloud.models.TacoOrder;

public class JavaSerializer implements Serializer<TacoOrder> {



    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {

    }

    @Override
    public void close() {

    }

    @Override
    public byte[] serialize(String arg0, TacoOrder data) {
        try {
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            ObjectOutputStream objectStream = new ObjectOutputStream(byteStream);
            objectStream.writeObject(data);
            objectStream.flush();
            objectStream.close();
            return byteStream.toByteArray();
        }
        catch (IOException e) {
            throw new IllegalStateException("Can't serialize object: " + data, e);
        }
    }

}