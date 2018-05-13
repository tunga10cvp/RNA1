package aufgabe2;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;



public class HochschuleGUI extends JFrame{
	
	
	public HochschuleGUI(){  
        
		Controller controller = new Controller();
		
        // mainPanel mit Umrandung versehen und das
        // Einfuegen- und  SuchenLoeschenPanel einbauen:
        JPanel mainPanel = new JPanel();
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
        
        mainPanel.add(new UserPanel(controller));
        mainPanel.add(new OutputPanel(controller));
        this.setContentPane(mainPanel);
        
        // Sonstige Eigenschaften des Hauptfenster setzen:
        this.setTitle("HAW Chat Client");
        this.setPreferredSize(new Dimension(700, 560));
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
        	@Override
        	public void windowClosing(WindowEvent event){
        		int n = JOptionPane.showConfirmDialog(mainPanel, "Soll die Verbindung wirklich getrennt werden?", "Verbindung trennen?", JOptionPane.YES_NO_OPTION);
        		if(n == JOptionPane.YES_OPTION){
        			controller.disconnectByUser();
        			System.exit(0);
        		} else if(n == JOptionPane.NO_OPTION){
        			return;
        		}
        	}
		});
        this.pack();
        this.setVisible(true);
	}
	public static void main(String[] args){
		new HochschuleGUI();
	}
}
