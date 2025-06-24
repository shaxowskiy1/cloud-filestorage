package ru.shaxowskiy.cloudfilestorage.service.impl;


import com.auth0.jwt.interfaces.DecodedJWT;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.stereotype.Service;
import ru.shaxowskiy.cloudfilestorage.AuthServiceGrpc;
import ru.shaxowskiy.cloudfilestorage.JwtResponse;
import ru.shaxowskiy.cloudfilestorage.security.JWTUtil;

@Service
@Slf4j
@GrpcService
public class AuthServiceImpl extends AuthServiceGrpc.AuthServiceImplBase {

    private final JWTUtil jwtUtil;

    public AuthServiceImpl(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void validateToken(JwtResponse.ValidateTokenRequest request,
                              StreamObserver<JwtResponse.ValidateTokenResponse> responseObserver){
        log.info("Request from client {}", request);

        //TODO принять токен из request, произвести валидацию и выставить юзернейм
        DecodedJWT decodedJWT = jwtUtil.validateToken(request.getToken());
        JwtResponse.ValidateTokenResponse response = JwtResponse.
                ValidateTokenResponse.newBuilder()
                .setUsername(decodedJWT.getClaim("username").asString())
                .build();

        responseObserver.onNext(response);

        responseObserver.onCompleted();

    }
}
