package edu.pucmm.eict.common;

import io.javalin.Javalin;

/**
 * <p>Abstracts Javalin Controller to be used in separate classes.
 * You need to pass your main instance of javalin.
 * </p>
 * <br />
 * <p>Example of usage:</p>
 * <pre>
 *  {@code
 *  public class MyAwesomeController extends Controller {
 *      public MyAwesomeController(Javalin app) {
 *          super(app);
 *          // Other stuff...
 *      }
 *
 *      @Override
 *      public void applyRoutes() {
 *          app.get(...);
 *          app.post(...);
 *      }
 *  }
 * }
 * </pre>
 */
public abstract class Controller {

    protected Javalin app;

    public Controller(Javalin app) {
        this.app = app;
    }

    public abstract void applyRoutes();
}