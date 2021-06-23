package az.storage.util;

import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A typical FactoryBean implementation . Ref Spring Factory bean
 */
@Component
@NoArgsConstructor
public class UtilityFactory {

    @Autowired
    private List<Utility> utilities;

    private static final Map<String,Utility> utilityMap = new HashMap<>();

    @PostConstruct
    public  void initializeUtilities(){
        for (Utility utility : utilities){
            utilityMap.put(utility.getType(),utility);
        }
    }
    /**
     *
     * @param type
     * @return
     */
    @SneakyThrows
    public Utility getUtility(String type){
        Utility utility = utilityMap.get(type);
        return  utility;
    }




}
