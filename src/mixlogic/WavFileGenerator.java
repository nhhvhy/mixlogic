package mixlogic;

import java.io.*;
import java.util.ArrayList;

public class WavFileGenerator extends HelperMethods {
	
	public short NUMCHANNELS;
	public int SAMPLERATE;
	public int NUMSAMPLES;
	public int BITSPERSAMPLE;
	public int SUBCHUNK1SIZE;
	
	public int BYTERATE;
	public int SUBCHUNK2SIZE;
	public int CHUNKSIZE;
	public short BLOCKALIGN;
	
	private static ArrayList<Integer> data = new ArrayList<Integer>();
	
	public WavFileGenerator() {};
	
	private void buildHeaderInfo() {
		NUMCHANNELS = 1;   // Mono=1, Stereo=2, and so on
		SAMPLERATE = 44100;
		NUMSAMPLES = data.size();
		BITSPERSAMPLE = 32;  // 32 bits should be fine for now, but it may cause problems later if a different value is used (int is 32 bits)
		SUBCHUNK1SIZE = 16;  // 16 for PCM (No extra params)
		
		BYTERATE = (SAMPLERATE * NUMCHANNELS * (BITSPERSAMPLE / 8));
		SUBCHUNK2SIZE = (NUMSAMPLES * NUMCHANNELS * (BITSPERSAMPLE / 8));
		CHUNKSIZE = (36 + SUBCHUNK2SIZE);
		BLOCKALIGN = (short) (NUMCHANNELS * (BITSPERSAMPLE / 8));
	}
	
	public void addSample(int sample) {
		data.add(sample);
	}
	
	public void writeToFile(String fileName) throws Exception {
		// Add wav extension if not already present in fileName
		if(!fileName.contains(".wav"))
			fileName += ".wav";
		
		OutputStream s = new FileOutputStream(fileName);
		buildHeaderInfo();
		
		/* WRITE WAV HEADER */
		s.write(concatByteArrs(
			
			/* RIFF Header */
			new byte[] {0x52, 0x49, 0x46, 0x46}, // "RIFF" in big-endian										ChunkID
			intToBytesLE(CHUNKSIZE), //																			ChunkSize
			new byte[] {0x57, 0x41, 0x56, 0x45}, // "WAVE" in big-endian										Format
			
			/* fmt Subchunk */
			new byte[] {0x66, 0x6d, 0x74, 0x20}, // " fmt" in big-endian													SubChunk1ID
			intToBytesLE(SUBCHUNK1SIZE), //																		Subchunk1Size
			BEtoLE( PadBytesBE(new byte[] {0x01}, 2)), // PCM = 1 (Values beside 1 indicate compression) 		AudioFormat
			shortToBytesLE(NUMCHANNELS), //																		NumChannels
			intToBytesLE(SAMPLERATE), //																		SampleRate
			intToBytesLE(BYTERATE), //																			ByteRate
			shortToBytesLE(BLOCKALIGN), //																		BlockAlign
			shortToBytesLE(BITSPERSAMPLE), //																	BitsPerSample
			
			/* data Subchunk */
			new byte[] {0x64, 0x61, 0x74, 0x61}, // "data" in big-endian										SubChunk2ID
			intToBytesLE(SUBCHUNK2SIZE)
		));
		
		/* WRITE DATA */
		for (Integer sample : data)
			s.write(intToBytesLE(sample));
		
		s.close();
	}

}
