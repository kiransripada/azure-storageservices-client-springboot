package az.storage.util;

import az.storage.config.AzureStorageConfiguration;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;


@Slf4j
@Component
public class SecurityUtil implements  Utility{

    @Autowired
    private AzureStorageConfiguration storageConfiguration;

    @SneakyThrows
    public String createSignature (String stringToSign){
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(Base64.decodeBase64(storageConfiguration.getAccessKey()), "HmacSHA256"));
        String authKey = new String(Base64.encodeBase64(mac.doFinal(stringToSign.toString().getBytes("UTF-8"))));
        return authKey;
    }

    @Override
    public String getType() {
        return UtilityType.SECURITY_UTILITY.getType();
    }
}
