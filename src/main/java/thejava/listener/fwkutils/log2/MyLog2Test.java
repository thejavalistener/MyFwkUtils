package thejava.listener.fwkutils.log2;

public class MyLog2Test
{
	public static void main(String[] args)
	{
		String path = "D:\\Java64\\Workspace\\MyFwkUtils\\src\\main\\java\\thejava\\listener\\fwkutils\\log2";
		String filename = "MyLog.txt";
		
		MyLog log = new MyLog(path+"/"+filename,true);
		log.config.showClassName=false;
		log.qdbg();
		log.qdbg();
	}
}
