/*
Forestia Online BMP Tool (FOBT)
A tool to extract & create Forestia Online BMP files.

Copyright (C) 2020 Sean Stafford (a.k.a. PyroSamurai)

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.
*/
import java.io.*;
import java.nio.*;
import java.nio.file.*;
import static java.lang.System.out;
/**
Class Description:
The Main class is the 'main' class of the FOBT program (this is obvious).
It's essentially the interface for all functionality of the program.

Dev Notes:
The original code for this program took me 14 hours to write. This includes the
time it took to reverse the proprietary format. If that doesn't emphasize how
useless these types of security measure are for you, I don't know what will.

Development Priority: HIGH
*/
public class Main
{
// Functions ordered by importance & thus more likely to be edited
// class variables
public static char mode;
public static int argsLen=0, fLen=0;
public static String dir, name, fs=File.separator;
public static File abmFile;

// Main function (keep clean)
public static void main(String[] args)
{
    argsLen = args.length;
    argCheck(args);
    for(int i=1; i < argsLen; i++)
    {
        abmFile = new File(args[i]);
        if(abmFile.exists()==false) argErrors(3);
        if(abmFile.isDirectory()==true) argErrors(4);
        setFileVars(abmFile);
        byte[] fobBA = file2BA(abmFile);
        switch(mode)
        {
        case 'c':
            Create optC = new Create(fobBA,dir,name);
            break;
        default:
            Extract optE = new Extract(fobBA,dir,name,fLen);
            break;
        }
    }
}

// Set basic file info
private static void setFileVars(File file)
{
    // Get the file name
    name = file.getName();
    // Get file extension's first char location
    int extIdx = name.lastIndexOf('.');
    // Remove the extension from the file name
    name = name.substring(0,extIdx);
    // Assign the directory where we will extract the image to
    dir = file.getParent()+fs;
    if(dir.equals("null"+fs)) dir=System.getProperty("user.dir")+fs;
    // Get the file size
    fLen = (int)file.length();
}

// Determines validity of cmd-line args & returns the resulting case number
// This exists as a function because it would make main() ugly if it didn't.
private static void argCheck(String[] args)
{
    // check for existence of mode argument of the correct length
    if(argsLen!=0 && args[0].length()==1)
    {
        // assign first argument to mode
        mode = args[0].charAt(0);
        // This 'if' tree checks for valid mode arg and correct # of args
        // for the given mode. Invokes helpful error messages on failure.
        if((mode=='e' || mode=='c'))
        {
            if(argsLen < 2) argErrors(2);
        }
        else
        {
            argErrors(1);
        }
    }
    else
    {
        argErrors(0);
    }
}

// Invalid command-line arguments responses
private static void argErrors(int argErrorNum)
{
    String errMsg0,errMsg1,errMsg2,errMsg3,errMsg4,errMsg5,errMsg6;
    errMsg0 ="Error: Mode argument is too long or missing";
    errMsg1 ="Error: Invalid Mode argument";
    errMsg2 ="Error: Incorrect number of arguments for this Mode";
    errMsg3 ="Error: File does not exist. Nice try.";
    errMsg4 ="Error: File is a Directory. Try being more specific.";
    switch(argErrorNum)
    {
    case 0:
        out.println(errMsg0);
        break;
    case 1:
        out.println(errMsg1);
        break;
    case 2:
        out.println(errMsg2);
        break;
    case 3:
        out.println(errMsg3);
        break;
    case 4:
        out.println(errMsg4);
        break;
    default:
        out.println("Unknown Error");
        break;
    }
    usage();
    System.exit(1);
}

// Standard usage output, explaining available modes & required arguments
private static void usage()
{
    String cr, use, col, bdr, ope, opc, ex;
    // You are not allowed to remove this copyright notice or its output
    cr ="Forestia Online BMP Tool (FOBT)\n"+
        "https://github.com/ForestiaOnline/FOBT\n"+
        "Copyright (C) 2020 Sean Stafford (a.k.a. PyroSamurai)\n"+
        "License: GPLv3+\n\n";

    use="Usage: java -jar FOBT.jar {mode} {/path/file.BMP} {etc}\n";
    col="| Mode | Arguments | Description                      |\n";
    bdr="=======================================================\n";
    ope="|  e   | [file(s)] | Extract BMP from ABM files       |\n";
    opc="|  c   | [file(s)] | Create ABM files from BMP        |\n";

    ex ="Example: java -jar FOBT.jar c ../ex/path/all.bmp\n";

    // Actual output function
    out.println("\n"+cr+use+bdr+col+bdr+ope+opc+bdr+ex);
}

// An anti-duplication + better readability function
private static byte[] file2BA(File file)
{
    byte[] ba = new byte[(int)file.length()];
    try
    {
        ba = Files.readAllBytes(file.toPath());
    }
    catch(Exception ex)
    {
        out.println("Error in (file2BA):");
        ex.printStackTrace(System.out);
    }
    return ba;
}
}
