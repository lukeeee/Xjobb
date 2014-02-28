package se.xjobb.scardfeud;

/**
 * Created by Svempa on 2013-12-09.
 */
public class User {

    private String username;
    private String password;
    private String countryCode;
    private int id;

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

    public int getUserId(){
        return this.id;
    }

    public void setUserId(int id){
        this.id = id;
    }

    // Stored information about the user
    // Available to the hole application, during running
    public static class UserDetails {
        private static String username;
        private static String identifier;
        private static int userId;
        private static String userCountryCode;
        private static String deviceRegId;
        private static int appVersion;
        private static boolean vibration;
        private static boolean sound;
        private static boolean soundNotification;
        private static ClassLoader classLoader;
        private static boolean hasRated = false;

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

        public static void setUserCountryCode(String countryCodeIn){
            userCountryCode = countryCodeIn;
        }

        public static String getUserCountryCode(){
            return userCountryCode;
        }

        public static String getDeviceRegId(){
            return deviceRegId;
        }

        public static void setDeviceRegId(String deviceRegIdIn){
            deviceRegId = deviceRegIdIn;
        }

        public static int getAppVersion(){
            return appVersion;
        }

        public static void setAppVersion(int appVersionIn){
            appVersion = appVersionIn;
        }

        public static boolean getVibration(){
            return vibration;
        }

        public static void setVibration(boolean vibrationIn){
            vibration = vibrationIn;
        }

        public static boolean getSound(){
            return sound;
        }

        public static void setSound(boolean soundIn){
            sound = soundIn;
        }

        public static boolean getHasRated(){
            return hasRated;
        }

        public static void setHasRated(boolean hasRated){
            hasRated = hasRated;
        }

        public static boolean getNotificationSound(){
            return soundNotification;
        }

        public static void setNotificationSound(boolean soundNotificationIn){
            soundNotification = soundNotificationIn;
        }

        public static ClassLoader getClassLoader() {
            return classLoader;
        }

        public static void setClassLoader(ClassLoader classLoader) {
            classLoader = classLoader;
        }
    }


}
