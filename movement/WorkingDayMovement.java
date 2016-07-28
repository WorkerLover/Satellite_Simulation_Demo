/* 
 * Copyright 2010 Aalto University, ComNet
 * Released under GPLv3. See LICENSE.txt for details. 
 */
package movement;

import core.Coord2;
import core.Settings;

/**
 * 
 * This movement model makes use of several other movement models to simulate
 * movement with daily routines. People wake up in the morning, go to work, 
 * go shopping or similar activities in the evening and finally go home to 
 * sleep. 
 * 
 * @author Frans Ekman
 */
public class WorkingDayMovement extends ExtendedMovementModel {

	public static final String PROBABILITY_TO_OWN_CAR_SETTING = "ownCarProb";
	public static final String PROBABILITY_TO_GO_SHOPPING_SETTING = 
		"probGoShoppingAfterWork";
	
	private BusTravellerMovement busTravellerMM;
	private OfficeActivityMovement workerMM;
	private HomeActivityMovement homeMM;
	private EveningActivityMovement eveningActivityMovement;
	private CarMovement carMM;
	
	private TransportMovement movementUsedForTransfers;
	
	private static final int BUS_TO_WORK_MODE = 0;
	private static final int BUS_TO_HOME_MODE = 1;
	private static final int BUS_TO_EVENING_ACTIVITY_MODE = 2;
	
	private static final int WORK_MODE = 3;
	private static final int HOME_MODE = 4;
	private static final int EVENING_ACTIVITY_MODE = 5;
	
	private int mode;
	
	private double ownCarProb;
	private double doEveningActivityProb;
	
	/**
	 * Creates a new instance of WorkingDayMovement
	 * @param settings
	 */
	public WorkingDayMovement(Settings settings) {
		super(settings);
		busTravellerMM = new BusTravellerMovement(settings);
		workerMM = new OfficeActivityMovement(settings);
		homeMM = new HomeActivityMovement(settings);
		eveningActivityMovement = new EveningActivityMovement(settings);
		carMM = new CarMovement(settings);
		ownCarProb = settings.getDouble(PROBABILITY_TO_OWN_CAR_SETTING);
		if (rng.nextDouble() < ownCarProb) { 
			movementUsedForTransfers = carMM;
		} else {
			movementUsedForTransfers = busTravellerMM;
		}
		doEveningActivityProb = settings.getDouble(
				PROBABILITY_TO_GO_SHOPPING_SETTING);
		
		setCurrentMovementModel(homeMM);
		mode = HOME_MODE;
	}
	
	/**
	 * Creates a new instance of WorkingDayMovement from a prototype
	 * @param proto
	 */
	public WorkingDayMovement(WorkingDayMovement proto) {
		super(proto);
		busTravellerMM = new BusTravellerMovement(proto.busTravellerMM);
		workerMM = new OfficeActivityMovement(proto.workerMM);
		homeMM = new HomeActivityMovement(proto.homeMM);
		eveningActivityMovement = new EveningActivityMovement(
				proto.eveningActivityMovement);
		carMM = new CarMovement(proto.carMM);
		
		ownCarProb = proto.ownCarProb;
		if (rng.nextDouble() < ownCarProb) { 
			movementUsedForTransfers = carMM;
		} else {
			movementUsedForTransfers = busTravellerMM;
		}
		doEveningActivityProb = proto.doEveningActivityProb;
		
		setCurrentMovementModel(homeMM);
		mode = proto.mode;
	}
	
	@Override
	public boolean newOrders() {
		switch (mode) {
		case WORK_MODE:
			if (workerMM.isReady()) {
				setCurrentMovementModel(movementUsedForTransfers);
				if (doEveningActivityProb > rng.nextDouble()) {
					movementUsedForTransfers.setNextRoute(
							workerMM.getOfficeLocation(), 
							eveningActivityMovement.
								getShoppingLocationAndGetReady());
					mode = BUS_TO_EVENING_ACTIVITY_MODE;
				} else {
					movementUsedForTransfers.setNextRoute(
							workerMM.getOfficeLocation(), 
							homeMM.getHomeLocation());
					mode = BUS_TO_HOME_MODE;
				}
			}
			break;
		case HOME_MODE:
			if (homeMM.isReady()) {
				setCurrentMovementModel(movementUsedForTransfers);
				movementUsedForTransfers.setNextRoute(homeMM.getHomeLocation(),
						workerMM.getOfficeLocation());
				mode = BUS_TO_WORK_MODE;
			}
			break;
		case EVENING_ACTIVITY_MODE:
			if (eveningActivityMovement.isReady()) {
				setCurrentMovementModel(movementUsedForTransfers);
				movementUsedForTransfers.setNextRoute(eveningActivityMovement.
						getLastLocation(), homeMM.getHomeLocation());
				mode = BUS_TO_HOME_MODE;
			}
			break;
		case BUS_TO_WORK_MODE:
			if (movementUsedForTransfers.isReady()) {
				setCurrentMovementModel(workerMM);
				mode = WORK_MODE;
			}
			break;
		case BUS_TO_HOME_MODE:
			if (movementUsedForTransfers.isReady()) {
				setCurrentMovementModel(homeMM);
				mode = HOME_MODE;
			}
			break;
		case BUS_TO_EVENING_ACTIVITY_MODE:
			if (movementUsedForTransfers.isReady()) {
				setCurrentMovementModel(eveningActivityMovement);
				mode = EVENING_ACTIVITY_MODE;
			}
			break;
		default:
			break;
		}
		return true;
	}

	@Override
	public Coord2 getInitialLocation() {
		Coord2 homeLoc = homeMM.getHomeLocation().clone();
		homeMM.setLocation(homeLoc);
		return homeLoc;
	}

	@Override
	public MovementModel replicateDefault() {
		return new WorkingDayMovement(this);
	}
	
	public SatelliteMovement replicateRandom(boolean multi_orbit) {
		return new SatelliteMovement();
	}
	
	public SatelliteMovement replicateRandom(boolean multi_orbit,int order) {
		return new SatelliteMovement();
	}
	
	public SatelliteMovement replicateUserConfig(double[] t) {
		return new SatelliteMovement();
	}

	
	public Coord2 getOfficeLocation() {
		return workerMM.getOfficeLocation().clone();
	}
	
	public Coord2 getHomeLocation() {
		return homeMM.getHomeLocation().clone();
	}
	
	public Coord2 getShoppingLocation() {
		return eveningActivityMovement.getShoppingLocation().clone();
	}
	
}
