package server;

import io.javalin.Javalin;

public class WebApplication {
    public static void main(String... args) {
        Javalin app = Javalin.create().start(7000);
        app.get("/solve", ctx -> ctx.result("hello world"));
    }
}
