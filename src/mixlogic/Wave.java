package mixlogic;

import java.util.Iterator;

public class Wave {
	public WaveIter iter;
	
	public Wave(WaveIter iter) {
		this.iter = iter;
	}
	
	public abstract static class WaveIter implements Iterator<Integer> {
		
		protected int wavelength;
		protected int amplitude;
		public int pos = 0;
		
		public WaveIter(int wavelength, int amplitude) {
			this.wavelength = wavelength;
			this.amplitude = amplitude;
		}
		
		// WaveIters should not have an end, even if their samples approach/reach 0
		@Override
		public boolean hasNext() {
			return true;
		}
		
		@Override
		public Integer next() {
			Integer sample = sample();
			pos++;
			return sample;
		}
		
		protected abstract Integer sample();
	}
}
