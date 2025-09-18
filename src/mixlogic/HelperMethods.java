package mixlogic;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class HelperMethods {
	
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
}
