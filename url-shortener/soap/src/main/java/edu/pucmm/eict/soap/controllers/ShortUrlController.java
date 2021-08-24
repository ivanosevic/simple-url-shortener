package edu.pucmm.eict.soap.controllers;

import edu.pucmm.eict.soap.webservices.ShortUrlWebServices;
import io.javalin.Javalin;
import org.eclipse.jetty.http.spi.JettyHttpContext;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import javax.xml.ws.Endpoint;

public class ShortUrlController extends SoapController {

    private final ShortUrlWebServices shortUrlWebServices;

    @Inject
    public ShortUrlController(@NotNull Javalin app, ShortUrlWebServices shortUrlWebServices) {
        super(app);
        this.shortUrlWebServices = shortUrlWebServices;
    }

    @Override
    public void applyRoutes() {
        ContextHandlerCollection contextHandlerCollection = new ContextHandlerCollection();
        server.setHandler(contextHandlerCollection);

        // The route becomes: http://localhost:port/ws/ShortUrlWebServices?wsdl
        try {
            JettyHttpContext context = build("/ws");
            var endPoint = Endpoint.create(shortUrlWebServices);
            endPoint.publish(context);
        } catch (Exception ex) {
            ex.printStackTrace();;
        }
    }
}
