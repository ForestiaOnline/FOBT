/*
Create.java: this file is part of the FOBT program.

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
public class Create
{
// class variables

// constructor for Create class
public Create(byte[] ba, String dir, String name)
{
    try
    {
        // byte array to bytebuffer, load JBL
        ByteBuffer fbb = mkLEBB(ba);
        JBL bl = new JBL();

        // Set JBL BMP variables
        bl.setFileVars(dir,name);
        bl.getBitmapVars(ba);
        bl.setBitFmtOut("RGB565");
        // Strip the header off the image
        fbb.position(bl.pxStart);
        byte[] hdrless = bl.getImgBytes(fbb,0);
        // Forestia ABM format uses top-down scanlines
        byte[] revData = bl.reverseRows(hdrless);
        // Strip any padding off the pixels
        byte[] rawData = bl.stripPadding(revData);
        // Convert to RGB565 w/ padding
        byte[] pixels = bl.toStdRGB(rawData);
        // Prepare ABM data
        byte[] abmBytes = new byte[(16+pixels.length)];
        ByteBuffer abb = mkLEBB(abmBytes);
        abb.putInt(541934145);//ABM' '
        abb.putInt(3157553);//1.0[null]
        abb.putChar((char)bl.w);//width
        abb.putChar((char)bl.h);//height
        abb.putInt(0);//4 null bytes
        abb.put(pixels);
        // Set ABM file location and name
        File abm = new File(dir+name+".BMP");
        File abm_orig = new File(dir+name+".BMP"+".orig");
        // Make backup file if it doesn't already exist
        if(abm.exists() && !abm_orig.exists()) abm.renameTo(abm_orig);
        // Write ABM to file
        Files.write(abm.toPath(),abmBytes);
    }
    catch(Exception ex)
    {
        out.println("Error in (CM):");
        ex.printStackTrace(System.out);
    }
}

// Shorthand function to wrap a byte array in a little-endian bytebuffer
private static ByteBuffer mkLEBB(byte[] ba)
{
    return ByteBuffer.wrap(ba).order(ByteOrder.LITTLE_ENDIAN);
}
}
