package sentiment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Session;
import org.neo4j.harness.Neo4j;
import org.neo4j.harness.Neo4jBuilders;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ScoreTest {

    private Neo4j neo4j;
    private Driver driver;

    @BeforeAll
    void start() {
        neo4j = Neo4jBuilders.newInProcessBuilder()
            .withDisabledServer()
            .withFunction(Score.class)
            .build();
        driver = GraphDatabase.driver(neo4j.boltURI());
    }

    @AfterAll
    void stop() {
        driver.close();
        neo4j.close();
    }

    private Map<String, Object> score(String argument) {
        try (Session session = driver.session()) {
            return session
                .run("RETURN sentiment.score(" + argument + ") AS scores")
                .single()
                .get("scores")
                .asMap();
        }
    }

    @Test
    void positiveTextHasAPositiveScore() {
        Map<String, Object> scores =
            score("'The movie was great'");

        assertTrue((Double) scores.get("compound") > 0.5);
        assertTrue((Double) scores.get("positive") > 0.0);
        assertEquals(0.0, (Double) scores.get("negative"));
    }

    @Test
    void capitalsIncreaseTheScore() {
        Map<String, Object> normal =
            score("'The movie was great'");
        Map<String, Object> shouting =
            score("'The movie was GREAT!'");

        assertTrue(
            (Double) shouting.get("compound")
                > (Double) normal.get("compound")
        );
    }

    @Test
    void blankTextIsNeutral() {
        Map<String, Object> scores = score("''");

        assertEquals(0.0, (Double) scores.get("compound"));
        assertEquals(1.0, (Double) scores.get("neutral"));
    }

    @Test
    void nullTextIsNeutral() {
        Map<String, Object> scores = score("null");

        assertEquals(0.0, (Double) scores.get("compound"));
        assertEquals(1.0, (Double) scores.get("neutral"));
    }
}
