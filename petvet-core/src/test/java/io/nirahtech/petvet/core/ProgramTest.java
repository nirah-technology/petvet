package io.nirahtech.petvet.core;

import java.time.temporal.ChronoUnit;

import io.nirahtech.petvet.core.base.House;
import io.nirahtech.petvet.core.planning.EventRecall;

public class ProgramTest {
    public static void main(String[] args) {
        final House house = House.getInstance();
        house.getCalendar().addEventRecall(EventRecall.getOrCreate(1, ChronoUnit.WEEKS));
        house.getCalendar().addEventRecall(EventRecall.getOrCreate(3, ChronoUnit.DAYS));
        house.getCalendar().addEventRecall(EventRecall.getOrCreate(1, ChronoUnit.DAYS));
        house.getCalendar().setNotificationToThrow((event) -> {
            
        });
        
    }
}
