package GUI;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.io.BufferedReader;

import edu.casetools.lfpubs2m.LFPUBS2MTranslator;
import GUI.LoadingData;

import javax.swing.JFrame;

public class TranslateData extends JFrame {

	private final static String newline = "\n"; 
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
				jPanelimport.setBounds(new Rectangle(74, 16, 550, 320));
				jPanelimport.setBorder(BorderFactory.createTitledBorder("LFPUBS Patterns"));
				jPanelimport.add(getJTextAreaShowPatterns(),null);
				jPanelimport.add(getJButtonShowPatterns(), null);
			}	
			return jPanelimport;
	}
		private JButton getJButtonShowPatterns(){
			if(jBunttonShowPatterns==null){
				jBunttonShowPatterns= new JButton();
				jBunttonShowPatterns.setBounds(new Rectangle(365,280,150,22));
				jBunttonShowPatterns.setText("Show Patterns");
				jBunttonShowPatterns.addActionListener(new java.awt.event.ActionListener(){
					public void actionPerformed(java.awt.event.ActionEvent e){
						LFPUBS2MTranslator translator = new LFPUBS2MTranslator(true);
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
				jScrollPaneShowPatterns.setBounds(new Rectangle(15,20,500,250));
				jScrollPaneShowPatterns.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
				jScrollPaneShowPatterns.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			}
			return jScrollPaneShowPatterns;
		}
		
		
		private JButton getJButtonTranslate() {
			if(jButtonTranslate==null){
				jButtonTranslate=new JButton();
				jButtonTranslate.setBounds(new Rectangle(365,280,150,22));
				jButtonTranslate.setText("Translate");
				jButtonTranslate.addActionListener(new java.awt.event.ActionListener(){
					public void actionPerformed(java.awt.event.ActionEvent e){
					
					}
				});
			}
			return jButtonTranslate;
		}
		private JPanel getJPanelShow(){
			if(jPanelShow==null){
				jPanelShow = new JPanel();
				jPanelShow.setLayout(null);
				jPanelShow.setBounds(new Rectangle(680,16, 550, 320));
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
			jScrollPaneShowRules.setBounds(new Rectangle(15,20,500,250));
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
						jTextAreaShowPatterns.append(""+line);
					}
				}

			}
		 catch (IOException e) {
			e.printStackTrace();
		}
			
}
}