/* 
 * Copyright 2010 Aalto University, ComNet
 * Released under GPLv3. See LICENSE.txt for details. 
 */
package core;

import jat.coreNOSA.cm.*;
import jat.coreNOSA.algorithm.integrators.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import movement.MovementModel;
import movement.Path;
import routing.MessageRouter;
import routing.util.RoutingInfo;
import movement.SatelliteMovement;
import jat.orbit.SatelliteOrbit;

/**
 * Copyright(C),2016-2020,USTC.
 * FileName:DTNHost.java
 * A DTN capable host,a satellite host.
 * 
 * @author XiJianXu
 * @Date 2016-06-03
 * @version 1.0
 */

public class DTNHost implements Comparable<DTNHost>, Printable {
	/** satellite host number*/
	private static int nextAddress = 0;
	/** address */
	private int address;
	/** satellite host location*/
	private Coord location;
	/** satellite host destination location*/
	private Coord destination;

    /** the router of host*/
	private MessageRouter router;
	/** the movement of host*/
	private MovementModel movement;
	/** the path of satellite host*/
	private Path path;
	/** the speed of the host*/
	private double speed;
	/** next time for the host to move*/
	private double nextTimeToMove;
	/**the name of the host*/
	private String name;
	/** Message Listener*/
	private List<MessageListener> msgListeners;
	/** Movement Listener*/
	private List<MovementListener> movListeners;
	/** the interface of net*/
	private List<NetworkInterface> net;
	/**???????*/
	private ModuleCommunicationBus comBus;
	
	/**存储由运动模型得到的200个三维坐标和200个经纬度坐标，图形界面画图*/
	int steps = 200;
	int step = 0;
	double max = 0;
	private double[][] XYZ = new double[steps][3];
	double[][] three_dem_points = new double[1][3];
	
	private double[][] BL = new double[steps][2];
	double[][] two_dem_points = new double[1][2];
	/**存储由运动模型得到的200个三维坐标和200个经纬度坐标，图形界面画图*/
	
	/**记录卫星节点的序号*/
	private int order_;
	/**记录卫星节点的序号*/
			
	/*修改函数部分!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!*/
	private  double []parameters= new double[6];
	private Neighbors nei;//新增;
	
	/** namespace for host group settings ({@value})*/
	public static final String GROUP_NS = "Group";
	/** number of hosts in the group -setting id ({@value})*/
	public static final String NROF_HOSTS_S = "nrofHosts";
	//private static final int NROF_PLANE = 32;//轨道平面数
	//private static final int NROF_SATELLITES = 1024;//总节点数
	//private static final int NROF_S_EACHPLANE = NROF_SATELLITES/NROF_PLANE;//每个轨道平面上的节点数
	
	private List<DTNHost> hosts;
	private int totalSatellites;//总节点数
	private int totalPlane;//总平面数
	private int nrofPlane;//卫星所属轨道平面编号
	private int nrofSatelliteINPlane;//卫星在轨道平面内的编号
	/*修改参数部分!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!*/
	
	static {
		DTNSim.registerForReset(DTNHost.class.getCanonicalName());
		reset();
	}
	
	/**
	 * Creates a new DTNHost.The satelltie host has a default
	 * orbit param or random orbit param.
	 * If the configuration param of users is default,then create
	 * a host which orbit param is default;If the configuration
	 * param of users is random ,then create a host which orbit
	 * param is random. 
	 * @param msgLs Message listeners
	 * @param movLs Movement listeners
	 * @param groupId GroupID of this host
	 * @param interf List of NetworkInterfaces for the class
	 * @param comBus Module communication bus object
	 * @param mmProto Prototype of the movement model of this host
	 * @param mRouterProto Prototype of the message router of this host
	 * @param defaultorbit The orbit param of host is default
	 * @param multiorbit The orbit param of host is random
	 */
	public DTNHost(List<MessageListener> msgLs,
			List<MovementListener> movLs,
			String groupId, List<NetworkInterface> interf,
			ModuleCommunicationBus comBus, 
			MovementModel mmProto, MessageRouter mRouterProto,int order,
			boolean defaultorbit,boolean multiorbit) {
		this.comBus = comBus;
		this.location = new Coord(0,0,0);
		this.address = getNextAddress();
		/**记录卫星节点的序号*/
		this.order_ = order;
		/**记录卫星节点的序号*/
		
		this.nei = new Neighbors(this);//新增，用于维护各DTNHost的邻居节点
		
	//	this.name = groupId+address;
		this.net = new ArrayList<NetworkInterface>();

		for (NetworkInterface i : interf) {
			NetworkInterface ni = i.replicate();
			ni.setHost(this);
			net.add(ni);
		}	

		// TODO - think about the names of the interfaces and the nodes

		this.msgListeners = msgLs;
		this.movListeners = movLs;

		//调用卫星的默认轨道参数构造函数，生成默认轨道参数的卫星
		if(defaultorbit == true && multiorbit == false) {
			this.movement = mmProto.replicateDefault();
			this.name = groupId+address+"default";
		}
		//调用卫星的默认轨道参数构造函数，生成默认轨道参数的卫星
		
		//调用卫星的均匀分布轨道参数构造函数，生成均匀分布轨道参数的卫星
		if(multiorbit == true && defaultorbit == false) {
			this.movement = mmProto.replicateRandom(true,getOrder());
			this.name = groupId+address+"random";
		}
		
		
		//通过运动模型得到200个三维和经纬度坐标，存储在节点变量中
		SatelliteOrbit so = ((SatelliteMovement)movement).getOrbit();
		TwoBody tb = ((SatelliteMovement)movement).getOrbit().getTwoBody();
		double tf = tb.period();
		double t0 = 0.0;
		tb.setSteps(steps);
		tb.propagate(t0, tf, this, true);
		
		for(int i=0;i<steps;++i) {
			double[][] bl = convert3DTo2D(XYZ[i][0]*1000,
					XYZ[i][1]*1000,XYZ[i][2]*1000);
			BL[i][0] = bl[0][0];
			BL[i][1] = bl[0][1];
		}
		
		this.three_dem_points=new double[1][3];
		this.three_dem_points = so.getInitLocation();
		
		this.two_dem_points = convert3DTo2D(three_dem_points[0][0]*1000,
				three_dem_points[0][1]*1000,three_dem_points[0][2]*1000);
		//通过运动模型得到200个三维和经纬度坐标，存储在节点变量中
		
		
		this.movement.setComBus(comBus);
		this.movement.setHost(this);
		setRouter(mRouterProto.replicate());

		this.location = movement.getInitialLocation1();

		this.nextTimeToMove = movement.nextPathAvailable();
		this.path = null;

		if (movLs != null) { // inform movement listeners about the location
			for (MovementListener l : movLs) {
				l.initialLocation(this, this.location);
			}
		}
	}
	
	/**
	 * Creates a new DTNHost.The orbit param of satellite host
	 * is inputed by users.
	 * @param msgLs Message listeners
	 * @param movLs Movement listeners
	 * @param groupId GroupID of this host
	 * @param interf List of NetworkInterfaces for the class
	 * @param comBus Module communication bus object
	 * @param mmProto Prototype of the movement model of this host
	 * @param mRouterProto Prototype of the message router of this host
	 * @param defaultorbit The orbit param of host is default
	 * @param multiorbit The orbit param of host is random
	 * @param t input orbit param of users
	 */
	public DTNHost(List<MessageListener> msgLs,
			List<MovementListener> movLs,
			String groupId, List<NetworkInterface> interf,
			ModuleCommunicationBus comBus, 
			MovementModel mmProto, MessageRouter mRouterProto,
			double[] t) {
		this.comBus = comBus;
		this.location = new Coord(0,0,0);
		this.address = getNextAddress();
		this.name = groupId+address+"user_config";
		this.order_ = 0;
		this.net = new ArrayList<NetworkInterface>();

		for (NetworkInterface i : interf) {
			NetworkInterface ni = i.replicate();
			ni.setHost(this);
			net.add(ni);
		}	

		this.nei = new Neighbors(this);//新增，用于维护各DTNHost的邻居节点
		
		// TODO - think about the names of the interfaces and the nodes

		this.msgListeners = msgLs;
		this.movListeners = movLs;

		//调用卫星的用户配置轨道参数构造函数，生成用户配置轨道参数的卫星
		this.movement = mmProto.replicateUserConfig(t);
		
		//通过运动模型得到200个三维和经纬度坐标，存储在节点变量中 
		TwoBody tb = ((SatelliteMovement)movement).getOrbit().getTwoBody();
		double tf = tb.period();
		double t0 = 0.0;
		tb.setSteps(steps);
		tb.propagate(t0, tf, this, true);
		
		for(int i=0;i<steps;++i) {
			double[][] bl = convert3DTo2D(XYZ[i][0]*1000,
					XYZ[i][1]*1000,XYZ[i][2]*1000);
			BL[i][0] = bl[0][0];
			BL[i][1] = bl[0][1];
		}
		
		this.three_dem_points=new double[1][3] ;
		this.three_dem_points[0][0]=tb.rv.x[0];
		this.three_dem_points[0][1]=tb.rv.x[1];
		this.three_dem_points[0][2]=tb.rv.x[2];
		
		this.two_dem_points = convert3DTo2D(three_dem_points[0][0]*1000,
				three_dem_points[0][1]*1000,three_dem_points[0][2]*1000);
		//通过运动模型得到200个三维和经纬度坐标，存储在节点变量中
		
		this.movement.setComBus(comBus);
		this.movement.setHost(this);
		setRouter(mRouterProto.replicate());

		this.location = movement.getInitialLocation1();

		this.nextTimeToMove = 0.0;
		//movement.nextPathAvailable();
		this.path = null;

		if (movLs != null) { // inform movement listeners about the location
			for (MovementListener l : movLs) {
				l.initialLocation(this, this.location);
			}
		}
	}
	
	/**
	 * Returns a new network interface address and increments the address for
	 * subsequent calls.
	 * @return The next address.
	 */
	private synchronized static int getNextAddress() {
		return nextAddress++;	
	}

	/**
	 * Reset the host and its interfaces
	 */
	public static void reset() {
		nextAddress = 0;
	}

	/**
	 * Returns true if this node is actively moving (false if not)
	 * @return true if this node is actively moving (false if not)
	 */
	public boolean isMovementActive() {
		return this.movement.isActive();
	}
	
	/**
	 * Returns true if this node's radio is active (false if not)
	 * @return true if this node's radio is active (false if not)
	 */
	public boolean isRadioActive() {
		/* TODO: make this work for multiple interfaces */
		return this.getInterface(1).isActive();
	}

	/**
	 * Set a router for this host
	 * @param router The router to set
	 */
	private void setRouter(MessageRouter router) {
		router.init(this, msgListeners);
		this.router = router;
	}

	/**
	 * Returns the router of this host
	 * @return the router of this host
	 */
	public MessageRouter getRouter() {
		return this.router;
	}

	/**
	 * Returns the network-layer address of this host.
	 */
	public int getAddress() {
		return this.address;
	}
	
	/**
	 * Returns this hosts's ModuleCommunicationBus
	 * @return this hosts's ModuleCommunicationBus
	 */
	public ModuleCommunicationBus getComBus() {
		return this.comBus;
	}
	
    /**
	 * Informs the router of this host about state change in a connection
	 * object.
	 * @param con  The connection object whose state changed
	 */
	public void connectionUp(Connection con) {
		this.router.changedConnection(con);
	}

	public void connectionDown(Connection con) {
		this.router.changedConnection(con);
	}

	/**
	 * Returns a copy of the list of connections this host has with other hosts
	 * @return a copy of the list of connections this host has with other hosts
	 */
	public List<Connection> getConnections() {
		List<Connection> lc = new ArrayList<Connection>();

		for (NetworkInterface i : net) {
			lc.addAll(i.getConnections());
		}

		return lc;
	}

	/**
	 * Returns the current location of this host. 
	 * @return The location
	 */
	public Coord getLocation() {
		return this.location;
	}

	/**
	 * Returns the Path this node is currently traveling or null if no
	 * path is in use at the moment.
	 * @return The path this node is traveling
	 */
	public Path getPath() {
		return this.path;
	}

	/**
	 * Sets the Node's location overriding any location set by movement model
	 * @param location The location to set
	 */
	/*public void setLocation(Coord1 location) {
		this.location = location.clone();
	}*/
	public void setLocation(Coord location) {
		this.location.setLocation(location);
	}

	/**
	 * Sets the Node's name overriding the default name (groupId + netAddress)
	 * @param name The name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the messages in a collection.
	 * @return Messages in a collection
	 */
	public Collection<Message> getMessageCollection() {
		return this.router.getMessageCollection();
	}

	/**
	 * Returns the number of messages this node is carrying.
	 * @return How many messages the node is carrying currently.
	 */
	public int getNrofMessages() {
		return this.router.getNrofMessages();
	}

	/**
	 * Returns the buffer occupancy percentage. Occupancy is 0 for empty
	 * buffer but can be over 100 if a created message is bigger than buffer 
	 * space that could be freed.
	 * @return Buffer occupancy percentage
	 */
	public double getBufferOccupancy() {
		double bSize = router.getBufferSize();
		double freeBuffer = router.getFreeBufferSize();
		return 100*((bSize-freeBuffer)/bSize);
	}

	/**
	 * Returns routing info of this host's router.
	 * @return The routing info.
	 */
	public RoutingInfo getRoutingInfo() {
		return this.router.getRoutingInfo();
	}

	/**
	 * Returns the interface objects of the node
	 */
	public List<NetworkInterface> getInterfaces() {
		return net;
	}

	/**
	 * Find the network interface based on the index
	 */
	public NetworkInterface getInterface(int interfaceNo) {
		NetworkInterface ni = null;
		try {
			ni = net.get(interfaceNo-1);
		} catch (IndexOutOfBoundsException ex) {
			throw new SimError("No such interface: "+interfaceNo + 
					" at " + this);
		}
		return ni;
	}

	/**
	 * Find the network interface based on the interfacetype
	 */
	protected NetworkInterface getInterface(String interfacetype) {
		for (NetworkInterface ni : net) {
			if (ni.getInterfaceType().equals(interfacetype)) {
				return ni;
			}
		}
		return null;	
	}

	/**
	 * Force a connection event
	 */
	public void forceConnection(DTNHost anotherHost, String interfaceId, 
			boolean up) {
		NetworkInterface ni;
		NetworkInterface no;

		if (interfaceId != null) {
			ni = getInterface(interfaceId);
			no = anotherHost.getInterface(interfaceId);

			assert (ni != null) : "Tried to use a nonexisting interfacetype "+interfaceId;
			assert (no != null) : "Tried to use a nonexisting interfacetype "+interfaceId;
		} else {
			ni = getInterface(1);
			no = anotherHost.getInterface(1);
			
			assert (ni.getInterfaceType().equals(no.getInterfaceType())) : 
				"Interface types do not match.  Please specify interface type explicitly";
		}
		
		if (up) {
			ni.createConnection(no);
		} else {
			ni.destroyConnection(no);
		}
	}

	/**
	 * for tests only --- do not use!!!
	 */
	public void connect(DTNHost h) {
		System.err.println(
				"WARNING: using deprecated DTNHost.connect(DTNHost)" +
		"\n Use DTNHost.forceConnection(DTNHost,null,true) instead");
		forceConnection(h,null,true);
	}

	/**
	 * Updates node's network layer and router.
	 * @param simulateConnections Should network layer be updated too
	 */
	public void update(boolean simulateConnections) {
		if (!isRadioActive()) {
			// Make sure inactive nodes don't have connections
			tearDownAllConnections();
			return;
		}
		
		if (simulateConnections) {
			for (NetworkInterface i : net) {
				i.update();
			}
		}
		this.router.update();
	}
	
	/** 
	 * Tears down all connections for this host.
	 */
	private void tearDownAllConnections() {
		for (NetworkInterface i : net) {
			// Get all connections for the interface
			List<Connection> conns = i.getConnections();
			if (conns.size() == 0) continue;
			
			// Destroy all connections
			List<NetworkInterface> removeList =
				new ArrayList<NetworkInterface>(conns.size());
			for (Connection con : conns) {
				removeList.add(con.getOtherInterface(i));
			}
			for (NetworkInterface inf : removeList) {
				i.destroyConnection(inf);
			}
		}
	}

	/**
	 * Moves the satellite node; Get timeIncrement's coordinate,
	 * then set node's coordinate. 
	 * @param timeIncrement set node's coordinate is timeIncrement's
	 * coordinate
	 */
	public void move(double timeIncrement) {
		Coord coor = new Coord(0,0,0);
		double time = SimClock.getTime();
		coor = this.movement.getSatelliteLocation(time);
		this.location.setLocation(coor);
	}
	
	/**
	 * Moves the node towards the next waypoint or waits if it is
	 * not time to move yet
	 * @param timeIncrement How long time the node moves
	 */
	public void move1(double timeIncrement) {		
		double possibleMovement;
		double distance;
		double dx, dy;

		if (!isMovementActive() || SimClock.getTime() < this.nextTimeToMove) {
			return; 
		}
		if (this.destination == null) {
			if (!setNextWaypoint()) {
				return;
			}
		}

		possibleMovement = timeIncrement * speed;
		distance = this.location.distance(this.destination);
		while (possibleMovement >= distance) {
			// node can move past its next destination
			this.location.setLocation(this.destination); // snap to destination
			possibleMovement -= distance;
			if (!setNextWaypoint()) { // get a new waypoint
				return; // no more waypoints left
			}
			distance = this.location.distance(this.destination);
		}

		// move towards the point for possibleMovement amount
		dx = (possibleMovement/distance) * (this.destination.getX() -
				this.location.getX());
		dy = (possibleMovement/distance) * (this.destination.getY() -
				this.location.getY());
		this.location.translate(dx, dy,0);
	}

	/**
	 * Sets the next destination and speed to correspond the next waypoint
	 * on the path.
	 * @return True if there was a next waypoint to set, false if node still
	 * should wait
	 */
	private boolean setNextWaypoint() {
		if (path == null) {
			path = movement.getPath();
		}

		if (path == null || !path.hasNext()) {
			this.nextTimeToMove = movement.nextPathAvailable();
			this.path = null;
			return false;
		}

	//	this.destination = convert(path.getNextWaypoint());
		this.speed = path.getSpeed();

		if (this.movListeners != null) {
			for (MovementListener l : this.movListeners) {
				l.newDestination(this, this.destination, this.speed);
			}
		}
		return true;
	}

	/**
	 * Sends a message from this host to another host
	 * @param id Identifier of the message
	 * @param to Host the message should be sent to
	 */
	public void sendMessage(String id, DTNHost to) {
		this.router.sendMessage(id, to);
	}

	/**
	 * Start receiving a message from another host
	 * @param m The message
	 * @param from Who the message is from
	 * @return The value returned by 
	 * {@link MessageRouter#receiveMessage(Message, DTNHost)}
	 */
	public int receiveMessage(Message m, DTNHost from) {
		int retVal = this.router.receiveMessage(m, from); 

		if (retVal == MessageRouter.RCV_OK) {
			m.addNodeOnPath(this);	// add this node on the messages path
		}

		return retVal;	
	}

	/**
	 * Requests for deliverable message from this host to be sent trough a
	 * connection.
	 * @param con The connection to send the messages trough
	 * @return True if this host started a transfer, false if not
	 */
	public boolean requestDeliverableMessages(Connection con) {
		return this.router.requestDeliverableMessages(con);
	}

	/**
	 * Informs the host that a message was successfully transferred.
	 * @param id Identifier of the message
	 * @param from From who the message was from
	 */
	public void messageTransferred(String id, DTNHost from) {
		this.router.messageTransferred(id, from);
	}

	/**
	 * Informs the host that a message transfer was aborted.
	 * @param id Identifier of the message
	 * @param from From who the message was from
	 * @param bytesRemaining Nrof bytes that were left before the transfer
	 * would have been ready; or -1 if the number of bytes is not known
	 */
	public void messageAborted(String id, DTNHost from, int bytesRemaining) {
		this.router.messageAborted(id, from, bytesRemaining);
	}

	/**
	 * Creates a new message to this host's router
	 * @param m The message to create
	 */
	public void createNewMessage(Message m) {
		this.router.createNewMessage(m);
	}

	/**
	 * Deletes a message from this host
	 * @param id Identifier of the message
	 * @param drop True if the message is deleted because of "dropping"
	 * (e.g. buffer is full) or false if it was deleted for some other reason
	 * (e.g. the message got delivered to final destination). This effects the
	 * way the removing is reported to the message listeners.
	 */
	public void deleteMessage(String id, boolean drop) {
		this.router.deleteMessage(id, drop);
	}

	/**
	 * Returns a string presentation of the host.
	 * @return Host's name
	 */
	public String toString() {
		return name;
	}

	/**
	 * Checks if a host is the same as this host by comparing the object
	 * reference
	 * @param otherHost The other host
	 * @return True if the hosts objects are the same object
	 */
	public boolean equals(DTNHost otherHost) {
		return this == otherHost;
	}

	/**
	 * Compares two DTNHosts by their addresses.
	 * @see Comparable#compareTo(Object)
	 */
	public int compareTo(DTNHost h) {
		return this.getAddress() - h.getAddress();
	}
	
	/**将三维坐标转换为二维坐标*/
	public double[][] convert3DTo2D(double X, double Y, double Z) {
		double[][] bl = new double[1][2]; 
		if(X>0) {
			bl[0][0] = Math.atan(Y/X)*180/3.1415926;
		}
		else if(X<0&&Y>0) {
			bl[0][0] = (3.1415926+Math.atan(Y/X))*180/3.1415926;
		}
		else if (X<0&&Y<0) {
			bl[0][0] = -(3.1415926-Math.atan(Y/X))*180/3.1415926;
		}
		
		bl[0][1] = Math.atan(Z/Math.sqrt(X*X+Y*Y))*180/3.1415926/**(201.5)*/;
		
		return bl;
	}
	/**将三维坐标转换为二维坐标*/
	
	/**得到运动模型产生的200的三维坐标函数，实现了Printable*/
	public void print(double t, double[] y) {

		// add data point to the plot
		if (step < XYZ.length) {
			XYZ[step][0] = y[0];
			XYZ[step][1] = y[1];
			XYZ[step][2] = y[2];
			
			if (y[0] > max)
				max = y[0];
			if (y[1] > max)
				max = y[1];
			if (y[2] > max)
				max = y[2];
			
			step++;
		}
	}
	/**得到运动模型产生的200的三维坐标函数，实现了Printable*/
	
	/**得到运动模型产生的200的三维坐标函数，实现了Printable*/
	public void print1(double t, double[] y) {
		
	}
	/**得到运动模型产生的200的三维坐标函数，实现了Printable*/
	
	/**得到运动模型产生的200的三维坐标函数，实现了Printable*/
	public void print2(double t, double[] y) {
		
	}
	/**得到运动模型产生的200的三维坐标函数，实现了Printable*/
	
	public double getMAX() {
		return this.max;
	}
	
	public double[][] getXYZ() {
		return this.XYZ;
	}
	
	public double[][] getBL() {
		return this.BL;
	}
	
	public double[][] get3Dpoints() {
		return this.three_dem_points;
	}
	
	public double[][] get2Dpoints() {
		return this.two_dem_points;
	}
	
	public int getOrder() {
		return this.order_;
	}

	/*新增函数*/
	/**
	 * 返回卫星所属轨道平面编号参数
	 */
	public int getNrofPlane(){
		return this.nrofPlane;
	}
	/**
	 * 返回卫星在轨道平面内的编号
	 */
	public int getNrofSatelliteINPlane(){
		return this.nrofSatelliteINPlane;
	}
	/**
	 * 用于Neighbors进行预测邻居节点生存时间时用,获取未来某个时间节点的位置
	 * @param time
	 * @return
	 */
	/*public Coord getCoordinate(double time){
		double[][] coordinate = new double[1][3];
		//double[] t = new double[]{8000,0.1,15,0.0,0.0,0.0};;

		SatelliteOrbit saot = new SatelliteOrbit(this.parameters);
		//saot.SatelliteOrbit(t);
		coordinate = saot.getSatelliteCoordinate(time);
		Coord c = new Coord(0,0);
		c.resetLocation((coordinate[0][0])/10+2000, (coordinate[0][1])/10+2000, (coordinate[0][2])/10+2000);
		return c;
	}*/
	public Coord getCoordinate(double time){
		Coord coor = this.movement.getSatelliteLocation(time);
		return coor;
	}
	/**
	 * 新增函数，返回新增的邻居数据库
	 * @return
	 */
	public Neighbors getNeighbors(){
		return this.nei;
	}

	/**
	 * 新增函数，返回新增的卫星轨道参数
	 * @return
	 */
	public double[] getParameters(){
		return this.parameters;
	}
	public void changeHostsList(List<DTNHost> hosts){
		 this.nei.changeHostsList(hosts);
		 this.hosts = hosts;
	}
	/*public void updateLocation(double timeNow){
		this.location.my_Test(0.0,timeNow,this.parameters);//修改节点的位置,获取timeNow时刻的位置
	}*/
	
}
