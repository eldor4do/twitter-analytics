package com.smit.twitterbdw;

import java.awt.BorderLayout;
import java.lang.String;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.awt.*;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DropMode;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.*;

import org.eclipse.swt.widgets.TableColumn;

public class GUI {

	private JFrame frmTweetAnalyser;
	private JTextField txtEntertag;
	private JTextField txtEnterTweetLocation;
	private JComboBox comboBox;
	private JComboBox comboBox_1;
	private JComboBox comboBox_2;
	private JTable table;

	/**
	* Launch the application.
	*/
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI window = new GUI();
					window.frmTweetAnalyser.setVisible(true);
				} 
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	* Create the application.
	*/
	
	public GUI() {
		initialize();
	}
	
	
	
	/**
	* Initialize the contents of the frame.
	*/
	private void initialize() {
		
		
		final Color twitterDarkBlue = new Color(0,132,180); //From Twitter's Color Palette
		
		frmTweetAnalyser = new JFrame();
		frmTweetAnalyser.setTitle("SmartTweet Analytics");
		frmTweetAnalyser.setBounds(100, 100, 723, 483);
		frmTweetAnalyser.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmTweetAnalyser.getContentPane().setLayout(new FlowLayout());
		frmTweetAnalyser.getContentPane().setBackground(new Color(192, 222, 237));	
		
		JLabel lblTag = new JLabel("Choose option:");
		lblTag.setBounds(25, 22, 46, 14);
		frmTweetAnalyser.getContentPane().add(lblTag);
		
		comboBox_2 = new JComboBox();
		comboBox_2.setModel(new DefaultComboBoxModel(new String[] {"@JeeveSobs" }));
		comboBox_2.setBounds(51, 47, 17, 10);
		frmTweetAnalyser.getContentPane().add(comboBox_2);
				
		comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"General Hashtag Data","Most used hashtags","Most used phrases" }));
		comboBox.setBounds(81, 77, 37, 20);
		frmTweetAnalyser.getContentPane().add(comboBox);
				
		JButton btnSearch = new JButton("Search");
		btnSearch.setBounds(357, 48, 109, 43);
		btnSearch.addActionListener(new ActionListener() {
			HiveDatabaseQuery hq=new HiveDatabaseQuery();
			
			String queries_2[]={ "SELECT YEAR(created_at) as year, MONTH (created_at) as month, entities.hashtags.text[0] as hashtag, COUNT (*) as TweetCount, count(retweeted_status.id) as RetweetCount from tweets WHERE entities.hashtags.text[0] is not null GROUP BY YEAR(created_at), MONTH(created_at), entities.hashtags.text[0] ORDER BY year DESC limit 30",
					   "SELECT LOWER(hashtags.text) as Hashtag, COUNT(*) AS total_count FROM tweets LATERAL VIEW EXPLODE(entities.hashtags) t1 AS hashtags GROUP BY LOWER(hashtags.text) ORDER BY total_count DESC limit 30",
					   "SELECT explode(ngrams(sentences(lower(text)), 5, 25)) AS x FROM tweets"};
			
			public void actionPerformed(ActionEvent e) {
				try 
				{				
						
					table = new JTable();
					table.setOpaque(true);
					table.setFillsViewportHeight(true);
					table.setBackground(Color.WHITE);
					table.setForeground(Color.BLACK);
					table.setFont(new Font("Serif", Font.PLAIN, 15));
					frmTweetAnalyser.getContentPane().add(table);
					final DefaultTableModel dm=new DefaultTableModel();
					//table.repaint();
					
					int itemIndex = comboBox.getSelectedIndex();
					
					ResultSet res;
					
						res = hq.QueryME( queries_2[ itemIndex ] );
					ResultSetMetaData rsmd=res.getMetaData();
					int cols=rsmd.getColumnCount();
			        String c[]=new String[cols];
			        for(int i=0;i<cols;i++){
				           c[i]=rsmd.getColumnName(i+1);
				           dm.addColumn(c[i]);
			       }
			        
			       Object row[]=new Object[cols];
			       String month[] = { "Jan", "Feb", "Mar", "Apr", "May", "June", "July", "Aug", "Sept", "Nov", "Dec"};
			       while(res.next())
			       {
			    	   for(int i=0;i<cols;i++){
		                   row[i]=res.getString(i+1);
		                   String s = res.getString(i+1);
		                   	                   
		                   if(itemIndex == 2) {
		                	   String[] temp;
			                   temp = s.split("\"");
			                   
			                   String ans = "";
			                   for(int ii =3; ii < temp.length -2; ii++) {
			                	   
			                	   ans=ans+temp[ii]+" ";
			                   }
			                   String freq = "";
			                   for(int ii=temp.length - 2; ii < temp.length; ii ++) {
			                	   freq = freq + temp[ii];
			                   }
			                   
			                   String freqS[] = freq.split(":");
			                   			                   
			                   String ans_final="";
			                   for(int x =0; x < ans.length(); x++) {
			                	   if(ans.charAt(x) !=',' && ans.charAt(x) != ']')
			                		   ans_final = ans_final + ans.charAt(x);
			                   }
			                   
			                   String ekdumFinal = "Phrase: \"" + ans_final + "\n\t\"  Frequency: " + freqS[1];   
			                   
			                   row[i] = ekdumFinal;
			                   //row[i] = ans_final
		                   }
		                   if(itemIndex == 0 && i == 1) {
		                	   int mon = s.charAt(0) - '0';
		                   	   row[i] = month[mon];
		                   }
			    	   }
			    	   dm.addRow(row);
			       }
			       table.setModel(dm);
	       
			       
			       /*
			        * Uncomment for auto resize of table, based on coloumn value width.
			        */
			       /*table.setAutoResizeMode( JTable.AUTO_RESIZE_OFF );
			       for (int column = 0; column < table.getColumnCount(); column++)
			       {
			           javax.swing.table.TableColumn tableColumn = table.getColumnModel().getColumn(column);
			           int preferredWidth = tableColumn.getMinWidth();
			           int maxWidth = tableColumn.getMaxWidth();
		
			           for (int roww = 0; roww < table.getRowCount(); roww++)
			           {
			               TableCellRenderer cellRenderer = table.getCellRenderer(roww, column);
			               Component cc = table.prepareRenderer(cellRenderer, roww, column);
			               int width = cc.getPreferredSize().width + table.getIntercellSpacing().width;
			               preferredWidth = Math.max(preferredWidth, width);
		
			               //  We've exceeded the maximum width, no need to check other rows
		
			               if (preferredWidth >= maxWidth)
			               {
			                   preferredWidth = maxWidth;
			                   break;
			               }
			           }
			           if( preferredWidth > 100)
			           tableColumn.setPreferredWidth( preferredWidth );
			           else
			        	   tableColumn.setPreferredWidth( 100 );
			       	}*/
			       	JScrollPane sp=new JScrollPane(table);
				    sp.setSize(table.WIDTH, table.HEIGHT);
				    frmTweetAnalyser.getContentPane().add(sp, BorderLayout.CENTER);
	 
				} 
				catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}	             
	        }
		});
		frmTweetAnalyser.getContentPane().add(btnSearch);
	}
}