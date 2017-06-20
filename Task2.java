import java.io.*;
import java.security.MessageDigest;

public class Task2
{
	public static void main(String[] args)
	{
		String md5 = "", sha1 = "", fileSHA1 = "", fileMD5 = "";
		String file = args[0];
		fileSHA1 = "SHA-1-"+getFileName(file)+".txt";
		fileMD5 = "MD5-"+getFileName(file)+".txt";
		 PrintWriter writer = null, writer1 = null;
		
		try {
			InputStream fis = new FileInputStream(file);;
			
			md5 = checkSumMD5(fis);
			fis = new FileInputStream(file);
			sha1 = checkSumSHA1(fis);
			System.out.println(md5);
			System.out.println(sha1);
			
			
			writer = new PrintWriter(new FileWriter(fileMD5,true), true);
			writer.flush();
			writer.print(sha1);
			writer.flush();
			writer1 = new PrintWriter(fileSHA1, "UTF-8");
			writer1.println(md5);
			writer1.flush();
			
		
			}
			catch(Exception e)
			{
				System.out.println("You Have an error");
			}
			finally{
				writer.close();
				writer1.close();
				//fis.close();
			}
			writer.close();
			writer1.close();
			//fis.close();
			
			// Analyze Partition
			
			
			
		InputStream is = null;
		String hex = "";
		int k = 0, r = 0;
		byte[] mbr = new byte[512];
		
		byte[] partition1 = new byte[16];
		byte[] partition2 = new byte[16];
		byte[] partition3 = new byte[16];
		byte[] partition4 = new byte[16];
		
		try
		{
			
			is = new FileInputStream(args[0]);
			
			k = is.read(mbr, 0, 512 );
			
			
			r = 0;
			for(int i = 512 - 16 -2; i <512 -2; i++) // Partition 4
			{
				partition4[r] = mbr[i];
				r++;
			}
			// End 
			
			r = 0;
			for(int i = 512 - 16 - 16-2; i <512 - 16-2; i++) // Partition 3
			{
				
				partition3[r] = mbr[i];
				r++;
			}
			// End 
			
			r = 0;
			for(int i = 512 - 16 - 16 - 16-2  ; i <512 - 16 - 16-2 ; i++) // Partition 2
			{
				
				partition2[r] = mbr[i];
				r++;
			}
			// End 
			
			r = 0;
			for(int i = 512 - 16 - 16 - 16 - 16 -2; i <512 - 16 - 16 - 16 -2; i++) // Partition 1
			{
				
				partition1[r] = mbr[i];
				r++;
			}
			
			System.out.println("\n");
			System.out.println("Partation1:"+"\n");
			analyzePart(partition1);
			System.out.println("\n");
			System.out.println("Partation2:"+"\n");
			analyzePart(partition2);
			System.out.println("\n");
			System.out.println("Partation3:"+"\n");
			analyzePart(partition3);
			System.out.println("\n");
			System.out.println("Partation4:"+"\n");
			
			analyzePart(partition4);
			
		} // end try
		catch(Exception e)
		{
			System.out.println("You have an error");
			e.printStackTrace();
		} // end catch
			
			
			
			
			
	}
	
	static String checkSumMD5(InputStream fis) throws Exception
	{
		int numRead = 0;
		String md5 = "";
		byte[] buffer = new byte[1024];
		byte[] checkSum;
		MessageDigest hash = MessageDigest.getInstance("MD5");
		
		while(numRead != -1)
	   {
		   
		   numRead = fis.read(buffer);
		   if(numRead > 0)
		   {
				hash.update(buffer, 0, numRead);
		   }
	   }
	   
	   
	   checkSum = hash.digest();
	   
	    for (int i=0; i < checkSum.length; i++) 
		{
           md5 = md5 + Integer.toString( ( checkSum[i] & 0xff ) + 0x100, 16).substring( 1 );
       }
	   md5 = md5.toUpperCase();
	   return md5;
	}
	
	
	
	static String checkSumSHA1(InputStream fis) throws Exception
	{
		int numRead = 0;
		String sha1 = "";
		byte[] buffer = new byte[1024];
		byte[] checkSum;
		MessageDigest hash = MessageDigest.getInstance("SHA-1");
		
		while(numRead != -1)
	   {
		   
		   numRead = fis.read(buffer);
		   if(numRead > 0)
		   {
				hash.update(buffer, 0, numRead);
		   }
	   }
	   
	   
	   checkSum = hash.digest();
	   
	    for (int i=0; i < checkSum.length; i++) 
		{
           sha1 = sha1 + Integer.toString( ( checkSum[i] & 0xff ) + 0x100, 16).substring( 1 );
       }
	   sha1 = sha1.toUpperCase();
	   return sha1;
	}
	
	static String getFileName(String arg)
	{
		String file = arg;
		int len = file.length(), indx1 = 0, indx2 = len;
		boolean bool = true, bool2 = true;
		
		for(int i = len-1; i >= 0; i--)
		{
			if((file.charAt(i) == '\\' || file.charAt(i) == '/') && bool == true)
			{
				indx1 = i+1;
				bool = false;
			}
			if((file.charAt(i) == '.') && bool2 == true)
			{
				indx2 = i;
				bool2 = false;
			}
		}
		return  file.substring(indx1, indx2);
	}
	
	public static void analyzePart(byte[] partation)
	{
		String type, flag, start_temp, end_temp, type1, status;
		int start, size;
		
		flag = String.format("%02X ",partation[0]);
		type = String.format("%02X ",partation[4]);
		type1 = getType(type);
		start_temp = String.format("%02X",partation[11])+String.format("%02X",partation[10])+String.format("%02X",partation[9])+String.format("%02X",partation[8]);
		start = hexToDecimal(start_temp);
		end_temp = String.format("%02X",partation[15])+String.format("%02X",partation[14])+String.format("%02X",partation[13])+String.format("%02X",partation[12]);
		size = hexToDecimal(end_temp);
		
		System.out.println("Flag: "+ flag+ " Type: " + type1 + " (" + type  + ") " + "start: " + start+" size: " + end_temp);
	}
	
	public static int hexToDecimal(String hex)
	{
		 Integer outputDecimal = Integer.parseInt(hex, 16);
		   
		 return outputDecimal;
	}
	
	public static String getType(String type)
	{
		String answer = ""; //= "Not Valid";
		
		if(type.equals("01 "))
		{
			answer = "DOS 12-bit FAT";
		}
		else if(type.equals("04 "))
		{
			answer = "DOS 16-bit FAT < 32 MB";
		}
		else if(type.equals("05 "))
		{
			answer = "Extended Partition";
		}
		else if(type.equals("06 "))
		{
			answer = " DOS 16-bit FAT > 32 MB";
		}
		else if(type.equals("07 "))
		{
			answer = "NTFS";
		}
		else if(type.equals("08 "))
		{
			answer = "Aix Bootable Partition";
		}
		else if(type.equals("09 "))
		{
			answer = "AIX Data Partition";
		}
		else if(type.equals("0B "))
		{
			answer = "DOS 32-bit FAT";
		}
		else if(type.equals("0C "))
		{
			answer = "DOS 32-bit FAT for interrupt 13 upport";
		}
		else if(type.equals("17 "))
		{
			answer = "Hidden NTFS Partition (XP and Earlier)";
		}
		else if(type.equals("1B "))
		{
			answer = "Hidden FAT32 Partition";
		}
		else if(type.equals("1E "))
		{
			answer = "Hidden VFAT Partition";
		}
		else if(type.equals("3C "))
		{
			answer = "Partition Magic Recovery Partition";
		}
		else if(type.equals("66 ") ||type.equals("67 ") || type.equals("68 ")||type.equals("69 "))
		{
			answer = "Novell Partition";
		}
		else if(type.equals("81 "))
		{
			answer = "Linux";
		}
		else if(type.equals("82 "))
		{
			answer = "Linux Swap Partition";
		}
		else if(type.equals("83 "))
		{
			answer = "Linux Native File System";
		}
		else if(type.equals("86 "))
		{
			answer = "Fat 16 Volume/stripe set";
		}
		else if(type.equals("87 "))
		{
			answer = "HPFS";
		}
		else if(type.equals("A5 "))
		{
			answer = "FreeBSD and BSD/386";
		}
		else if(type.equals("A6 "))
		{
			answer = "OpenBSD";
		}
		else if(type.equals("A9 "))
		{
			answer = "NetBSD";
		}
		else if(type.equals("C7 "))
		{
			answer = "Typical Of Corrupted NTFS Volume/stripe set";
		}
		else if(type.equals("EB "))
		{
			answer = "BeOS";
		}
		
		
		return answer;
	}
	
	
	
	
	
}