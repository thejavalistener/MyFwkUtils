package thejava.listener.fwkutils.log;

public class MyLog2Test
{
	public static void main(String[] args)
	{
		String path = "D:\\Java64\\Workspace\\MyFwkUtils\\src\\main\\java\\thejava\\listener\\fwkutils\\log";
		String filename = "MyLog.txt";
		
		MyLog log = MyLog.get();
		MyLogConfig cfg = log.getConfig();
		cfg.setLogFileName(path+"/"+filename);
		log.applyConfig();
		
		
		log.println("Pepino KO!!");
	}
}
