package ru.aim.anotheryetbashclient;

import javax.inject.Singleton;

import dagger.Component;
import ru.aim.anotheryetbashclient.network.INetworkApi;

@Singleton
@Component(modules = BashModule.class)
public interface BashComponent {

    INetworkApi networkApi();

    final class Initializer {

        public static BashComponent init() {
            return DaggerBashComponent.builder().build();
        }
    }
}
