package lk.edu.campus.lehan.core.config;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 * @author Client Server Architecture CW (Lehan Methyuga - 20233217)
 */


@ApplicationPath("/api/v1")
public class SmartCampusApplication extends Application {
    // Left empty. Using empty implementation enables auto-discovery of
    // @Path and @Provider classes included in the package.
}
