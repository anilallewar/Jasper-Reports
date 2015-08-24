package net.ensode.datasource;

public class AircraftWing implements IAircraftComponent {
	private int width;
	private int height;
	private String aircraftWingSpan;
	private String aircraftWingMaterial;
	/**
	 * Constructor with field arguments
	 *  
	 * @param tailWidth
	 * @param tailHeight
	 */
	public AircraftWing(int tailWidth, int tailHeight, String aircraftWingSpan, String aircraftWingMaterial) {
		super();
		this.width = tailWidth;
		this.height = tailHeight;
		this.aircraftWingSpan = aircraftWingSpan;
		this.aircraftWingMaterial = aircraftWingMaterial;
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

	/**
	 * Constructor with field arguments
	 *  
	 * @param aircraftWingSpan
	 * @param aircraftWingMaterial
	 */
	public AircraftWing() {
		super();
		
	}

	/**
	 * @return the aircraftWingSpan
	 */
	public String getAircraftWingSpan() {
		return aircraftWingSpan;
	}

	/**
	 * @param aircraftWingSpan
	 *            the aircraftWingSpan to set
	 */
	public void setAircraftWingSpan(String aircraftWingSpan) {
		this.aircraftWingSpan = aircraftWingSpan;
	}

	/**
	 * @return the aircraftWingMaterial
	 */
	public String getAircraftWingMaterial() {
		return aircraftWingMaterial;
	}

	/**
	 * @param aircraftWingMaterial
	 *            the aircraftWingMaterial to set
	 */
	public void setAircraftWingMaterial(String aircraftWingMaterial) {
		this.aircraftWingMaterial = aircraftWingMaterial;
	}
}
