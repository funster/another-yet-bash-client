package ru.aim.anotheryetbashclient.helper.impl;

import android.net.http.AndroidHttpClient;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.params.BasicHttpParams;
import org.junit.Test;
import ru.aim.anotheryetbashclient.helper.impl.BashSearchAction;

/**
 *
 */
public class SearchActionTest {

    @Test
    public void testSimpleQuery() throws Exception {
        AndroidHttpClient httpClient = AndroidHttpClient.newInstance("test-client");
        HttpGet httpGet = new HttpGet(BashSearchAction.);
        BasicHttpParams httpParams = new BasicHttpParams();
        httpParams.setParameter("text", search);
        httpGet.setParams(httpParams);
        HttpResponse httpResponse = httpClient.execute(httpGet);
    }
}
