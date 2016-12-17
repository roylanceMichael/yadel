package org.roylance.yadel;

import org.roylance.yadel.services.MainLogic;
import org.roylance.yadel.sample.SampleManager;
import org.roylance.yadel.sample.SampleWorker;

public class Main {
    public static void main(final String[] args) {
        MainLogic.INSTANCE.main(args, SampleManager.class, SampleWorker.class);
    }
}
