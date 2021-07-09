package edu.pucmm.eict.auth;

import edu.pucmm.eict.common.Controller;
import edu.pucmm.eict.common.MyValidator;
import edu.pucmm.eict.users.Role;
import edu.pucmm.eict.users.User;
import edu.pucmm.eict.users.UserForm;
import edu.pucmm.eict.users.UserService;
import io.javalin.Javalin;
import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AuthController extends Controller {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    private final MyValidator myValidator;
    private final AuthService authService;
    private final UserService userService;

    public AuthController(Javalin app) {
        super(app);
        myValidator = MyValidator.getInstance();
        authService = AuthService.getInstance();
        userService = UserService.getInstance();
    }

    private void loginView(Context ctx) {
        ctx.status(200).render("templates/login.vm");
    }

    private void signupView(Context ctx) {
        ctx.status(200).render("templates/signup.vm");
    }

    private void processLogin(Context ctx) {
        String username = ctx.formParam("username");
        String password = ctx.formParam("password");
        String rememberMe = ctx.formParam("remember-me");
        User user = authService.login(username, password);

        if(rememberMe != null) {
            String secret = authService.assignSecret(user);
            int duration = 60 * 60 * 24 * 7;
            ctx.cookie("remember-me", secret, duration);
        }

        ctx.sessionAttribute("user", user);
        ctx.req.changeSessionId();

        // After login sucessfully, redirect...
        if(user.getRoles().contains(Role.ADMIN)) {
            ctx.redirect("/admin-zone/dashboard");
        } else {
            ctx.redirect("/app/dashboard");
        }
    }

    private void processSignup(Context ctx) {
        // Get the form data and build the form
        String email = ctx.formParam("email");
        String username = ctx.formParam("username");
        String password = ctx.formParam("password");
        String name = ctx.formParam("name");
        String lastname = ctx.formParam("lastname");
        UserForm form = new UserForm(username, password, email, name, lastname, Set.of("APP_USER"));

        // Validation for form...
        Map<String, List<String>> errors = myValidator.validate(form);

        // Data to be sent to model...
        var data = new HashMap<String, Object>();

        if(!errors.isEmpty()) {
            log.info("Errors: {}", errors);
            data.put("formErrors", errors);
            ctx.status(400).render("templates/signup.vm", data);
            return;
        }

        // Create the user
        User user = userService.create(form);

        // Immediately sign in the user.
        ctx.sessionAttribute("user", user);
        ctx.req.changeSessionId();
        ctx.redirect("/app/dashboard");
    }

    private void handleBadCredentials(Exception ex, Context ctx) {
        var data = new HashMap<String, Object>();
        data.put("badCredentials", true);
        ctx.status(400).render("templates/login.vm", data);
    }

    @Override
    public void applyRoutes() {
        app.get("/app/login", this::loginView);
        app.get("/app/signup", this::signupView);
        app.post("/auth/login", this::processLogin);
        app.post("/auth/signup", this::processSignup);
        app.exception(BadCredentialsException.class, this::handleBadCredentials);
    }
}