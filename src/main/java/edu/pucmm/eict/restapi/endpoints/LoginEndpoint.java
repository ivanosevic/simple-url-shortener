package edu.pucmm.eict.restapi.endpoints;

import edu.pucmm.eict.auth.AuthService;
import edu.pucmm.eict.auth.BadCredentialsException;
import edu.pucmm.eict.restapi.apiresponses.ApiError;
import edu.pucmm.eict.restapi.apiresponses.SubError;
import edu.pucmm.eict.restapi.common.*;
import edu.pucmm.eict.restapi.dtos.LoginDto;
import edu.pucmm.eict.restapi.mappers.DtoMapper;
import edu.pucmm.eict.restapi.security.JwtUtils;
import edu.pucmm.eict.restapi.security.LoginForm;
import edu.pucmm.eict.users.User;
import io.javalin.Javalin;
import io.javalin.http.Context;
import org.eclipse.jetty.http.HttpStatus;
import org.modelmapper.ModelMapper;

import java.util.List;

public class LoginEndpoint extends BaseEndPoint {

    private final AuthService authService;
    private final JwtUtils jwtUtils;
    private final ModelMapper modelMapper;

    public LoginEndpoint(Javalin app) {
        super(app);
        authService = AuthService.getInstance();
        jwtUtils = JwtUtils.getInstance();
        modelMapper = DtoMapper.getInstance().getModelMapper();
    }

    private void login(Context ctx) {
        LoginForm loginForm = ctx.bodyValidator(LoginForm.class).get();
        List<SubError> errors = MyValidator.getInstance().validate(loginForm);
        if(!errors.isEmpty()) {
            ApiError apiError = new ApiError("Invalid Form", "The login form is invalid. Please, check errors.", errors);
            ctx.json(apiError);
            ctx.status(HttpStatus.BAD_REQUEST_400);
            return;
        }

        User user =  authService.login(loginForm.getUsername(), loginForm.getPassword());
        // Prepare the response
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
    public void applyRoutes() {
        app.post("/login", this::login);
        app.exception(BadCredentialsException.class, this::handleBadCredentials);
    }
}
