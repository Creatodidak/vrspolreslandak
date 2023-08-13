package id.creatodidak.vrspolreslandak.admin.model;

import java.util.List;

public class ResponseNotif {
    private long multicast_id;
    private int success;
    private int failure;
    private int canonical_ids;
    private List<NotificationResult> results;

    public long getMulticast_id() {
        return multicast_id;
    }

    public int getSuccess() {
        return success;
    }

    public int getFailure() {
        return failure;
    }

    public int getCanonical_ids() {
        return canonical_ids;
    }

    public List<NotificationResult> getResults() {
        return results;
    }

    public static class NotificationResult {
        private String message_id;

        public String getMessage_id() {
            return message_id;
        }
    }
}
