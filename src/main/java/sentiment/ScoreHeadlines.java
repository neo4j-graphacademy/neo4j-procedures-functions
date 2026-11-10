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

    // Change the mode so that the procedure is allowed to write.
    @Procedure(name = "sentiment.scoreHeadlines", mode = Mode.READ)
    @Description("Scores every Headline that has no compound score.")
    public Stream<Result> scoreHeadlines() {
        long scored = 0;
        // your code here
        return Stream.of(new Result(scored));
    }
}
