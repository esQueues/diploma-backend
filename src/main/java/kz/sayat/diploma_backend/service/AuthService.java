package kz.sayat.diploma_backend.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kz.sayat.diploma_backend.exception.AuthException;

import kz.sayat.diploma_backend.models.User;
import kz.sayat.diploma_backend.models.UserRole;
import kz.sayat.diploma_backend.repository.UserRepository;
import kz.sayat.diploma_backend.security.authReq.LoginRequest;
import kz.sayat.diploma_backend.security.authReq.RegisterRequest;
import lombok.AllArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
@AllArgsConstructor
public class AuthService {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();
    private final SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();
//    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

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
        User user = new User();
        user.setEmail(request.email());
        user.setPassword(encoder.encode(request.password()));
        user.setFirstname(request.firstname());
        user.setLastname(request.lastname());
        user.setRole(UserRole.STUDENT);
        user.setCreatedAt(LocalDateTime.now());
        userService.save(user);
        //vertification
        System.out.println(request.email()+"signed up");
    }





    private void authenticate(UsernamePasswordAuthenticationToken token, HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = this.authenticationManager.authenticate(token);
        SecurityContext context = this.securityContextHolderStrategy.createEmptyContext();
        context.setAuthentication(authentication);
        this.securityContextHolderStrategy.setContext(context);
        this.securityContextRepository.saveContext(context,request,response);
    }


}
