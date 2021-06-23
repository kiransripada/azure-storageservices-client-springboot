package az.storage.builder;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.StringJoiner;

/**
 * //creating a builder involves lots of boilerplate code
 *     // Not using Fluent Builder since the order of steps is not mandatory
 */
@Component
public class BlobAuthheaderBuilder {

    private  String requestTime;
    private  String canonicalizedHeader;
    private String canonicalizedResource;
    private String authorizationHeader;


    private BlobAuthheaderBuilder() {

    }

    public static BlobAuthheaderBuilder getBuilder(){
        return new BlobAuthheaderBuilder();

    }

    public BlobAuthHeader build(){
        return  new BlobAuthHeader(canonicalizedHeader,canonicalizedResource,authorizationHeader,requestTime);
    }

    public BlobAuthheaderBuilder setRequestTime() {
        Instant instant = Instant.now();
        this.requestTime  = DateTimeFormatter.RFC_1123_DATE_TIME
                .withZone(ZoneOffset.UTC)
                .format(instant);
        return this;
    }

    public BlobAuthheaderBuilder setCanonicalizedHeader(String  canonicalizedHeader) {
        StringJoiner canonicalHeader = new StringJoiner("", "", "");
        // TODO cleanup -- use string[] properly
        canonicalHeader.add("x-ms-date:" + this.requestTime).add("\nx-ms-version:" + canonicalizedHeader);
        System.out.println("canonicalHeader+"+canonicalHeader.toString());
        this.canonicalizedHeader = canonicalHeader.toString();
        return  this;
    }
    public BlobAuthheaderBuilder setCanonicalizedResource(String... canonicalizedResource) {
        // TODO cleanup -- use string[] properly
        StringJoiner canonicalResource = new StringJoiner("", "", "");
        canonicalResource.add("/darwinmdmnonprod/mdmproducts-container\ncomp:list\nrestype:container");
        this.canonicalizedResource = canonicalResource.toString();
        return  this;
    }

    public BlobAuthheaderBuilder setAuthorizationHeader(String authorizationHeader){
        String stringToSign = String.join("\n"
                , "GET"
                , ""
                , ""
                , ""
                , ""
                , ""
                , ""
                , ""
                , ""
                , ""
                , ""
                , ""
                , this.canonicalizedHeader
                , this.canonicalizedResource);
        System.out.println("stringToSign= \n" + stringToSign);
        this.authorizationHeader = stringToSign;
        return this;

    }
}
