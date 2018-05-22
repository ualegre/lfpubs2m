package edu.casetools.mreasoner.lfpubs2m.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Rectangle;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import edu.casetools.mreasoner.lfpubs2m.core.LFPUBS2MTranslator;

public class TranslateData extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel  jPanelTranslateData=null;
	private JPanel jPanelimport=null;
	private JPanel jPanelShow=null;
	private JButton jButtonTranslate=null;
	private JButton jBunttonShowPatterns=null;
	private JTextArea	jTextAreaShowRules=null;
	private JTextArea 	jTextAreaShowPatterns=null;
	private JScrollPane jScrollPaneShowRules=null;
	private JScrollPane	jScrollPaneShowPatterns=null;
	private String file;
	public LFPUBS2MTranslator translator = new LFPUBS2MTranslator(true);
	
	//private LFPUBS2MTranslator translator=new LFPUBS2MTranslator(true);
	
	
	
	public TranslateData(){
	}
		public JPanel setJTranslateData(String file) {
			if (jPanelTranslateData == null) {
				jPanelTranslateData = new JPanel();
				jPanelTranslateData.setLayout(null);
				jPanelTranslateData.setSize(new Dimension(1172, 567));
				jPanelTranslateData.add(getJPanelImport(), null);
				jPanelTranslateData.add(getJPanelShow(), null);
			}
			this.file=file;
			return jPanelTranslateData;
		}
		
		private JPanel getJPanelImport(){
			if(jPanelimport==null){
				GridBagConstraints gridBagConstraints = new GridBagConstraints();
				gridBagConstraints.gridx = 0;
				gridBagConstraints.gridy = 0;
				jPanelimport= new JPanel();
				jPanelimport.setLayout(null);
				jPanelimport.setBounds(new Rectangle(20, 16, 550, 700));
				jPanelimport.setBorder(BorderFactory.createTitledBorder("LFPUBS Patterns"));
				jPanelimport.add(getJTextAreaShowPatterns(),null);
				jPanelimport.add(getJButtonShowPatterns(), null);
			}	
			return jPanelimport;
	}
		private JButton getJButtonShowPatterns(){
			if(jBunttonShowPatterns==null){
				jBunttonShowPatterns= new JButton();
				jBunttonShowPatterns.setBounds(new Rectangle(365,650,150,22));
				jBunttonShowPatterns.setText("Show Patterns");
				jBunttonShowPatterns.addActionListener(new java.awt.event.ActionListener(){
					public void actionPerformed(java.awt.event.ActionEvent e){
						translator.setFileName(file);
						writeLFPUBS(translator);
					}

					
				});
			}
			return jBunttonShowPatterns;
		}
		
		private JScrollPane getJTextAreaShowPatterns(){
			if(jTextAreaShowPatterns==null){
				jTextAreaShowPatterns=new JTextArea();
				jScrollPaneShowPatterns=new JScrollPane(jTextAreaShowPatterns);
				jScrollPaneShowPatterns.setBounds(new Rectangle(15,20,500,600));
				jScrollPaneShowPatterns.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
				jScrollPaneShowPatterns.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			}
			return jScrollPaneShowPatterns;
		}
		
		
		private JButton getJButtonTranslate() {
			if(jButtonTranslate==null){
				jButtonTranslate=new JButton();
				jButtonTranslate.setBounds(new Rectangle(365,650,150,22));
				jButtonTranslate.setText("Translate");
				jButtonTranslate.addActionListener(new java.awt.event.ActionListener(){
					public void actionPerformed(java.awt.event.ActionEvent e){
						jTextAreaShowRules.append(""+translator.getTranslation(file));
						try{
							File files=new File("./results/lfpubs2mes.mtpl");
							BufferedWriter bw = new BufferedWriter(new FileWriter(files));
							PrintWriter writer = new PrintWriter(bw);
							writer.println("");
							writer.print(""+translator.getTranslation(file));
							writer.print("");
							writer.close();
						}
						catch(Exception error){	
							System.out.println("Error Message: " + error.getMessage());
						}
					}
				});
			}
			return jButtonTranslate;
		}
		private JPanel getJPanelShow(){
			if(jPanelShow==null){
				jPanelShow = new JPanel();
				jPanelShow.setLayout(null);
				jPanelShow.setBounds(new Rectangle(625,16, 850, 700));
				jPanelShow.setBorder(BorderFactory.createTitledBorder("M Rules"));
				jPanelShow.add(getJTextAreaShowRules(),null);
				jPanelShow.add(getJButtonTranslate(),null);
				}
			return jPanelShow;
		}
		
		private JScrollPane getJTextAreaShowRules(){
			if(jTextAreaShowRules==null){
			jTextAreaShowRules = new JTextArea();
			jScrollPaneShowRules= new JScrollPane(jTextAreaShowRules);
			jScrollPaneShowRules.setBounds(new Rectangle(15,20,750,600));
			jScrollPaneShowRules.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
			jScrollPaneShowRules.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			//jScrollPaneShowRules.setBorder(BorderFactory.createTitledBorder("Translated Patterns"));
		}
		return jScrollPaneShowRules;
	}
		public void writeLFPUBS(LFPUBS2MTranslator translator){
			String line;
			try {
				translator.open();
				BufferedReader bufferedReader	= new BufferedReader( translator.getFileReader());	
				line = bufferedReader.readLine();
				 boolean debug=true;
				
				while(line != null){
					line = line.replaceAll("\\s","");
					if(debug){
						jTextAreaShowPatterns.append(""+line+" \n");
						line=bufferedReader.readLine();
					}
				}

			}
		 catch (IOException e) {
			e.printStackTrace();
		}
			
}
}
