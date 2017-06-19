package org.roylance.yadel;

import org.roylance.yadel.services.YadelMainLogic;
import org.roylance.yadel.sample.SampleManager;
import org.roylance.yadel.sample.SampleWorker;

public class Main {
    public static void main(final String[] args) {
        YadelMainLogic.INSTANCE.main(args, SampleManager.class, SampleWorker.class);
    }
}
