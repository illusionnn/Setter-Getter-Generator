package com.chinmay.bhide.util;
import java.lang.reflect.*;
import java.io.*;
public class SetterGetterGenerator{
	public static void main(String arg[]){
		if(arg.length != 1 && arg.length != 2){
			System.out.println("usage : java classpath  path_to_jar_file;. com.chinmay.bhide.util .SetterGetterGenerator class_name");
			return;
		}
		if(arg.length == 2){
			if(arg[1].equalsIgnoreCase("constructor = true")==false&&arg[1].equalsIgnoreCase("constructor = false")==false){
				System.out.println("usage : java classpath  path_to_jar_file;. com.chinmay.bhide.util .SetterGetterGenerator class_name");
			 	return;
			}
		}
		String className = arg[0];
		try{
			Class c = Class.forName(className);
			Field fields[]  = c.getDeclaredFields();
			Field field;
			TMList<String> list = new TMArrayList<String>();
			String setterName;
			String getterName;
			String tmp;
			String fieldName;
			String line;
			Class fieldType;
			if(arg.length==1||(arg.length == 2 && arg[1].equalsIgnoreCase("constructor = true"))){
				line = "public "+c.getSimpleName()+"(){";
				list.add(line);
				for(int e = 0 ; e < fields.length ; e++){
					field = fields[e];
					line = "this."+field.getName()+" = "+getDefaultValue(field.getType())+";";
					list.add(line);
				}
				list.add("}");
			}
			for(int e = 0 ; e < fields.length ; e++){
				field = fields[e];
				fieldName = field.getName();
				fieldType = field.getType();
				if(fieldName.charAt(0) >= 97 && fieldName.charAt(0) <=122){
					tmp = fieldName.substring(0,1).toUpperCase()+fieldName.substring(1);
				}
				else{
					tmp = fieldName;
				}
				setterName = "set"+tmp;
				getterName = "get"+tmp;
				line ="public void "+setterName+"("+fieldType.getName()+" "+fieldName+"){";
				list.add(line);
				line ="this."+fieldName+" = "+fieldName+";";
				list.add(line);
				list.add("}");
				line ="public "+fieldType.getName()+" "+getterName+"(){";
				list.add(line);
				line ="return this."+fieldName+";";
				list.add(line);
				list.add("}");
			}
			File file = new File("tmp.tmp");
			if(file.exists()) file.delete();
			RandomAccessFile randomAccessFile = new RandomAccessFile(file,"rw");
			TMIterator<String> iterator = list.iterator();
			while(iterator.hasNext()){
				line = iterator.next();
				randomAccessFile.writeBytes(line+"\r\n");
			}
			randomAccessFile.close();
			System.out.println("setter/getter for : "+c.getName()+" is generated in filed named as tmp.tmp");
		}catch(ClassNotFoundException cnf){
			System.out.println("unable to load class, classpath missing");
		}
		catch(IOException ioe){
			System.out.println("ioe.getMessage()");
		}
	}
	private static String getDefaultValue(Class c){
		String className = c.getName();
		if(className.equals("java.lang.Long")||className.equals("long")) return "0";
		if(className.equals("java.lang.Integer")||className.equals("int")) return "0";
		if(className.equals("java.lang.Short")||className.equals("short")) return "0";
		if(className.equals("java.lang.Byte")||className.equals("byte")) return "0";
		if(className.equals("java.lang.Double")||className.equals("double")) return "0.0";
		if(className.equals("java.lang.Float")||className.equals("float")) return "0.0f";
		if(className.equals("java.lang.Character")||className.equals("char")) return "' '";
		if(className.equals("java.lang.Boolean")||className.equals("boolean")) return "false";
		if(className.equals("java.lang.String")) return "\"\"";
		return "null";		
	}
}