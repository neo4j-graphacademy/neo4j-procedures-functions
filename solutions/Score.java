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
        if (text == null || text.isBlank()) {
            return Map.of(
                "compound", 0.0,
                "positive", 0.0,
                "negative", 0.0,
                "neutral", 1.0
            );
        }

        SentimentPolarities polarities =
            SentimentAnalyzer.getScoresFor(text);

        return Map.of(
            "compound", (double) polarities.getCompoundPolarity(),
            "positive", (double) polarities.getPositivePolarity(),
            "negative", (double) polarities.getNegativePolarity(),
            "neutral", (double) polarities.getNeutralPolarity()
        );
    }
}
