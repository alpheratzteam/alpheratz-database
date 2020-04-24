package pl.alpheratzteam.database.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author hp888 on 27.11.2019.
 */

public final class IOUtil
{
    private IOUtil() {}

    public static byte[] toByteArray(final InputStream inputStream) throws IOException {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            int read;
            while ((read = inputStream.read()) != -1)
                byteArrayOutputStream.write(read);

            return byteArrayOutputStream.toByteArray();
        } finally {
            inputStream.close();
        }
    }

    public static void write(final InputStream inputStream, final OutputStream outputStream) throws IOException {
        outputStream.write(toByteArray(inputStream));
        outputStream.close();
    }
}