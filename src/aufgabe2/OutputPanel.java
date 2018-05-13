package aufgabe2;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;

public class OutputPanel extends JPanel implements ActionListener{

	JTextField messageTextField;
	JTextArea tArea;
	String []tables;
	JComboBox tableSelection;
	JButton sendButton;
	
	Controller controller;
	
	public OutputPanel(Controller con) {
		controller = con;
		con.outputP = this;
		
		sendButton = new JButton();
		sendButton.setText("Senden");
		sendButton.addActionListener(this);
		
		messageTextField = new JTextField("",20);
		messageTextField.setPreferredSize(new Dimension(500, 25));
		
		JPanel panelB = new JPanel();
		panelB.setLayout(new FlowLayout());
		panelB.add(messageTextField);
		panelB.add(sendButton);
		
		
		//Panel f�r Textfeld
		JPanel panelT = new JPanel();
		panelT.setLayout(new BoxLayout(panelT, BoxLayout.Y_AXIS));
		Border borderT = BorderFactory.createTitledBorder("Verlauf");
		panelT.setBorder(borderT);
		
        tArea = new JTextArea(20, 50);
        tArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(tArea);
        panelT.add(scrollPane);	
        panelT.add(panelB);
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        this.add(panelT);
	}
	public void setOutput(String s){
		tArea.append("\n" + s);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if(source == sendButton){
			System.out.println(messageTextField.getText());
			controller.sendMessage(messageTextField.getText());
		}
		
		
	}

}
