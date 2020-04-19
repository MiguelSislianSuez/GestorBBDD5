package formularios;

import java.awt.BorderLayout;
import java.awt.Component;
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

import javax.swing.JButton;
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
		crearBotonesDialog();

		try {
			DriverManager.registerDriver(new Driver());
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(this, "Error de registros del driver", "Error", JOptionPane.ERROR_MESSAGE);
		}

	}

	private void crearBotonesDialog() {
		JPanel pnlBotons = new JPanel(new GridLayout(1, 3, 0, 0));
		pnlBotons.setBorder(new EmptyBorder(0, 10, 10, 10));

		JButton btnCrear = new JButton("Crear cliente");
		btnCrear.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				actualizarCrearCliente(true);
			}
		});
		pnlBotons.add(btnCrear);

		JButton btnActualizar = new JButton("Actualizar cliente");
		btnActualizar.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				actualizarCrearCliente(false);
			}
		});

		pnlBotons.add(btnActualizar);

		JButton btnEliminar = new JButton("Eliminar cliente");
		btnEliminar.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				eliminar();

			}
		});
		pnlBotons.add(btnEliminar);

		add(pnlBotons, BorderLayout.SOUTH);

	}

	protected void eliminar() {

		Connection conn = null;

		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost/Acme?serverTimezone=Europe/Madrid", this.usuario,
					this.contrasena);

			PreparedStatement ps = conn.prepareStatement("DELETE FROM Clientes WHERE DNI = ?");
			ps.setString(1, txtDni.getText());
			ps.executeUpdate();
			JOptionPane.showMessageDialog(this, "Cliente borrado correctametne", "Todo OK",
					JOptionPane.INFORMATION_MESSAGE);
			// llamamos al metodo limpiar datos
			limpiarDatos();

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

	protected void actualizarCrearCliente(boolean isCrear) {// le decimos al boton actualizar que crear es falso
		Connection conn = null;

		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost/Acme?serverTimezone=Europe/Madrid", this.usuario,
					this.contrasena);

			String consulta;

			if (isCrear) {
				consulta = "INSERT INTO Clientes (Nombre, Ape1, Ape2, Fec_Nac, DNI) VALUES (?,?,?,?,?)"; // consulta
																											// para
																											// insert
			} else {
				consulta = "UPDATE Clientes SET Nombre = ?, Ape1 = ?, Ape2 = ?, Fec_Nac = ? WHERE DNI = ? "; // consulta
																												// Update

			}

			PreparedStatement ps = conn.prepareStatement(consulta);
			// pasamos los valores que queremos meter y hacemos omproaciones
			if (txtNombre.getText().equals("")) {
				JOptionPane.showMessageDialog(this, "Introduzca un nombre", "Nombre de cliente",
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			ps.setString(1, txtNombre.getText());

			if (txtApellido1.getText().equals("")) {
				JOptionPane.showMessageDialog(this, "Introduzca un primer apellido", "Apellido de cliente",
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			ps.setString(2, txtApellido1.getText());

			// si el campo del apl2 contiene algo escrito pasa el valor gettextapl2 y sino
			// no devuelve un null
			ps.setString(3, !txtApellido2.getText().equals("") ? txtApellido2.getText() : null);

			if (txtFechaNacimiento.getText().equals("")) {
				JOptionPane.showMessageDialog(this, "Introduzca fecha de nacimiento", "Fecha de nacimiento de cliente",
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			ps.setString(4, txtFechaNacimiento.getText());

			if (isCrear && txtDni.equals("")) {// sino el campo de dni esta vacio entonces estamos creando cliente
				JOptionPane.showMessageDialog(this, "Introduzca DNI", "DNI de cliente", JOptionPane.ERROR_MESSAGE);
				return;
			}
			ps.setString(5, txtDni.getText());

			ps.executeUpdate();
			String mensajeResult;

			if (isCrear) {

				mensajeResult = "Se ha creado el cliente correctamente";
				txtDni.setEditable(false); // despues de crear el cliente ya no dejará modificar el dni

			} else {

				mensajeResult = "Se ha actualizado al cliente correctamente ";
			}

			JOptionPane.showMessageDialog(this, mensajeResult, "Todo OK", JOptionPane.INFORMATION_MESSAGE);

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

	private void crearCampos() {

		pnlCampos = new JPanel();
		pnlCampos.setLayout(new GridLayout(5, 2, 0, 0));
		pnlCampos.setBorder(new EmptyBorder(30, 30, 0, 30));

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

		add(pnlCampos, BorderLayout.NORTH); // para que todos los campos que meta entren por arriba

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

				limpiarDatos();

			}
		});

		// ******Informes******
		JMenu dInfo = new JMenu("Informes");
		dInfo.setMnemonic(KeyEvent.VK_I);

		// Facturas
		JMenuItem itmFactura = new JMenuItem("Facturas");
		itmFactura.setMnemonic(KeyEvent.VK_T);
		itmFactura.setAccelerator(KeyStroke.getKeyStroke("ctrl T"));
		itmFactura.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				mostrarFacturas();
			}
		});

		// Clientes
		JMenuItem itmCliente = new JMenuItem("Clientes");
		itmCliente.setMnemonic(KeyEvent.VK_C);
		itmCliente.setAccelerator(KeyStroke.getKeyStroke("ctrl C"));
		itmCliente.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				mostrarCliente();
			}
		});
		dOpciones.add(itmChangeUsr);
		dOpciones.add(cDatos);
		dOpciones.add(lDatos);
		dInfo.add(itmFactura);
		dInfo.add(itmCliente);
		menuBar.add(dOpciones);
		menuBar.add(dInfo);
		setJMenuBar(menuBar);

	}

	protected void mostrarFacturas() {
		
		
		Connection conn = null;

		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost/Acme?serverTimezone=Europe/Madrid", this.usuario,
					this.contrasena);

			
			
			PreparedStatement ps;
			String consulta = "SELECT * FROM Facturas";
			
			
			if(!txtDni.getText().equals("")) {
				//
				consulta += " WHERE Cliente = ?";
				
			}
			ps = conn.prepareStatement(consulta);
			
			if(!txtDni.getText().equals("")) {
				ps.setString(1, txtDni.getText());
				
			}
			
			ResultSet rs = ps.executeQuery();// almacenamos consultaa

			new VistaListados(rs);
			
			

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

	protected void mostrarCliente() {

		Connection conn = null;

		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost/Acme?serverTimezone=Europe/Madrid", this.usuario,
					this.contrasena);

			PreparedStatement ps = conn.prepareStatement("SELECT * FROM Clientes;");
			ResultSet rs = ps.executeQuery();// almacenamos consultaa

			VistaListados vl = new VistaListados(rs);

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

	protected void limpiarDatos() {// poner el acceso n privado

		Component[] componentes = pnlCampos.getComponents();

		for (Component componente : componentes) {// del aarray componentes coge cada elemento
			if (componente instanceof JTextField) {// comprobamos que el compoennte que recorremos es un textfield
				JTextField jtf = (JTextField) componente;// parseamos / convertimos obj component a componente jtf
				jtf.setText(""); // vaciamos el campo

				// otro modo de hacer seria poniendo cada campo txtDni.serText("") pero sería
				// más largo pero nos quitamos el bucle
			}
		}

		// otro modo de hacer el bucle
//		for (int i = 0; i < componentes.length; i++) {
//			Component componente = componentes[i];
//		}

	}

	protected void cargarDatos() {
		Connection conn = null;

		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost/Acme?serverTimezone=Europe/Madrid", this.usuario,
					this.contrasena);

			String dni = JOptionPane.showInputDialog(this, "Introduce el dni de cliente", "Busqueda de cliente",
					JOptionPane.QUESTION_MESSAGE);
			if (dni == null || dni.equals("")) {// si dni es diferente a null y a campo vacio sale del dialogo
				JOptionPane.showMessageDialog(this, "Introduce el dni de cliente", "Introduzca un dni",
						JOptionPane.QUESTION_MESSAGE);
				return;
			}

			// --------Buscamos clientes y datos a campos
			PreparedStatement ps = conn
					.prepareStatement("SELECT Nombre, Ape1, Ape2, Fec_Nac FROM Clientes WHERE DNI = ?");
			ps.setString(1, dni);// valor un que se gurda en resultst
			ResultSet rs = ps.executeQuery(); //
			if (rs.first()) { // first mostrará la primera linea si exise, y coloca el cursor en esa línea
								// (parece al next)
				txtDni.setText(dni);
				txtDni.setEditable(false);// aseguramos que no se pueda modificar el dni
				txtNombre.setText(rs.getString("Nombre"));
				txtApellido1.setText(rs.getString("Ape1"));
				txtApellido2.setText(rs.getObject("Ape2") == null ? "" : rs.getString("Ape2")); // hacemos la
																								// comprobacion
																								// con
																								// getObject, si
																								// es nula
																								// dejamos campo
																								// vacilo
				// sino escribe apellido dos
				txtFechaNacimiento.setText(rs.getDate("Fec_Nac").toString());

			} else {
				JOptionPane.showMessageDialog(this, "No existe ningún cliente con ese DNI", "Error en la búsqueda",
						JOptionPane.ERROR_MESSAGE);
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