package edu.pucmm.eict.webapp.configuration;

import io.javalin.http.Context;
import io.javalin.plugin.rendering.FileRenderer;
import org.jetbrains.annotations.NotNull;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import java.util.Map;
import java.util.concurrent.TimeUnit;

public class CustomThymeleafRenderer implements FileRenderer {

    private final TemplateEngine templateEngine;

    public CustomThymeleafRenderer() {
        templateEngine = templateEngine();
    }

    private ITemplateResolver templateResolver() {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setCharacterEncoding("UTF-8");
        templateResolver.setCacheable(false);
        templateResolver.setCacheTTLMs(TimeUnit.MINUTES.toMillis(30));
        templateResolver.setTemplateMode(TemplateMode.HTML);
        return templateResolver;
    }

    private TemplateEngine templateEngine() {
        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver());
        return templateEngine;
    }


    @Override
    public String render(@NotNull String filePath, @NotNull Map<String, Object> model, @NotNull Context ctx) throws Exception {
        WebContext context = new WebContext(ctx.req, ctx.res, ctx.req.getServletContext(), ctx.req.getLocale());
        context.setVariables(model);
        return templateEngine.process(filePath, context);
    }
}
