package io.oth.xdsgenerator.handlers;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.oth.xdsgenerator.model.root.Links;
import io.oth.xdsgenerator.model.root.Root;

@RestController
public class RootHandler extends AbstractController {
    public static final Logger log = LoggerFactory.getLogger(RootHandler.class);

    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/", method = RequestMethod.GET)
    public Root handleRoot(HttpServletRequest request) {
        log.info("Got request " + request.getRequestURL());

        String url = request.getRequestURL().toString();

        log.info(request.getRemoteHost());
        // Construct new root resources and return it.
        Root r = new Root(request.getRemoteHost(), "1.0.0", "production");
        Links l = new Links();

        l.setSelf(url);
        l.setApi(url + "api/createphmr");
        l.setHealth(url + "actuator/health");

        r.setLinks(l);
        return r;
    }
}
