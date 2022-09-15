package ms.movie_service.exception;

public class EmailNotDelivered extends RuntimeException{
    public EmailNotDelivered(String message){
        super(message);
    }
}
