package demos;

import mixlogic.Wave.WaveIter;

public class Sine extends WaveIter {
	
		int wavelength;
		int amplitude;
		
		public Sine() {
			this(500, 1);
		}	
		
		public Sine(int wavelength) {
			this(wavelength, 1);
		}

		public Sine(int wavelength, int amplitude) {
			super(wavelength, amplitude);
			
			this.amplitude = amplitude;
			this.wavelength = wavelength;
		}

		@Override
		public Integer sample() {
			double indx = (((pos % wavelength) / (double) wavelength) * 2 * Math.PI);
			return
				(int) ((Math.pow(2.0, 31.0) - 1) * Math.sin(indx));
		}
		
}
