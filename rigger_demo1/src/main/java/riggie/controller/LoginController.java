package riggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import riggie.common.R;
import riggie.pojo.Employee;
import riggie.service.EmployeeService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/employee")
public class LoginController {
    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/login")
    public R<Employee> doLogin(@RequestBody Employee employee, HttpServletRequest request) {
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        String username = employee.getUsername();
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername, username);
        Employee employee1 = employeeService.getOne(queryWrapper);
        if (employee1 == null) {
            return R.error("登录失败");

        }
        if (!employee1.getPassword().equals(password)) {
            return R.error("密码错误");
        }
        if(employee1.getStatus()==0){
            return R.error("账号已经锁定");

        }
        request.getSession().setAttribute("employee",employee1.getId());
        return R.success(employee1);
    }
    @PostMapping("/logout")
    public R logout(HttpSession session){
        session.removeAttribute("employee");
        return R.success("退出成功！");

    }




}
