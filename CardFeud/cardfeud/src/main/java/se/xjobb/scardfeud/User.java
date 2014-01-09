package se.xjobb.scardfeud;

/**
 * Created by Svempa on 2013-12-09.
 */
public class User {

    private String username;
    private String password;
    private String countryCode;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }


    // Stored information about the user
    // Available to the hole application, during running
    public static class UserDetails {
        private static String username;
        private static String identifier;
        private static int userId;
        private static String deviceId;

        public static String getUsername(){
            return username;
        }

        public static void setUsername(String usernameIn){
            username = usernameIn;
        }

        public static String getIdentifier(){
            return identifier;
        }

        public static void setIdentifier(String identifierIn){
            identifier = identifierIn;
        }

        public static int getUserId(){
            return userId;
        }

        public static void setUserId(int userIdIn){
            userId = userIdIn;
        }

        public static String getDeviceId(){
            return deviceId;
        }

        public static void setDeviceId(String deviceIdIn){
            deviceId = deviceIdIn;
        }

    }


}
