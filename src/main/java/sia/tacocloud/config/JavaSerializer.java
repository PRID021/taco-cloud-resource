package sia.tacocloud.config;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Map;

import org.apache.kafka.common.serialization.Serializer;

import com.fasterxml.jackson.databind.ObjectMapper;

import sia.tacocloud.models.TacoOrder;

public class JavaSerializer implements Serializer<TacoOrder> {
     private final ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {

    }

    @Override
    public void close() {

    }

    @Override
    public byte[] serialize(String arg0, TacoOrder data) {
        try {
            return objectMapper.writeValueAsBytes(data);
        }
        catch (IOException e) {
            throw new IllegalStateException("Can't serialize object: " + data, e);
        }
    }

}