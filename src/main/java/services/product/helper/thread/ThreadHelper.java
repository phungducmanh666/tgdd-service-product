package services.product.helper.thread;

public class ThreadHelper {
    public static void delay(long millis) {
        try {
            Thread.sleep(millis);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
}
