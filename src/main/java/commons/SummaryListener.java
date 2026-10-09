package commons;

import model.AiResponse;

public interface SummaryListener {
    void onSummaryReceived(AiResponse aiResponse);
}
