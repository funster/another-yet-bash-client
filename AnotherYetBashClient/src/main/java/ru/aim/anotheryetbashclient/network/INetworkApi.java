package ru.aim.anotheryetbashclient.network;

import java.io.IOException;
import java.io.InputStream;

public interface INetworkApi {

    ResponseWrapper performRequest(String url) throws IOException;

    class ResponseWrapper {
        public final InputStream response;
        public final String charset;

        public ResponseWrapper(InputStream response, String charset) {
            this.response = response;
            this.charset = charset;
        }
    }
}
