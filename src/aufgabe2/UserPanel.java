package aufgabe2;

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.Border;

public class UserPanel extends JPanel implements ActionListener {
	JTextField loginTextField, serverTextField;
	JButton loginButton;
	private String loginName;
	private String serverpath;
	Connection conn = null;
	Statement stmt = null;
	ResultSet rset = null;

	
	Controller controller;
	
	public UserPanel(Controller con) {
		controller = con;
		con.userP = this;
		
		JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayout(2, 1));
        panel1.add(new JLabel("Server:"));
        panel1.add(new JLabel("Login:"));
		
		
        JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayout(2, 1));
        serverTextField = new JTextField("", 20);
        panel2.add(serverTextField);
        loginTextField = new JTextField("", 20);
        panel2.add(loginTextField);

        Border border = BorderFactory.createTitledBorder("Anmeldung zum Chat-Server");
        this.setBorder(border);
        //this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        this.add(panel1);
        this.add(panel2);
        loginButton = new JButton("Anmelden");
        this.add(loginButton);
        loginButton.addActionListener(this);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if(source == loginButton){
        	if(loginTextField.getText().isEmpty() || serverTextField.getText().isEmpty()){
        		JOptionPane.showMessageDialog(this, "Bitte Username und Serverpfad angeben.", "Login fehlerhaft", JOptionPane.OK_OPTION);
        		return;
        	}
        	loginName = loginTextField.getText();
        	serverpath = serverTextField.getText();
        	
        	controller.connectToServer(loginName, serverpath);
        	
        }
		
	}
}

