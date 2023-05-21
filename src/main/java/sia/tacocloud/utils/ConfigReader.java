package sia.tacocloud.utils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.json.JSONObject;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;

public class ConfigReader {
    
    public static String getValueByKey(String key) throws IOException {
        ClassPathResource resource = new ClassPathResource("config.json");
        byte[] fileData = FileCopyUtils.copyToByteArray(resource.getInputStream());
        String jsonContent = new String(fileData, StandardCharsets.UTF_8);
        
        JSONObject jsonObject = new JSONObject(jsonContent);
        return jsonObject.getString(key);
    }
}
