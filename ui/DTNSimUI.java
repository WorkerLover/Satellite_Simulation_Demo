/* 
 * Copyright 2010 Aalto University, ComNet
 * Released under GPLv3. See LICENSE.txt for details. 
 */
package ui;

import java.io.IOException;

import jat.application.orbitviewer.*;

import jat.application.orbitviewer.orbitviewerEvents;
import jat.application.orbitviewer.orbitviewerGUI;
import jat.core.plot.plot.Plot3DPanel;
import jat.coreNOSA.cm.cm;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;

import javax.swing.JFormattedTextField;
import javax.swing.JFrame;

import report.Report;
import core.ApplicationListener;
import core.ConnectionListener;
import core.MessageListener;
import core.MovementListener;
import core.Settings;
import core.SettingsError;
import core.SimClock;
import core.SimError;
import core.SimScenario;
import core.UpdateListener;
import core.World;

/**
 * Abstract superclass for user interfaces; contains also some simulation
 * settings.
 */
public abstract class DTNSimUI {
	/** 
	 * Number of reports -setting id ({@value}). Defines how many reports
	 * are loaded. 
	 */
	public static final String NROF_REPORT_S = "Report.nrofReports";
	/** 
	 * Report class name -setting id prefix ({@value}). Defines name(s) of
	 * the report classes to load. Must be suffixed with numbers starting from
	 * one.
	 */
	public static final String REPORT_S = "Report.report";
	/**
	 * Movement model warmup time -setting id ({@value}). Defines how many
	 * seconds of movement simulation is run without connectivity etc. checks 
	 * before starting the real simulation.
	 */
	public static final String MM_WARMUP_S = 
		movement.MovementModel.MOVEMENT_MODEL_NS + ".warmup";
	
	/** report class' package name */
	private static final String REPORT_PAC = "report.";
	
	/** configuration_file's location*/
	private static final String CONFIGURATION_FILE_LOCATION = 
			"E:\\xiangmu\\Satellite_Simulation_Demo\\default_settings.txt";

	/** The World where all actors of the simulator are */
	protected World world;
	/** Reports that are loaded for this simulation */
	protected Vector<Report> reports;
	/** has simulation terminated normally */
	protected boolean simDone;
	/** is simulation termination requested */
	protected boolean simCancelled;
	/** Scenario of the current simulation */
	protected SimScenario scen;
	/** simtime of last UI update */
	protected double lastUpdate;
	
/*	protected Settings s;
	
	protected Configuration config;*/
	
	//Plot3DPanel plot;
	
	/**
	 * Constructor.
	 */
	public DTNSimUI() {
		this.lastUpdate = 0;
		this.reports = new Vector<Report>();
		this.simDone = false;
		this.simCancelled = false;
	}
	
	/**
	 * Starts the simulation.
	 */
	public void start(Configuration config,boolean flag) {
		/**将图形界面参数写入配置文件*/
		addSettings(config);
		/**将图形界面参数写入配置文件*/
		initModel(config,flag);
	//	runSim();
	}
	
	/**
	 * Runs simulation after the model has been initialized.
	 */
	protected abstract void runSim();
	
	/**
	 * Initializes the simulator model.
	 */
	private void initModel(Configuration config,boolean flag) {
		Settings settings = null;
				
		try {
			settings = new Settings();
			this.scen = SimScenario.getInstance(config,flag);

			// add reports
			for (int i=1, n = settings.getInt(NROF_REPORT_S); i<=n; i++){
				String reportClass = settings.getSetting(REPORT_S + i);
				addReport((Report)settings.createObject(REPORT_PAC + 
						reportClass));	
			}
		
			double warmupTime = 0;
			if (settings.contains(MM_WARMUP_S)) {
				warmupTime = settings.getDouble(MM_WARMUP_S);
				if (warmupTime > 0) {
					SimClock c = SimClock.getInstance();
					c.setTime(-warmupTime);
				}
			}

			this.world = this.scen.getWorld();
			world.warmupMovementModel(warmupTime);
		}
		catch (SettingsError se) {
			System.err.println("Can't start: error in configuration file(s)");
			System.err.println(se.getMessage());
			System.exit(-1);			
		}
		catch (SimError er) {
			System.err.println("Can't start: " + er.getMessage());
			System.err.println("Caught at " + er.getStackTrace()[0]);
			System.exit(-1);
		}		
	}
	
	/**
	 * Runs maintenance jobs that are needed before exiting.
	 */
	public void done() {
		for (Report r : this.reports) {
			r.done();
		}
	}
	
	/**
	 * Adds a new report for simulator
	 * @param r Report to add
	 */
	protected void addReport(Report r) {
		if (r instanceof MessageListener) {
			scen.addMessageListener((MessageListener)r);
		}
		if (r instanceof ConnectionListener) {
			scen.addConnectionListener((ConnectionListener)r);
		}
		if (r instanceof MovementListener) {
			scen.addMovementListener((MovementListener)r);
		}
		if (r instanceof UpdateListener) {
			scen.addUpdateListener((UpdateListener)r);
		}
		if (r instanceof ApplicationListener) {
			scen.addApplicationListener((ApplicationListener)r);
		}

		this.reports.add(r);
	}
	
	/**将参数写入配置文件的函数*/
	protected void addSettings(Configuration config) {
		Settings settings = new Settings();
		settings.setSetting(CONFIGURATION_FILE_LOCATION,"Group.defaultNumbers",String.valueOf(config.default_numbers));
		settings.setSetting(CONFIGURATION_FILE_LOCATION,"Group.randomNumbers",String.valueOf(config.random_numbers));
		settings.setSetting(CONFIGURATION_FILE_LOCATION,"Group.userConfigNumbers",String.valueOf(config.user_config_numbers));
		String s = "0,";
		s = s + String.valueOf(config.random_numbers);
		settings.setSetting(CONFIGURATION_FILE_LOCATION,"Events1.hosts",s);
	}
}
