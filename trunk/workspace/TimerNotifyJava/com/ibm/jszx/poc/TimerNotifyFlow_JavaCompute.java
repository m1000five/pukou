package com.ibm.jszx.poc;

import java.io.File;
import java.sql.Timestamp;

import com.ibm.broker.javacompute.MbJavaComputeNode;
import com.ibm.broker.plugin.*;

public class TimerNotifyFlow_JavaCompute extends MbJavaComputeNode {

	public void evaluate(MbMessageAssembly inAssembly) throws MbException {
		MbOutputTerminal out = getOutputTerminal("out");
		MbOutputTerminal alt = getOutputTerminal("alternate");
		Timestamp startTimeStamp ; 
		MbMessage inMessage = inAssembly.getMessage();
		String fileName ,monitorDir;
		// create new message
		MbMessage outMessage = new MbMessage(inMessage);
		MbMessageAssembly outAssembly = new MbMessageAssembly(inAssembly,
				outMessage);

		try {
			// ----------------------------------------------------------
			// Add user code below

			// End of user code
			// ----------------------------------------------------------

			// The following should only be changed
			// if not propagating message to the 'out' terminal
			
			startTimeStamp = (Timestamp)getUserDefinedAttribute("STARTTIME");
			fileName = (String)getUserDefinedAttribute("FILENAME");
			monitorDir = (String)getUserDefinedAttribute("MONITORDIR");
			long currentTime = System.currentTimeMillis();
			if(startTimeStamp.before(new Timestamp(currentTime))) // ˵��ʱ���Ѿ����ˣ���������
			{
				//��d:\txt\aa4.txt �ļ�������FileInput �ڵ���Ƶ�Ŀ¼
				File srcFile  = new File(fileName);
				if(srcFile.exists())
				{
					String destFilename = monitorDir+"/"+srcFile.getName();
					File destFile = new File(destFilename);
					if(destFile.exists())
					{
						destFile.delete();
					}
					if(srcFile.canWrite())
					{
						srcFile.renameTo(destFile);
					}
				}	
			}
			out.propagate(outAssembly);

		} finally {
			// clear the outMessage
			outMessage.clearMessage();
		}
	}

}
