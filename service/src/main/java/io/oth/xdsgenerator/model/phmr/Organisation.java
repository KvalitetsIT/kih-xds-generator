package io.oth.xdsgenerator.model.phmr;

public class Organisation {

	public String code;
	public String name;
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Organisation{" +
				"code='" + code + '\'' +
				", name='" + name + '\'' +
				'}';
	}
}
