package services.product.exception.custome;

public class BadRequestException extends RuntimeException {
    
    public BadRequestException(String message){
        super(message);
    }

}
