package cn.mintimate.img2d.api.exception;

import cn.mintimate.img2d.api.enums.StatusCodeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static cn.mintimate.img2d.api.enums.StatusCodeEnum.FAIL;

/**
 * 自定义异常
 *
 * @author Mintimate
 */
@Getter
@AllArgsConstructor
public class dataException extends RuntimeException{
    /**
     * 错误信息
     */
    private final String message;
    /**
     * 错误码
     */
    private Integer code = FAIL.getCode();

    public dataException(String message) {
        this.message = message;
    }

    public dataException(StatusCodeEnum statusCodeEnum) {
        this.code = statusCodeEnum.getCode();
        this.message = statusCodeEnum.getDesc();
    }

}
