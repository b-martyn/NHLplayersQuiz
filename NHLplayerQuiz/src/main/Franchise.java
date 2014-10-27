package main;

import java.awt.Color;

public class Franchise {
	private String province;
	private String teamName;
	
	private Color baseColor;
	private Color mainColor;
	private Color secondaryColor;
	
	Franchise(TeamName teamName){
		this.teamName = teamName.toString();
		this.province = teamName.value();
		pickColors(teamName);
	}
	
	public String getProvince(){
		return province;
	}
	
	public String getTeamName(){
		return teamName;
	}
	
	public Color getBaseColor(){
		return baseColor;
	}
	
	public Color getMainColor(){
		return mainColor;
	}
	
	public Color getSecondaryColor(){
		return secondaryColor;
	}
	
	@Override
	public String toString(){
		return this.province + " " + this.teamName;
	}
	
	private void pickColors(TeamName teamName){
		switch(teamName){
			case BRUINS:
				this.baseColor = Color.BLACK;
				this.mainColor = Color.YELLOW;
				this.secondaryColor = Color.WHITE;
				break;
			case CANADIENS:
				this.baseColor = Color.WHITE;
				this.mainColor = Color.RED;
				this.secondaryColor = Color.BLUE;
				break;
			default:
				this.baseColor = Color.WHITE;
				this.mainColor = Color.BLACK;
				this.secondaryColor = Color.GRAY;
				break;
		}
	}
}
