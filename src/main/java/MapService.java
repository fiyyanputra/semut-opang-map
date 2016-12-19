import config.Config;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

/**
 * Created by fiyyanp on 10/20/2016.
 */
public class MapService extends Application<Config> {

    public static void main(String[] args) throws Exception {
        new MapService().run(args);
    }

    @Override
    public void initialize(Bootstrap<Config> bootstrap) {

    }

    @Override
    public void run(Config config, Environment environment) throws Exception {
            environment.jersey().register(new MapResource(config));
    }
}
