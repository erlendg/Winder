package com.eim.winder;


import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

/**
 * Created by Mari on 04.05.2016.
 * Custom matcher for RecyclerView for the Hamcrest dependency and Espresso.
 * Helps to find and match an item inside the RecycleView, no standard method for this.
 *
 * Based on code from: baconpat https://gist.github.com/baconpat/8405a88d04bd1942eb5e430d33e4faa2
 * Licence: https://www.tldrlegal.com/l/mit
 */
public class RVMatcher {
    private final int id;

    public RVMatcher(int recyclerViewId) {
        this.id = recyclerViewId;
    }

    /**
     * Finds and matches a specific item inside the RecycleView
     * @param position in RecycleView
     * @param targetViewId the view you want to match inside the item
     * @return Matcher
     */
    public Matcher<View> atPositionInView(final int position, final int targetViewId) {
        return new TypeSafeMatcher<View>() {
            Resources res = null;
            View childView;

            public void describeTo(Description description) {
                String descriptionID = Integer.toString(id);
                if (this.res != null) {
                    try {
                        descriptionID = this.res.getResourceName(id);
                    } catch (Resources.NotFoundException e) {
                        descriptionID = String.format("%s name of resource not found",
                                new Object[] { Integer.valueOf
                                        (id) });
                    }
                }
                description.appendText("with id: " + descriptionID);
            }

            public boolean matchesSafely(View view) {
                this.res = view.getResources();
                if (childView == null) {
                    RecyclerView rv =
                            (RecyclerView) view.getRootView().findViewById(id);
                    if (rv != null && rv.getId() == id) {
                        childView = rv.getChildAt(position);
                    }
                    else {
                        return false;
                    }
                }
                if (targetViewId == -1) {
                    return view == childView;
                } else {
                    View targetView = childView.findViewById(targetViewId);
                    return view == targetView;
                }
            }
        };
    }
}
