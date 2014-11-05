package com.naisinpo.fujianto.repoviewer;

import android.test.suitebuilder.TestSuiteBuilder;

import junit.framework.Test;

/**
 * Created by fujianto on 04/11/14.
 */
public class FullTestSuite {
    /**
     * Constructs a new instance of {@code Object}.
     */
    public FullTestSuite() {
        super();
    }

    public static Test suite(){
        return new TestSuiteBuilder(FullTestSuite.class).
                includeAllPackagesUnderHere().build();
    }


}
