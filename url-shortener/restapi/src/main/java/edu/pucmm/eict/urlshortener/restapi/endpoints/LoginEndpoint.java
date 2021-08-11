package edu.pucmm.eict.urlshortener.restapi.endpoints;

import edu.pucmm.eict.urlshortener.restapi.config.JwtUtil;
import edu.pucmm.eict.urlshortener.restapi.dtos.LoginDto;
import edu.pucmm.eict.urlshortener.restapi.forms.LoginForm;
import edu.pucmm.eict.urlshortener.restapi.responses.ApiError;
import edu.pucmm.eict.urlshortener.restapi.responses.ApiMessage;
import edu.pucmm.eict.urlshortener.users.AuthService;
import edu.pucmm.eict.urlshortener.users.BadCredentialsException;
import edu.pucmm.eict.urlshortener.users.RoleList;
import edu.pucmm.eict.urlshortener.users.User;
import io.javalin.Javalin;
import io.javalin.http.Context;
import org.eclipse.jetty.http.HttpStatus;
import org.modelmapper.ModelMapper;

import java.util.Set;

public class LoginEndpoint extends BaseEndpoint{

    private final AuthService authService;
    private final JwtUtil jwtUtil;
    private final ModelMapper modelMapper;

    public LoginEndpoint(Javalin app, AuthService authService, JwtUtil jwtUtil, ModelMapper modelMapper) {
        super(app);
        this.authService = authService;
        this.jwtUtil = jwtUtil;
        this.modelMapper = modelMapper;
    }

    private void welcome(Context ctx) {
        ApiMessage apiMessage = new ApiMessage("Welcome to Wolfisc - Simple Url Shortener API", "Hope you enjoy this cool API");
        ctx.status(HttpStatus.OK_200).json(apiMessage);
    }

    private void login(Context ctx) {
        LoginForm loginForm = ctx.bodyValidator(LoginForm.class).get();
        User user = authService.login(loginForm.getUsername(), loginForm.getPassword());
        String token = "Bearer " + jwtUtil.generateToken(user.getUsername());
        LoginDto loginDto = modelMapper.map(user, LoginDto.class);
        loginDto.setToken(token);
        ctx.status(HttpStatus.OK_200).json(loginDto);
    }

    private void handleBadCredentials(Exception ex, Context ctx) {
        ApiError apiError = new ApiError("Bad credentials", "Please, check your username or password");
        ctx.status(HttpStatus.BAD_REQUEST_400).json(apiError);
    }

    @Override
    public void applyEndpoints() {
        app.post("/login", this::login);
        app.get("/welcome", this::welcome, Set.of(RoleList.APP_USER, RoleList.ADMIN));
        app.exception(BadCredentialsException.class, this::handleBadCredentials);
    }
}
