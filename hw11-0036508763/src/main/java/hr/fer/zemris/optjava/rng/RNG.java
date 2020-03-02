package hr.fer.zemris.optjava.rng;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class RNG {
    private static IRNGProvider rngProvider;

    static {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream("./src/main/resources/rng-config.properties"));
        } catch (IOException e) {
            System.out.println("Unable to load properties.");
        }
        try {
            rngProvider = (IRNGProvider) ClassLoader.getSystemClassLoader()
                    .loadClass(properties.getProperty("rng-provider"))
                    .getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public static IRNG getRNG() {
        return rngProvider.getRNG();
    }
}