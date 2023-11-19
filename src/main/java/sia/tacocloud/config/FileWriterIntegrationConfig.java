package sia.tacocloud.config;

import java.io.File;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.core.GenericTransformer;
import org.springframework.integration.file.FileWritingMessageHandler;

@Configuration
public class FileWriterIntegrationConfig {
    static  private final String  textInChannel = "textInChannel";
    static private final String fileWriterChannel = "fileWriterChannel";
    @Bean
    @Transformer(inputChannel = textInChannel, outputChannel = fileWriterChannel)
    public GenericTransformer<String,String> upperCaseTransformer(){
        return text -> text.toUpperCase();
    }
    

    @Bean
    @ServiceActivator(inputChannel = fileWriterChannel)
    public FileWritingMessageHandler fileWriter(){
            FileWritingMessageHandler handler = new FileWritingMessageHandler(new File("/tmp/sia/files"));
            handler.setExpectReply(false);
            handler.setAppendNewLine(true);
            return handler;
    }
}
