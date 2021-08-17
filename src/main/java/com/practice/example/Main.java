package com.practice.example;

import com.segment.analytics.Analytics;
import com.segment.analytics.Log;
import com.segment.analytics.messages.GroupMessage;
import com.segment.analytics.messages.IdentifyMessage;
import com.segment.analytics.messages.PageMessage;
import com.segment.analytics.messages.ScreenMessage;
import com.segment.analytics.messages.TrackMessage;


import java.util.LinkedHashMap;
import java.util.Map;

public class Main {
    public static final String WRITE_KEY = "OLyqY8X4qZC9DBShyXpEI1EUPTiqd51F";
    public static void main(String [] args) {
        System.out.println("I am in main.");
        Log STDOUT = new Log() {
            @Override
            public void print(Level level, String format, Object... args) {
                System.out.println(level + ":\t" + String.format(format, args));
            }

            @Override
            public void print(Level level, Throwable error, String format, Object... args) {
                System.out.println(level + ":\t" + String.format(format, args));
                System.out.println(error);
            }
        };

        final String userId = System.getProperty("user.name");
        Analytics analytics = Analytics.builder(WRITE_KEY)
                .endpoint("https://api.segment.build")
                .log(STDOUT)
                .build();


        System.out.println("IdentifyMessage");
        Map<String, Object> properties = new LinkedHashMap<>();
        properties.put("name", "Michael Bolton");
        properties.put("email", "mbolton@example.com");

        analytics.enqueue(IdentifyMessage.builder()
                .userId(userId)
                .traits(properties)
        );


        properties = new LinkedHashMap<>();
        properties.put("revenue", 39.95);
        properties.put("shipping", "2-day");
        for (int i = 0; i < 10; ++i) {
            System.out.println("TrackMessage");
            analytics.enqueue(TrackMessage.builder("Item Purchased")
                    .userId(userId)
                    .properties(properties)
            );
        }


        System.out.println("ScreenMessage");
        properties = new LinkedHashMap<>();
        properties.put("category", "Sports");
        properties.put("path", "/sports/schedule");

        analytics.enqueue(ScreenMessage.builder("Schedule")
                .userId(userId)
                .properties(properties)
        );

        System.out.println("PageMessage");
        analytics.enqueue(PageMessage.builder("Schedule")
                .userId(userId)
                .properties(properties)
        );


        System.out.println("GroupMessage");
        properties = new LinkedHashMap<>();
        properties.put("name", "Segment");
        properties.put("size", 50);
        analytics.enqueue(GroupMessage.builder("some-group-id")
                .userId(userId)
                .traits(properties)
        );

        analytics.flush();
        analytics.shutdown();
    }
}
