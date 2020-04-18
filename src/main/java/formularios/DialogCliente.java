package formularios;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;

import com.mysql.cj.jdbc.Driver;

public class DialogCliente extends JFrame {

	private String usuario;
	private String contrasena;
	private Login login;
	private JPanel pnlCampos;
	private JTextField txtDni;
	private JTextField txtNombre;
	private JTextField txtApellido1;
	private JTextField txtApellido2;
	private JTextField txtFechaNacimiento;

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
		crearCampos();
		
		try {
			DriverManager.registerDriver(new Driver());
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(this, "Error de registros del driver", "Error", JOptionPane.ERROR_MESSAGE);
		}

	}

	private void crearCampos() {

		pnlCampos = new JPanel();
		pnlCampos.setLayout(new GridLayout(5,2,0,0));
		pnlCampos.setBorder(new EmptyBorder(30,30,0,30));
		
		pnlCampos.add(new JLabel("DNI"));
		txtDni = new JTextField();
		pnlCampos.add(txtDni);

		
		pnlCampos.add(new JLabel("Nombre"));
		txtNombre = new JTextField();
		pnlCampos.add(txtNombre);

		pnlCampos.add(new JLabel("Apellido1"));
		txtApellido1 = new JTextField();
		pnlCampos.add(txtApellido1);
		
		pnlCampos.add(new JLabel("Apellido2"));
		txtApellido2 = new JTextField();
		pnlCampos.add(txtApellido2);
		
		pnlCampos.add(new JLabel("Fecha de nacimiento"));
		txtFechaNacimiento = new JTextField();
		pnlCampos.add(txtFechaNacimiento);
		
		add(pnlCampos, BorderLayout.NORTH); //para que todos los campos que meta entren por arriba
		
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
			conn = DriverManager.getConnection("jdbc:mysql://localhost/Acme?serverTimezone=Europe/Madrid", this.usuario,
					this.contrasena);

			String dni = JOptionPane.showInputDialog(this, "Introduce el dni de cliente", "Busqueda de cliente",
					JOptionPane.QUESTION_MESSAGE);
			if(dni == null && dni.equals("")) {//si dni es diferente a null y a campo vacio sale del dialogo
				JOptionPane.showMessageDialog(this, "Introduce el dni de cliente", "Introduzca un dni",
						JOptionPane.QUESTION_MESSAGE);
				return;
			}
			
			
			//--------Buscamos clientes y datos a campos
			PreparedStatement ps = conn.prepareStatement("SELECT Nombre, Ape1, Ape2, Fec_Nac FROM Clientes WHERE DNI = ?");
			ps.setString(1, dni);//valor un que se gurda en resultst
			ResultSet rs = ps.executeQuery(); //
			if (rs.first()) { //first mostrará la primera linea si exise, y coloca el cursor en esa línea (parece al next)
				txtDni.setText(dni);
				txtDni.setEditable(false);//aseguramos que no se pueda modificar el dni
				txtNombre.setText(rs.getString("Nombre"));
				txtApellido1.setText(rs.getString("Ape1"));
				txtApellido2.setText(rs.getObject("Ape2") == null ? "" : rs.getNString("Apellido2")); //hacemos la comprobacion con getObject, si es nula dejamos campo vacilo
				//sino escribe apellido dos
				txtFechaNacimiento.setText(rs.getDate("Fec_Nac").toString());
				
			}else {
				JOptionPane.showMessageDialog(this, "No existe ningún cliente con ese DNI",
						"Error en la búsqueda", JOptionPane.ERROR_MESSAGE);
			}

		} catch (SQLException ex) {// SQLexcetion se da cunado no hay conewxion o algun error con la bbdd
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Error al cargar datos", JOptionPane.ERROR_MESSAGE);

		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException ex1) {
					System.out.println("Excepción. Cerrando conexión");
				}
			}
		}

	}

	protected void cambiarUsuario() {
		login.setVisible(true);
		setVisible(false);

	}

}