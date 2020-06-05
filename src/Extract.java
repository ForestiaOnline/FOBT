/*
Extract.java: this file is part of the FOBT program.

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

Dev Notes:

Development Priority: MEDIUM
*/
public class Extract
{
// class variables
public static int w=0, h=0, bpp=0;

// constructor for Extract class
public Extract(byte[] ba, String dir, String name, int fLen)
{
    try
    {
        // byte array to bytebuffer, load JBL
        ByteBuffer fbb = ByteBuffer.wrap(ba).order(ByteOrder.LITTLE_ENDIAN);
        JBL bl = new JBL();

        getABMSpecs(fbb);
        bl.setFileVars(dir,name);
        bl.setBitmapVars(w,h,bpp);
        bl.set16BitFmtIn("RGB565");
        bl.setBitFmtOut("RGB24");

        // Get image data
        byte[] hdrless = bl.getImgBytes(fbb,(fLen-16));
        // Reverse the scanlines
        byte[] revData = bl.reverseRows(hdrless);
        // Strip any padding off the pixels
        byte[] rawData = bl.stripPadding(revData);
        // Convert to RGB24
        byte[] pixels = bl.toStdRGB(rawData);
        // Prepare the BMP
        byte[] bmp = bl.setBMP(pixels,false);
        // Write the new BMP into existence
        bl.makeBMP(bmp);
    }
    catch(Exception ex)
    {
        out.println("Error in (EM):");
        ex.printStackTrace(System.out);
    }
}

// Assign the BMP header info
private static void getABMSpecs(ByteBuffer bb)
{
    bb.getDouble();// skip ABM signature
    w = (int)bb.getChar();
    h = (int)bb.getChar();
    bb.getInt(); // skip 4 unknown bytes
    bpp = 16;
}
}
