package ex02.definenewclass;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;

public class DefineNewClass {
	static String workDir = System.getProperty("user.dir");
	static String outputDir = workDir + File.separator + "output";

	public static void main(String hi[]) {
		String args[] = null;
		Scanner input = new Scanner(System.in);
		
		System.out.print("Please enter the three classes to make seperated by commas: ");
		args = input.nextLine().split(",");
		while (args.length != 3) {
			System.out.println("[WRN] Invalid Input");
			System.out.print("Please enter the three classes to make seperated by commas: ");
			args = input.nextLine().split(",");
		}
		input.close();
		
		int index = 0;
		String common = "Common";

		if (args[0].startsWith(common)) {
			if (args[1].startsWith(common) && args[2].startsWith(common)) {
				if (args[0].length() > args[1].length() && args[0].length() > args[2].length()) {
					index = 0;
				} else if (args[1].length() > args[0].length() && args[1].length() > args[2].length()) {
					index = 1;
				} else {
					index = 2;
				}
			} else if (args[1].startsWith(common)) {
				if (args[1].length() > args[0].length()) {
					index = 1;
				} else {
					index = 0;
				}
			} else if (args[2].startsWith(common)) {
				if (args[0].length() > args[2].length()) {
					index = 0;
				} else {
					index = 2;
				}
			} else {
				index = 0;
			}
		} else if (args[1].startsWith(common)) {

			if (args[2].startsWith(common)) {
				if (args[1].length() > args[2].length()) {
					index = 1;
				} else {
					index = 2;
				}
			} else {
				index = 2;
			}
		} else if (args[2].startsWith(common)) {
			index = 2;
		}

		String superClass = args[index];

		for (int i = 0; i < args.length; i++) {
			try {
				ClassPool pool = ClassPool.getDefault();

				CtClass cc = makeClass(pool, args[i]);
				if (i != index) {
					setSuperclass(cc, superClass, pool);
				}
				cc.writeFile(outputDir);
				System.out.println("[DBG] write output to: " + outputDir);

				CtClass ccInterface = makeInterface(pool, "I".concat(args[i]));
				ccInterface.writeFile(outputDir);
				System.out.println("[DBG] write output to: " + outputDir);
				
			} catch (CannotCompileException | IOException | NotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	static CtClass makeClass(ClassPool pool, String newClassName) {
		CtClass cc = pool.makeClass(newClassName);
		System.out.println("[DBG] make class: " + cc.getName());
		return cc;
	}

	private static CtClass makeInterface(ClassPool pool, String newInterfaceName) {
		CtClass cc = pool.makeInterface(newInterfaceName);
		System.out.println("[DBG] make interface: " + cc.getName());
		return cc;
	}
	
	private static void setSuperclass(CtClass curClass, String superClass, ClassPool pool) throws NotFoundException, CannotCompileException {
	      curClass.setSuperclass(pool.get(superClass));
	      System.out.println("[DBG] set superclass: " + curClass.getSuperclass().getName() + //
	            ", subclass: " + curClass.getName());
	   }
}
