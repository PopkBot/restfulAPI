package egorov.restfulAPI;


public enum Status {
    COMPLETED,
    FAILED,
    WAITING,
    IN_PROGRESS;

    public static boolean isContains(String string) {
        try {
            Status.valueOf(string);
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }
}
