package sentiment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
public class MostNegativeTest {

    private Neo4j neo4j;
    private Driver driver;

    @BeforeAll
    void start() {
        neo4j = Neo4jBuilders.newInProcessBuilder()
            .withDisabledServer()
            .withAggregationFunction(MostNegative.class)
            .build();
        driver = GraphDatabase.driver(neo4j.boltURI());
    }

    @AfterAll
    void stop() {
        driver.close();
        neo4j.close();
    }

    @Test
    void returnsTheTextWithTheLowestScore() {
        try (Session session = driver.session()) {
            String worst = session.run("""
                UNWIND [
                    'I love this great product',
                    'This is terrible and awful',
                    'The report is on the table'
                ] AS text
                RETURN sentiment.mostNegative(text) AS worst
                """).single().get("worst").asString();

            assertEquals("This is terrible and awful", worst);
        }
    }

    @Test
    void ignoresNullAndBlankText() {
        try (Session session = driver.session()) {
            String worst = session.run("""
                UNWIND [null, '', '   ', 'What a terrible day'] AS text
                RETURN sentiment.mostNegative(text) AS worst
                """).single().get("worst").asString();

            assertEquals("What a terrible day", worst);
        }
    }

    @Test
    void returnsNullWhenThereIsNoUsableText() {
        try (Session session = driver.session()) {
            boolean isNull = session.run("""
                UNWIND [null, ''] AS text
                RETURN sentiment.mostNegative(text) AS worst
                """).single().get("worst").isNull();

            assertTrue(isNull);
        }
    }

    @Test
    void groupsByTheOtherColumns() {
        try (Session session = driver.session()) {
            long rows = session.run("""
                UNWIND [
                    {stock: 'A', text: 'Great news'},
                    {stock: 'A', text: 'Awful news'},
                    {stock: 'B', text: 'Fine results'}
                ] AS row
                RETURN row.stock AS stock,
                       sentiment.mostNegative(row.text) AS worst
                """).list().size();

            assertEquals(2, rows);
        }
    }
}
