package proyecto;

import javafx.application.Application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Main{
    private static ConfigurableApplicationContext context;
    public static String serverURL = "http://localhost:8080";
    public static int sessionID = -1;
    public static String sessionName = null;

    public static void main(String[] args) {
        Main.context = SpringApplication.run(Main.class);
        Application.launch(Pepito.class, args);
    }

    public static ConfigurableApplicationContext getContext() {
        return context;
    }

}