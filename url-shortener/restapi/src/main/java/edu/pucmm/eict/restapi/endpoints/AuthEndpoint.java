package edu.pucmm.eict.restapi.endpoints;

import edu.pucmm.eict.restapi.config.JwtUtils;
import edu.pucmm.eict.restapi.dtos.LoginDto;
import edu.pucmm.eict.restapi.forms.LoginForm;
import edu.pucmm.eict.restapi.reponses.ApiError;
import edu.pucmm.eict.users.AuthService;
import edu.pucmm.eict.users.BadCredentialsException;
import edu.pucmm.eict.users.User;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.plugin.openapi.annotations.*;
import org.eclipse.jetty.http.HttpStatus;
import org.modelmapper.ModelMapper;

import javax.inject.Inject;

public class AuthEndpoint extends BaseEndpoint {

    private final AuthService authService;
    private final JwtUtils jwtUtils;
    private final ModelMapper modelMapper;

    @Inject
    public AuthEndpoint(Javalin app, AuthService authService, JwtUtils jwtUtils, ModelMapper modelMapper) {
        super(app);
        this.authService = authService;
        this.jwtUtils = jwtUtils;
        this.modelMapper = modelMapper;
    }

    @OpenApi(
            path = "/login",
            method = HttpMethod.POST,
            tags = ("Authentication"),
            summary = "Log in to the application",
            requestBody = @OpenApiRequestBody(content = @OpenApiContent(from = LoginForm.class), required = true),
            responses = {
                    @OpenApiResponse(status = "200", content = @OpenApiContent(from = LoginDto.class)),
                    @OpenApiResponse(status = "401", content = @OpenApiContent(from = ApiError.class))
            }
    )
    private void login(Context ctx) {
        LoginForm loginForm = ctx.bodyValidator(LoginForm.class).get();
        User user = authService.login(loginForm.getUsername(), loginForm.getPassword());
        String token = "Bearer " + jwtUtils.generateToken(user.getUsername());
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
        app.exception(BadCredentialsException.class, this::handleBadCredentials);
    }
}
