package io.oth.xdsgenerator.model.root;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Root {
  private String environment;

  @JsonIgnore
  private String rootUrl;

  private String version;

  private Links links;

  public Root() {
  }

  public Root(String rootUrl, String environment, String version) {
    this.rootUrl = rootUrl;
    this.environment = environment;
    this.version = version;

  }

  public void setRootUrl(String s) {
    this.rootUrl = s;
  }

  public String getRootUrl() {
    return this.rootUrl;
  }


  public String getEnvironment() {
    return environment;
  }

  public void setEnvironment(String environment) {
    this.environment = environment;
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public Links getLinks() {
    return links;
  }

  public void setLinks(Links links) {
    this.links = links;
  }

}
