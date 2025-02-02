package kz.sayat.diploma_backend.security.authReq;

public record LoginRequest(
     String email,
     String password
){}
