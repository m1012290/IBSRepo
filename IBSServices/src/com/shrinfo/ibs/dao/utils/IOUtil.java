package com.shrinfo.ibs.dao.utils;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;

import com.shrinfo.ibs.cmn.logger.Logger;
import com.shrinfo.ibs.cmn.utils.Utils;


public class IOUtil {
    
    private static final Logger logger = Logger.getLogger(IOUtil.class);
    
    /**
     * 
     * @throws IOException
     */
    public static byte[] getFilaDataAsArray(InputStream in) throws IOException {

        if(Utils.isEmpty(in)) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        int totalRead = 0;
        if ((null != in) && (null != out)) {
            int bufferSize = 16;
            byte[] ba = new byte[1024 * bufferSize];
            int len = 0;

            while (-1 != (len = in.read(ba))) {
                out.write(ba, 0, len);
                totalRead += len;
            }

            close(in);
            close(out);
        }
        return out.toByteArray();

    }

    public static boolean close(Closeable c) {
        if (null == c) {
            return true;
        }
        try {
            if (c instanceof Flushable) {
                ((Flushable) c).flush();
            }
        } catch (Exception e) {
            logger.warn(e, "couldn't close stream");
        }

        try {
            c.close();
        } catch (IOException e) {
            logger.warn(e, "couldn't close stream");
            return false;
        }
        return true;
    }

}
