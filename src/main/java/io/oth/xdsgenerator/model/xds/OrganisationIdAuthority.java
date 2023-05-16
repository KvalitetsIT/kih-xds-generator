package io.oth.xdsgenerator.model.xds;


public class OrganisationIdAuthority implements IdAuthority {

    private String organisationIdAuthority;

    public OrganisationIdAuthority(String organisationIdAuthority) {
        this.organisationIdAuthority = organisationIdAuthority;
    }

    @Override
    public String getPatientIdAuthority() {
        return organisationIdAuthority;
    }
}
