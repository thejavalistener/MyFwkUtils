package thejavalistener.fwk.console;

import thejavalistener.fwk.util.MyThread;

public class MyConsoleDemo
{
	public static void main(String[] args)
	{
		MyConsole c = MyConsole.openWindow("Demo");
		
//		// un banner
//		c.banner("Esto es un banner!");
//		
//		// un mensaje
//		c.println("Esto es un mensaje");
//		
//		// espero hasta que toque una tecla
//		c.println("Presione una tecla para continuar...");
//		c.pressAnyKey();
//		
//		// ingreso un valor alfanumérico
//		String s = c.print("Ingrese una cadena de caracteres: ").readlnString();
//		c.println("La cadena que ingresaste es: ["+s+"]");
//		
//		// ingreso un valor numérico
//		int i = c.print("Ingrese un valor numérico: ").readlnInteger();
//		c.println("El valor que ingresaste es: ["+i+"]");
//		
//		// ingreso un mail
//		String regex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
//		String mail = c.print("Ingrese un mail: ").readlnString(regex);
//		c.println("El mail es: ["+mail+"]");
//		
//		// un password
//		String pwd = c.print("Ingrese un mail: ").readlnPassword();
//		c.println("El password es: ["+pwd+"]");
//		
//		// un proceso....
//		c.print("Presione la tecla p... ").pressAnyKey('P');
//		Progress p = c.progressBar(20,100);
//		p.execute(()->{
//			for(int i=0; i<100; i++)
//			{
//				MyThread.randomSleep(100);
//				p.increase();
//			}
//		});
//		
//		c.println("Demoró: "+p.elapsedTime()/1000+" segundos");
//		
//		// selecciono un archivo
//		String filename = c.print("Seleccione un archivo: ").fileExplorer();
//		c.println(filename);
//		
//		// menú
//		String ops[] = {"Mar del Plata","Pinamar","Necochea","Mar de Ajo","Miramar"};
//		int op = c.print("A dónde viajarás en las vacas? ").menuln(ops);
//		c.println("Excelente elección: "+ops[op]);
//
		// fin
		c.println("Presiona una tecla para finalizar... ").pressAnyKey();
		c.closeAndExit("Cerrando en ",3);
	}
}
