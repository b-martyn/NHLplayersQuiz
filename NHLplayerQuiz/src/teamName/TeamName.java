package teamName;

import java.awt.Color;

public enum TeamName {
	BRUINS("Boston"), CANADIENS("Montr\u00E9al"), DUCKS("Anaheim"), COYOTES("Arizona"), SABRES("Buffalo"), FLAMES("Calgary"), HURRICANES("Carolina"), 
	BLACKHAWKS("Chicago"), AVALANCHE("Colorado"), BLUE_JACKETS("Columbus"), STARS("Dallas"), RED_WINGS("Detroit"), OILERS("Edmonton"), 
	PANTHERS("Florida"), KINGS("Los Angeles"), WILD("Minnesota"), PREDATORS("Nashville"), DEVILS("New Jersey"), ISLANDERS("New York"), 
	RANGERS("New York"), SENATORS("Ottowa"), FLYERS("Philadelphia"), PENGUINS("Pittsburgh"), SHARKS("San Jose"), BLUES("St Louis"), 
	LIGHTNING("Tampa Bay"), MAPLE_LEAFS("Toronto"), CANUCKS("Vancouver"), CAPITALS("Washington"), JETS("Winnipeg");

	private String province;
	private String teamName;

	private Color baseColor;
	private Color mainColor;
	private Color secondaryColor;
	
	private TeamName(String s) {
		this.province = s;
		this.teamName = this.toString();
		pickColors();
	}
	
	public String getProvince() {
		return province;
	}

	public String getTeamName() {
		return teamName;
	}

	public Color getBaseColor() {
		return baseColor;
	}

	public Color getMainColor() {
		return mainColor;
	}

	public Color getSecondaryColor() {
		return secondaryColor;
	}
	
	private void pickColors() {
		switch (province) {
			case "Boston":
				this.baseColor = new Color(0, 0, 0);
				this.mainColor = new Color(255, 196, 34);
				this.secondaryColor = new Color(255, 255, 255);
				break;
			case "Montr\u00E9al":
				this.baseColor = new Color(191, 47, 56);
				this.mainColor = new Color(255, 255, 255);
				this.secondaryColor = new Color(33, 55, 112);
				break;
			case "Anaheim":
				this.baseColor = new Color(0, 0, 0);
				this.mainColor = new Color(145, 118, 75);
				this.secondaryColor = new Color(239, 82, 37);
				break;
			case "Arizona":
				this.baseColor = new Color(132, 31, 39);
				this.mainColor = new Color(0, 0, 0);
				this.secondaryColor = new Color(239, 255, 198);
				break;
			case "Buffalo":
				this.baseColor = new Color(0, 46, 98);
				this.mainColor = new Color(235, 187, 47);
				this.secondaryColor = new Color(147, 182, 185);
				break;
			case "Calgary":
				this.baseColor = new Color(224, 58, 62);
				this.mainColor = new Color(255, 199, 88);
				this.secondaryColor = new Color(0, 0, 0);
				break;
			case "Carolina":
				this.baseColor = new Color(224, 58, 62);
				this.mainColor = new Color(0, 0, 0);
				this.secondaryColor = new Color(142, 142, 144);
				break;
			case "Chicago":
				this.baseColor = new Color(227, 38, 58);
				this.mainColor = new Color(0, 0, 0);
				this.secondaryColor = new Color(255, 255, 255);
				break;
			case "Colorado":
				this.baseColor = new Color(139, 41, 66);
				this.mainColor = new Color(1, 84, 138);
				this.secondaryColor = new Color(0, 0, 0);
				break;
			case "Columbus":
				this.baseColor = new Color(0, 40, 92);
				this.mainColor = new Color(224, 58, 62);
				this.secondaryColor = new Color(169, 176, 184);
				break;
			case "Dallas":
				this.baseColor = new Color(0, 106, 78);
				this.mainColor = new Color(0, 0, 0);
				this.secondaryColor = new Color(255, 255, 255);
				break;
			case "Detroit":
				this.baseColor = new Color(236, 31, 38);
				this.mainColor = new Color(255, 255, 255);
				this.secondaryColor = new Color(0, 0, 0);
				break;
			case "Edmonton":
				this.baseColor = new Color(0, 55, 119);
				this.mainColor = new Color(230, 106, 32);
				this.secondaryColor = new Color(255, 255, 255);
				break;
			case "Florida":
				this.baseColor = new Color(200, 33, 63);
				this.mainColor = new Color(0, 46, 95);
				this.secondaryColor = new Color(213, 156, 5);
				break;
			case "Los Angeles":
				this.baseColor = new Color(0, 0, 0);
				this.mainColor = new Color(255, 255, 255);
				this.secondaryColor = new Color(175, 183, 186);
				break;
			case "Minnesota":
				this.baseColor = new Color(2, 87, 54);
				this.mainColor = new Color(191, 43, 55);
				this.secondaryColor = new Color(239, 180, 16);
				break;
			case "Nashville":
				this.baseColor = new Color(253, 187, 47);
				this.mainColor = new Color(0, 46, 98);
				this.secondaryColor = new Color(255, 255, 255);
				break;
			case "New Jersey":
				this.baseColor = new Color(224, 58, 62);
				this.mainColor = new Color(0, 0, 0);
				this.secondaryColor = new Color(255, 255, 255);
				break;
			case "New York":
				// Rangers
				if(teamName.startsWith("R")){
					this.baseColor = new Color(1, 97, 171);
					this.mainColor = new Color(230, 57, 63);
					this.secondaryColor = new Color(255, 255, 255);
				}else{// Islanders
					this.baseColor = new Color(0, 82, 155);
					this.mainColor = new Color(245, 125, 49);
					this.secondaryColor = new Color(255, 255, 255);
				}
				break;
			case "Ottowa":
				this.baseColor = new Color(228, 23, 62);
				this.mainColor = new Color(0, 0, 0);
				this.secondaryColor = new Color(214, 159, 15);
				break;
			case "Philadelphia":
				this.baseColor = new Color(244, 121, 64);
				this.mainColor = new Color(0, 0, 0);
				this.secondaryColor = new Color(255, 255, 255);
				break;
			case "Pittsburgh":
				this.baseColor = new Color(0, 0, 0);
				this.mainColor = new Color(209, 189, 128);
				this.secondaryColor = new Color(255, 255, 255);
				break;
			case "San Jose":
				this.baseColor = new Color(5, 83, 93);
				this.mainColor = new Color(243, 143, 32);
				this.secondaryColor = new Color(0, 0, 0);
				break;
			case "St Louis":
				this.baseColor = new Color(5, 70, 160);
				this.mainColor = new Color(255, 195, 37);
				this.secondaryColor = new Color(16, 31, 72);
				break;
			case "Tampa Bay":
				this.baseColor = new Color(1, 62, 125);
				this.mainColor = new Color(255, 255, 255);
				this.secondaryColor = new Color(0, 0, 0);
				break;
			case "Toronto":
				this.baseColor = new Color(0, 55, 119);
				this.mainColor = new Color(255, 255, 255);
				this.secondaryColor = new Color(0, 0, 0);
				break;
			case "Vancouver":
				this.baseColor = new Color(7, 52, 111);
				this.mainColor = new Color(4, 122, 74);
				this.secondaryColor = new Color(168, 169, 173);
				break;
			case "Washington":
				this.baseColor = new Color(207, 19, 43);
				this.mainColor = new Color(0, 33, 78);
				this.secondaryColor = new Color(255, 255, 255);
				break;
			case "Winnipeg":
				this.baseColor = new Color(0, 46, 98);
				this.mainColor = new Color(255, 255, 255);
				this.secondaryColor = new Color(168, 169, 173);
				break;
			default:
				this.baseColor = Color.WHITE;
				this.mainColor = Color.BLACK;
				this.secondaryColor = Color.GRAY;
				System.out.println("Missed Team: " + province);
				break;
		}
	}
}
