package org.tomac.protocol.fix.fix50sp2;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.nio.ByteBuffer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.tomac.protocol.fix.FixGarbledException;
import org.tomac.protocol.fix.FixSessionException;
import org.tomac.protocol.fix.messaging.FixExecutionReport;
import org.tomac.protocol.fix.messaging.FixLogon;
import org.tomac.protocol.fix.messaging.FixMessage;
import org.tomac.protocol.fix.messaging.FixMessageInfo;
import org.tomac.protocol.fix.messaging.FixMessageParser;
import org.tomac.protocol.fix.messaging.FixNewOrderSingle;
import org.tomac.protocol.fix.messaging.FixTestRequest;
import org.tomac.protocol.fix.messaging.FixTradeCaptureReport;

public class TestFixContainers {
	FixMessageListenerTest listener;
	FixMessageParser parser;
	FixMessage outMsg;

	@Before
	public void setUp() throws Exception {
		listener = new FixMessageListenerTest();
		parser = new FixMessageParser();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testParseValidFixInMessages() {
		
		String[] fixMessages  = {
			"8=FIXT.1.1\u00019=105\u000135=A\u000149=MSI\u000156=TOMAC\u000134=1\u000152=20101007-06:00:07.732\u000150=STOM1\u000198=0\u0001108=60\u0001141=N\u00011137=9\u0001553=MSITS1\u0001554=MWAT\u000110=251\u0001",
			"8=FIXT.1.1\u00019=060\u000135=0\u000149=MSI\u000156=TOMAC\u000134=2\u000152=20101007-06:01:07.836\u000150=STOM1\u000110=191\u0001",
			"8=FIXT.1.1\u00019=153\u000135=D\u000149=MSI\u000156=TOMAC\u000134=230\u000152=20101007-15:43:36.290\u000150=STOM1\u000111=GEDVOO81\u000154=1\u000140=2\u000159=0\u000155=OMXS300K\u000138=15\u000144=1090\u000160=20101007-15:43:36.242\u0001432=20101007\u000110=010\u0001"
		};

		for (String s : fixMessages) {
			ByteBuffer buf = ByteBuffer.wrap(s.getBytes());

			try {
				parser.parse(buf, listener);
			} catch (FixSessionException e) {
				fail(e.getMessage() + " " + s);
			} catch (FixGarbledException e) {
				fail(e.getMessage() + " " + s);
			}

		}
	}

	@Test
	public void testParseValidStreamOfFixInMessages() {
		
		String[] fixMessages = { 
			"8=FIXT.1.1\u00019=0105\u000135=A\u000149=MSI\u000156=TOMAC\u000134=1\u000152=20101007-06:00:07.732\u000150=STOM1\u000198=0\u0001108=60\u0001141=N\u00011137=9\u0001553=MSITS1\u0001554=MWAT\u000110=043\u0001",
			"8=FIXT.1.1\u00019=0060\u000135=0\u000149=MSI\u000156=TOMAC\u000134=2\u000152=20101007-06:01:07.836\u000150=STOM1\u000110=239\u0001"
		};

		String ss = "";
		int count = 0;
		for (String s : fixMessages) {
			if (!s.contains(new String(FixMessageInfo.BEGINSTRING_VALUE)) ) continue; 
			count++;
			ss += s;
		}

		ByteBuffer buf = ByteBuffer.wrap(ss.getBytes());

		while(buf.hasRemaining()) {
			try {
				parser.parse(buf, listener);
			} catch (FixSessionException e) {
				fail(e.getMessage());
			} catch (FixGarbledException e) {
				fail(e.getMessage());
			}
			count--;
		}
		assertEquals(0, count);
	}
	
	
	@Test
	public void testParseValidSessionFixOutMessages() {
		
		String[] fixMessages = { 
				"8=FIXT.1.1\u00019=0105\u000135=A\u000149=MSI\u000156=TOMAC\u000134=1\u000152=20101007-06:00:07.732\u000150=STOM1\u000198=0\u0001108=60\u0001141=N\u00011137=9\u0001553=MSITS1\u0001554=MWAT\u000110=43\u0001",
				"8=FIXT.1.1\u00019=0060\u000135=0\u000149=MSI\u000156=TOMAC\u000134=2\u000152=20101007-06:01:07.836\u000150=STOM1\u000110=239\u0001",
				"8=FIXT.1.1\u00019=000094\u000135=1\u000149=TOMAC\u000156=MSI\u000134=218\u000152=20101007-09:39:36.644\u0001112=d9a1fd5f-e538-4a44-a552-fabc6c258167\u000110=049\u0001"
		};
		
		for (String s : fixMessages) {
			ByteBuffer buf = ByteBuffer.wrap(s.getBytes());
			
			try {
				parser.parse(buf, listener);
			} catch (FixSessionException e) {
				fail(e.getMessage());
			} catch (FixGarbledException e) {
				fail(e.getMessage());
			}
			
			outMsg = listener.getOutMsg();

			if (outMsg instanceof FixTestRequest) 
				assertTrue(((FixTestRequest)outMsg).toString().length() > 0);
			if (outMsg instanceof FixLogon) 
				assertTrue(((FixLogon)outMsg).toString().length() > 0);

			ByteBuffer out = ByteBuffer.allocate(1024);
			try {
				outMsg.encode(out);
			} catch (Exception e) {
				fail(e.getMessage());
			} 
			
		}
	}

	
	@Test
	public void testParseValidExecutionReportFixOutMessages() {
		
		String[] fixMessages = { 
				"8=FIXT.1.1\u00019=326\u000135=AE\u000149=TOMAC\u000156=MSI\u000134=255\u000157=STOM1\u000152=20101007-16:07:41.781\u0001571=191\u00011003=113:378\u0001487=0\u0001856=0\u0001828=0\u0001829=2\u0001855=1\u0001570=N\u000155=OMXS300K\u000148=21758906\u000122=M\u000132=10.0000000\u000132=10.0000000\u000131=1089.5000000\u000175=20101007\u0001715=20101007\u000160=20101007-16:07:41.000\u0001573=0\u0001552=1\u000154=2\u0001453=1\u0001448=MSI\u0001447=D\u0001452=1\u0001802=1\u0001523=SE\u0001803=32\u000137=51C9A9C1055581A4\u000110=035\u0001",
				"8=FIXT.1.1\u00019=202\u000135=8\u000149=TOMAC\u000156=MSI\u000134=217\u000152=20101007-09:38:59.908\u000137=NONE\u000111=GEDESZOT\u000117=6\u0001150=8\u000139=8\u0001103=0\u000155=[N/A]\u000154=1\u0001151=0\u000114=0\u00016=0\u000160=20101007-09:38:59.908\u000158=REJ- <-110023> : Illegal transaction at this time\u000110=099\u0001",
				"8=FIXT.1.1\u00019=235\u000135=8\u000149=TOMAC\u000156=MSI\u000134=252\u000157=STOM1\u000152=20101007-16:07:22.816\u000137=51C9A9C123448104\u000111=GEDWSAMA\u000117=174\u0001150=0\u000139=0\u000155=OMXS300K\u000148=21758906\u000122=M\u000154=1\u000138=10.0000000\u000140=2\u000144=1086.7500000\u000159=0\u0001151=10.0000000\u000114=0\u00016=0\u000160=20101007-16:07:22.816\u000110=107\u0001",
				"8=FIXT.1.1\u00019=454\u000135=8\u000149=TOMAC\u000156=MSI\u000134=364\u000157=STOM1\u000152=20101007-17:25:27.070\u000137=51C9A9C106677149\u000111=GEDYPUL9\u000117=855\u0001150=0\u000139=0\u000155=OMXC200K\u000148=7213980\u000122=M\u000154=1\u000138=11.0000000\u000140=1\u000159=3\u0001151=11.0000000\u000114=0\u00016=0\u000160=20101007-17:25:27.070\u000110=204\u00018=FIXT.1.1\u00019=000210\u000135=8\u000149=TOMAC\u000156=MSI\u000134=365\u000157=STOM1\u000152=20101007-17:25:27.070\u000137=51C9A9C100099119\u000111=GEDYPUL9\u000117=857\u0001150=4\u000139=4\u000155=OMXC200K\u000148=7213980\u000122=M\u000154=1\u000138=11.0000000\u000140=1\u000159=3\u0001151=0\u000114=0\u00016=0\u000160=20101007-17:25:27.070\u000110=155\u0001"
		};
		
		for (String s : fixMessages) {
			ByteBuffer buf = ByteBuffer.wrap(s.getBytes());
			
			try {
				parser.parse(buf, listener);
			} catch (FixSessionException e) {
				fail(e.getMessage());
			} catch (FixGarbledException e) {
				fail(e.getMessage());
			}
			
			outMsg = listener.getOutMsg();

			assertTrue (outMsg instanceof FixExecutionReport || outMsg instanceof FixTradeCaptureReport);

			ByteBuffer out = ByteBuffer.allocate(1024);
			try {
				outMsg.encode(out);
			} catch (Exception e) {
				fail(e.getMessage());
			} 
			
		}
		
	}

	@Test
	public void testParseValidRaquirdeTagMissignFixOutMessages() {
		listener = new FixMessageListenerTest() {
			@Override
			public void onFixExecutionReport(FixExecutionReport msg) {
				super.onFixExecutionReport(msg);
			}
		};
		
		String[] fixMessages = { 
				"8=FIXT.1.1\u00019=229\u000135=8\u000149=TOMAC\u000156=MSI\u000134=252\u000157=STOM1\u000152=20101007-16:07:22.816\u000137=23445660008104\u000111=TOMACSAMA\u000117=174\u0001150=0\u000139=0\u000155=OMXS300K\u000148=21758906\u000122=M\u000138=10.0000000\u000140=2\u000144=1086.7500000\u000159=0\u0001151=10.0000000\u000114=0\u00016=0\u000160=20101007-16:07:22.816\u000110=068\u0001", // \u000154=1
				"8=FIXT.1.1\u00019=188\u000135=8\u000149=TOMAC\u000156=MSI\u000134=217\u000152=20101007-09:38:59.908\u000137=NONE\u000111=GEDESZOT\u000117=6\u0001150=8\u000139=8\u0001103=0\u0001151=0\u000114=0\u00016=0\u000160=20101007-09:38:59.908\u000158=REJ- <-110023> : Illegal transaction at this time\u000110=122\u0001" // \u000155=[N/A]
		};
		
		for (String s : fixMessages) {

			ByteBuffer buf = ByteBuffer.wrap(s.getBytes());
			
			try {
				parser.parse(buf, listener);
				//fail("should be rejected");
			} catch (FixSessionException e) {

			} catch (FixGarbledException e) {
				fail(e.getMessage());
			}

		}
	}

	@Test
	public void testParseUnknownTagFixInMessages() {
		listener = new FixMessageListenerTest() {
			@Override
			public void onFixNewOrderSingle(FixNewOrderSingle msg) {
				super.onFixNewOrderSingle(msg);
			}
		};
		String[] fixMessages = { 
				"8=FIXT.1.1\u00019=0105\u000135=A\u000149=MSI\u000156=TOMAC\u000134=1\u000152=20101007-06:00:07.732\u000150=STOM1\u000198=0\u0001108=60\u0001141=N\u00011137=9\u0001553=MSITS1\u0001554=MWAT\u000110=43\u0001",
				"8=FIXT.1.1\u00019=0060\u000135=0\u000149=MSI\u000156=TOMAC\u000134=2\u000152=20101007-06:01:07.836\u000150=STOM1\u000110=239\u0001",
				"8=FIXT.1.1\u00019=177\u000135=D\u000149=MSI\u000156=TOMAC\u000134=230\u000152=20101007-15:43:36.290\u000150=STOM1\u000111=GEDVOO81\u000154=1\u000140=2\u000159=0\u000155=OMXS300K\u000138=15\u000144=1090\u000160=20101007-15:43:36.242\u000158=Limit,Stock,Day,Buy.\u0001432=20101007\u000110=190\u0001"
			
		};
		// 35=D contains unknown tag Text(58)
		
		for (String s : fixMessages) {

			ByteBuffer buf = ByteBuffer.wrap(s.getBytes());
			
			int startPos = buf.position();
			
			buf.position(startPos);
			
			try {
				parser.parse(buf, listener);
			} catch (FixSessionException e) {
				fail(e.getMessage());
			} catch (FixGarbledException e) {
				fail(e.getMessage());
			}
		}
	}

}
