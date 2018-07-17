package z.server.utils.office.excel.tools.exception;

/**
 * @description:
 * @author: zbn
 * @create: 2018-04-03 02:56
 **/
public class ExcelException extends RuntimeException{

    private static final long serialVersionUID = 7662633139545320670L;

    public ExcelException() {
        super();
    }

    public ExcelException(String message) {
        super(message);
    }

    public ExcelException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExcelException(Throwable cause) {
        super(cause);
    }

    protected ExcelException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
