package formularios;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class Login extends JDialog {

	//Crear campos de texto para usuarios
	//Crear campos para password
	
	private JTextField txtUser;
	private JPasswordField txtPwd;
	private DialogCliente dc;
	
	public Login() {
		super();
		setTitle("Introduzca usuario y contraseña");
		setSize(300,200);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);//dipose para mantener la maquina virtual funcionando y optimizar la memoria
		setLayout(new BorderLayout());
		
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(3, 2, 10, 10));//filas, columnas, distancias h y v
		
		//Etiqueta y cmpos de user
		
		JLabel lbUser = new JLabel("Usuario");
		txtUser = new JTextField(5);

		JLabel lbPass = new JLabel("Contraseña");
		txtPwd = new JPasswordField(5);
		txtPwd.addKeyListener(new KeyAdapter() {
			
			@Override
			public void keyPressed(KeyEvent e) {
				super.keyPressed(e);
				
				if(e.getKeyCode() == KeyEvent.VK_ENTER)//si e. es igual a tecla llama a aceptar
					aceptar();
			}
			
			
		});
		
		
		//Hacer botones 
		//Funcionalidades
		JButton btnAceptar = new JButton("Aceptar");
		btnAceptar.addActionListener(new ActionListener() {
			
			
			public void actionPerformed(ActionEvent e) {
				aceptar();
			}
		});
		
		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.addActionListener(new ActionListener() {
			
			
			public void actionPerformed(ActionEvent e) {
				cancelar();
			
				
			}
		});
		
		
		//Siempre en orden de ventana
		panel.add(lbUser);
		panel.add(txtUser);
		panel.add(lbPass);
		panel.add(txtPwd);
		panel.add(btnAceptar);
		panel.add(btnCancelar);
		
		panel.setBorder(new EmptyBorder(30, 30, 30, 30));//emptyBorder para evitar que se pegue a lo laterales
		add(panel, BorderLayout.CENTER);
	}

	protected void cancelar() {
		
		if (dc == null) {
			System.exit(DO_NOTHING_ON_CLOSE);
		
		}//si no es igual a null...etnocnces vamos al dialogo
		 dc.setVisible(true);
		 setVisible(false);
		
	}

	private void aceptar() {
		if("".equals(txtUser.getText())) {//si lacadena es equals a el txtUser pide
			JOptionPane.showMessageDialog(this, "Introduzca usuario", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		if("".equals(txtPwd.getText())) {
			JOptionPane.showMessageDialog(this, "Introduzca contraseña", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		dc = new DialogCliente(txtUser.getText(), txtPwd.getText(), this);
		dc.setVisible(true);
		this.setVisible(false);
	}
}
