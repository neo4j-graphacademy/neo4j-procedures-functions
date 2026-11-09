package sentiment;

import com.vader.sentiment.analyzer.SentimentAnalyzer;
import com.vader.sentiment.analyzer.SentimentPolarities;
import java.util.Map;
import org.neo4j.procedure.Description;
import org.neo4j.procedure.Name;
import org.neo4j.procedure.UserFunction;

public class Score {

    @UserFunction("sentiment.score")
    @Description("Scores text with VADER. Returns compound, "
        + "positive, negative and neutral.")
    public Map<String, Double> score(@Name("text") String text) {
        // your code here
        return Map.of();
    }
}
