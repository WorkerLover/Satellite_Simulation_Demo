/* 
 * Copyright 2010 Aalto University, ComNet
 * Released under GPLv3. See LICENSE.txt for details. 
 */
package movement;

import core.Coord2;

/**
 * MovementModels used for transportation should implement this interface
 * 
 * @author Frans Ekman
  */
public interface TransportMovement extends SwitchableMovement {

	public void setNextRoute(Coord2 nodeLocation, Coord2 nodeDestination);
	
}
