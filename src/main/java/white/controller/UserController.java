package white.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import white.common.MailUtils;
import white.common.R;
import white.common.ValidateCodeUtils;
import white.entity.User;
import white.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Slf4j
@RequestMapping("/user")
@RestController
public class UserController {
    @Autowired
    private UserService userService;
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpServletRequest request){
        String phone = user.getPhone();
        if (StringUtils.isNotEmpty(phone)){
            String code = ValidateCodeUtils.generateValidateCode4String(6);
            log.info("code :{}",code);
            request.getSession().setAttribute(phone,code);
            return R.success("短信验证码发送成功");
        }
        return R.error("短信发送失败");
    }
    @PostMapping("/login")
    public R<User> login(@RequestBody Map user, HttpServletRequest request){
        String phone =  user.get("phone").toString();
        String code = (String)request.getSession().getAttribute(phone);
        String code1 = (String)user.get("code");
        if (code != null && code.equals(code1)){
            LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(User::getPhone,phone);
            User user1 = userService.getOne(wrapper);
            if(user1 == null){
                user1 = new User();
                user1.setPhone(phone);
                user1.setStatus(1);
                userService.save(user1);
            }
            request.getSession().setAttribute("user",user1.getId());
            return R.success(user1);
        }
        return R.error("登录失败");



    }

}
