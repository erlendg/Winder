package com.eim.winder;


import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

/**
 * Created by Mari on 04.05.2016.
 */
public class RecyclerViewMatcher {
    private final int id;

    public RecyclerViewMatcher(int recyclerViewId) {
        this.id = recyclerViewId;
    }

    public Matcher<View> atPositionInView(final int position, final int targetViewId) {

        return new TypeSafeMatcher<View>() {
            Resources resources = null;
            View childView;

            public void describeTo(Description description) {
                String descriptionID = Integer.toString(id);
                if (this.resources != null) {
                    try {
                        descriptionID = this.resources.getResourceName(id);
                    } catch (Resources.NotFoundException e) {
                        descriptionID = String.format("%s (name of resource not found)",
                                new Object[] { Integer.valueOf
                                        (id) });
                    }
                }
                description.appendText("with id: " + descriptionID);
            }

            public boolean matchesSafely(View view) {
                this.resources = view.getResources();
                if (childView == null) {
                    RecyclerView recyclerView =
                            (RecyclerView) view.getRootView().findViewById(id);
                    if (recyclerView != null && recyclerView.getId() == id) {
                        childView = recyclerView.getChildAt(position);
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
