package formularios;

import java.awt.BorderLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import com.mysql.cj.jdbc.Driver;

public class DialogCliente extends JFrame {

	private String usuario;
	private String contrasena;
	private Login login;

	public DialogCliente(String usuario, String contrasena, Login login) throws HeadlessException {

		super("Clientes");
		this.usuario = usuario;
		this.contrasena = contrasena;
		this.login = login;

		setSize(600, 250);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());

		// ----------Menú superior------------

		menuSup();
		
		try {
			DriverManager.registerDriver(new Driver());
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(this, "Error de registros del driver", "Error", JOptionPane.ERROR_MESSAGE);
		}

	}

	private void menuSup() {
		// ****Opciones*****
		JMenuBar menuBar = new JMenuBar();
		JMenu dOpciones = new JMenu("Opciones");
		dOpciones.setMnemonic(KeyEvent.VK_O);

		// Cambiar Usuario
		JMenuItem itmChangeUsr = new JMenuItem("Cambiar usuario");
		itmChangeUsr.setMnemonic(KeyEvent.VK_U);
		itmChangeUsr.setAccelerator(KeyStroke.getKeyStroke("ctrl U"));
		
		itmChangeUsr.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				cambiarUsuario();
			}
		});
		
		
		// Cargar datos
		JMenuItem cDatos = new JMenuItem("Cargar Datos");
		cDatos.setMnemonic(KeyEvent.VK_D);
		cDatos.setAccelerator(KeyStroke.getKeyStroke("ctrl D"));
		
		cDatos.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				cargarDatos();
				
			}
		});
		
		
		// Limpiar datos
		JMenuItem lDatos = new JMenuItem("Limpiar Datos");
		lDatos.setMnemonic(KeyEvent.VK_L);
		lDatos.setAccelerator(KeyStroke.getKeyStroke("ctrl L"));
		
		lDatos.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				
			}
		});
		
		
		// ******Informes******
		JMenu dInfo = new JMenu("Informes");
		dInfo.setMnemonic(KeyEvent.VK_I);


		// Facturas
		JMenuItem itmFactura = new JMenuItem("Facturas");
		itmFactura.setMnemonic(KeyEvent.VK_T);
		itmFactura.setAccelerator(KeyStroke.getKeyStroke("ctrl T"));

		// Clientes
		JMenuItem itmCliente = new JMenuItem("Clientes");
		itmCliente.setMnemonic(KeyEvent.VK_C);
		itmCliente.setAccelerator(KeyStroke.getKeyStroke("ctrl C"));

		dOpciones.add(itmChangeUsr);
		dOpciones.add(cDatos);
		dOpciones.add(lDatos);
		dInfo.add(itmFactura);
		dInfo.add(itmCliente);
		menuBar.add(dOpciones);
		menuBar.add(dInfo);
		setJMenuBar(menuBar);

	}

	protected void cargarDatos() {
		Connection conn = null;
		
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost/Acme?serverTimezone=Europe/Madrid",
					this.usuario, this.contrasena);
			
			String dni = JOptionPane.showInputDialog(this, "Introduce el dni de cliente", 
					"Busqueda de cliente", JOptionPane.QUESTION_MESSAGE);
		
		
		}catch (SQLException ex){//SQLexcetion se da cunado no hay conewxion o algun error con la bbdd
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Error al cargar datos", JOptionPane.ERROR_MESSAGE);
			
			
		}
		
		
	}

	protected void cambiarUsuario() {
		login.setVisible(true);
		setVisible(false);
		
	}

}