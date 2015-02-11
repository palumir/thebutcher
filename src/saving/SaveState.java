package saving;

import java.util.ArrayList;
import java.util.Map;

import audio.SoundClip;
import terrain.TerrainChunk;
import units.Unit;
import drawables.Canvas;
import drawables.Node;

public class SaveState {
	
	private ArrayList<Node> nodes;
	private Map<int[],TerrainChunk> terrain;
	private ArrayList<Unit> units;
	
	// Create a local savestate.
	public SaveState() {
	}
	
	public void save() {
		nodes = Canvas.getGameCanvas().getNodes();
		terrain = TerrainChunk.getTerrain();
		units = Unit.units;
	}
	
	public void restore() {
		Canvas.getGameCanvas().setNodes(nodes);
		TerrainChunk.setTerrain(terrain);
		Unit.units = units;
	}
	
	// Clear everything from the game to move on to another phase.
	public static void purgeAll() {
		// Clear everything.
		Unit.units.clear();
		TerrainChunk.getTerrain().clear();
		Canvas.getGameCanvas().getNodes().clear();
		SoundClip.stopSounds();
	}
}