package enums;

public enum JobApplicationStatus {
    SAVED,
    APPLIED,
    ASSESSMENT,
    GROUP_DISCUSSION,
    INTERVIEW,
    FINAL_INTERVIEW,
    OFFER,
    ACCEPTED,
    REJECTED,
    WITHDRAWN,
    UNKNOWN;

    public static boolean isValid(String status) {
        for (JobApplicationStatus s : JobApplicationStatus.values()) {
            if (s.name().equals(status)) {
                return true;
            }
        }
        return false;
    }
}
