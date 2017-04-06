package org.iii.nmi.air.test.web;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputMethodEvent;
import java.awt.event.InputMethodListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.iii.nmi.air.crc.CRC16;

public class EmsMainAPP extends JFrame
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8570962107631047836L;

	private String ip;

	private int port;

	LinkedList<String> linkedList = new LinkedList<String>();

	private static int count = 0;

	private static final int INIT_WIDTH = 620;

	private static final int INIT_HEIGHT = 600;

	private Container container;

	private JTextArea textArea;

	private JComboBox readWriteChoice;

	private JComboBox byteChoice;

	private JComboBox masterIP;

	private JComboBox powerID;

	private JButton XX10H, XX11H, XX12H, XX13H, XX14H, XX15H, XX16H, XX17H,
			XX18H, XX1AH, XX1BH;

	private String readWriteByte;

	private String byteStart;

	private String masterIp;

	private String powerId;

	private JTextField setPointField;

	private String text;

	public EmsMainAPP()
	{
		configuration();
		init();
		consoleManagement();
	}

	public static synchronized String getCmdSn()
	{
		count = ++count;

		String cmdSn = Integer.toString(count);

		if(cmdSn.equals("65535"))
		{
			count = 0;
			return getCmdSn();
		}
		if(cmdSn.length() == 1)
			return "0000" + cmdSn;

		if(cmdSn.length() == 2)
			return "000" + cmdSn;

		if(cmdSn.length() == 3)
			return "00" + cmdSn;

		if(cmdSn.length() == 4)
			return "0" + cmdSn;

		if(cmdSn.length() == 5)
			return cmdSn;

		return null;
	}

	private void configuration()
	{
		Properties properties = new Properties();
		FileInputStream is = null;

		try
		{
			is = new FileInputStream(new File("conf/emsgw.properties"));
			properties.load(is);

			this.ip = properties.getProperty("socketEmsIp");
			this.port = Integer.parseInt(properties.getProperty("socketEmsPort"));
		}
		catch(FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch(IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			try
			{
				is.close();
			}
			catch(IOException e)
			{

				e.printStackTrace();
			}
		}

	}

	private void init()
	{

		setLookFeel();
		setFrameDimensize();
		setSubUIComponent();
		setEventListener();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		TaskInf task = new TaskImpl();
		new PutThread(task, linkedList).start();
		new GetThread(task, ip, port).start();

	}

	private String makeCommand(String command)
	{
		String[] datas = command.split(";");

		byte[] bytes = new byte[datas.length];

		for(int k = 0; k < datas.length; k++)
		{

			bytes[k] = (byte) Integer.parseInt(datas[k], 16);
		}

		String crcStr = Integer.toHexString(CRC16.crc16(bytes));
		int count = crcStr.length() - 4;

		String crc16H = crcStr.substring(count, count + 2);
		String crc16L = crcStr.substring(count + 2, count + 4);

		String reqCmd = command + crc16L + ";" + crc16H + ";";

		return reqCmd;
	}

	private void setEventListener()
	{
		XX10H.addActionListener(new ActionListener()
		{

			public void actionPerformed(ActionEvent e)
			{
				String command = masterIp + ";" + readWriteByte + ";" + powerId + ";" + "10" + ";00;" + byteStart + ";";

				String reqCmd = makeCommand(command);

				linkedList.addLast(reqCmd);

				textArea.append(reqCmd + "\n");
				textArea.setCaretPosition(textArea.getDocument().getLength());

			}

		});

		XX11H.addActionListener(new ActionListener()
		{

			public void actionPerformed(ActionEvent e)
			{

				String command = masterIp + ";" + readWriteByte + ";" + powerId + ";" + "11" + ";00;" + byteStart + ";";

				String reqCmd = makeCommand(command);

				linkedList.addLast(reqCmd);

				textArea.append(reqCmd + "\n");
				textArea.setCaretPosition(textArea.getDocument().getLength());

			}

		});
		XX12H.addActionListener(new ActionListener()
		{

			public void actionPerformed(ActionEvent e)
			{
				String command = masterIp + ";" + readWriteByte + ";" + powerId + ";" + "12" + ";00;" + byteStart + ";";

				String reqCmd = makeCommand(command);

				linkedList.addLast(reqCmd);

				textArea.append(reqCmd + "\n");
				textArea.setCaretPosition(textArea.getDocument().getLength());

			}

		});

		XX13H.addActionListener(new ActionListener()
		{

			public void actionPerformed(ActionEvent e)
			{
				String command = masterIp + ";" + readWriteByte + ";" + powerId + ";" + "13" + ";00;" + byteStart + ";";

				String reqCmd = makeCommand(command);

				linkedList.addLast(reqCmd);

				textArea.append(reqCmd + "\n");
				textArea.setCaretPosition(textArea.getDocument().getLength());

			}

		});

		XX14H.addActionListener(new ActionListener()
		{

			public void actionPerformed(ActionEvent e)
			{

				String command = masterIp + ";" + readWriteByte + ";" + powerId + ";" + "14" + ";00;" + byteStart + ";";

				String reqCmd = makeCommand(command);

				linkedList.addLast(reqCmd);

				textArea.append(reqCmd + "\n");
				textArea.setCaretPosition(textArea.getDocument().getLength());

			}

		});

		XX15H.addActionListener(new ActionListener()
		{

			public void actionPerformed(ActionEvent e)
			{
				String command = masterIp + ";" + readWriteByte + ";" + powerId + ";" + "15" + ";00;" + byteStart + ";";

				String reqCmd = makeCommand(command);

				linkedList.addLast(reqCmd);

				textArea.append(reqCmd + "\n");
				textArea.setCaretPosition(textArea.getDocument().getLength());

			}

		});

		XX16H.addActionListener(new ActionListener()
		{

			public void actionPerformed(ActionEvent e)
			{
				String command = masterIp + ";" + readWriteByte + ";" + powerId + ";" + "16" + ";00;" + byteStart + ";";

				String reqCmd = makeCommand(command);

				linkedList.addLast(reqCmd);

				textArea.append(reqCmd + "\n");
				textArea.setCaretPosition(textArea.getDocument().getLength());

			}

		});

		XX17H.addActionListener(new ActionListener()
		{

			public void actionPerformed(ActionEvent e)
			{
				String command = masterIp + ";" + readWriteByte + ";" + powerId + ";" + "17" + ";00;" + byteStart + ";";

				String reqCmd = makeCommand(command);

				linkedList.addLast(reqCmd);

				textArea.append(reqCmd + "\n");
				textArea.setCaretPosition(textArea.getDocument().getLength());

			}

		});

		XX18H.addActionListener(new ActionListener()
		{

			public void actionPerformed(ActionEvent e)
			{
				String command = masterIp + ";" + readWriteByte + ";" + powerId + ";" + "18" + ";00;" + byteStart + ";";

				String reqCmd = makeCommand(command);

				linkedList.addLast(reqCmd);

				textArea.append(reqCmd + "\n");
				textArea.setCaretPosition(textArea.getDocument().getLength());

			}

		});

		XX1AH.addActionListener(new ActionListener()
		{

			public void actionPerformed(ActionEvent e)
			{
				String command = masterIp + ";" + readWriteByte + ";" + powerId + ";" + "1A" + ";00;" + byteStart + ";";

				String reqCmd = makeCommand(command);

				linkedList.addLast(reqCmd);

				textArea.append(reqCmd + "\n");
				textArea.setCaretPosition(textArea.getDocument().getLength());

			}

		});

		XX1BH.addActionListener(new ActionListener()
		{

			public void actionPerformed(ActionEvent e)
			{
				String command = null;
				if(readWriteByte.equals("03"))
				{
					command = masterIp + ";" + readWriteByte + ";" + powerId + ";" + "1B" + ";00;" + byteStart + ";";
				}
				else
				{
					Integer point = Integer.parseInt(text) * 2;
					String setpoint = Integer.toHexString(point);
					command = masterIp + ";" + readWriteByte + ";" + powerId + ";" + "1B" + ";00;" + setpoint + ";";
				}

				String reqCmd = makeCommand(command);

				linkedList.addLast(reqCmd);

				textArea.append(reqCmd + "\n");
				textArea.setCaretPosition(textArea.getDocument().getLength());

			}

		});

	}

	private void setSubUIComponent()
	{
		container = getContentPane();
		container.setLayout(new BorderLayout());
		container.add(getPanel(), BorderLayout.CENTER);

	}

	private Component getPanelTextArea()
	{
		JPanel textAreaPanel = new JPanel();
		textAreaPanel.setBorder(new OvalBorder());
		textAreaPanel.setLayout(new GridLayout(1, 1));
		textArea = new JTextArea();
		textArea.setBackground(Color.black);
		textArea.setForeground(Color.white);
		JScrollPane scrollPanel = new JScrollPane(textArea, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		textAreaPanel.add(scrollPanel);
		textAreaPanel.setBounds(0, 350, 610, 210);
		return textAreaPanel;
	}

	private Component getPanel()
	{
		JPanel pan = new JPanel();
		pan.setLayout(null);

		XX10H = new JButton("XX10H");
		XX10H.setBounds(10, 10, 80, 30);
		XX10H.setBorder(new OvalBorder());
		pan.add(XX10H);

		XX11H = new JButton("XX11H");
		XX11H.setBounds(95, 10, 80, 30);
		XX11H.setBorder(new OvalBorder());
		pan.add(XX11H);

		XX12H = new JButton("XX12H");
		XX12H.setBounds(180, 10, 80, 30);
		XX12H.setBorder(new OvalBorder());
		pan.add(XX12H);

		XX13H = new JButton("XX13H");
		XX13H.setBounds(265, 10, 80, 30);
		XX13H.setBorder(new OvalBorder());
		pan.add(XX13H);

		XX14H = new JButton("XX14H");
		XX14H.setBounds(350, 10, 80, 30);
		XX14H.setBorder(new OvalBorder());
		pan.add(XX14H);

		XX15H = new JButton("XX15H");
		XX15H.setBounds(435, 10, 80, 30);
		XX15H.setBorder(new OvalBorder());
		pan.add(XX15H);

		XX16H = new JButton("XX16H");
		XX16H.setBounds(10, 60, 80, 30);
		XX16H.setBorder(new OvalBorder());
		pan.add(XX16H);

		XX17H = new JButton("XX17H");
		XX17H.setBounds(95, 60, 80, 30);
		XX17H.setBorder(new OvalBorder());
		pan.add(XX17H);

		XX18H = new JButton("XX18H");
		XX18H.setBounds(180, 60, 80, 30);
		XX18H.setBorder(new OvalBorder());
		pan.add(XX18H);

		XX1AH = new JButton("XX1AH");
		XX1AH.setBounds(265, 60, 80, 30);
		XX1AH.setBorder(new OvalBorder());
		pan.add(XX1AH);

		XX1BH = new JButton("XX1BH");
		XX1BH.setBounds(350, 60, 80, 30);
		XX1BH.setBorder(new OvalBorder());
		pan.add(XX1BH);

		JLabel masterIPLab = new JLabel("MasterIP");
		masterIPLab.setBounds(10, 110, 80, 30);
		pan.add(masterIPLab);

		masterIP = new JComboBox();
		ActionListener lst = new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				masterIp = (String) masterIP.getSelectedItem();

			}
		};
		masterIP.addActionListener(lst);

		masterIP.setBounds(95, 110, 80, 30);
		masterIP.addItem("01");
		masterIP.addItem("02");
		masterIP.addItem("03");
		masterIP.addItem("04");
		masterIP.addItem("05");
		masterIP.addItem("06");
		pan.add(masterIP);

		JLabel readWrite = new JLabel("ReadWrite");
		readWrite.setBounds(10, 160, 80, 30);
		pan.add(readWrite);

		readWriteChoice = new JComboBox();
		ActionListener readWriteChoiceList = new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				readWriteByte = (String) readWriteChoice.getSelectedItem();

			}
		};
		readWriteChoice.addActionListener(readWriteChoiceList);
		readWriteChoice.setBounds(95, 160, 80, 30);
		readWriteChoice.addItem("03");
		readWriteChoice.addItem("06");
		pan.add(readWriteChoice);

		JLabel SetPoint = new JLabel("SetPoint");
		SetPoint.setBounds(10, 210, 80, 30);
		pan.add(SetPoint);

		setPointField = new JTextField();
		ActionListener setPointFieldList = new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				text = setPointField.getText();

			}

		};
		setPointField.addActionListener(setPointFieldList);
		setPointField.setBounds(95, 210, 80, 30);
		pan.add(setPointField);
		JLabel powerIDLab = new JLabel("PowerID");
		powerIDLab.setBounds(180, 110, 80, 30);
		pan.add(powerIDLab);

		powerID = new JComboBox();
		ActionListener powerIDlist = new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				powerId = (String) powerID.getSelectedItem();

			}
		};
		powerID.addActionListener(powerIDlist);
		powerID.setBounds(265, 110, 80, 30);
		powerID.addItem("01");
		powerID.addItem("02");
		powerID.addItem("03");
		powerID.addItem("04");
		powerID.addItem("05");
		powerID.addItem("06");
		powerID.addItem("07");
		powerID.addItem("08");
		powerID.addItem("09");
		powerID.addItem("0a");
		powerID.addItem("0b");
		powerID.addItem("0c");
		powerID.addItem("0d");
		powerID.addItem("0e");
		powerID.addItem("0f");
		powerID.addItem("10");
		powerID.addItem("11");
		powerID.addItem("12");
		powerID.addItem("13");
		powerID.addItem("14");
		powerID.addItem("15");
		powerID.addItem("16");
		pan.add(powerID);

		JLabel ReadCharacter = new JLabel("ReadChar");
		ReadCharacter.setBounds(180, 160, 80, 30);
		pan.add(ReadCharacter);

		byteChoice = new JComboBox();
		ActionListener byteChoicelist = new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				byteStart = (String) byteChoice.getSelectedItem();

			}
		};
		byteChoice.addActionListener(byteChoicelist);
		byteChoice.setBounds(265, 160, 80, 30);
		byteChoice.addItem("00");
		byteChoice.addItem("01");
		byteChoice.addItem("02");
		byteChoice.addItem("03");
		byteChoice.addItem("04");
		byteChoice.addItem("05");
		byteChoice.addItem("06");
		byteChoice.addItem("07");
		byteChoice.addItem("08");
		byteChoice.addItem("09");
		byteChoice.addItem("0a");
		byteChoice.addItem("0b");
		byteChoice.addItem("0c");

		pan.add(byteChoice);

		pan.add(getPanelTextArea());

		return pan;
	}

	private void setFrameDimensize()
	{
		Dimension dimensize = Toolkit.getDefaultToolkit().getScreenSize();
		int xPos = (dimensize.width - INIT_WIDTH) / 2;
		int yPos = (dimensize.height - INIT_HEIGHT) / 2;

		int wPos = INIT_WIDTH;
		int hPos = INIT_HEIGHT;

		setBounds(xPos, yPos, wPos, hPos);

	}

	private void setLookFeel()
	{
		try
		{
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
			SwingUtilities.updateComponentTreeUI(this);
		}
		catch(Exception e)
		{

		}

	}

	private void consoleManagement()
	{

		BufferedReader buf = new BufferedReader(new InputStreamReader(System.in));
		String adminMessage;
		try
		{
			while((adminMessage = buf.readLine()) != null)
			{
				if(adminMessage.equalsIgnoreCase("A1"))
				{

					String command = "02;A01;" + getCmdSn() + ";00158d0000071ae9;00158d00000836ba;FFFFFF;00090;";
					// String command = "02;A01;" + getCmdSn() +
					// ";1234567890123465;9876543210123499;FFFFFF;00045;";
					linkedList.addLast(command);
				}
				else if(adminMessage.equalsIgnoreCase("A2"))
				{

					String command = "02;A02;" + getCmdSn() + ";0000000000000000;0000000000000000;FFFFFF;00045;";
					// String command = "2;A02;" + getCmdSn() +
					// ";1234567890123456;9876543210123458;FFFFFF;00045;";
					linkedList.addLast(command);
				}
				else if(adminMessage.equalsIgnoreCase("A3"))
				{
					String command = "02;A03;" + getCmdSn() + ";0000000000000000;0000000000000000;FFFFFF;00045;";
					// String command = "2;A03;" + getCmdSn() +
					// ";1234567890123456;9876543210123458;FFFFFF;00045;";
					linkedList.addLast(command);
				}
				else if(adminMessage.equalsIgnoreCase("A4"))
				{
					String command = "02;A04;" + getCmdSn() + ";0000000000000000;0000000000000000;FFFFFF;00045;";
					// String command = "2;A04;" + getCmdSn() +
					// ";1234567890123456;9876543210123458;FFFFFF;00060;";
					linkedList.addLast(command);
				}
				else if(adminMessage.equalsIgnoreCase("A5"))
				{

					String command = "02;A05;" + getCmdSn() + ";0000000000000000;0000000000000000;FFFFFF;00045;";
					// String command = "2;A05;" + getCmdSn() +
					// ";1234567890123456;9876543210123458;FFFFFF;00060;";
					linkedList.addLast(command);
				}
				else if(adminMessage.equalsIgnoreCase("A6"))
				{
					String command = "02;A06;" + getCmdSn() + ";0000000000000000;0000000000000000;FFFFFF;00045;";
					// String command = "2;A06;" + getCmdSn() +
					// ";1234567890123456;9876543210123458;FFFFFF;00060;";
					linkedList.addLast(command);

				}
				else if(adminMessage.equalsIgnoreCase("A7"))
				{
					String command = "02;A07;" + getCmdSn() + ";0000000000000000;0000000000000000;FFFFFF;00045;";
					// String command = "2;A07;" + getCmdSn() +
					// ";1234567890123456;9876543210123458;FFFFFF;00060;";
					linkedList.addLast(command);

				}
				else if(adminMessage.equalsIgnoreCase("A8"))
				{
					String command = "02;A08;" + getCmdSn() + ";0000000000000000;0000000000000000;FFFFFF;00045;";
					// String command = "2;A08;" + getCmdSn() +
					// ";1234567890123456;9876543210123458;FFFFFF;00060;";
					linkedList.addLast(command);

				}
				else if(adminMessage.equalsIgnoreCase("A9"))
				{
					String command = "02;A09;" + getCmdSn() + ";0000000000000000;0000000000000000;FFFFFF;00045;";
					// String command = "2;A09;" + getCmdSn() +
					// ";1234567890123456;9876543210123458;FFFFFF;00060;";
					linkedList.addLast(command);

				}
				else if(adminMessage.equalsIgnoreCase("A10"))
				{
					String command = "02;A10;" + getCmdSn() + ";0000000000000000;0000000000000000;FFFFFF;00045;";
					// String command = "2;A10;" + getCmdSn() +
					// ";1234567890123456;9876543210123458;FFFFFF;00060;";
					linkedList.addLast(command);

				}
				else if(adminMessage.equalsIgnoreCase("A11"))
				{
					String command = "02;A11;" + getCmdSn() + ";0000000000000000;0000000000000000;FFFFFF;00045;";
					// String command = "2;A11;" + getCmdSn() +
					// ";1234567890123456;9876543210123458;FFFFFF;00060;";
					linkedList.addLast(command);

				}
				else if(adminMessage.equalsIgnoreCase("A12"))
				{
					String command = "02;A12;" + getCmdSn() + ";0000000000000000;0000000000000000;FFFFFF;00045";
					// String command = "2;A12;" + getCmdSn() +
					// ";1234567890123456;9876543210123458;FFFFFF;00060;";
					linkedList.addLast(command);

				}
				else if(adminMessage.equalsIgnoreCase("A13"))
				{
					String command = "02;A13;" + getCmdSn() + ";0000000000000000;0000000000000000;FFFFFF;00045;";
					// String command = "2;A13;" + getCmdSn() +
					// ";1234567890123456;9876543210123458;FFFFFF;00060;";
					linkedList.addLast(command);

				}
				else if(adminMessage.equalsIgnoreCase("A14"))
				{
					String command = "02;A14;" + getCmdSn() + ";0000000000000000;0000000000000000;FFFFFF;00045;";
					// String command = "2;A14;" + getCmdSn() +
					// ";1234567890123456;9876543210123458;FFFFFF;00060;";
					linkedList.addLast(command);

				}
				else if(adminMessage.equalsIgnoreCase("A15"))
				{
					String command = "02;A15;" + getCmdSn() + ";0000000000000000;0000000000000000;FFFFFF;00045;";
					// String command = "2;A15;" + getCmdSn() +
					// ";1234567890123456;9876543210123458;FFFFFF;00060;";
					linkedList.addLast(command);

				}
				else if(adminMessage.equalsIgnoreCase("B1"))
				{
					String command = "02;B01;" + getCmdSn() + ";0000000000000000;0000000000000000;FFFFFF;00045;";
					// String command = "2;B01;" + getCmdSn() +
					// ";1234567890123456;9876543210123458;FFFFFF;00060;";
					linkedList.addLast(command);

				}
				else if(adminMessage.equalsIgnoreCase("B2"))
				{
					String command = "02;B02;" + getCmdSn() + ";0000000000000000;0000000000000000;FFFFFF;00045;";
					// String command = "2;B02;" + getCmdSn() +
					// ";1234567890123456;9876543210123458;FFFFFF;00060;";
					linkedList.addLast(command);

				}
				else if(adminMessage.equalsIgnoreCase("B3"))
				{
					String command = "02;B03;" + getCmdSn() + ";0000000000000000;0000000000000000;FFFFFF;00045;";
					// String command = "2;B03;" + getCmdSn() +
					// ";1234567890123456;9876543210123458;FFFFFF;00060;";
					linkedList.addLast(command);

				}
				else if(adminMessage.equalsIgnoreCase("B4"))
				{
					String command = "02;B04;" + getCmdSn() + ";0000000000000000;0000000000000000;FFFFFF;00045;";
					// String command = "2;B04;" + getCmdSn() +
					// ";1234567890123456;9876543210123458;FFFFFF;00060;";
					linkedList.addLast(command);

				}
				else if(adminMessage.equalsIgnoreCase("B5"))
				{
					String command = "01;B05;" + getCmdSn() + ";0000000000000000;FFFFFF;00045;";
					// String command = "1;B05;" + getCmdSn() +
					// ";1234567890123456;FFFFFF;00060;";
					linkedList.addLast(command);

				}
				else if(adminMessage.equalsIgnoreCase("B6"))
				{
					String command = "01;B06;" + getCmdSn() + ";0000000000000000;FFFFFF;00045;";
					// String command = "1;B06;" + getCmdSn() +
					// ";1234567890123456;FFFFFF;00060;";
					linkedList.addLast(command);

				}
				else if(adminMessage.equalsIgnoreCase("C1"))
				{

					String command = "02;C01;" + getCmdSn() + ";0000000000000000;0000000000000000;FFFFFF;00045;";
					// String command = "2;C01;" + getCmdSn() +
					// ";1234567890123456;9876543210123458;FFFFFF;00060;";
					linkedList.addLast(command);
				}
				else if(adminMessage.equalsIgnoreCase("C2"))
				{
					String command = "02;C02;" + getCmdSn() + ";0000000000000000;0000000000000000;000180;00045;";
					// String command = "2;C02;" + getCmdSn() +
					// ";1234567890123456;9876543210123458;000030;00060;";
					linkedList.addLast(command);

				}
				else if(adminMessage.equalsIgnoreCase("C3"))
				{
					String command = "02;C03;" + getCmdSn() + ";0000000000000000;0000000000000000;FFFFFF;00045;";
					// String command = "2;C03;" + getCmdSn() +
					// ";1234567890123456;9876543210123458;FFFFFF;00060;";
					linkedList.addLast(command);

				}
				else if(adminMessage.equalsIgnoreCase("C4"))
				{

					String command = "02;C04;" + getCmdSn() + ";0000000000000000;0000000000000000;000180;00045;";
					// String command = "2;C04;" + getCmdSn() +
					// ";1234567890123456;9876543210123458;000030;00060;";
					linkedList.addLast(command);
				}
				else if(adminMessage.equalsIgnoreCase("C5"))
				{
					String command = "01;C05;" + getCmdSn() + ";0000000000000000;FFFFFF;00045;";
					// String command = "1;C05;" + getCmdSn() +
					// ";1234567890123456;FFFFFF;00060;";
					linkedList.addLast(command);

				}
				else if(adminMessage.equalsIgnoreCase("C6"))
				{
					String command = "01;C06;" + getCmdSn() + ";0000000000000000;000180;00045;";
					// String command = "1;C06;" + getCmdSn() +
					// ";1234567890123456;000030;00060;";
					linkedList.addLast(command);

				}
				else if(adminMessage.equalsIgnoreCase("C7"))
				{
					String command = "01;C07;" + getCmdSn() + ";0000000000000000;123456;00045;";
					// String command = "1;C07;" + getCmdSn() +
					// ";1234567890123456;123456;00060;";
					linkedList.addLast(command);

				}
				else if(adminMessage.equalsIgnoreCase("C8"))
				{
					String command = "01;C08;" + getCmdSn() + ";0000000000000000;8200;00045;";
					// String command = "1;C08;" + getCmdSn() +
					// ";1234567890123456;8200;00060;";
					linkedList.addLast(command);

				}
				else if(adminMessage.equalsIgnoreCase("C9"))
				{
					String command = "01;C09;" + getCmdSn() + ";0000000000000000;140.92.71.211;00045;";
					// String command = "1;C09;" + getCmdSn() +
					// ";1234567890123456;140.92.71.211;00060;";
					linkedList.addLast(command);

				}
				else if(adminMessage.equalsIgnoreCase("C10"))
				{

					String command = "02;C10;" + getCmdSn() + ";0000000000000000;0000000000000000;FFFFFF;00045;";
					// String command = "2;C10;" + getCmdSn() +
					// ";1234567890123456;9876543210123458;FFFFFF;00060;";
					linkedList.addLast(command);
				}
				else if(adminMessage.equalsIgnoreCase("C11"))
				{
					String command = "02;C11;" + getCmdSn() + ";0000000000000000;0000000000000000;000150;00045;";
					// String command = "2;C11;" + getCmdSn() +
					// ";1234567890123456;9876543210123458;000150;00060;";
					linkedList.addLast(command);

				}
				else if(adminMessage.equalsIgnoreCase("all"))
				{
					while(true)
					{
						String command = "02;A01;" + "00001" + ";0000000000000000;0000000000000000;FFFFFF;00045;";
						linkedList.addLast(command);
						Thread.sleep(2000);

						String command2 = "02;A02;" + "00002" + ";0000000000000000;0000000000000000;FFFFFF;00045;";
						linkedList.addLast(command2);
						Thread.sleep(2000);

						String command3 = "02;A03;" + "00003" + ";0000000000000000;0000000000000000;FFFFFF;00045;";
						linkedList.addLast(command3);
						Thread.sleep(2000);

						String command4 = "02;A04;" + "00004" + ";0000000000000000;0000000000000000;FFFFFF;00045;";
						linkedList.addLast(command4);
						Thread.sleep(2000);

						String command5 = "02;A05;" + "00005" + ";0000000000000000;0000000000000000;FFFFFF;00045;";
						linkedList.addLast(command5);
						Thread.sleep(2000);

						String command6 = "02;A06;" + "00006" + ";0000000000000000;0000000000000000;FFFFFF;00045;";
						linkedList.addLast(command6);
						Thread.sleep(2000);

						String command7 = "02;A07;" + "00007" + ";0000000000000000;0000000000000000;FFFFFF;00045;";
						linkedList.addLast(command7);
						Thread.sleep(2000);

						String command8 = "02;A08;" + "00008" + ";0000000000000000;0000000000000000;FFFFFF;00045;";
						linkedList.addLast(command8);
						Thread.sleep(2000);

						String command9 = "02;A09;" + "00009" + ";0000000000000000;0000000000000000;FFFFFF;00045;";
						linkedList.addLast(command9);
						Thread.sleep(2000);

						String command10 = "02;A10;" + "00010" + ";0000000000000000;0000000000000000;FFFFFF;00045;";
						linkedList.addLast(command10);
						Thread.sleep(2000);

						String command11 = "02;A11;" + "00011" + ";0000000000000000;0000000000000000;FFFFFF;00045;";
						linkedList.addLast(command11);
						Thread.sleep(2000);

						String command12 = "02;A12;" + "00012" + ";0000000000000000;0000000000000000;FFFFFF;00045;";
						linkedList.addLast(command12);
						Thread.sleep(2000);

						String command13 = "02;A13;" + "00013" + ";0000000000000000;0000000000000000;FFFFFF;00045;";
						linkedList.addLast(command13);
						Thread.sleep(2000);

						String command14 = "02;A14;" + "00014" + ";0000000000000000;0000000000000000;FFFFFF;00045;";
						linkedList.addLast(command14);
						Thread.sleep(2000);

						String command15 = "02;A15;" + "00015" + ";0000000000000000;0000000000000000;FFFFFF;00045;";
						linkedList.addLast(command15);
						Thread.sleep(2000);

						String commandb1 = "02;B01;" + "00016" + ";0000000000000000;0000000000000000;FFFFFF;00045;";
						linkedList.addLast(commandb1);
						Thread.sleep(2000);

						String commandb2 = "02;B02;" + "00017" + ";0000000000000000;0000000000000000;FFFFFF;00045;";
						linkedList.addLast(commandb2);
						Thread.sleep(2000);

						String commandb3 = "02;B03;" + "00018" + ";0000000000000000;0000000000000000;FFFFFF;00045;";
						linkedList.addLast(commandb3);
						Thread.sleep(2000);

						String commandb4 = "02;B04;" + "00019" + ";0000000000000000;0000000000000000;FFFFFF;00045;";
						linkedList.addLast(commandb4);
						Thread.sleep(2000);

						String commandb5 = "01;B05;" + "00020" + ";0000000000000000;FFFFFF;00045;";
						linkedList.addLast(commandb5);
						Thread.sleep(2000);

						String commandb6 = "01;B06;" + "00021" + ";0000000000000000;FFFFFF;00045;";
						linkedList.addLast(commandb6);
						Thread.sleep(2000);

						String commandc1 = "02;C01;" + "00022" + ";0000000000000000;0000000000000000;FFFFFF;00045;";
						linkedList.addLast(commandc1);
						Thread.sleep(2000);

						String commandc2 = "02;C02;" + "00023" + ";0000000000000000;0000000000000000;000180;00045;";
						linkedList.addLast(commandc2);
						Thread.sleep(2000);

						String commandc3 = "02;C03;" + "00024" + ";0000000000000000;0000000000000000;FFFFFF;00045;";
						linkedList.addLast(commandc3);
						Thread.sleep(2000);

						String commandc4 = "02;C04;" + "00025" + ";0000000000000000;0000000000000000;000180;00045;";
						linkedList.addLast(commandc4);
						Thread.sleep(2000);

						String commandc5 = "01;C05;" + "00026" + ";0000000000000000;FFFFFF;00045;";
						linkedList.addLast(commandc5);
						Thread.sleep(2000);

						String commandc6 = "01;C06;" + "00027" + ";0000000000000000;000180;00045;";
						linkedList.addLast(commandc6);
						Thread.sleep(2000);

						String commandc7 = "01;C07;" + "00028" + ";0000000000000000;123456;00045;";
						linkedList.addLast(commandc7);
						Thread.sleep(2000);

						String commandc8 = "01;C08;" + "00029" + ";0000000000000000;8200;00045;";
						linkedList.addLast(commandc8);
						Thread.sleep(2000);

						String commandc9 = "01;C09;" + "00030" + ";0000000000000000;140.92.71.211;00045;";
						linkedList.addLast(commandc9);
						Thread.sleep(2000);

						String commandc10 = "02;C10;" + "00031" + ";0000000000000000;0000000000000000;FFFFFF;00045;";
						linkedList.addLast(commandc10);
						Thread.sleep(2000);

						String commandc11 = "02;C11;" + "00032" + ";0000000000000000;0000000000000000;000150;00045;";
						linkedList.addLast(commandc11);
						Thread.sleep(2000);
					}

				}

				/*
				 * else { // for test
				 * managerServer.broadCastToClient(adminMessage); }
				 */
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();

		}
		catch(InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @param args
	 *            2008/6/4
	 */
	public static void main(String[] args)
	{
		new EmsMainAPP();

	}

}
