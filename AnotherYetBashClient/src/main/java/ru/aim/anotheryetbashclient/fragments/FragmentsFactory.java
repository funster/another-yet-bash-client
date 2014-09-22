package ru.aim.anotheryetbashclient.fragments;

import android.support.v4.app.Fragment;
import android.util.SparseArray;

import ru.aim.anotheryetbashclient.ActionsAndIntents;

/**
 *
 */
public final class FragmentsFactory {

    static SparseArray<Class<?>> map = new SparseArray<Class<?>>();

    static {
        map.put(ActionsAndIntents.TYPE_NEW, FreshFragment.class);
        map.put(ActionsAndIntents.TYPE_RANDOM, RandomFragment.class);
        map.put(ActionsAndIntents.TYPE_BEST, BestFragment.class);
        map.put(ActionsAndIntents.TYPE_BY_RATING, RatingFragment.class);
        map.put(ActionsAndIntents.TYPE_SEARCH, SearchFragment.class);
        map.put(ActionsAndIntents.TYPE_FAVORITES, FavoritesFragment.class);
//        map.put(ActionsAndIntents.TYPE_ABYSS, AbstractBashFragment.class);
//        map.put(ActionsAndIntents.TYPE_TOP_ABYSS, TopAbyssFragment.class);
//        map.put(ActionsAndIntents.TYPE_BEST_ABYSS, BestAbyssFragment.class);
    }

    private FragmentsFactory() {
        throw new AssertionError();
    }

    @SuppressWarnings("TryWithIdenticalCatches")
    public static Fragment getFragment(int type) {
        Class<?> clazz = map.get(type);
        if (clazz == null) {
            throw new UnsupportedOperationException("not implemented yet");
        }
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
