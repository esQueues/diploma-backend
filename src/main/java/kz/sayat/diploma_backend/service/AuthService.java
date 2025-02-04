package kz.sayat.diploma_backend.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kz.sayat.diploma_backend.exceptions.AuthException;

import kz.sayat.diploma_backend.mapper.TeacherMapper;
import kz.sayat.diploma_backend.mapper.UserMapper;
import kz.sayat.diploma_backend.models.Teacher;
import kz.sayat.diploma_backend.models.User;
import kz.sayat.diploma_backend.models.enums.UserRole;
import kz.sayat.diploma_backend.security.authReq.LoginRequest;
import kz.sayat.diploma_backend.security.authReq.RegisterRequest;
import lombok.AllArgsConstructor;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class AuthService {

    private final UserService userService;
    private final TeacherService teacherService;
    private final TeacherMapper teacherMapper;
    private final AuthenticationManager authenticationManager;
    private final SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();
    private final SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();
    private final UserMapper userMapper;


    public void login(HttpServletRequest request, HttpServletResponse response, LoginRequest authRequest) {
        UsernamePasswordAuthenticationToken token = UsernamePasswordAuthenticationToken.unauthenticated
            (authRequest.email(), authRequest.password());
        authenticate(token, request, response);

        System.out.println(authRequest.email()+"   signed in");
    }

    public void register(RegisterRequest request){

        if(userService.isEmailExist(request.email())){
            throw new AuthException("Email already exists");
        }

        User user = userMapper.toUser(request);
        user.setRole(UserRole.STUDENT);
        userService.save(user);
        //vertification
        System.out.println(request.email()+"  registered");
    }


    public void registerTeacher(RegisterRequest request){
        if(userService.isEmailExist(request.email())){
            throw new AuthException("Email already exists");
        }
        Teacher teacher = teacherMapper.toTeacher(request);

        teacherService.save(teacher);
        //vertification
        System.out.println(request.email()+"  register (teacher)");
    }

    private void authenticate(UsernamePasswordAuthenticationToken token, HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = this.authenticationManager.authenticate(token);
        SecurityContext context = this.securityContextHolderStrategy.createEmptyContext();
        context.setAuthentication(authentication);
        this.securityContextHolderStrategy.setContext(context);
        this.securityContextRepository.saveContext(context,request,response);
    }


}
