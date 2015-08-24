package net.ensode.datasource;

public class AircraftTail implements IAircraftComponent {
	
	private int width;
	private int height;
	/**
	 * Constructor with field arguments
	 *  
	 * @param tailWidth
	 * @param tailHeight
	 */
	public AircraftTail(int tailWidth, int tailHeight) {
		super();
		this.width = tailWidth;
		this.height = tailHeight;
	}
	
	@Override
	public int getHeight() {
		return this.height;
	}
	
	@Override
	public int getWidth() {
		return this.width;
	}
	
	@Override
	public void setHeight(int height) {
		this.height = height;
	}
	
	@Override
	public void setWidth(int width) {
		this.width = width;
	}
}
