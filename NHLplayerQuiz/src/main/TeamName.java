package main;

enum TeamName {
	BRUINS("Boston"), CANADIENS("Montreal"), DUCKS("Anaheim"), COYOTES("Arizona"), SABRES("Buffalo"), FLAMES("Calgary"), HURRICANES("Carolina"), 
	BLACKHAWKS("Chicago"), AVALANCHE("Colorado"), BLUE_JACKETS("Columbus"), STARS("Dallas"), RED_WINGS("Detroit"), OILERS("Edmonton"), 
	PANTHERS("Florida"), KINGS("Los Angeles"), WILD("Minnesota"), PREDATORS("Nashville"), DEVILS("New Jersey"), ISLANDERS("New York"), 
	RANGERS("New York"), SENATORS("Ottowa"), FLYERS("Philadelphia"), PENGUINS("Pittsburgh"), SHARKS("San Jose"), BLUES("St Louis"), 
	LIGHTNING("Tampa Bay"), MAPLE_LEAFS("Toronto"), CANUCKS("Vancouver"), CAPITALS("Washington"), JETS("Winnipeg");

	private String province;

	private TeamName(String s) {
		this.province = s;
	}

	public String value() {
		return province;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		String[] strings = super.toString().toLowerCase().split("_");
		for(String string : strings){
			if(sb.length() != 0){
				sb.append(" ");
			}
			StringBuilder sbFormat = new StringBuilder(string);
			sbFormat.replace(0, 1, sbFormat.substring(0, 1).toUpperCase());
			sb.append(sbFormat.toString());
		}
		return sb.toString();
	}
}
