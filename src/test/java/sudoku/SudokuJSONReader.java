package sudoku;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.io.IOException;

public class SudokuJSONReader {

    public static Sudoku read(String json) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule("CandidateModule");
        module.addDeserializer(Candidate.class, new CandidateDeserializer());
        mapper.registerModule(module);
        return mapper.readValue(json, Sudoku.class);
    }

    static class CandidateDeserializer extends StdDeserializer<Candidate> {

        public CandidateDeserializer() {
            this(Candidate.class);
        }

        protected CandidateDeserializer(Class<?> vc) {
            super(vc);
        }

        @Override
        public Candidate deserialize(JsonParser p,
                DeserializationContext ctxt) throws IOException, JsonProcessingException {
            JsonNode node = p.getCodec().readTree(p);
            return new Candidate(node.get(0).asInt(), node.get(1).asInt(), node.get(2).textValue().charAt(0));
        }
    }
}
