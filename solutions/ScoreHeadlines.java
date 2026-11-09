package sentiment;

import java.util.Map;
import java.util.stream.Stream;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Transaction;
import org.neo4j.procedure.Context;
import org.neo4j.procedure.Description;
import org.neo4j.procedure.Mode;
import org.neo4j.procedure.Procedure;

public class ScoreHeadlines {

    @Context
    public Transaction tx;

    public static class Result {
        public long scored;

        public Result(long scored) {
            this.scored = scored;
        }
    }

    @Procedure(name = "sentiment.scoreHeadlines", mode = Mode.WRITE)
    @Description("Scores every Headline that has no compound score.")
    public Stream<Result> scoreHeadlines() {
        Score scorer = new Score();
        long scored = 0;

        try (ResourceIterator<Node> nodes =
                 tx.findNodes(Label.label("Headline"))) {
            while (nodes.hasNext()) {
                Node headline = nodes.next();
                if (headline.hasProperty("compound")) {
                    continue;
                }

                Object text = headline.getProperty("headline", null);
                Map<String, Double> scores = scorer.score(
                    text instanceof String s ? s : null
                );
                scores.forEach(headline::setProperty);
                scored++;
            }
        }

        return Stream.of(new Result(scored));
    }
}
