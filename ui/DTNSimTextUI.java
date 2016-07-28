/* 
 * Copyright 2010 Aalto University, ComNet
 * Released under GPLv3. See LICENSE.txt for details. 
 */
package ui;

import java.io.IOException;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;

import core.SimClock;
import jat.application.orbitviewer.orbitviewerEvents;
import jat.application.orbitviewer.orbitviewerGUI;
import jat.application.orbitviewer.Configuration;
import jat.orbit.SatelliteOrbit;
/**
 * Copyright(C),2016-2020,USTC¡£
 * FileName:DTNSimTextUI.java
 * Simple text-based user interface.
 * 
 * @author XiJianXu
 *   *@Date 2016-6-3
 * @version 1.00
 */
public class DTNSimTextUI extends DTNSimUI {
	/** real time of last ui update*/
	private long lastUpdateRt;	
	/** simulation start time*/
	private long startTime; 
	/** How often the UI view is updated (milliseconds) */
	public static final long UI_UP_INTERVAL = 60000;	
	
	/**
	 * overrwrite the runSim ,extended from DTNSimUI
	 * 
	 * @param NULL
	 * @return void
	 * @throws AssertionError throw the error
	 */
	public void runSim() {

		double simTime = SimClock.getTime();
		double endTime = scen.getEndTime();
		print("Running simulation '" + scen.getName()+"'");
		

		startTime = System.currentTimeMillis();
		lastUpdateRt = startTime;
		
		while (simTime < endTime && !simCancelled){
			try {
				world.update();
			} catch (AssertionError e) {
				e.printStackTrace();
				done();
				return;
			}
			simTime = SimClock.getTime();
			this.update(false);
		
		//	this.t = this.t+8000/5000;
		}
		
		double duration = (System.currentTimeMillis() - startTime)/1000.0;
		
		simDone = true;
		done();
		this.update(true); // force final UI update
		
		print("Simulation done in " + String.format("%.2f", duration) + "s");
		
		double duration2 = (System.currentTimeMillis() - startTime)/1000.0;
		print("---\nAll done in " + String.format("%.2f", duration2) + "s");
	
	}
	
	/**
	 * Updates user interface if the long enough (real)time (update interval)
	 * has passed from the previous update.
	 * @param forced If true, the update is done even if the next update
	 * interval hasn't been reached.
	 */
	private void update(boolean forced) {
		long now = System.currentTimeMillis();
		long diff = now - this.lastUpdateRt;
		double dur = (now - startTime)/1000.0;
		if (forced || (diff > UI_UP_INTERVAL)) {
			// simulated seconds/second calc
			double ssps = ((SimClock.getTime() - lastUpdate)*1000) / diff;
			print(String.format("%.1f %d: %.2f 1/s", dur, 
					SimClock.getIntTime(),ssps));
			
			this.lastUpdateRt = System.currentTimeMillis();
			this.lastUpdate = SimClock.getTime();
		}		
	}
	
	private void print(String txt) {
		System.out.println(txt);
	}
	
}
