package thejavalistener.fwkutils.console;

import thejavalistener.fwkutils.various.MyThread;

public class MyConsoleDemo
{
	public static void main(String[] args)
	{
		MyConsole c = MyConsole.openWindow("Demo");
		
		// un banner
		c.banner("Esto es un banner!");
		
		// un mensaje
		c.println("Esto es un mensaje");
		
		// espero hasta que toque una tecla
		c.println("Presione una tecla para continuar...");
		c.pressAnyKey();
		
		// ingreso un valor alfanumérico
		String s = c.print("Ingrese una cadena de caracteres: ").readlnString();
		c.println("La cadena que ingresaste es: "+s);
		
		// ingreso un valor numérico
		int v = c.print("Ingrese un valor numérico: ").readlnInteger();
		c.println("El valor que ingresaste es: "+v);
		
		// validación por regex 
		String regex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
		String mail = c.print("Ingrese un mail: ").readlnString(regex);
		c.println("El mail es: "+mail);
		
		// un password
		String pwd = c.print("Ingrese un password: ").readlnPassword();
		c.println("El password es: "+pwd);
		
		
		// NOTA - El método execute sólo es necesario si la consola está dentro 
		//        de una GUI si se trata de una app netamente de consola 
		//        puede obviarse e ir directo al increas.

		// un proceso usando progressBar....
		c.print("Presione la tecla [p]... ").pressAnyKey('P');
		int loops = 100;
		Progress p1 = c.progressBar(20,loops);
		p1.execute(()->{
			for(int i=0; i<loops; i++)
			{
				MyThread.randomSleep(100); // demoro simulando que hago algo...
				p1.increase();
			}
		});

		c.println("El proceso demoró: "+p1.elapsedTime()/1000+" segundos");

		// otro proceso usando progressMeter....
		c.print("Presione nuevamente la tecla [p]... ").pressAnyKey('P');
		Progress p2 = c.progressMeter(loops);
		p2.execute(()->{
			for(int i=0; i<loops; i++)
			{
				MyThread.randomSleep(100); // demoro simulando que hago algo...
				p2.increase();
			}
		});
		
		c.println(" El proceso demoró: "+p2.elapsedTime()/1000+" segundos");

		c.print("Esperemos 5 segundos para seleccionar un archivo. ").countdownln(5);
		
		
		// selecciono un archivo
		String filename = c.print("Seleccione un archivo: ").fileExplorer();
		c.println(filename);
		
		// menú
		String ops[] = {"Mar del Plata","Pinamar","Necochea","Mar de Ajo","Miramar"};
		int op = c.print("A dónde viajarás en las vacas? ").menuln(ops);
		c.println("Excelente elección: "+ops[op]);

		// otro menú
		String ops2[] = {"Beatles","Rolling Stones","Kiss","AC/CD","Black Sabath"};
		op = c.print("Qué preferís? ").menuln(ops2);
		c.println("Qué genio!: "+ops2[op]);
		
		// colores
		c.println("Esto sale en [fg(red)]Rojo[x] y esto en [fg(blue)]Azul[x], se entiende?");
		c.println("Esto sale en [b]negrita[x] y esto en [i]cursiva[x], ok?");
		c.println("Esto sale en [fg(orange)][b]naranja y negrita[x][x], y esto en [fg(green)][i]verde y cursiva[x][x], ok?");
		
		// fin
		c.println("Presiona una tecla para finalizar... ").pressAnyKey();
		c.print("Cerrando en ").closeAndExit(3);
	}
}
