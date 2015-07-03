package gex.example.restv1

import gex.javax.ws.rs.Resource
import org.springframework.beans.factory.annotation.Autowired

import gex.example.jaxrs.v1.XkcdResource
import gex.example.service.XkcdService

import javax.ws.rs.core.Response

@Resource
class XkcdResourceV1 implements XkcdResource {

  @Autowired
  XkcdService xkcdService

  Response getXkcd() {
    Map comicData = xkcdService.getLastComic("http://xkcd.com")
    Response.status(200).entity(comicData).build()
  }

}


