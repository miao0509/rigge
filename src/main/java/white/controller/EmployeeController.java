package white.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import white.common.R;
import white.entity.Employee;
import white.service.EmployeeService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee){
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        LambdaQueryWrapper<Employee> wrapper =  new LambdaQueryWrapper<>();
        wrapper.eq(Employee::getUsername,employee.getUsername());
        Employee emp = employeeService.getOne(wrapper);

        if (emp == null){
            return R.error("登录失败");
        }

        if (!emp.getPassword().equals(password)){
            return R.error("密码错误");
        }

        if (emp.getStatus() == 0){
            return R.error("账号已禁用");
        }

        request.getSession().setAttribute("employee",emp.getId());
        return R.success(emp);
    }

    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");

    }
    @PostMapping
    public R<String> save(@RequestBody Employee employee,HttpServletRequest request){
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        employeeService.save(employee);
        return R.success("新增员工成功");
    }
    @GetMapping("/page")
    public R<Page<Employee>> page(int page,int pageSize,String name){
        Page pageInfo = new Page(page,pageSize);
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotEmpty(name),Employee::getName,name);
        wrapper.orderByDesc(Employee::getUpdateTime);
        employeeService.page(pageInfo, wrapper);
        return R.success(pageInfo);

    }
    @PutMapping
    public R<String> update(@RequestBody Employee employee,HttpServletRequest request){
        employeeService.updateById(employee);
        return R.success("更新成功");
    }
    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id){
        Employee employee = employeeService.getById(id);
        return R.success(employee);
    }
}
