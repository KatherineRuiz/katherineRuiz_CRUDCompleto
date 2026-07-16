package SystemITR.JosueGuinea2A.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class ApiResponse<T> {

    private boolean success;
    private String message;
    private T data;

    //Este necesita 3 parametros
    public ApiResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    //Este solo necesita 2 parametos
    public ApiResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
}
