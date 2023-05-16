package io.oth.xdsgenerator.model.kih;

public class Author {
	
	public Organization RepresentedOrganization;
	
	public AssignedEntity AssignedAuthor;

	public Organization getRepresentedOrganization() {
		return RepresentedOrganization;
	}

	public void setRepresentedOrganization(Organization representedOrganization) {
		RepresentedOrganization = representedOrganization;
	}

	public AssignedEntity getAssignedAuthor() {
		return AssignedAuthor;
	}

	public void setAssignedAuthor(AssignedEntity assignedAuthor) {
		AssignedAuthor = assignedAuthor;
	}	

}
