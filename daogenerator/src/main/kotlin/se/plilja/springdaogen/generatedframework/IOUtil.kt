package se.plilja.springdaogen.generatedframework


fun iOUtil(_package: String): Pair<String, String> {
    return Pair(
            "IOUtil", """
package $_package;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * A class providing utility methods not available in Java 8.
 */
public final class IOUtil {

    private IOUtil() {
        // hide by making private
    }

    public static byte[] readAllBytes(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int len;
        byte[] buff = new byte[1024];
        while ((len = in.read(buff)) != -1) {
            out.write(buff, 0, len);
        }
        return out.toByteArray();
    }
}
    """.trimIndent()
    )
}
