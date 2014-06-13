package ru.aim.anotheryetbashclient.fragments;

import android.support.v4.app.Fragment;
import ru.aim.anotheryetbashclient.ActionsAndIntents;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public final class FragmentsFactory {

    static Map<Integer, Class<?>> map = new HashMap<Integer, Class<?>>();

    static {
        map.put(ActionsAndIntents.TYPE_NEW, FreshBashFragment.class);
        map.put(ActionsAndIntents.TYPE_RANDOM, RandomBashFragment.class);
        map.put(ActionsAndIntents.TYPE_BEST, BestBashFragment.class);
        map.put(ActionsAndIntents.TYPE_BY_RATING, ByRatingBashFragment.class);
        map.put(ActionsAndIntents.TYPE_ABYSS, AbstractBashFragment.class);
        map.put(ActionsAndIntents.TYPE_TOP_ABYSS, TopAbyssFragment.class);
        map.put(ActionsAndIntents.TYPE_BEST_ABYSS, BestAbyssFragment.class);
    }

    private FragmentsFactory() {
        throw new AssertionError();
    }

    @SuppressWarnings("TryWithIdenticalCatches")
    public static Fragment getFragment(int type) {
        Class<?> clazz = map.get(type);
        try {
            Object fragment = clazz.newInstance();
            return (Fragment) fragment;
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
