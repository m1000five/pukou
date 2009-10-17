package com.ibm.js.poc.test; 

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import com.ibm.mq.*;
import com.ibm.mq.constants.MQConstants;

public class SendMsg {
	public static boolean writeQ(String qManager, String host, int port, String queue, byte cmd[]) {
		MQQueueManager qMgr = null;
		MQQueue myQueue = null;
		MQEnvironment.hostname = host;
		MQEnvironment.channel = "SYSTEM.DEF.SVRCONN";
		MQEnvironment.port = port;		
		try {
			qMgr = new MQQueueManager(qManager);
					
			int openOptions = MQConstants.MQOO_OUTPUT;
			myQueue = qMgr.accessQueue(queue, openOptions);
					
			MQMessage hello_world = new MQMessage();
			hello_world.persistence = MQC.MQPER_PERSISTENT;
			hello_world.format = MQC.MQFMT_NONE ;
			hello_world.write(cmd);
			//hello_world.writeUTF(cmd);
			//hello_world.writeString(cmd);
			MQPutMessageOptions pmo = new MQPutMessageOptions();
			myQueue.put(hello_world,pmo);
			System.out.println("Command has been queued");
			//System.out.println("Command has been queued");
		}
		catch (MQException ex){
			
			ex.printStackTrace();
			return false;
		}
		catch (IOException ex) {
			ex.printStackTrace();
			return false;
		}
		finally
		{
			
			if(myQueue!=null)
			{
				try
				{
					myQueue.close();
				}
				catch(Exception exc)
				{
					
				}
				myQueue = null;
			}
			if(qMgr!=null)
			{
				try
				{
					qMgr.disconnect();
				}
				catch(Exception exc)
				{
					
				}
				qMgr = null;
			}
		}
		return true;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO 自动生成方法存根
		try
		{
			if(args.length!=10)
			{
				System.out.println("命令格式不对，请使用如下格式:SendMsg agentAHostName agentAPort agentAQMName agentAName agentAMsgFileName agentBHostName agentBPort agentBQMName agentBName agentBMsgFileName");
				return ;
			
			}
			String agentAHostName = args[0];
			String agentAPort = args[1];
			String agentAQMName = args[2];
			String agentAName = args[3];
			File fileAgentA = new File(args[4]);
			if(!fileAgentA.exists())
			{
				System.out.println("文件名:"+args[4]+"不存在!");
				return ;
			}
			FileInputStream fisAgentA = new FileInputStream(fileAgentA);
			byte fileAgentAByte[] = new byte[(int)fileAgentA.length()];
			fisAgentA.read(fileAgentAByte);
			
			String agentBHostName = args[5];
			String agentBPort = args[6];
			String agentBQMName = args[7];
			String agentBName = args[8];
			File fileAgentB = new File(args[9]);
			if(!fileAgentB.exists())
			{
				System.out.println("文件名:"+args[9]+"不存在!");
				return ;
			}
			FileInputStream fisAgentB = new FileInputStream(fileAgentB);
			byte fileAgentBByte[] = new byte[(int)fileAgentB.length()];
			fisAgentB.read(fileAgentBByte);
			
			//writeQ(String qManager, String host, String chl, int port, String queue, byte cmd[])
			//开始打消息 writeQ
			writeQ(agentAQMName,agentAHostName,Integer.parseInt(agentAPort),"SYSTEM.FTE.COMMAND."+agentAName,fileAgentAByte);
			
			writeQ(agentBQMName,agentBHostName,Integer.parseInt(agentBPort),"SYSTEM.FTE.COMMAND."+agentBName,fileAgentBByte);
		}
		catch(Exception exc)
		{
			exc.printStackTrace();
		}

	}

}
