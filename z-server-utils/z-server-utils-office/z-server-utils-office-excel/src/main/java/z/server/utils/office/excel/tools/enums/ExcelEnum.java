package z.server.utils.office.excel.tools.enums;

/**
 * @description:
 * @author: zbn
 * @create: 2018-04-03 03:05
 **/
public enum ExcelEnum {

    EXCEL_03_VERSION(1, "03版"),
    EXCEL_07_VERSION(2, "07版")
    ;

    private Integer code;

    private String message;

    ExcelEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
