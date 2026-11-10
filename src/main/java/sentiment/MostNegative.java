package sentiment;

import org.neo4j.procedure.Description;
import org.neo4j.procedure.Name;
import org.neo4j.procedure.UserAggregationFunction;
import org.neo4j.procedure.UserAggregationResult;
import org.neo4j.procedure.UserAggregationUpdate;

public class MostNegative {

    @UserAggregationFunction("sentiment.mostNegative")
    @Description("Returns the text with the lowest compound score.")
    public MostNegativeAggregator mostNegative() {
        return new MostNegativeAggregator();
    }

    public static class MostNegativeAggregator {
        private final Score scorer = new Score();
        private String worstText;
        private double worstScore = Double.MAX_VALUE;

        @UserAggregationUpdate
        public void update(@Name("text") String text) {
            // your code here
        }

        @UserAggregationResult
        public String result() {
            return worstText;
        }
    }
}
