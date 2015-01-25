package items;

public class Weapon {
	
	private String name;
	private String type;
	private String desc;
	private int weight;
	
	private int slashMaxDMG;
	private int chopMaxDMG;
	private int thrustMaxDMG;

	public Weapon(String name, String type) {
		
		this.name = name;
		this.type = type;
		this.desc = "";
		this.weight = 0;
		
		this.slashMaxDMG = 0;
		this.chopMaxDMG = 0;
		this.thrustMaxDMG = 0;
	}
}
