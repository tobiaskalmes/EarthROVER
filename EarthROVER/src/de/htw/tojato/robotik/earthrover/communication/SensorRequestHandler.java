package de.htw.tojato.robotik.earthrover.communication;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Created with IntelliJ IDEA.
 * User: Tobias
 * Date: 27.09.13
 * Time: 18:07
 * To change this template use File | Settings | File Templates.
 */
@Path("/sensors")
public class SensorRequestHandler extends RequestHandler {
    @GET
    @Path("/magnetic_field")
    @Produces(MediaType.APPLICATION_JSON)
    public Object getMagneticFieldSensorData() {
        return null;
    }
}
