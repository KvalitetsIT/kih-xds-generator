package io.oth.xdsgenerator.model.root;

public class Links {

    private String self;
    private String health;
    private String api;

    public String getSelf() {
        return self;
    }

    // Sets link back to root resource
    public void setSelf(String self) {
        this.self = self;
    }

    // Sets link to health check
    public String getHealth() {
        return health;
    }

    public void setHealth(String health) {
        this.health = health;
    }

	public String getApi() {
		return api;
	}

	public void setApi(String api) {
		this.api = api;
	}

}
