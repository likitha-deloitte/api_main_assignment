package utils;

public class RandomString {

    public static String getAlphaNumericString(int stringLength) {
        String alphaNumericString="0123456789" + "abcdefghijklmnopqrstuvwxyz";
        StringBuilder sb = new StringBuilder(stringLength);
        for (int i =0;i<stringLength;i++) {
            int index = (int)(alphaNumericString.length()*Math.random());
            sb.append(alphaNumericString.charAt(index));
        }
        return sb.toString();

    }
}
