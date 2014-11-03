package main;

enum TeamName {
	BRUINS("Boston"), CANADIENS("Montreal");

	private String province;

	private TeamName(String s) {
		this.province = s;
	}

	public String value() {
		return province;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(super.toString().toLowerCase());
		sb.replace(0, 1, sb.substring(0, 1).toUpperCase());
		return sb.toString();
	}
}
