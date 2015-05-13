package ru.aim.anotheryetbashclient;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.aim.anotheryetbashclient.network.INetworkApi;
import ru.aim.anotheryetbashclient.network.NetworkApiImpl;

@Module
public class BashModule {

    @Provides
    @Singleton
    INetworkApi provideNetworkApi() {
        return new NetworkApiImpl();
    }
}
