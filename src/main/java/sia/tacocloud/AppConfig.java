package sia.tacocloud;

import java.io.IOException;

import org.springframework.context.annotation.Configuration;

import sia.tacocloud.utils.ConfigReader;

@Configuration
public class AppConfig {
    public static String getCreditCard() throws IOException {
        return ConfigReader.getValueByKey("creditCard");
    }
}
