package az.storage.util;

import lombok.ToString;

@ToString
public enum UtilityType {

 SECURITY_UTILITY("SecurityUtility"),
 JSON_UTILITY("JSONUtility"),
 XML2JSON_UTILITY("XML2JSONUtility");



    private  final  String type;

    UtilityType(String type) {
        this.type = type;
    }

    public String getType(){
        return  type;
    }

}
