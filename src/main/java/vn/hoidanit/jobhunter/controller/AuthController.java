package vn.hoidanit.jobhunter.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.dto.LoginDTO;
import vn.hoidanit.jobhunter.domain.dto.ResLoginDTO;
import vn.hoidanit.jobhunter.service.UserService;
import vn.hoidanit.jobhunter.util.SecurityUtil;

@RestController
@RequestMapping("/api/v1")
public class AuthController {

    private AuthenticationManagerBuilder authenticationManagerBuilder;

    private SecurityUtil securityUtil;

    private UserService userService;

     @Value("${hoidanit.jwt.refresh-token-validity-in-seconds}")
    private long jwtRefreshExpiration;

    public AuthController(AuthenticationManagerBuilder authenticationManagerBuilder,
            SecurityUtil securityUtil, UserService userService) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.securityUtil = securityUtil;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<ResLoginDTO> login(@Valid @RequestBody LoginDTO loginDTO) {

        //Nạp input gồm username/password vào Security
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginDTO.getUsername(), loginDTO.getPassword());
        //xác thực người dùng => cần viết hàm loadUserByUsername
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        String access_token = this.securityUtil.createAccessToken(authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        ResLoginDTO res = new ResLoginDTO();
        res.setAccessToken(access_token);
        User user = this.userService.handleGetUserByUsername(loginDTO.getUsername());
        if (user != null) {
            res.setUser(new ResLoginDTO.UserLogin(user.getId(), user.getName(), user.getEmail()));
        }
        String refresh_token = this.securityUtil.createRefreshToken(loginDTO.getUsername(), res);

        //update token
        this.userService.updateUserToken(refresh_token, loginDTO.getUsername());

        //set cookies
        ResponseCookie cookie = ResponseCookie.from("refresh_token", refresh_token)
        .httpOnly(true)
        .secure(true)
        .path("/")
        .maxAge(jwtRefreshExpiration)
        .build();
        return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE, cookie.toString())
            .body(res);
    }
}
