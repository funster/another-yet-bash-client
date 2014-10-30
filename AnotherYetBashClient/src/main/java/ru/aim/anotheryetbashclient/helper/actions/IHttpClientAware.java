package ru.aim.anotheryetbashclient.helper.actions;

import org.apache.http.client.HttpClient;

public interface IHttpClientAware {

    void setHttpClient(HttpClient httpClient);
}
