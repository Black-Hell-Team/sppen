package oauth.signpost;

import com.google.gdata.util.common.base.PercentEscaper;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import oauth.signpost.http.HttpParameters;

public class OAuth {
    public static final String ENCODING = "UTF-8";
    public static final String FORM_ENCODED = "application/x-www-form-urlencoded";
    public static final String HTTP_AUTHORIZATION_HEADER = "Authorization";
    public static final String OAUTH_CALLBACK = "oauth_callback";
    public static final String OAUTH_CALLBACK_CONFIRMED = "oauth_callback_confirmed";
    public static final String OAUTH_CONSUMER_KEY = "oauth_consumer_key";
    public static final String OAUTH_NONCE = "oauth_nonce";
    public static final String OAUTH_SIGNATURE = "oauth_signature";
    public static final String OAUTH_SIGNATURE_METHOD = "oauth_signature_method";
    public static final String OAUTH_TIMESTAMP = "oauth_timestamp";
    public static final String OAUTH_TOKEN = "oauth_token";
    public static final String OAUTH_TOKEN_SECRET = "oauth_token_secret";
    public static final String OAUTH_VERIFIER = "oauth_verifier";
    public static final String OAUTH_VERSION = "oauth_version";
    public static final String OUT_OF_BAND = "oob";
    public static final String VERSION_1_0 = "1.0";
    private static final PercentEscaper percentEncoder = new PercentEscaper("-._~", false);

    public static String percentEncode(String str) {
        return str == null ? "" : percentEncoder.escape(str);
    }

    public static String percentDecode(String str) {
        if (str != null) {
            return URLDecoder.decode(str, ENCODING);
        }
        try {
            return "";
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static <T extends Entry<String, String>> void formEncode(Collection<T> collection, OutputStream outputStream) throws IOException {
        if (collection != null) {
            Object obj = 1;
            for (T t : collection) {
                if (obj != null) {
                    obj = null;
                } else {
                    outputStream.write(38);
                }
                outputStream.write(percentEncode(safeToString(t.getKey())).getBytes());
                outputStream.write(61);
                outputStream.write(percentEncode(safeToString(t.getValue())).getBytes());
            }
        }
    }

    public static <T extends Entry<String, String>> String formEncode(Collection<T> collection) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        formEncode(collection, byteArrayOutputStream);
        return new String(byteArrayOutputStream.toByteArray());
    }

    public static HttpParameters decodeForm(String str) {
        HttpParameters httpParameters = new HttpParameters();
        if (isEmpty(str)) {
            return httpParameters;
        }
        for (String str2 : str.split("\\&")) {
            String str22;
            String str3;
            int indexOf = str22.indexOf(61);
            if (indexOf < 0) {
                str22 = percentDecode(str22);
                str3 = null;
            } else {
                String percentDecode = percentDecode(str22.substring(0, indexOf));
                str3 = percentDecode(str22.substring(indexOf + 1));
                str22 = percentDecode;
            }
            httpParameters.put(str22, str3);
        }
        return httpParameters;
    }

    public static HttpParameters decodeForm(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder stringBuilder = new StringBuilder();
        for (String readLine = bufferedReader.readLine(); readLine != null; readLine = bufferedReader.readLine()) {
            stringBuilder.append(readLine);
        }
        return decodeForm(stringBuilder.toString());
    }

    public static <T extends Entry<String, String>> Map<String, String> toMap(Collection<T> collection) {
        HashMap hashMap = new HashMap();
        if (collection != null) {
            for (T t : collection) {
                String str = (String) t.getKey();
                if (!hashMap.containsKey(str)) {
                    hashMap.put(str, t.getValue());
                }
            }
        }
        return hashMap;
    }

    public static final String safeToString(Object obj) {
        return obj == null ? null : obj.toString();
    }

    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    public static String addQueryParameters(String str, String... strArr) {
        String str2 = "?";
        String str3 = "&";
        if (str.contains(str2)) {
            str2 = str3;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(str);
        stringBuilder.append(str2);
        StringBuilder stringBuilder2 = new StringBuilder(stringBuilder.toString());
        for (int i = 0; i < strArr.length; i += 2) {
            if (i > 0) {
                stringBuilder2.append(str3);
            }
            StringBuilder stringBuilder3 = new StringBuilder();
            stringBuilder3.append(percentEncode(strArr[i]));
            stringBuilder3.append("=");
            stringBuilder3.append(percentEncode(strArr[i + 1]));
            stringBuilder2.append(stringBuilder3.toString());
        }
        return stringBuilder2.toString();
    }

    public static String addQueryParameters(String str, Map<String, String> map) {
        String[] strArr = new String[(map.size() * 2)];
        int i = 0;
        for (String str2 : map.keySet()) {
            strArr[i] = str2;
            strArr[i + 1] = (String) map.get(str2);
            i += 2;
        }
        return addQueryParameters(str, strArr);
    }

    public static String addQueryString(String str, String str2) {
        String str3 = "?";
        if (str.contains(str3)) {
            str3 = "&";
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(str);
        stringBuilder.append(str3);
        StringBuilder stringBuilder2 = new StringBuilder(stringBuilder.toString());
        stringBuilder2.append(str2);
        return stringBuilder2.toString();
    }

    public static String prepareOAuthHeader(String... strArr) {
        StringBuilder stringBuilder = new StringBuilder("OAuth ");
        for (int i = 0; i < strArr.length; i += 2) {
            if (i > 0) {
                stringBuilder.append(", ");
            }
            Object obj = (strArr[i].startsWith("oauth_") || strArr[i].startsWith("x_oauth_")) ? 1 : null;
            String percentEncode = obj != null ? percentEncode(strArr[i + 1]) : strArr[i + 1];
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append(percentEncode(strArr[i]));
            stringBuilder2.append("=\"");
            stringBuilder2.append(percentEncode);
            stringBuilder2.append("\"");
            stringBuilder.append(stringBuilder2.toString());
        }
        return stringBuilder.toString();
    }

    public static HttpParameters oauthHeaderToParamsMap(String str) {
        HttpParameters httpParameters = new HttpParameters();
        if (str != null && str.startsWith("OAuth ")) {
            for (String split : str.substring(6).split(",")) {
                String[] split2 = split.split("=");
                httpParameters.put(split2[0].trim(), split2[1].replace("\"", "").trim());
            }
        }
        return httpParameters;
    }

    public static String toHeaderElement(String str, String str2) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(percentEncode(str));
        stringBuilder.append("=\"");
        stringBuilder.append(percentEncode(str2));
        stringBuilder.append("\"");
        return stringBuilder.toString();
    }

    public static void debugOut(String str, String str2) {
        if (System.getProperty("debug") != null) {
            PrintStream printStream = System.out;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("[SIGNPOST] ");
            stringBuilder.append(str);
            stringBuilder.append(": ");
            stringBuilder.append(str2);
            printStream.println(stringBuilder.toString());
        }
    }
}
