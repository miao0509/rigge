package white.Execption;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ExceptionHandler;
import white.common.CustomException;
import white.common.R;

import java.sql.SQLIntegrityConstraintViolationException;

@ControllerAdvice(annotations = {RestController.class, Component.class})
@ResponseBody
public class GlobalExceptionHandler {
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException ex){
        if (ex.getMessage().contains("Duplicate entity")){
            String duplicateName = ex.getMessage().split(" ")[2];
            return R.error(duplicateName+"已存在");
        }
        return R.error("数据库异常");
    }

    @ExceptionHandler(CustomException.class)
    public R<String> exceptionHandler(CustomException ex){

        return R.error(ex.getMessage());
    }
    @ExceptionHandler(NullPointerException.class)
    public R<String> exceptionHandler(NullPointerException ex){
        return R.error("数据格式错误");
    }
}
