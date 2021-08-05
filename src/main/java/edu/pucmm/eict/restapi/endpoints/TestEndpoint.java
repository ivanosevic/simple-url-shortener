package edu.pucmm.eict.restapi.endpoints;

import edu.pucmm.eict.restapi.apiresponses.ApiMessage;
import edu.pucmm.eict.users.Role;
import io.javalin.Javalin;
import io.javalin.http.Context;
import org.eclipse.jetty.http.HttpStatus;

import java.util.Set;

public class TestEndpoint extends BaseEndPoint {

    public TestEndpoint(Javalin app) {
        super(app);
    }

    private void welcome(Context ctx) {
        ApiMessage apiMessage = new ApiMessage("Welcome", "You are authorized to use this endpoint.");
        ctx.status(HttpStatus.OK_200).json(apiMessage);
    }

    @Override
    public void applyRoutes() {
        app.get("/test", this::welcome, Set.of(Role.ADMIN, Role.APP_USER));
    }
}
