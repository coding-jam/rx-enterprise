package it.cosenonjaviste.rest;

import it.cosenonjaviste.dtos.Message;
import it.cosenonjaviste.service.LongTimeProcessingService;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.logging.Logger;

/**
 * Created by pizzo on 25/05/15.
 */
@Path("message")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MessageRestService {

    private static final Logger LOGGER = Logger.getLogger(MessageRestService.class.getName());

    @Inject
    private LongTimeProcessingService service;

    @POST
    public Response sendMessage(@Valid Message message) throws InterruptedException {
        LOGGER.info("Ricevuto il messaggio '" + message.getMessage() + "' nel bean " + this.hashCode());
        service.echo(message.getMessage());

        return Response.ok().build();
    }
}
