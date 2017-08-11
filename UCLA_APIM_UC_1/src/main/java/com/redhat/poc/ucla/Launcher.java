package com.redhat.poc.ucla;

import org.apache.camel.main.Main;

public class Launcher {
    /**
     * A main() so we can easily run these routing rules in our IDE
     */
    public static void main(String... args) throws Exception {
        Main main = new Main();

        CamelRoute routeBuilder = new CamelRoute();
        routeBuilder.restConfiguration().component("spark-rest").port("8088");
		main.addRouteBuilder(routeBuilder);

        main.run(args);
    }
}
