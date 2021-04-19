package server;

import com.fasterxml.jackson.databind.JsonNode;
import io.javalin.Javalin;
import io.javalin.plugin.json.JavalinJackson;

import java.util.stream.Collectors;

public class WebApplication {

    private static JsonNode perform(JsonNode puzzle) {
        var boxHeight = puzzle.get("boxheight").asInt();
        var boxWidth = puzzle.get("boxwidth").asInt();
        var domain = puzzle.findValuesAsText("domain")
                .stream()
                .map(s -> s.charAt(0))
                .collect(Collectors.toSet());
        var candidates = puzzle.findValues("puzzle");

        return null;
    }

    public static void main(String... args) {
        Javalin app = Javalin.create(config -> {
            config.defaultContentType = "application/json";
            config.addStaticFiles("/webapp/public");
        }).start(7000);

        app.get("/solve", ctx -> ctx.result("hello world"));

        app.post("/solve", ctx -> {
            var puzzle = JavalinJackson.defaultObjectMapper().readTree(ctx.body());
            perform(puzzle);
            System.err.println(puzzle);
            ctx.status(204);
        });
    }
}
