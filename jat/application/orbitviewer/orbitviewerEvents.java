/* JAT: Java Astrodynamics Toolkit
 * 
  Copyright 2012 Tobias Berthold

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

package jat.application.orbitviewer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import ui.DTNSimTextUI;

import java.io.IOException;

public class orbitviewerEvents implements ActionListener, ItemListener {
	orbitviewerGUI d;
	Configuration config;
	DTNSimTextUI DSTUI;

	public orbitviewerEvents(orbitviewerGUI d) {
		this.d = d;
	}

	public void actionPerformed(ActionEvent ev) {

		// Read in values
		/*if (ev.getSource() == d.setButton) {
			//System.out.println("button 1 pressed");

			d.o.a = (Double) d.semimajorfield.getValue();
			d.o.e = (Double) d.EccentricityField.getValue();
			d.o.i = (Double) d.InclinationField.getValue();
			d.o.raan = (Double) d.RightAscensionField.getValue();
			d.o.w = (Double) d.ArgumentofPerigeeField.getValue();
			d.o.ta = (Double) d.TrueAnomalyField.getValue();
			//System.out.println("a "+ d.o.a);

			d.o.plot.removeAllPlots();
			d.o.add_scene();

		}*/
		
		if(ev.getSource() == d.setButton1) {
			
			config = new Configuration();
			config.default_numbers = ((Number)d.Default_num.getValue()).intValue();
			config.random_numbers = ((Number)d.Random_num.getValue()).intValue();
			config.user_config_numbers = 0;
			config.parameter[0] = (Double) d.semimajorfield.getValue();
			config.parameter[1] = (Double) d.EccentricityField.getValue();
			config.parameter[2] = (Double) d.InclinationField.getValue();
			config.parameter[3] = (Double) d.RightAscensionField.getValue();
			config.parameter[4] = (Double) d.ArgumentofPerigeeField.getValue();
			config.parameter[5] = (Double) d.TrueAnomalyField.getValue();
			config.d = this.d;
	//		d.o.plot.removeAllPlots();
			DSTUI = new DTNSimTextUI();
		    DSTUI.start(config,true);
			System.out.println("Insert Done!");
		}
		if(ev.getSource() == d.setButton2) {
			System.out.println("Start Run!");
			this.DSTUI.runSim();
		}
	}// End of ActionPerformed

	@Override
	public void itemStateChanged(ItemEvent e) {
		
		Object source = e.getItemSelectable();

		if (source == d.rotationCheckBox) {
			 System.out.println("rot");
			 if(d.rotationCheckBox.isSelected()) {
			//	 int steps = 200;
			//	 new Thread(d.o.getFunction()).start();
				 d.o.getFunction().move();
				 d.o.plot.plotCanvas.timer.start();
			 //    Thread.sleep(10*1000);
			/*	 for(int j=0;j<steps;++j) {
					 d.o.getFunction().move(j);
				 }*/
			 }
			 else {
				 d.o.plot.plotCanvas.timer.stop();
			 }
		}		
	}

}
