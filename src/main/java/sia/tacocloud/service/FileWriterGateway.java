package sia.tacocloud.service;

import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.file.FileHeaders;
import org.springframework.messaging.handler.annotation.Header;

@MessagingGateway(defaultRequestChannel = "textInChannel")
public interface FileWriterGateway {
    void writeToFileChannel(@Header(FileHeaders.FILENAME) String fileName, String data);
}
