package tradecast.data;

public final class LoginMockCredentials {

    public static final String MOCK_USER_ID = "demo";
    public static final String MOCK_PASSWORD = "Password01";

    private LoginMockCredentials() {
    }

    public static int passwordLength() {
        return MOCK_PASSWORD.length();
    }
}
