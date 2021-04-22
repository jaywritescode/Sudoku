package server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.javalin.Javalin;
import io.javalin.plugin.json.JavalinJackson;
import sudoku.Candidate;
import sudoku.Sudoku;

import java.util.Set;

public class WebApplication {

    private static Set<Candidate> solve(Sudoku sudoku) {
        return sudoku.solve();
    }

    public static void main(String... args) {
        Javalin app = Javalin.create(config -> {
            config.defaultContentType = "application/json";
            config.addStaticFiles("/webapp/public");
        }).start(7000);

        app.post("/solve", ctx -> {
            Sudoku puzzle = JavalinJackson.defaultObjectMapper().readValue(ctx.body(), Sudoku.class);

            Set<Candidate> solution = puzzle.solve();

            ObjectWriter jsonWriter = new ObjectMapper()
                    .enable(SerializationFeature.WRAP_ROOT_VALUE)
                    .writer()
                    .withRootName("solution");

            ctx.result(jsonWriter.writeValueAsString(solution)).contentType("application/json");
            ctx.status(200);
        });
    }
}
