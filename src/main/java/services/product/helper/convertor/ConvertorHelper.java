package services.product.helper.convertor;

import java.util.UUID;

public class ConvertorHelper {
    public static UUID String2UUID(String uidStr){
        return UUID.fromString(uidStr);
    }
}
