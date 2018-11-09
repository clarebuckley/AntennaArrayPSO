import java.util.*;
public class Antenna_PSO {
	private int antennaeNum;
	private double angle;
	private double aperture;
	private AntennaArray antenna;
	private double[] gBestPos;

	public Antenna_PSO(int antennaeNum, double angle){
		this.antennaeNum = antennaeNum;
		this.angle = angle;
		this.antenna = new AntennaArray(antennaeNum, angle);
		this.aperture = (double) antennaeNum/2;
		System.out.println("Getting random search:");
		System.out.println("Random search: " + randomSearch(100));
		System.out.println("Getting swarm search: ");
		System.out.println("PSO: " + particleSwarm(100));
	}

	public static void main(String[] args) {
		new Antenna_PSO(3, 90);	
	}

	/**
	 * Generate random design to be evaluated by AntennaArray
	 * @return
	 */
	private double[] generatePositions(){
		double[] design =  new double[antennaeNum];
		
		while(!antenna.is_valid(design)){
			//Last antenna is always going to be equal to aperture
			for(int i = 0; i < antennaeNum -1; i++){
				double randomNumber = Math.random() * aperture;
				if(randomNumber < aperture && randomNumber > 0){
					design[i] = randomNumber;
				}
			}
			design[design.length-1] = aperture;
			
		}
		
		return design;
	}


	private double randomSearch(long timeLimit){
		long timer = System.currentTimeMillis() + timeLimit;
		double peakSLL = antenna.evaluate(generatePositions());
		while(System.currentTimeMillis() < timer){
			double[] randomDesign = generatePositions();
			double currentSLL = antenna.evaluate(randomDesign);
			if(currentSLL < peakSLL){
				peakSLL = currentSLL;
			}
		}
		return peakSLL;
	}

	private double particleSwarm(long timeLimit) {
		int swarmSize = (int)(20 + Math.sqrt(antennaeNum));
		Particle[] swarm = new Particle[swarmSize];
		
		for(int i = 0; i < swarmSize; i++) {
			swarm[i] = new Particle(antennaeNum, angle, antenna);
		}
		//find gbest from looping through swarm
		
		double[] gBestPos = swarm[0].generateRandomPosition();
		double peakSLL = antenna.evaluate(gBestPos);
		long timer = System.currentTimeMillis() + timeLimit;
		while(System.currentTimeMillis() < timer) {
			for(int i = 0; i < swarm.length; i++) {
				double[] currentPos = swarm[i].moveNext(gBestPos);
				double currentSLL = antenna.evaluate(currentPos);
				if(currentSLL < peakSLL) {
					peakSLL = currentSLL;
					gBestPos = currentPos;
				}
			}
		}
		return peakSLL;	
	}
	
	
	public double[] getGBestPos() {
		return gBestPos;
	}
	

}
