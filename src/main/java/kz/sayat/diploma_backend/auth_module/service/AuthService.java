package kz.sayat.diploma_backend.auth_module.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kz.sayat.diploma_backend.auth_module.exceptions.AuthException;

import kz.sayat.diploma_backend.auth_module.mapper.StudentMapper;
import kz.sayat.diploma_backend.auth_module.mapper.TeacherMapper;
import kz.sayat.diploma_backend.auth_module.models.Student;
import kz.sayat.diploma_backend.auth_module.models.Teacher;
import kz.sayat.diploma_backend.auth_module.dto.LoginRequest;
import kz.sayat.diploma_backend.auth_module.dto.RegisterRequest;
import kz.sayat.diploma_backend.auth_module.repository.UserRepository;
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

    private final TeacherService teacherService;
    private final TeacherMapper teacherMapper;
    private final AuthenticationManager authenticationManager;
    private final SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();
    private final SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();
    private final StudentMapper studentMapper;
    private final StudentService studentService;
    private final UserRepository  userRepository;


    public void login(HttpServletRequest request, HttpServletResponse response, LoginRequest authRequest) {
        UsernamePasswordAuthenticationToken token = UsernamePasswordAuthenticationToken.unauthenticated
            (authRequest.email(), authRequest.password());
        authenticate(token, request, response);

        System.out.println(authRequest.email()+"   signed in");
    }

    public void register(RegisterRequest request){

        if(isEmailExist(request.email())){
            throw new AuthException("Email already exists");
        }

        Student student= studentMapper.toStudent(request);
        studentService.save(student);

        System.out.println(request.email()+"  registered");
    }


    public void registerTeacher(RegisterRequest request){
        if(isEmailExist(request.email())){
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

    private boolean isEmailExist(String email) {
        return userRepository.existsByEmail(email);
    }

}
