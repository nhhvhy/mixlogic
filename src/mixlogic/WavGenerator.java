package mixlogic;

import java.io.*;

public class WavGenerator {
	
	public static byte[] BEtoLE(byte[] BEbytes) {
		if (BEbytes == null || BEbytes.length == 0) {
			throw new IllegalArgumentException("Invalid input");
		}
		
		byte[] LEbytes = new byte[BEbytes.length];
		for (int i=0; i < BEbytes.length; i++) {
			LEbytes[i] = BEbytes[BEbytes.length - i - 1];
		}
		
		return LEbytes;
	}
	
	public static byte[] PadBytesBE(byte[] BEbytes, int padLength) {
		if (BEbytes == null) {
			throw new IllegalArgumentException("null BEbytes input");
		}
		
		byte[] paddedBytesBE = new byte[padLength];
		for (int i=0; i < padLength; i++) {
			
			if (i >= (padLength - BEbytes.length)) {
				paddedBytesBE[i] = BEbytes[i - (padLength - BEbytes.length)]; 
			} else {
				paddedBytesBE[i] = 0x00;
			}
		}
		
		return paddedBytesBE;
	}
	
	public static byte[] concatByteArrs(byte[]... arrays) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		for (byte[] arr : arrays) {
			try {
				baos.write(arr);
			} catch (IOException e) {
				throw new RuntimeException("Unexpected IOException from baos", e);
			}
		}
		
		byte[] result = baos.toByteArray();
		return result;		
	}
	
	public static byte[] intToBytesLE(int value) {
		return new byte[] {
				(byte)(value),
				(byte)(value >>> 8),
				(byte)(value >>> 16),
				(byte)(value >>> 24)
		};
	}
	
	public static byte[] shortToBytesLE(int value) {
		return new byte[] {
				(byte)(value),
				(byte)(value >>> 8)
		};
	}
	
	public static void main(String[] args) {
		
		final short NUMCHANNELS = 1;   // Mono=1, Stereo=2, and so on
		final int SAMPLERATE = 44100;
		final int NUMSAMPLES = 88200;
		final int BITSPERSAMPLE = 16;
		final int SUBCHUNK1SIZE = 16;  // 16 for PCM (No extra params)
		
		final int BYTERATE = (SAMPLERATE * NUMCHANNELS * (BITSPERSAMPLE / 8));
		final int SUBCHUNK2SIZE = (NUMSAMPLES * NUMCHANNELS * (BITSPERSAMPLE / 8));
		final int CHUNKSIZE = (36 + SUBCHUNK2SIZE);
		final short BLOCKALIGN = (NUMCHANNELS * (BITSPERSAMPLE / 8));
		
		
		try {
			
			OutputStream Stream = new FileOutputStream("data.wav");
			
			/* WRITE WAV HEADER */
			Stream.write(concatByteArrs(
				
				/* RIFF Header */
				new byte[] {0x52, 0x49, 0x46, 0x46}, // "RIFF" in big-endian										ChunkID
				intToBytesLE(CHUNKSIZE), //																			ChunkSize
				new byte[] {0x57, 0x41, 0x56, 0x45}, // "WAVE" in big-endian										Format
				
				/* fmt Subchunk */
				new byte[] {0x66, 0x6d, 0x74, 0x20}, // 															SubChunk1ID
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
			
			Stream.close();
		
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
