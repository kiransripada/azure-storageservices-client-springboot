package az.storage.config;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
@ConfigurationProperties(prefix = "services.azurestorage")
public class AzureStorageConfiguration {

    private @Getter
    @Setter
    String storageAccountName;
    private @Getter @Setter  String containerName;
    private @Getter @Setter  String apiVersion;
    private @Getter @Setter  String accessKey;
    private @Getter @Setter String blobEndpoint;
    private @Getter @Setter String tableEndpoint;


}
