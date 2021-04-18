package server;

import io.javalin.Javalin;
import io.javalin.plugin.json.JavalinJackson;

public class WebApplication {
    public static void main(String... args) {
        Javalin app = Javalin.create(config -> {
            config.defaultContentType = "application/json";
            config.addStaticFiles("/webapp/public");
        }).start(7000);

        app.get("/solve", ctx -> ctx.result("hello world"));

        app.post("/solve", ctx -> {
            var puzzle = ctx.body();
            System.err.println(puzzle);
            ctx.status(204);
        });
    }
}
