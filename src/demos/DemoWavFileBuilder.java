package demos;

import mixlogic.WavFileGenerator;
import mixlogic.Wave;
import mixlogic.Wave.*;

public class DemoWavFileBuilder {
	
	public static void main(String args[]) {
		WavFileGenerator wav = new WavFileGenerator();
		
		int duration = 10 * wav.SAMPLERATE;
		int wavelength = 200;
		
		WaveIter DeepTone = new Sine(wavelength);
		Wave sampleWave = new Wave(DeepTone);

		
		for (int i=0; i < duration; i++) {
			int value = sampleWave.iter.next();
			wav.addSample(value);
		}
		
		try {
			wav.writeToFile("test.wav");
		} catch (Exception e) {
			System.out.println("you screwed up :skull:");
			e.printStackTrace();
		}

	}
}
