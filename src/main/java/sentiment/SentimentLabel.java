package sentiment;

import org.neo4j.procedure.Description;
import org.neo4j.procedure.Name;
import org.neo4j.procedure.UserFunction;

public class SentimentLabel {

    @UserFunction("sentiment.label")
    @Description("Turns a compound score into positive, negative or neutral.")
    public String label(@Name("compound") Double compound) {
        if (compound == null) {
            return null;
        }
        // Replace both 0.0 values with the VADER thresholds.
        if (compound >= 0.0) {
            return "positive";
        }
        if (compound <= 0.0) {
            return "negative";
        }
        return "neutral";
    }
}
