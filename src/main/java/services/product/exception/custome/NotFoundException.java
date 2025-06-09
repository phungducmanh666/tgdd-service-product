package services.product.exception.custome;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message){
        super(message);
    }
}
